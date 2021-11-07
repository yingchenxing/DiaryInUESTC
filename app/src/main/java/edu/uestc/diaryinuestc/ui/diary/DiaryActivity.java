package edu.uestc.diaryinuestc.ui.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import edu.uestc.diaryinuestc.databinding.ActivityDiaryBinding;

public class DiaryActivity extends AppCompatActivity {

    public static final String DIARY_ID = "uid";
    private static final String TAG = DiaryActivity.class.getSimpleName();

    private ActivityDiaryBinding binding;
    private DiaryViewModel diaryViewModel;
    private long diary_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        diaryViewModel = ViewModelProviders.of(this).get(DiaryViewModel.class);
        //load diary
        Intent intent = getIntent();
        diary_id = intent.getLongExtra(DIARY_ID, 0);

        LiveData<Diary> diary1 = diaryViewModel.getDiary(diary_id);
        if (diary1 == null) {
            Toast.makeText(this, "fail to load diary", Toast.LENGTH_LONG).show();
            Log.e(TAG, "fail to load diary uid:" + diary_id);
        } else {
            diary1.observe(this, diary -> {
                binding.collapsingToolbarLayout.setTitle(diary.getTitle());
                binding.diaryContentText.setText(diary.getContent());
            });
        }
//        Glide.with(this).load(diaryCoverId).into(diaryImageView);图片功能后置

//        String diaryContent = generaterDiaryContent(diaryTittle);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.collapsingToolbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.NEW_TAG, false);
                intent.putExtra(EditActivity.DIARY_ID, diary_id);
                intent.putExtra(EditActivity.SELECT, 0);//click title
                startActivity(intent);
            }
        });
        binding.diaryContentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.NEW_TAG, false);
                intent.putExtra(EditActivity.DIARY_ID, diary_id);
                intent.putExtra(EditActivity.SELECT, 1);//click title
                startActivity(intent);
            }
        });
    }

//for test
//    private String generaterDiaryContent(String diaryName) {
//        StringBuilder diaryContent = new StringBuilder();
//        for (int i = 0; i < 500; i++) {
//            diaryContent.append(diaryName);
//        }
//        return diaryContent.toString();
//    }
}