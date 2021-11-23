package edu.uestc.diaryinuestc.ui.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int todoID;


    private String content;
    private boolean selected;

    private int Year=-1;
    private int Month=-1;
    private int Day=-1;

    private int Hour=-1;
    private int Min=-1;

    public Todo(String content, int year, int month, int day, int hour, int min) {
        this.content = content;
        Year = year;
        Month = month;
        Day = day;
        Hour = hour;
        Min = min;
    }

    public Todo(String content) {
        this.content = content;
        this.selected = false;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        this.selected = b;
    }

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }



    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        Hour = hour;
    }

    public int getMin() {
        return Min;
    }

    public void setMin(int min) {
        Min = min;
    }
}
