package edu.uestc.diaryinuestc.ui.diary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityEditBinding;
import edu.uestc.diaryinuestc.databinding.ChooseLocationDialogBinding;
import edu.uestc.diaryinuestc.databinding.ManualLocationEditorBinding;
import edu.uestc.diaryinuestc.databinding.ShareDiaryDialogBinding;
import edu.uestc.diaryinuestc.ui.PhotoSelectorPopupWindow;
import edu.uestc.diaryinuestc.ui.me.ThemeSelectActivity;
import edu.uestc.diaryinuestc.utils.AppPathUtils;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";
    public static final String NEW_TAG = "isNew";
    public static final String DIARY_ID = "uid";
    public static final String LOCATION_KEY = "location";
    public static final int START_DUR = 200;
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
        if (ThemeSelectActivity.getThemeColor(this) == Color.WHITE)
            binding.editBody.setBackgroundColor(Color.WHITE);

        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);

        isNew = getIntent().getBooleanExtra(NEW_TAG, true);

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
        else {
            postponeEnterTransition();
            startPostponedEnterTransition();
        }
        //edittext change listen
        binding.diaryEditTitle.addTextChangedListener(textWatcher);
        binding.richEdit.addTextChangedListener(textWatcher);

        //set click
        setOnClickListener();

        //menu
        initMenu();

        //set basic start
