package edu.uestc.diaryinuestc.ui.bill.day.bill;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;

public class BillDay {
    private int inAmount;
    private int outAmount;
    private int date1;//日期
    private int date2;//星期
    public List<Bill> billList;

    public BillDay(int date1, int date2) {
        this.date1 = date1;
        this.date2 = date2;
        this.billList = new ArrayList<>();
        inAmount = 0;
        outAmount = 0;
        for (Bill bill:billList){
            if(bill.isIn())
                inAmount+=bill.getAmount();
            else
                outAmount+=bill.getAmount();
        }
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }

    public int getInAmount() {
        return 0;
    }

    public int getOutAmount() {
        return 0;
    }

    public int getDate1() {
        return date1;
    }

    public void setDate1(int date1) {
        this.date1 = date1;
    }

    public int getDate2() {
        return date2;
    }

    public void setDate2(int date2) {
        this.date2 = date2;
    }
}
