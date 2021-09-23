package edu.uestc.diaryinuestc.ui.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import edu.uestc.diaryinuestc.R;

public class DiaryActivity extends AppCompatActivity {

    public static final String DIARY_TITTLE = "diayt_title";

    public static final String DIARY_Cover_ID = "fruit_cover_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Intent intent = getIntent();
        String diaryTittle = intent.getStringExtra(DIARY_TITTLE);
        int diaryCoverId = intent.getIntExtra(DIARY_Cover_ID,0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        ImageView diaryImageView = findViewById(R.id.diary_image_view);
        TextView diaryContentText = findViewById(R.id.diary_content_text);
        //setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(diaryTittle);
        Glide.with(this).load(diaryCoverId).into(diaryImageView);
        String diaryContent = generaterDiaryContent(diaryTittle);
        diaryContentText.setText(diaryContent);

    }

    private String generaterDiaryContent(String diaryName){
        StringBuilder diaryContent = new StringBuilder();
        for(int i =0;i<500;i++){
            diaryContent.append(diaryName);
        }
        return diaryContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}