package edu.uestc.diaryinuestc.ui.todo;

public class Todo {
    private String content;
    private int ddl;
    private boolean selected;

    public Todo(String content) {
        this.content = content;
        this.selected = false;
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
}
