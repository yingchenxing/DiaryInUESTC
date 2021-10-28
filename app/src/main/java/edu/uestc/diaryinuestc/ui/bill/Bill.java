package edu.uestc.diaryinuestc.ui.bill;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bill {
    @PrimaryKey
    private int ID;//主键

    //账单属性
    private int date;//日期
    private int type;//收支类型
    private double amount;//金额
    private boolean in;//判断收入还是支出（0为支出，1为收入）
    private String content;//账单注释

    //定义常量，收款
    public static int PAYMENT = 1;
    public static int BONUS = 2;
    public static int FAVOR = 3;
    public static int REDBAG = 4;
    public static int TRANSFER = 5;
    //支出
    public static int RECREATION = 6;
    public static int MEDICATION = 7;
    public static int EATING = 8;
    public static int CLOTHING = 9;
    public static int TRAVELLING = 10;
    public static int EDUCATION = 11;
    public static int TRAFFIC = 12;
    //其他
    public static int ELSE = 13;

    public Bill(int date, int type, double amount, boolean in, String content) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.in = in;
        this.content = content;
    }

    public Bill(int date, double amount, boolean in) {
        this.date = date;
        this.amount = amount;
        this.in = in;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
