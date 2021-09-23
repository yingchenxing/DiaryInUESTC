package edu.uestc.diaryinuestc.ui.diary;

public class Diary {
    private String tittle;
    private int coverId;
    private String content;

    public Diary (String tittle,int coverId){
        this.tittle = tittle;
        this.coverId = coverId;
    }

    public String getTittle(){
        return this.tittle;
    }

    public int getCoverId(){
        return this.coverId;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setTittle (String tittle){
        this.tittle = tittle;
    }

    public void setCoverId(int coverId){
        this.coverId = coverId;
    }

}
