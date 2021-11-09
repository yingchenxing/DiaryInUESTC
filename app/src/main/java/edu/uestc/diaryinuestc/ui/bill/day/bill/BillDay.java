package edu.uestc.diaryinuestc.ui.bill.day.bill;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;

public class BillDay {
    private int inAmount;
    private int outAmount;
    private int month;//日期
    private int date1;//日期
    private int date2;//星期
    public List<Bill> billList;

    public BillDay(int month, int date1, int date2) {
        this.date1 = date1;
        this.date2 = date2;
        this.month = month;
        this.billList = new ArrayList<>();
        inAmount = 0;
        outAmount = 0;
        for (Bill bill : billList) {
            if (bill.isIn())
                inAmount += bill.getAmount();
            else
                outAmount += bill.getAmount();
        }
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public double getInAmount() {
        double amount = 0;
        for (Bill bill : billList) {
            if (bill.isIn())
                amount += bill.getAmount();
        }
        return amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getOutAmount() {

        double amount = 0;
        for (Bill bill : billList) {
            if (!bill.isIn())
                amount += bill.getAmount();
        }
        return amount;
    }

    public int getDate1() {
        return date1;
    }

    public void setDate1(int date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        switch (date2) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";

            default:
                return null;
        }
    }

    public void setDate2(int date2) {
        this.date2 = date2;
    }
}
