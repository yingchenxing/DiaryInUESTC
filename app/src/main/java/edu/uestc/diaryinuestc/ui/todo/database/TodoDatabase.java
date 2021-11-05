package edu.uestc.diaryinuestc.ui.todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.uestc.diaryinuestc.ui.todo.Todo;

@Database(entities = {Todo.class},version = 1,exportSchema = false)
public abstract
class TodoDatabase extends RoomDatabase {
    public abstract TodoDao getTodoDao();
    //单例模式 返回DB
    private static TodoDatabase INSTANCE;
    public static synchronized TodoDatabase getInstance(Context context){
        if(INSTANCE ==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TodoDatabase.class,"todo_database").build();
            //
        }
        return INSTANCE;
    }
}
