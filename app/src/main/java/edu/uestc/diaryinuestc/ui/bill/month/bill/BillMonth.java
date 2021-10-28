package edu.uestc.diaryinuestc.ui.bill.month.bill;

import java.util.List;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;

public class BillMonth {
    private List<Bill> billList;
    private int inAmount;
    private int outAmount;
    private int date;

    public BillMonth(List<Bill> billList, int date) {
        this.billList = billList;
        this.date = date;
        inAmount = 0;
        outAmount = 0;
        for (Bill bill:billList){
            if(bill.isIn())
                inAmount+=bill.getAmount();
            else
                outAmount+=bill.getAmount();
        }
    }

    public int getAmountOfType(int type){
        int sum = 0;
        for(Bill bill:billList){
            if(bill.isIn()){
                if(bill.getType()==type)
                    sum+=bill.getAmount();
            }
        }
        return sum;
    }

    public int getInAmount() {
        return inAmount;
    }

    public int getOutAmount() {
        return outAmount;
    }

    public int getDate() {
        return date;
    }
}
