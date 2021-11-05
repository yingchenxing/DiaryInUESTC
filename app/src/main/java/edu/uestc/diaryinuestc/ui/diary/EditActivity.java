package edu.uestc.diaryinuestc.ui.diary;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditActivity";
    public static final String NEW_TAG = "isNew";
    public static final String DIARY_ID = "uid";
    public static final String SELECT = "select";
    private long diary_id;
    private ActivityEditBinding binding;
    private DiaryViewModel diaryViewModel;

    private PowerMenu moreMenu;
    private final TextWatcher richTextWatcher = new TextWatcher() {
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
    private final TextWatcher titleTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            saveDiary();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);

        //load diary
        boolean isNew = getIntent().getBooleanExtra(NEW_TAG, true);
        if (isNew) {
            Diary diary = new Diary();
            //insert 会改变uid(auto generator)更改为其真值
            diary_id = diaryViewModel.insert(diary);
        } else {
            diary_id = getIntent().getLongExtra(DIARY_ID, 0);
            int select = getIntent().getIntExtra(SELECT, -1);
            LiveData<Diary> live_diary = diaryViewModel.getDiary(diary_id);
            if (live_diary != null) {
                Observer<Diary> observer = new Observer<Diary>() {
                    @Override
                    public void onChanged(Diary diary) {
                        binding.diaryEditTitle.setText(diary.getTitle());
                        binding.richEdit.setText(diary.getContent());
                        binding.diaryDate.setText(diary.getDate());
                        //同步一次就关闭
                        live_diary.removeObserver(this);
                    }
                };
                live_diary.observe(this, observer);

            } else {
                Toast.makeText(this, "fail to load diary", Toast.LENGTH_LONG).show();
                Log.e(TAG, "fail to load diary uid:" + diary_id);
            }
        }

        setOnClickListener();
        initMenu();

        binding.richEdit.setMovementMethod(null);
        binding.editBody.autoRefresh();

        //edittext change listen
        binding.richEdit.addTextChangedListener(richTextWatcher);
        binding.diaryEditTitle.addTextChangedListener(titleTextWatcher);
    }

    private void initMenu() {
        moreMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("删除", false))
                .addItem(new PowerMenuItem("更换封面", false))
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
                        if (position == 0) {
                            //delete diary
                        } else if (position == 1) {
                            //change cover
                        }
                    }
                })
                .build();
    }

    private void saveDiary() {
//        Toast.makeText(this, "保存数据", Toast.LENGTH_SHORT).show();
        Diary diary = new Diary(diary_id);

        diary.setTitle(binding.diaryEditTitle.getText().toString());
        diary.setContent(binding.richEdit.getText().toString());
        //每次保存刷新时间
        diary.setDate(getCurrentDate());

        if (diary.isEmpty()) return;
        diaryViewModel.update(diary);

        Log.e(TAG, "更新数据" + diary.getUid() + ":" + diary.getTitle() + " " + diary.getContent());
    }

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
        SimpleDateFormat df3 = new SimpleDateFormat("k:mm", Locale.CHINA);
        String dateValue = df1.format(date);
        String timeValue = df3.format(date);
        StringBuilder time = new StringBuilder();
        time.append(dateValue).append(" ").append(timePartValue).append(timeValue);
        //顺便更新
        binding.diaryDate.setText(time.toString());
        return time.toString();
    }

    private void refreshTextCount() {
        int len = 0;
        if (binding.richEdit.getText() != null)
            len = binding.richEdit.getText().toString().length();
        binding.diaryTextCount.setText(String.valueOf(len));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.skin) {
            Toast.makeText(this, "选择皮肤", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.share) {
            Toast.makeText(this, "分享日记", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.more) {
            moreMenu.showAsDropDown(v, 0, -v.getHeight());
        }
    }

    private void setOnClickListener() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.skin.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.more.setOnClickListener(this);
        binding.editBody.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //empty for nothing to do
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        //animation
    }

}