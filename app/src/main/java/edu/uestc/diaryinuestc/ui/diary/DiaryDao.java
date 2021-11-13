package edu.uestc.diaryinuestc.ui.diary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DiaryDao {

    @Query("SELECT * FROM diary")
    LiveData<List<Diary>> getAllDiaries();

    @Query("SELECT * FROM diary ORDER BY millis DESC")
    LiveData<List<Diary>> getOrderDiaries();

    @Query("SELECT * FROM diary WHERE uid=:diaryId")
    Diary getDiary(long diaryId);

    @Insert
    long insert(Diary diary);

    @Delete
    void delete(Diary... diaries);

    @Update
    void update(Diary... diaries);
}
