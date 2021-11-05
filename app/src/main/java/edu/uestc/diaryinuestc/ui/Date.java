package edu.uestc.diaryinuestc.ui;

public class Date {
    private int year;
    private int month;
    private int date;
    private int dayOfWeek;
    private int hour;
    private int min;

    public Date(int year, int month, int date, int dayOfWeek, int hour, int min) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.min = min;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
