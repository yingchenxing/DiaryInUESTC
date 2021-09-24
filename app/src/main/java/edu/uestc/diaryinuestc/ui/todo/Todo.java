package edu.uestc.diaryinuestc.ui.todo;

public class Todo {
    private String content;
    private int ddl;

    public Todo(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getDdl(){
        return ddl;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setDdl(int ddl){
        this.ddl = ddl;
    }
}
