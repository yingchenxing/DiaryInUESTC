package edu.uestc.diaryinuestc.ui.diary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Diary.class}, version = 1,exportSchema = false)
public abstract class DiaryDataBase extends RoomDatabase {

    public abstract DiaryDao diaryDao();

    private static DiaryDataBase INSTANCE;

    public static DiaryDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    DiaryDataBase.class, "DiaryDataBase").allowMainThreadQueries().build();
        }

        return INSTANCE;
    }
}
