package edu.uestc.diaryinuestc.ui;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition,int toPosition);

    void onItemDissmiss(int position);
}
