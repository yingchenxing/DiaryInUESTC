package edu.uestc.diaryinuestc.ui.diary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import edu.uestc.diaryinuestc.databinding.ItemDiaryGridBinding;
import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.GridViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = DiaryAdapter.class.getName();
    private final Context mContext;
    private final DiaryViewModel diaryViewModel;
    private List<Diary> mDiaryList = new ArrayList<>();

    static class GridViewHolder extends RecyclerView.ViewHolder {
        ItemDiaryGridBinding binding;

        public GridViewHolder(ItemDiaryGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public DiaryAdapter(@NonNull Context mContext, @NonNull DiaryViewModel diaryViewModel) {
        this.mContext = mContext;
        this.diaryViewModel = diaryViewModel;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDiaryList(List<Diary> diaryIDList) {
        mDiaryList.clear();
        mDiaryList.addAll(diaryIDList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridViewHolder(ItemDiaryGridBinding.inflate(LayoutInflater.from(mContext)));
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        if (mDiaryList == null || mDiaryList.size() == 0) {
            Toast.makeText(mContext, "异常数据处理：" + position + holder.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "ERROR IN onBindViewHolder with null diary list but still onBind");
            return;
        }
        Diary diary = mDiaryList.get(position);

        //deal with Diary
        if (diary == null) return;
        if (diary.isEmpty(mContext)) {
//            Toast.makeText(mContext, "异常数据处理：日记为空但仍存在", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "ERROR IN onBindViewHolder with empty diary uid:" + diary.getUid());
//            diaryViewModel.delete(diary);
//            notifyItemRemoved(holder.getAdapterPosition());
            Log.d(TAG, "empty not load" + diary.getUid());
            return;
        }
        //set preview
        String[] preview = diary.requirePreview();
        Log.d(TAG, Arrays.toString(preview) + diary);
        if (preview[0].length() == 0)
            holder.binding.diaryPreviewTitle.getLayoutParams().height = 0;
        else
            holder.binding.diaryPreviewTitle.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (preview[1].length() == 0)
            holder.binding.diaryPreviewContent.getLayoutParams().height = 0;
        else
            holder.binding.diaryPreviewContent.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;


        holder.binding.diaryPreviewTitle.setText(preview[0]);
        holder.binding.diaryPreviewContent.setText(preview[1]);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月d日", Locale.CHINA);
        String dateValue = df1.format(date);
        if (dateValue.equals(diary.getDate().split(" ")[0]))
            holder.binding.diaryPreviewDate.setText("今天 " + diary.getDate().split(" ")[1]);
        else if (dateValue.substring(0, 3).equals(diary.getDate().substring(0, 3)))
            holder.binding.diaryPreviewDate.setText("今年 " + diary.getDate().substring(5));
        else
            holder.binding.diaryPreviewDate.setText(diary.getDate());


        //load cover through view model
        Bitmap cover = diary.requireCover(mContext);
        Glide.with(mContext).load(cover).into(holder.binding.diaryCover);
//        holder.binding.diaryCover.setImageBitmap(cover);

        //set onclick intent
        holder.binding.diaryCard.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditActivity.class);
            intent.putExtra(EditActivity.DIARY_ID, diary.getUid());
            intent.putExtra(EditActivity.NEW_TAG, false);

//            holder.binding.

            Pair<View, String> pairCover = new Pair<>(holder.binding.diaryCover, holder.binding.diaryCover.getTransitionName());
            Pair<View, String> pairCard = new Pair<>(holder.binding.diaryCard, holder.binding.diaryCard.getTransitionName());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,pairCover);

            mContext.startActivity(intent, options.toBundle());
        });

    }

    @Override
    public int getItemCount() {
        if (mDiaryList == null)
            return 0;
        return mDiaryList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mDiaryList, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
//        mDiaryList.remove(position);
//        notifyItemRemoved(position);
    }
}