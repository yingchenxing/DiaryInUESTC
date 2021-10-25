package edu.uestc.diaryinuestc.ui;

import androidx.room.Entity;

@Entity
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition,int toPosition);

    void onItemDismiss(int position);
}
