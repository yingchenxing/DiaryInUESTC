package edu.uestc.diaryinuestc.ui.todo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;

class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter adapter;

    public SimpleItemTouchHelper(ItemTouchHelperAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //拖拽可用
        int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        //拖拽不可用
        //int dragFlags = 0;

        int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags,swipeFlags);
    }


    /**
     * 上下拖动，
     * return true;//可以滑动
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        adapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setScrollX(0);
    }
}
