package edu.uestc.diaryinuestc.ui.diary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Diary {
    @ColumnInfo(name = "uid")
    @PrimaryKey(autoGenerate = true)
    private long uid;
    @ColumnInfo(name = "cover_ID")
    private int coverId;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;

    public Diary() {
    }

    @Ignore
    public Diary(long uid) {
        this.uid = uid;
    }

    public boolean isEmpty() {
        return content == null && title == null;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setCoverId(int coverId) {
        this.coverId = coverId;
    }

    public int getCoverId() {
        return this.coverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
