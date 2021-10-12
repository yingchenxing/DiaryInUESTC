package edu.uestc.diaryinuestc.ui.todo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;

    private final List<Todo> mTodoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CheckBox todoContent;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            todoContent = view.findViewById(R.id.todo_content);
        }
    }

    public TodoAdapter(List<Todo> todoList) {
        mTodoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (mContext == null) {
//            mContext = parent.getContext();
//        }
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false);
        final TodoAdapter.ViewHolder holder = new TodoAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position;
        Todo todo = mTodoList.get(i);

        //加入点击划线功能
        holder.todoContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                todo.setSelected(b);
                if (b) {
                    holder.todoContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加中划线
                    holder.todoContent.setTextColor(Color.parseColor("#C0C0C0"));
                } else {
                    holder.todoContent.getPaint().setFlags(0);//回复划线
                    holder.todoContent.setTextColor(Color.parseColor("#000000"));
                }
                mTodoList.set(i, todo);
            }
        });

        //将监听点击的范围扩展到cardview
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.todoContent.setChecked(!holder.todoContent.isChecked());
            }
        });

        //生成item
        holder.todoContent.setText(todo.getContent());
        holder.todoContent.setChecked(todo.getSelected());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    //item上下拖动实现换位效果
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mTodoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    //item左右拖动实现删除、编辑效果
    @Override
    public void onItemDismiss(int position) {
        mTodoList.remove(position);
        notifyItemRemoved(position);
    }

}
