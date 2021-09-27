package edu.uestc.diaryinuestc.ui.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


import com.shehuan.niv.NiceImageView;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityThemeSelectBinding;

public class ThemeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThemeSelectActivity";
    public static final String THEME_KEY = "DiaryInUESTCTheme";
    public static final String COLOR_KEY = "DiaryInUESTCThemeColor";

    private static int colorCode = 0;
    private static ConstraintLayout[] groups;
    private static ConstraintLayout[] colorCards;
    private static NiceImageView[] colorImages;
    private static CheckBox[] colorCheckBoxes;

    private ActivityThemeSelectBinding binding;
    private SharedPreferences themePreferences;
    private SharedPreferences.Editor editor;
    private Drawable background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        background = ResourcesCompat.getDrawable(getResources(), R.drawable.round_border_line, null);

        //设置数组
        groups = new ConstraintLayout[]{null, binding.group1, binding.group2, binding.group3, binding.group4,
                binding.group5, binding.group6, binding.group7, binding.group8};
        colorCards = new ConstraintLayout[]{null, binding.colorCard1, binding.colorCard2, binding.colorCard3,
                binding.colorCard4, binding.colorCard5, binding.colorCard6, binding.colorCard7, binding.colorCard8};
        colorImages = new NiceImageView[]{null, binding.colorImage1, binding.colorImage2, binding.colorImage3,
                binding.colorImage4, binding.colorImage5, binding.colorImage6, binding.colorImage7, binding.colorImage8};
        colorCheckBoxes = new CheckBox[]{null, binding.colorCheckBox1, binding.colorCheckBox2,
                binding.colorCheckBox3, binding.colorCheckBox4, binding.colorCheckBox5,
                binding.colorCheckBox6, binding.colorCheckBox7, binding.colorCheckBox8};

        //读取用户默认主题并勾选
        themePreferences = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE);
        editor = themePreferences.edit();
        colorCode = themePreferences.getInt(COLOR_KEY, 1);
        if (!checkCode(colorCode)) {
            colorCode = 1;
            editor.putInt(COLOR_KEY, colorCode);
            editor.commit();
        }
        colorCheckBoxes[colorCode].setChecked(true);
        colorCards[colorCode].setBackground(background);
        binding.themeSelectToolbar.setBackgroundColor(getThemeColor(this));
        binding.themeTitleText.setTextColor(getTextColor(this));

        //操作click
        setOnClickListener();

    }

    @Override
    public void onClick(View v) {
        int code = ArrayUtils.indexOf(groups, v);
        if (!checkCode(code)) {
            Log.e(TAG, "有问题的ColorCode:" + code + " view:" + v.toString());
            Toast.makeText(this, "颜色修改失败", Toast.LENGTH_LONG).show();
            return;
        }
        if (code != colorCode) {
            colorCheckBoxes[colorCode].setChecked(false);
            colorCheckBoxes[code].setChecked(true);
            colorCards[colorCode].setBackgroundResource(0);
            colorCards[code].setBackground(background);
            colorCode = code;
            editor.putInt(COLOR_KEY, colorCode);
            editor.commit();
        }
        binding.themeSelectToolbar.setBackgroundColor(Code2Color(code));
        binding.themeTitleText.setTextColor(getTextColor(this));
    }

    private void setOnClickListener() {
        binding.themeSelectToolbar.setNavigationOnClickListener(v -> finish());
        for (View view : groups)
            if (view != null) view.setOnClickListener(this);
    }

    public static int getThemeColor(Context context) {
        SharedPreferences themePreferences = context.getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE);
        return Code2Color(themePreferences.getInt(COLOR_KEY, 1));
    }

    public static int getTextColor(Context context) {
        SharedPreferences themePreferences = context.getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE);
        int color = themePreferences.getInt(COLOR_KEY, 1);
        if (color == 3 || color == 4 || color == 8) return Color.WHITE;
        return Color.BLACK;
    }

    private static int Code2Color(int code) {
        if (code < 0 || code > 8) {
            Log.e(TAG, "使用了非法code 函数返回默认值");
            return Color.WHITE;
        }
        int[] array = new int[]{Color.WHITE, Color.WHITE, 0xFFF19EC2, 0xFF282828, 0xFFF44436, 0xFFFFC108, 0xFF8BC24B, 0xFF2195F3, 0xFF9B27B0};
        return array[code];
    }

    private boolean checkCode(int code) {
        if (code <= 0 || code > 8) {
            Log.e(TAG, "有问题的colorCode:" + colorCode);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}