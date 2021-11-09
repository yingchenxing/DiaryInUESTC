package edu.uestc.diaryinuestc.ui.bill.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.security.cert.Extension;

import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.todo.Todo;
import edu.uestc.diaryinuestc.ui.todo.database.TodoDao;

@Database(entities = {Bill.class}, version = 1, exportSchema = false)
public abstract class BillDatabase extends RoomDatabase {

    public abstract BillDao getBillDao();

    //单例模式，返回DB
    private static edu.uestc.diaryinuestc.ui.bill.database.BillDatabase INSTANCE;

    public static synchronized edu.uestc.diaryinuestc.ui.bill.database.BillDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    edu.uestc.diaryinuestc.ui.bill.database.BillDatabase.class, "bill_database").build();
        }
        return INSTANCE;
    }
}
