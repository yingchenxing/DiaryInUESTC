package edu.uestc.diaryinuestc.ui.me;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

import edu.uestc.diaryinuestc.ui.PhotoSelectorPopupWindow;
import edu.uestc.diaryinuestc.utils.AppPathUtils;
import edu.uestc.diaryinuestc.BuildConfig;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentMeBinding;

public class MeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private Activity activity;

    private PopupWindow popupPhotoSelectorWindow;
    private Bitmap userAvatarBitmap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = requireActivity();

        loadUserInfo();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener();
    }

    /**
     * 从path读取用户图片以及用户名
     */
    private void loadUserInfo() {
        //加载头像
        File avatarFile = new File(activity.getFilesDir().getPath(), AppPathUtils.AVATAR_PATH);
        if (avatarFile.exists()) {
            userAvatarBitmap = BitmapFactory.decodeFile(avatarFile.getPath());
            Glide.with(activity).load(userAvatarBitmap).into(binding.userAvatar);
        }
        //加载用户名
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        binding.userName.setText(defaultPreferences.getString("name", ""));
        //加载性别
        String gender = defaultPreferences.getString("gender", "null");
        int genderDraw;
        if (gender.equals("boy"))
            genderDraw = R.drawable.ic_boy;
        else if (gender.equals("girl"))
            genderDraw = R.drawable.ic_girl;
        else
            genderDraw = R.drawable.ic_boygirl;
        binding.genderIc.setImageResource(genderDraw);
        //加载简介
        binding.mySign.setText(defaultPreferences.getString("sign", ""));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.userAvatar.setOnClickListener(this);
        binding.userName.setOnClickListener(this);
        binding.theme.setOnClickListener(this);
        binding.mySign.setOnClickListener(this);
        binding.genderIc.setOnClickListener(this);
        binding.myInfo.setOnClickListener(this);
        binding.mineHelp.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
        binding.mineAbout.setOnClickListener(this);
        binding.mineSetting.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        loadUserInfo();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.userAvatar.getId()) {
            popAvatarSelectorWindow();
        } else if (v.getId() == binding.userName.getId()) {
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open","name"));
        } else if (v.getId() == binding.theme.getId()) {
            Intent intent = new Intent(activity, ThemeSelectActivity.class);
            startActivity(intent);
        } else if (v.getId() == binding.mySign.getId()){
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open","sign"));
        } else if (v.getId() == binding.genderIc.getId()){
            startActivity(new Intent(activity, MyInfoActivity.class).putExtra("open","gender"));
        } else if (v.getId() == binding.myInfo.getId()) {
//            startActivity(new Intent(activity, PhotoSelectorPopupWindow.class).putExtra("isBottom", false));
            startActivity(new Intent(activity, MyInfoActivity.class));
        } else if (v.getId() == binding.mineHelp.getId()) {

        } else if (v.getId() == binding.mineSetting.getId()) {

        } else if (v.getId() == binding.mineAbout.getId()) {
            startActivity(new Intent(activity, AboutActivity.class));
        } else if (v.getId() == R.id.pop_camera) {
            popupPhotoSelectorWindow.dismiss();
            getPicFromCamera();

        } else if (v.getId() == R.id.pop_album) {
            popupPhotoSelectorWindow.dismiss();
            getPicFromAlbum();

        } else {
            Log.e(TAG, "Unsettled onClick view:" + v.toString());
            Toast.makeText(activity, getResources().getString(R.string.toast_unsettled_onclick)
                    + ":" + v.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建并弹出photo selector window
     */
    private void popAvatarSelectorWindow() {
        //创建popupWindow
        View popView = getLayoutInflater().inflate(R.layout.photo_select_pop_center, null);

//        Log.e(TAG, String.valueOf(binding.getRoot().getWidth())+"  "+ getResources().getDisplayMetrics().widthPixels);

        popupPhotoSelectorWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupPhotoSelectorWindow.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline, null));
        popupPhotoSelectorWindow.setOutsideTouchable(true);
        popupPhotoSelectorWindow.setFocusable(true);
        popupPhotoSelectorWindow.setAnimationStyle(R.style.popupWindow_anim_style);

        //设置弹出内容监听
        View popCameraBtn = popView.findViewById(R.id.pop_camera);
        View popAlbumBtn = popView.findViewById(R.id.pop_album);
        popCameraBtn.setOnClickListener(this);
        popAlbumBtn.setOnClickListener(this);

        //弹出窗口
        popupPhotoSelectorWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

        //淡化背景
        backgroundAlpha(0.5f);
        popupPhotoSelectorWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bg_alpha 1.0f 全透明没有阴影
     */
    public void backgroundAlpha(float bg_alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }

    //临时存储图片
    File tempFile;

    //最终存储图片
    File avatarFile;

    ActivityResultLauncher<Intent> picFromCameraIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (tempFile != null && tempFile.exists()) {
                        Log.e(TAG, "成功获取拍照图片");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(activity,
                                    BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
                            cropPicture(contentUri);
                        } else {
                            cropPicture(Uri.fromFile(tempFile));
                        }
                    } else {
                        assert tempFile != null;
                        Log.e(TAG, "获取拍照图片失败" + "file:" + tempFile.toString() + " code:" + result.getResultCode());
                    }
                }
            });

    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //顺便删除其他AvatarPhoto

        //临时接收图片文件
        tempFile = new File(activity.getCacheDir() + "/AvatarPhoto_" + System.currentTimeMillis() + ".png");
        //intent连接image_capture 并用EXTRA_OUTPUT传入tempFile的Uri
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        picFromCameraIntentLauncher.launch(intent);

    }


    ActivityResultLauncher<Intent> picFromAlbumIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        cropPicture(result.getData().getData());
                    }
                }
            });

    /**
     * 从手机相册获取图片
     */
    private void getPicFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        picFromAlbumIntentLauncher.launch(intent);
    }

    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult result) {
                    if (result.isSuccessful()) {
                        userAvatarBitmap = BitmapFactory.decodeFile(result.getUriFilePath(activity, false));
//                        userAvatarBitmap = result.getBitmap();
                        if (userAvatarBitmap == null) {
                            Log.e(TAG, "null bitmap! path=" + result.getUriFilePath(activity, false));
                            Toast.makeText(activity, getResources().getString(R.string.toast_null_picture), Toast.LENGTH_LONG).show();
                        } else {
                            if (AppPathUtils.saveImage(avatarFile, userAvatarBitmap)) {
                                Toast.makeText(activity, getResources().getString(R.string.toast_success_save_avatar), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.toast_fail_save_avatar), Toast.LENGTH_LONG).show();
                            }
                            Glide.with(activity).load(userAvatarBitmap).into(binding.userAvatar);
                        }
                    } else {
                        Log.e(TAG, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString());
                        Toast.makeText(activity, "Error in CropImage" + Objects.requireNonNull(result.getError()).toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

    /**
     * 裁剪图片<br><br/>
     *
     * @param uri 图片资源
     */
    private void cropPicture(Uri uri) {
        avatarFile = new File(activity.getFilesDir().getPath(), AppPathUtils.AVATAR_PATH);
        CropImageContractOptions options = new CropImageContractOptions(uri, new CropImageOptions())
                .setScaleType(CropImageView.ScaleType.CENTER)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMaxZoom(8)
                .setAutoZoomEnabled(false)
                .setMultiTouchEnabled(true)
                .setCenterMoveEnabled(true)
                .setShowCropOverlay(true)
                .setAllowFlipping(true)
                .setSnapRadius(30f)
                .setTouchRadius(48f)
                .setInitialCropWindowPaddingRatio(0.1f)
                .setBorderLineThickness(3f)
                .setBorderLineColor(R.color.black)
                .setBorderCornerThickness(2f)
                .setBorderCornerOffset(2f)
                .setBorderCornerLength(20f)
//                .setBorderCornerColor(R.color.RED)
                .setGuidelinesThickness(5f)
//                .setGuidelinesColor(RED)
//                .setBackgroundColor(Color.argb(119, 30, 60, 90))
                .setMinCropWindowSize(42, 42)
                .setMinCropResultSize(40, 40)
                .setMaxCropResultSize(999, 999)
                .setActivityTitle(getResources().getString(R.string.crop_image_activity_title))
//                .setActivityMenuIconColor(RED)
                /*
                 * set out put settings
                 * NONE uri will create a temp file
                 * 不设置是避免从fileprovider获取uri
                 * !!!
                 */
//                .setOutputUri()
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .setOutputCompressQuality(100)
//                .setRequestedSize(100, 100)
//                .setRequestedSize(100, 100, CropImageView.RequestSizeOptions.RESIZE_FIT)
//                .setInitialCropWindowRectangle(null)
                .setInitialRotation(180)
                .setAllowCounterRotation(true)
                .setFlipHorizontally(true)
                .setFlipVertically(true)
                .setCropMenuCropButtonTitle(getResources().getString(R.string.crop_image_button_title))
                .setCropMenuCropButtonIcon(R.drawable.ic_baseline_aspect_ratio_24)
                .setAllowRotation(true)
                .setNoOutputImage(false)
                .setFixAspectRatio(true);

        cropImage.launch(options);
    }

}