package edu.uestc.diaryinuestc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemeUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.uestc.diaryinuestc.databinding.ActivityMainBinding;
import edu.uestc.diaryinuestc.ui.me.MeFragment;
import edu.uestc.diaryinuestc.ui.me.ThemeSelectActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private SharedPreferences themePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeSelectActivity.setThemeToActivity(this, null);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_diary, R.id.nav_bill, R.id.nav_todo, R.id.nav_me)
//                .build();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //切换fragment
        Intent intent = getIntent();
        if (intent != null) {
            int fragment = intent.getIntExtra("Fragment", 0);
            switch (fragment) {
                default:
                case 1:
                    navController.navigate(R.id.nav_diary);
                    break;
                case 2:
                    navController.navigate(R.id.nav_bill);
                    break;
                case 3:
                    navController.navigate(R.id.nav_todo);
                    break;
                case 4:
                    navController.navigate(R.id.nav_me);
                    break;
            }
        }
    }
}