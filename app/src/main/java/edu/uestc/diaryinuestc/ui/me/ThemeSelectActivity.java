package edu.uestc.diaryinuestc.ui.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.uestc.diaryinuestc.databinding.ActivityThemeSelectBinding;

public class ThemeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThemeSelectActivity";
    public static final String THEME_KEY = "DiaryInUESTCTheme";
    public static final String COLOR_KEY = "DiaryInUESTCThemeColor";

    private static int color = 0;

    private ActivityThemeSelectBinding binding;
    private int colorSelectCode;
    private View selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //读取用户默认主题
//        themePreferences = getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE);
//        themePreferences.getInt(COLOR_KEY, 1);

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.themeSelectToolbar.setNavigationOnClickListener(v -> finish());
    }

    public static int getThemeColor(Context context) {
        SharedPreferences themePreferences = context.getSharedPreferences(THEME_KEY, Context.MODE_PRIVATE);
        return Code2Color(themePreferences.getInt(COLOR_KEY, 0));
    }

    private static int Code2Color(int code) {
        if (code < 0 || code > 8) {
            Log.e(TAG, "使用了非法code 函数返回默认值");
            return Color.WHITE;
        }
        int[] array = new int[]{Color.WHITE, Color.WHITE, 0xFFF19EC2, Color.BLACK, 0xFFF44436, 0xFFFFC108, 0xFF8BC24B, 0xFF2195F3, 0xFF9B27B0};
        return array[code];
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}