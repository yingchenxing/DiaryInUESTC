package edu.uestc.diaryinuestc.ui.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Objects;

import edu.uestc.diaryinuestc.databinding.ActivityThemeSelectBinding;

public class ThemeSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThemeSelectActivity";
    public static final String THEME = "DiaryInUESTCTheme";
    public static final String COLOR = "DiaryInUESTCThemeColor";

    private ActivityThemeSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemeSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.themeSelectToolbar.setNavigationOnClickListener(v -> finish());
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