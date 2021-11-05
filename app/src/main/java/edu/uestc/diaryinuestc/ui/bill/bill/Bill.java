package edu.uestc.diaryinuestc.ui.bill.bill;

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
    public static final int PAYMENT = 1;
    public static final int BONUS = 2;
    public static final int FAVOR = 3;
    public static final int REDBAGIN = 4;
    public static final int TRANSFER = 5;
    //支出
    public static final int RECREATION = 6;
    public static final int MEDICATION = 7;
    public static final int EATING = 8;
    public static final int CLOTHING = 9;
    public static final int TRAVELLING = 10;
    public static final int EDUCATION = 11;
    public static final int TRAFFIC = 12;
    public static final int REDBAGOUT = 13;
    //其他
    public static final int ELSE = 14;

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

    //提供账单类型转换器
    public String getTypeName() {
        switch (this.type) {
            case PAYMENT:
                return "工资";
            case BONUS:
                return "奖金";
            case FAVOR:
                return "";
            case REDBAGOUT:
            case REDBAGIN:
                return "红包";
            case TRANSFER:
                return "转账";
            //支出
            case RECREATION:
                return "娱乐";
            case MEDICATION:
                return "医疗";
            case EATING:
                return "吃喝";
            case CLOTHING:
                return "衣物";
            case TRAVELLING:
                return "旅行";
            case EDUCATION:
                return "教育";
            case TRAFFIC:
                return "交通";
            //其他
            case ELSE:
                return "其他";
            default:
                return null;
        }
    }

    public int getImgId() {
        return 0;
    }
}
