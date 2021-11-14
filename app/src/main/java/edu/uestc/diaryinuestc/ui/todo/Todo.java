package edu.uestc.diaryinuestc.ui.todo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int todoID;


    private String content;
    private int ddl;
    private boolean selected;

    public Todo(String content) {
        this.content = content;
        this.selected = false;
        this.ddl = 0;
    }

    public String getContent() {
        return content;
    }

    public int getDdl() {
        return ddl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDdl(int ddl) {
        this.ddl = ddl;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean b){
        this.selected = b;
    }

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }
}
