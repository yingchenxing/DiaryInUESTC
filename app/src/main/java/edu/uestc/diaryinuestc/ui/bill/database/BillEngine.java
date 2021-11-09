package edu.uestc.diaryinuestc.ui.bill.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.todo.Todo;
import edu.uestc.diaryinuestc.ui.todo.database.TodoDao;
import edu.uestc.diaryinuestc.ui.todo.database.TodoEngine;

public class BillEngine {
    final BillDao billDao;

    public BillEngine(Context context) {
        BillDatabase billDatabase = BillDatabase.getINSTANCE(context);
        billDao = billDatabase.getBillDao();
    }

    //插入
    public void insertBill(Bill... bills) {
        new InsertAsyncTask(billDao).execute(bills);
    }

    static class InsertAsyncTask extends AsyncTask<Bill, Void, Void> {
        final BillDao dao;

        public InsertAsyncTask(BillDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Bill... bills) {
            dao.insertBill(bills);
            return null;
        }
    }

    //更新
    public void updateBills(Bill... bills) {
        new UpdateAsyncTack(billDao).execute(bills);
    }

    static class UpdateAsyncTack extends AsyncTask<Bill, Void, Void> {
        final BillDao dao;

        public UpdateAsyncTack(BillDao billDao) {
            dao = billDao;
        }

        @Override
        protected Void doInBackground(Bill... bills) {
            dao.updateBill(bills);
            return null;
        }
    }

    //删除
    public void deleteBills(Bill... bills) {
        new DeleteAsyncTack(billDao).execute(bills);
    }

    static class DeleteAsyncTack extends AsyncTask<Bill, Void, Void> {
        private BillDao dao;

        public DeleteAsyncTack(BillDao billDao) {
            dao = billDao;
        }

        @Override
        protected Void doInBackground(Bill... bills) {
            dao.deleteBills(bills);
            return null;
        }
    }

    //全部删除
    public void deleteAll() {
        new DeleteAllAsyncTack(billDao).execute();
    }

    static class DeleteAllAsyncTack extends AsyncTask<Void, Void, Void> {
        private BillDao dao;

        public DeleteAllAsyncTack(BillDao billDao) {
            dao = billDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    //查询
    public List<Bill> queryAllBills() {

        List<Bill> billList = null;
        try {
            billList = new QueryAsyncTack(billDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return billList;
    }

    static class QueryAsyncTack extends AsyncTask<Void, Void, List<Bill>> {
        private BillDao dao;

        public QueryAsyncTack(BillDao billDao) {
            dao = billDao;
        }

        @Override
        protected List<Bill> doInBackground(Void... voids) {
            List<Bill> billList = dao.getAll();
            for (Bill bill : billList) {
                Log.i("BILL", bill.toString());
            }
            return billList;
        }
    }

    //根据id查找
    public Bill queryById(int id) {
        List<Bill> bills = queryAllBills();
        for (Bill bill:bills){
            if(bill.getBillId()==id)
                return bill;
        }
        return null;
    }

}
