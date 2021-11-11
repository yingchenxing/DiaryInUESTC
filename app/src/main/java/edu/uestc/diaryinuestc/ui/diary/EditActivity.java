package edu.uestc.diaryinuestc.ui.diary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uestc.diaryinuestc.BuildConfig;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityEditBinding;
import edu.uestc.diaryinuestc.ui.PhotoSelectorPopupWindow;
import edu.uestc.diaryinuestc.ui.me.ThemeSelectActivity;
import edu.uestc.diaryinuestc.utils.AppPathUtils;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";
    public static final String NEW_TAG = "isNew";
    public static final String DIARY_ID = "uid";
    public static final int START_DUR = 1000;
    private boolean isNew;
    private Long diary_id = null;
    private ActivityEditBinding binding;
    private DiaryViewModel diaryViewModel;

    private PowerMenu moreMenu;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            refreshTextCount();
        }

        @Override
        public void afterTextChanged(Editable s) {
            saveDiary();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //必须要在之前request
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        super.onCreate(savedInstanceState);
        ThemeSelectActivity.setThemeToActivity(this, null);
        binding = ActivityEditBinding.inflate(getLayoutInflater());

        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);

        isNew = getIntent().getBooleanExtra(NEW_TAG, true);

        postponeEnterTransition();

        initTransition();

        //set 0.618
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        binding.diaryCover.getLayoutParams().height = (int) Math.floor(width * 0.618);

        setContentView(binding.getRoot());

        //load diary
        if (!isNew)
            loadDiary();

        //edittext change listen
        binding.diaryEditTitle.addTextChangedListener(textWatcher);
        binding.richEdit.addTextChangedListener(textWatcher);

        //set click
        setOnClickListener();

        //menu
        initMenu();

        //set basic start
