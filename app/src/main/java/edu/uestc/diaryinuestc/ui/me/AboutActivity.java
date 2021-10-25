package edu.uestc.diaryinuestc.ui.me;

import static java.security.AccessController.getContext;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.app.ComponentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import edu.uestc.diaryinuestc.databinding.ActivityAboutBinding;

import edu.uestc.diaryinuestc.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ThemeSelectActivity.setThemeToActivity(this, null);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_about);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back_32);
        binding.toolbar.setTitle(R.string.about_fragment_label);

        binding.fab.setOnClickListener(this);
        binding.fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("url", "https://github.com/yingchenxing/DiaryInUESTC/issues");
                Toast.makeText(AboutActivity.this, "已复制网址", Toast.LENGTH_SHORT).show();
                cm.setPrimaryClip(mClipData);
                return true;
            }
        });

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!navController.navigateUp())
                    finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_about);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.fab.getId()) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_about);
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.AboutContactFragment) {
                ((WebView) this.findViewById(R.id.web_base)).loadUrl("https://github.com/yingchenxing/DiaryInUESTC/issues");
                Toast.makeText(this, "长按复制网址", Toast.LENGTH_SHORT).show();
            } else {
                Bundle b = new Bundle();
                b.putString("url", "https://github.com/yingchenxing/DiaryInUESTC/issues");
                Toast.makeText(this, "通过GitHub联系我们", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_AboutFragment_to_AboutContactFragment, b);
            }
        }
    }
}