package edu.uestc.diaryinuestc.ui.diary;

import android.annotation.SuppressLint;
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

import java.util.Collections;
import java.util.List;


import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private List<Diary> mDiaryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView diaryCover;
        TextView diaryTitle;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            diaryCover = view.findViewById(R.id.diary_cover);
            diaryTitle = view.findViewById(R.id.diary_edit_title);
        }
    }

    public DiaryAdapter(Context context) {
        mContext = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDiaries(List<Diary> diaryList) {
        mDiaryList = diaryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_diary, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mDiaryList != null) {
            Diary diary = mDiaryList.get(position);
            holder.diaryTitle.setText(diary.getTitle());
            holder.cardView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, DiaryActivity.class);
                intent.putExtra(DiaryActivity.DIARY_ID, diary.getUid());
                mContext.startActivity(intent);
            });
//        Glide.with(mContext).load(diary.getCoverId()).into(holder.diaryCover);
        } else {
            holder.diaryTitle.setText("Data Not FOUND");
        }
    }

    @Override
    public int getItemCount() {
        if (mDiaryList == null)
            return 0;
        return mDiaryList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDiaryList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mDiaryList.remove(position);
        notifyItemRemoved(position);
    }
}
