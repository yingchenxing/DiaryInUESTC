package edu.uestc.diaryinuestc.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.uestc.diaryinuestc.R;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
    private Context mContext;

    private List<Diary> mDiaryList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView diaryCover;
        TextView diaryTittle;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            diaryCover = (ImageView) view.findViewById(R.id.diary_cover);
            diaryTittle = (TextView) view.findViewById(R.id.diary_tittle);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(parent.getContext(),"未设置点击事件",Toast.LENGTH_SHORT).show();
            }
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
}