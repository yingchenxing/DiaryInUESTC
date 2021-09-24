package edu.uestc.diaryinuestc.ui.todo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import edu.uestc.diaryinuestc.R;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private Context mContext;

    private final List<Todo> mTodoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        CheckBox todoContent;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            todoContent = view.findViewById(R.id.todo_content);
        }
    }

    public TodoAdapter(List<Todo> todoList){
        mTodoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todo,parent,false);
        final TodoAdapter.ViewHolder holder = new TodoAdapter.ViewHolder(view);
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
        Todo todo = mTodoList.get(position);
        holder.todoContent.setText(todo.getContent());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }
}
