package edu.uestc.diaryinuestc.ui.bill.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;

@Dao
public interface BillDao {
    @Query("SELECT * FROM bill")
    List<Bill> BillList();

    @Query("SELECT * FROM bill ORDER BY billId DESC")
    List<Bill> getAll();

    @Insert
    void insertBill(Bill... bills);

    @Delete
    void deleteBills(Bill... bills);

    @Update
    void updateBill(Bill... bills);

    @Query("DELETE FROM Bill")
    void deleteAll();

    @Query("SELECT * FROM bill WHERE billId= :id")
    Bill getBillById(int id);

}