//        binding.richEdit.setMovementMethod(null);
        binding.editBody.autoRefresh(START_DUR);

        startPostponedEnterTransition();
    }

    private void initID() {
        if (diary_id == null)
            diary_id = diaryViewModel.insert(new Diary());
    }

    private void initTransition() {
//        if (isNew) {
//            MaterialContainerTransform enterTransition = new MaterialContainerTransform();
//            enterTransition.setEndView(binding.getRoot());
//            enterTransition.setDuration(START_DUR);
//
//            getWindow().setEnterTransition(enterTransition);
//        } else {
//            Fade fadein = new Fade();
//            fadein.excludeTarget(android.R.id.navigationBarBackground, true);
//            fadein.excludeTarget(android.R.id.statusBarBackground, true);
//            fadein.setMode(Visibility.MODE_IN);
//
//            getWindow().setEnterTransition(fadein);
//        }

        TransitionSet outTran = new TransitionSet();

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(binding.diaryCover, true);

//        Slide slide = new Slide();
//        slide.setDuration(getResources().getInteger(R.integer.reply_motion_duration_large));
//        slide.addTarget(binding.editBody);
//        slide.excludeTarget(android.R.id.statusBarBackground, true);
//        slide.setSlideEdge(Gravity.BOTTOM);
//        slide.setMode(Visibility.MODE_OUT);

//        outTran.addTransition(slide);
        outTran.addTransition(fade);

        getWindow().setReturnTransition(fade);
        getWindow().setExitTransition(fade);

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
                for (View view : sharedElements) {
//                    Log.e(TAG,view.getTransitionName());
                    view.setAlpha(0.5F);
                }
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
            }
        });
    }

    /**
     * 仅仅在isNew false时调用
     * 在无法读入数据时强制退出界面
     * 根据diary_id读入数据到界面
     * 包括:标题，内容，日期，封面，字数
     */
    private void loadDiary() {
        diary_id = (Long) getIntent().getSerializableExtra(DIARY_ID);

        if (diary_id == null) {
            Toast.makeText(this, "fail to load diary", Toast.LENGTH_LONG).show();
            Log.e(TAG, "fail to load diary uid:" + diary_id);
            finish();
            return;
        }

        Diary diary = diaryViewModel.getDiary(diary_id);

        if (diary == null) {
            Toast.makeText(this, "fail to load diary", Toast.LENGTH_LONG).show();
            Log.e(TAG, "fail to load diary uid:" + diary_id);
            finish();
            return;
        }

        binding.diaryEditTitle.setText(diary.getTitle());
        binding.richEdit.setText(diary.getContent());
        binding.diaryDate.setText(diary.getDate());
        loadCover();
        refreshTextCount();
    }

    private void initMenu() {
        ArrayList<PowerMenuItem> list = new ArrayList<>();
        PowerMenuItem delete = new PowerMenuItem("删除", false);
        PowerMenuItem skin = new PowerMenuItem("更换封面", false);
        list.add(delete);
        list.add(skin);
        moreMenu = new PowerMenu.Builder(this)
                .addItemList(list)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                .setMenuRadius(20f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(Color.BLACK)
                .setTextGravity(Gravity.CENTER)
//                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setTextSize(20)
                .setBackgroundAlpha(0.3f)
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
//                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        if (item.equals(delete)) {
                            //delete diary
                            deleteDialog();
                        } else if (item.equals(skin)) {
                            //change cover
                            changeCover();
                        }
                    }
                })
                .build();
    }

    private void deleteDialog() {
        View view = getLayoutInflater().inflate(R.layout.delet_diary_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_outline_top);
        dialog.show();
        //设置在show之后生效,啊这我服了
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.diary_delete_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.diary_delete_sure).setOnClickListener(v -> {
            diaryViewModel.delete(new Diary(diary_id));
            dialog.dismiss();
            finish();
        });
    }

    ActivityResultLauncher<Intent> coverChangeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() == null) {
                            Toast.makeText(EditActivity.this, "加载图片失败", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "change cover with null data:" + result.toString());
                            return;
                        }
                        String path = result.getData().getStringExtra(PhotoSelectorPopupWindow.PATH);
                        File coverFile = new File(path);
                        Bitmap cover = BitmapFactory.decodeFile(coverFile.getPath());
                        Glide.with(EditActivity.this)
                                .load(cover)
                                .into(binding.diaryCover);

                        initID();
                        if (diary_id == null) return;

                        AppPathUtils.saveImage(
                                AppPathUtils.getDiaryFile(EditActivity.this, diary_id, Diary.COVER_PNG),
                                cover);
                        AppPathUtils.deleteFile(path);
                        saveDiary();
                    }
                }
            });

    private void changeCover() {
        Intent intent = new Intent(this, PhotoSelectorPopupWindow.class);
        intent.putExtra("isBottom", true);
        //let it generate a cache file
//        intent.putExtra(PhotoSelectorPopupWindow.PATH,
//                AppPathUtils.getDiaryFile(this, diary_id, Diary.COVER_PNG).getPath());
        intent.putExtra(PhotoSelectorPopupWindow.ASPECT_RATIO, 1.618);
        coverChangeLauncher.launch(intent);
    }

    /**
     * 涉及到id存取，需要initID()
     * loadCover from Diary file
     */
    private void loadCover() {
        initID();
        if (diary_id == null) return;

        Bitmap cover = new Diary(diary_id).requireCover(this);
        if (cover != null) {
            Glide.with(this)
                    .load(cover)
//                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.diaryCover);
        }
    }

    /**
     * 涉及到id存取，需要initID()
     */
    private void saveDiary() {
        initID();
        if (diary_id == null) return;
        Diary diary = new Diary(diary_id);

        diary.setTitle(binding.diaryEditTitle.getText().toString());
        diary.setContent(binding.richEdit.getText().toString());
//        if (diary.isEmpty(this)) return;

        //每次保存刷新时间
        diary.setDate(getCurrentDate());
        diary.setMillis(System.currentTimeMillis());

        diaryViewModel.update(diary);

        Log.d(TAG, "更新数据" + diary.getUid() + ":" + diary.getTitle() + " " + diary.getContent());
    }

    @NonNull
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月d日", Locale.CHINA);
        SimpleDateFormat df2 = new SimpleDateFormat("HH", Locale.CHINA);
        int a = Integer.parseInt(df2.format(date));
        String timePartValue = null;
        if (a >= 0 && a <= 6) timePartValue = "凌晨";
        else if (a > 6 && a < 12) timePartValue = "上午";
        else if (a >= 12 && a <= 13) timePartValue = "中午";
        else if (a > 13 && a <= 18) timePartValue = "下午";
        else if (a > 18 && a <= 24) timePartValue = "晚上";
        SimpleDateFormat df3 = new SimpleDateFormat("H:mm", Locale.CHINA);
        String dateValue = df1.format(date);
        String timeValue = df3.format(date);
        StringBuilder time = new StringBuilder();
        time.append(dateValue).append(" ").append(timePartValue).append(timeValue);
        //顺便更新(对操作无影响，可预留)
        binding.diaryDate.setText(time.toString());
        return time.toString();
    }

    private void refreshTextCount() {
        int len = 0;
        if (binding.richEdit.getText() != null)
            len += binding.richEdit.getText().toString().length();
        if (binding.diaryEditTitle.getText() != null)
            len += binding.diaryEditTitle.getText().toString().length();
        binding.diaryTextCount.setText(String.valueOf(len));
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.skin) {
            Toast.makeText(this, "选择皮肤", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.share) {
            Toast.makeText(this, "分享日记", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.more) {
            moreMenu.showAsDropDown(v, 0, -v.getHeight());
        } else if (v.getId() == R.id.diary_cover) {
            //change cover
            changeCover();
        }
    }

    private void setOnClickListener() {
        binding.toolbar.setNavigationOnClickListener(v -> finishAfterTransition());
        binding.diaryCover.setOnClickListener(this);
        binding.skin.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.more.setOnClickListener(this);
        binding.editBody.setOnRefreshListener(refreshLayout -> {
            //empty for nothing to do
        });
    }

    @Override
    public void finish() {
        //为空删日记
        refreshTextCount();
        if (diary_id == null
                || (!new Diary(diary_id).checkRes(this)
                && Integer.parseInt((String) binding.diaryTextCount.getText()) == 0)
        ) {
            Toast.makeText(this, "内容为空自动删除", Toast.LENGTH_SHORT).show();
            diaryViewModel.delete(new Diary(diary_id));
        }
        super.finish();
    }
}