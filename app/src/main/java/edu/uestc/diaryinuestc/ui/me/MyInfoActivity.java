package edu.uestc.diaryinuestc.ui.me;

import android.app.DatePickerDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LifecycleOwner;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.util.Calendar;
import java.util.Objects;

import edu.uestc.diaryinuestc.MainActivity;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.ActivityMyInfoBinding;

public class MyInfoActivity extends AppCompatActivity {

    private static final String TAG = "MyInfoActivity";
    private ActivityMyInfoBinding binding;
    static private SettingsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeSelectActivity.setThemeToActivity(this, null);
        binding = ActivityMyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            String openKey = getIntent().getStringExtra("open");
            fragment = new SettingsFragment();
            if (openKey != null) {
                Bundle b = new Bundle();
                b.putString("open", openKey);
                fragment.setArguments(b);
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.myInfoSettings.getId(), fragment)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements DialogPreference.TargetFragment {

        private SharedPreferences preference;
        private Preference birth;
        private EditTextPreference sign;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.my_info_preferences, rootKey);
            preference = PreferenceManager.getDefaultSharedPreferences(requireContext());

            //birth设置summary
            birth = findPreference("birth");
            assert birth != null;
            birth.setSummaryProvider(DatePreference.DateSummaryProvider.getInstance());

            //sign设置字数限制
            sign = findPreference("sign");
            assert sign != null;
            sign.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
//                    editText.setInputType(InputType.TYPE_CLASS_NUMBER); // set only numbers allowed to input
                    editText.selectAll(); // Select all text
                    int maxLength = 100;
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)}); // set maxLength to 2

                }
            });
        }

        @Override
        public void onStart() {
            super.onStart();
            if (getArguments() != null) {
                String openKey = getArguments().getString("open", null);
                if (openKey != null) {
                    Preference preference = findPreference(openKey);
                    if (preference != null)
                        getPreferenceManager().showDialog(preference);
                }
            }
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            if (preference instanceof DatePreference) {
                final DialogFragment f;
                f = DatePreferenceDialogFragment.newInstance(preference.getKey());
                this.getParentFragmentManager().setFragmentResultListener
                        ("0", this.getViewLifecycleOwner(), new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                if (requestKey.equals("0")) {
                                    birth.callChangeListener(result.getString("birth"));
                                }
                            }
                        });

                f.setTargetFragment(this, 0);
                this.getParentFragmentManager().beginTransaction().add(f, "birth").commit();
//                f.show(getParentFragmentManager(), null);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TaskStackBuilder.create(this)
                    .addNextIntent(new Intent(this, MainActivity.class).putExtra("Fragment", 4))
                    .startActivities();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void setOnClickListener() {
        binding.themeSelectToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyInfoActivity.this.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
            }
        });
    }
}