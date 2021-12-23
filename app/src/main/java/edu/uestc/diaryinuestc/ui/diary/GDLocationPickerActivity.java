package edu.uestc.diaryinuestc.ui.diary;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.preference.PreferenceManager;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityGdlocationPickerBinding;
import edu.uestc.diaryinuestc.databinding.SubmitLocationDialogBinding;
import edu.uestc.diaryinuestc.ui.me.ThemeSelectActivity;

public class GDLocationPickerActivity extends AppCompatActivity implements
        PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener,
        SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private static final String TAG = "GDLocationPicker";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION
    };

    private static final int PERMISSION_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private static final String AGREE_KEY = "PRIVACY_AGREE_KEY";
    private boolean agree = false;

    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static final String BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    private ActivityGdlocationPickerBinding binding;

    private AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private PoiSearch poiSearch;
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeSelectActivity.setThemeToActivity(this, null);
        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION
            };
            needCheckBackLocation = true;
        }

        binding = ActivityGdlocationPickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchToolbar.setNavigationOnClickListener((v) -> finish());

        //necessary WARNING!
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);

        privacyCompliance();

        binding.map.onCreate(savedInstanceState);
        aMap = binding.map.getMap();

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.interval(30000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        //指南针
        mUiSettings.setCompassEnabled(true);
        //比例尺
        mUiSettings.setScaleControlsEnabled(true);


//        aMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听

        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮

        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        //Marker click
        aMap.setOnMarkerClickListener(marker -> {
            String location_str = marker.getTitle();
            if (location_str == null || location_str.trim().length() == 0) {
                Toast.makeText(GDLocationPickerActivity.this, "空的地址", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "空的地址" + marker.toString());
                return false;
            }

            SubmitLocationDialogBinding binding = SubmitLocationDialogBinding.inflate(getLayoutInflater());
            AlertDialog dialog = new AlertDialog.Builder(GDLocationPickerActivity.this)
                    .setView(binding.getRoot())
                    .create();
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_outline_top);
            dialog.show();
            //设置在show之后生效,啊这我服了
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            binding.chosenLocation.setText(location_str);
            binding.cancel.setOnClickListener(v -> dialog.dismiss());
            binding.submit.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra(EditActivity.LOCATION_KEY, location_str);
                setResult(Activity.RESULT_OK, intent);
                dialog.dismiss();
                Toast.makeText(GDLocationPickerActivity.this, "成功修改地址", Toast.LENGTH_SHORT).show();
                finish();
            });
            return false;
        });

        //search listener
        binding.search.setSubmitButtonEnabled(true);
        binding.search.setOnQueryTextListener(this);
        binding.search.setOnSuggestionListener(this);
        binding.search.setOnCloseListener(() -> true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    private void privacyCompliance() {
        MapsInitializer.updatePrivacyShow(GDLocationPickerActivity.this, true, true);

        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        agree = defaultPreferences.getBoolean(GDLocationPickerActivity.AGREE_KEY, false);

        if (agree) return;
        SpannableStringBuilder spannable = new SpannableStringBuilder("\"感谢您使用成电微记！在定位功能中我们必须按照最新监管《隐私权政策》向用户提出声明，特向您说明如下\n1.为向您提供基本功能，我们会收集、使用必要的信息仅用于本地；\n2.基于您的明示授权，我们可能会获取您的位置（为您提供附近的商品、店铺及优惠资讯等）等信息，您有权拒绝或取消授权；\n3.我们会采取业界先进的安全措施保护您的信息安全；\n4.未经您同意，我们不会从第三方处获取、共享或向提供您的信息；\n");
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 27, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(this)
                .setTitle("温馨提示(使用高德SDK定位功能权限声明)")
                .setMessage(spannable)
                .setPositiveButton("同意", (dialogInterface, i) -> {
                    agree = true;
                    defaultPreferences.edit().putBoolean(GDLocationPickerActivity.AGREE_KEY, true).apply();
                    MapsInitializer.updatePrivacyAgree(GDLocationPickerActivity.this, true);
                })
                .setNegativeButton("不同意", (dialogInterface, i) -> {
                    agree = false;
                    defaultPreferences.edit().putBoolean(GDLocationPickerActivity.AGREE_KEY, false).apply();
                    MapsInitializer.updatePrivacyAgree(GDLocationPickerActivity.this, false);
                })
                .setOnDismissListener(dialog -> {
                    if (agree) {
                        Toast.makeText(GDLocationPickerActivity.this, "已同意隐私政策", Toast.LENGTH_SHORT).show();
                    } else {
                        MapsInitializer.updatePrivacyAgree(GDLocationPickerActivity.this, false);
                        Toast.makeText(GDLocationPickerActivity.this, "未同意隐私政策", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .show();
    }

    @TargetApi(23)
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissionList = findDeniedPermissions(permissions);
                if (null != needRequestPermissionList
                        && needRequestPermissionList.size() > 0) {
                    try {
                        String[] array = needRequestPermissionList.toArray(new String[0]);
//                        Method method = getClass().getMethod("requestPermissions", String[].class, int.class);
                        requestPermissions(array, 0);
//                        method.invoke(this, array, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions 权限
     * @return denied permissions
     */
    @TargetApi(23)
    private List<String> findDeniedPermissions(String[] permissions) {
        try {
            List<String> needRequestPermissionList = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                for (String perm : permissions) {
                    if (checkMySelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                            || shouldShowMyRequestPermissionRationale(perm)) {
                        if (!needCheckBackLocation
                                && BACK_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissionList.add(perm);
                    }
                }
            }
            return needRequestPermissionList;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private int checkMySelfPermission(String perm) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(perm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean shouldShowMyRequestPermissionRationale(String perm) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return shouldShowRequestPermissionRationale(perm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults 结果
     * @return verify
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        try {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requestCode == PERMISSION_REQUESTCODE) {
                    if (!verifyPermissions(paramArrayOfInt)) {
                        Log.e(TAG, Arrays.toString(permissions));
                        showMissingPermissionDialog();
                        isNeedCheck = false;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前应用缺少必要权限。\n请打开定位功能或读写权限，仅用作位置读取\n请点击\"设置\"-\"权限\"-打开所需权限");

            // 拒绝, 退出应用
            builder.setNegativeButton("取消",
                    (dialog, which) -> {
                        try {
                            finish();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });

            builder.setPositiveButton("设置",
                    (dialog, which) -> {
                        try {
                            startAppSettings();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    });

            builder.setCancelable(false);

            builder.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        try {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poi_items有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                    } else
                        Toast.makeText(this, "没有所搜索的位置", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器返回结果失败 code:" + rCode, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "服务器返回结果失败 code:" + rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            String[] columnNames = {"_id", "text"};
            MatrixCursor cursor = new MatrixCursor(columnNames);
            int id = 0;
            for (String item : listString) {
                String[] temp = new String[2];
                temp[0] = String.valueOf(id++);
                temp[1] = item;
                cursor.addRow(temp);
            }
            String[] from = {"text"};
            int[] to = {R.id.online_user_list_item_textview};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.route_inputs, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            binding.search.setSuggestionsAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "error in onGetInput_tips code:" + rCode);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String kewWord) {
//        Toast.makeText(GDLocationPickerActivity.this, query, Toast.LENGTH_SHORT).show();
        if (kewWord == null || kewWord.trim().length() == 0) {
            Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        currentPage = 0;
        query = new PoiSearch.Query(kewWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        try {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        } catch (AMapException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null) return false;
        newText = newText.trim();
        if (newText.length() == 0) return false;
        InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, "");//全国范围搜索
        Inputtips inputTips = new Inputtips(GDLocationPickerActivity.this, inputtipsQuery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        CursorAdapter c = binding.search.getSuggestionsAdapter();
        Cursor cursor = c.getCursor();
        cursor.moveToPosition(position);
        String value = cursor.getString(1);
        binding.search.setQuery(value, false);
        return false;
    }
}