//        binding.richEdit.setMovementMethod(null);
        if (new Diary(diary_id).checkRes(this))
            binding.editBody.autoRefresh(START_DUR);
        else
            binding.editBody.autoRefresh();

    }

    private void initID() {
        if (diary_id == null)
            diary_id = diaryViewModel.insert(new Diary());
    }

    private void initTransition() {
        TransitionSet outTran = new TransitionSet();

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(binding.diaryCover, true);

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
        binding.location.setText(diary.getLocation());
        loadCover();
        refreshTextCount();
    }

    private void initMenu() {
        ArrayList<PowerMenuItem> list = new ArrayList<>();
        PowerMenuItem delete = new PowerMenuItem("删除", false);
        PowerMenuItem skin = new PowerMenuItem("更换封面", false);
        PowerMenuItem delete_skin = new PowerMenuItem("删除封面", false);
        PowerMenuItem location = new PowerMenuItem("修改位置", false);
        list.add(delete);
        list.add(skin);
        list.add(delete_skin);
        list.add(location);

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
                .setAutoDismiss(true)
                .setOnMenuItemClickListener((position, item) -> {
                    if (item.equals(delete)) {
                        //delete diary
                        deleteDialog();
                    } else if (item.equals(skin)) {
                        //change cover
                        changeCover();
                    } else if (item.equals(delete_skin)) {
                        File coverFile = AppPathUtils.getDiaryFile(getApplication(), diary_id, "");
                        if (diary_id == null || !coverFile.exists()) {
                            Toast.makeText(EditActivity.this, "封面图片未设置", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (AppPathUtils.delete(coverFile.getPath())) {
                            Toast.makeText(EditActivity.this, "封面删除成功", Toast.LENGTH_SHORT).show();
                            binding.diaryCover.animate()
                                    .alpha(0)
                                    .withEndAction(() -> binding.diaryCover.setImageDrawable(null))
                                    .setDuration(250)
                                    .start();
                            saveDiary();
                        }
                    } else if (item.equals(location)) {
                        //choose location
                        chooseLocation();
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
        intent.putExtra(PhotoSelectorPopupWindow.ASPECT_RATIO, 1.618);
        coverChangeLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> location_launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String location = data.getStringExtra(LOCATION_KEY);
                            if (location == null || location.trim().length() == 0) {
                                Toast.makeText(EditActivity.this, "空的location值", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "空的location值");
                                return;
                            }
                            binding.location.setText(location);
                            saveDiary();
                        } else {
                            Toast.makeText(EditActivity.this, "空的intent传递", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "空的intent传递");
                        }
                    }
                }
            });

    private void chooseLocation() {
        ChooseLocationDialogBinding locationBinding = ChooseLocationDialogBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(locationBinding.getRoot())
                .create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_outline_top);
        dialog.show();
        //设置在show之后生效,啊这我服了
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        locationBinding.cancel.setOnClickListener(v -> dialog.dismiss());
        locationBinding.editManual.setOnClickListener(v -> {
            ManualLocationEditorBinding editBinding = ManualLocationEditorBinding.inflate(getLayoutInflater());
            Diary diary = diaryViewModel.getDiary(diary_id);
            editBinding.locationEdit.setText(diary.getLocation());

            AlertDialog editDialog = new AlertDialog.Builder(EditActivity.this)
                    .setView(editBinding.getRoot())
                    .create();
            editDialog.getWindow().setGravity(Gravity.CENTER);
            editDialog.getWindow().setBackgroundDrawableResource(R.drawable.round_outline);
            editDialog.show();
            dialog.dismiss();

            editBinding.cancel.setOnClickListener(v1 -> editDialog.dismiss());
            editBinding.submit.setOnClickListener(v12 -> {
                String location = editBinding.locationEdit.getText().toString().trim();
                if (location.equals(diary.getLocation())) {
                    editDialog.dismiss();
                    return;
                }
                binding.location.setText(location);
                saveDiary();
                Toast.makeText(EditActivity.this, "修改位置成功", Toast.LENGTH_SHORT).show();
                editDialog.dismiss();
            });
        });
        locationBinding.openMap.setOnClickListener(v -> {
            Intent intent = new Intent(EditActivity.this, GDLocationPickerActivity.class);
            location_launcher.launch(intent);
            dialog.dismiss();
        });
    }

    /**
     * 涉及到id存取，需要initID()
     * loadCover from Diary file
     */
    private void loadCover() {
        initID();
        if (diary_id == null) return;
        postponeEnterTransition();

        Bitmap cover = new Diary(diary_id).requireCover(this);
        if (cover != null) {
            Glide.with(this)
                    .load(cover)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(binding.diaryCover);
        } else
            startPostponedEnterTransition();
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
        diary.setLocation(binding.location.getText().toString());

        //每次保存刷新时间
        diary.setDate(getCurrentDate());
        diary.setMillis(System.currentTimeMillis());

        diaryViewModel.update(diary);

        Log.d(TAG, "更新数据" + diary.getUid() + ":" + diary.getTitle() + " " + diary.getContent());
    }

    private void shareDiary() {
        ShareDiaryDialogBinding shareBinding = ShareDiaryDialogBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(shareBinding.getRoot())
                .create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_outline_top);
        dialog.show();
        //设置在show之后生效,啊这我服了
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        shareBinding.shareCancel.setOnClickListener(v -> dialog.dismiss());
        shareBinding.shareText.setOnClickListener(v -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            String title = binding.diaryEditTitle.getText().toString();
            String content = binding.richEdit.getText().toString();
            StringBuilder out = new StringBuilder();
            if (title.length() != 0)
                out.append(title);
            if (content.length() != 0) {
                out.append('\n');
                out.append(content);
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, out.toString());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
            dialog.dismiss();
        });
        shareBinding.shareImage.setOnClickListener(v -> {
            List<View> list = new ArrayList<>();
            if (binding.diaryCover.getDrawable() != null)
                list.add(binding.diaryCover);
            list.add(binding.extraInfo);
            list.add(binding.textBody);

            int height = 0;
            for (View view : list)
                height += view.getHeight();

            int extraBorder = 50;
            int extraHeight = 200;
            Bitmap b = Bitmap.createBitmap(binding.getRoot().getWidth() + 2 * extraBorder, height + extraBorder + extraHeight, Bitmap.Config.ARGB_4444);

            Canvas c = new Canvas(b);
            binding.diaryCover.setCornerRadius(20);
            c.drawColor(ThemeSelectActivity.getThemeColor(EditActivity.this));

            int y = 0;
            c.translate(extraBorder, extraBorder);
            for (View view : list) {
                c.translate(0, y);
                view.draw(c);
                y = view.getHeight();
            }
            c.translate(0, y + extraHeight - binding.diaryFooter.getHeight());
            binding.diaryFooter.draw(c);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), b,
                    binding.diaryEditTitle.getText().toString(), "diary_share"));
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
            dialog.dismiss();
        });
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
        if (binding.diaryEditTitle.getText() != null)
            len += binding.diaryEditTitle.getText().toString()
                    .replace(" ", "").replace("\n", "").length();
        if (binding.richEdit.getText() != null)
            len += binding.richEdit.getText().toString()
                    .replace(" ", "").replace("\n", "").length();
        binding.diaryTextCount.setText(String.valueOf(len));
    }

    @Override
    public void onClick(@NonNull View v) {
        if (v.getId() == R.id.skin) {
            Intent intent = new Intent(this, ThemeSelectActivity.class);
            intent.putExtra(ThemeSelectActivity.FRAGMENT_KEY, 1);
            startActivity(intent);
//            Toast.makeText(this, "选择皮肤", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.share) {
//            Toast.makeText(this, "分享日记", Toast.LENGTH_SHORT).show();
            shareDiary();
        } else if (v.getId() == R.id.more) {
            moreMenu.showAsDropDown(v, 0, -v.getHeight());
        } else if (v.getId() == R.id.diary_cover) {
            //change cover
            changeCover();
        } else if (v.getId() == R.id.extra_info2) {
            chooseLocation();
        } else {
            Log.e(TAG, "unhandled onclick listener" + v.getId() + v.toString());
        }
    }

    private void setOnClickListener() {
        binding.toolbar.setNavigationOnClickListener(v -> fadeout());
        binding.diaryCover.setOnClickListener(this);
        binding.skin.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.more.setOnClickListener(this);
        binding.editBody.setOnRefreshListener(refreshLayout -> {
            //empty for nothing to do
        });
        binding.extraInfo2.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fadeout();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fadeout() {
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finishAfterTransition();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };

        if (isNew && binding.diaryCover.getDrawable() == null) {
            ObjectAnimator root_animator = ObjectAnimator.ofFloat(binding.getRoot(), "alpha", 1, 0);
            root_animator.setDuration(100);
            root_animator.setRepeatCount(0);
            root_animator.addListener(listener);
            root_animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(binding.diaryCover, "alpha", 1, 0.5F);
            animator.setDuration(100);
            animator.setRepeatCount(0);
            animator.addListener(listener);
            animator.start();
        }


    }

    @Override
    public void finish() {
        //为空删日记
        refreshTextCount();
        if (diary_id == null
                || (!new Diary(diary_id).checkRes(this)
                && Integer.parseInt((String) binding.diaryTextCount.getText()) == 0)
        ) {
//            Toast.makeText(this, "内容为空自动删除", Toast.LENGTH_SHORT).show();
            diaryViewModel.delete(new Diary(diary_id));
        }
        super.finish();
    }
}