package edu.uestc.diaryinuestc.ui.todo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.uestc.diaryinuestc.ui.todo.Todo;

@Dao
public interface TodoDao {
    @Query("SELECT * FROM todo")
    List<Todo> TodoList();

    @Query("SELECT * FROM todo ORDER BY todoID DESC")
    List<Todo> getAll();

    @Insert
    void insertTodo(Todo... todos);

    @Delete
    void deleteTodos(Todo... todos);

    @Update
    void updateTodos(Todo...todos);

    @Query("DELETE FROM todo")
    void deleteAll();

}
