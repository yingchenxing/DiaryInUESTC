package edu.uestc.diaryinuestc.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;


import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;

    final List<Diary> mDiaryList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView diaryCover;
        TextView diaryTittle;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            diaryCover = view.findViewById(R.id.diary_cover);
            diaryTittle = view.findViewById(R.id.diary_tittle);
        }
    }

    public DiaryAdapter(List<Diary> diaryList){
        mDiaryList = diaryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_diary,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(view1 -> {
            int position = holder.getAdapterPosition();
            Diary diary = mDiaryList.get(position);

            Intent intent = new Intent(mContext, DiaryActivity.class);
            intent.putExtra(DiaryActivity.DIARY_TITTLE,diary.getTittle());
            intent.putExtra(DiaryActivity.DIARY_Cover_ID,diary.getCoverId());

            mContext.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diary diary = mDiaryList.get(position);
        holder.diaryTittle.setText(diary.getTittle());
        Glide.with(mContext).load(diary.getCoverId()).into(holder.diaryCover);
    }

    @Override
    public int getItemCount() {
        return mDiaryList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDiaryList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mDiaryList.remove(position);
        notifyItemRemoved(position);
    }
}
