package edu.uestc.diaryinuestc.ui.todo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.ItemTouchHelperAdapter;
import edu.uestc.diaryinuestc.ui.todo.database.TodoEngine;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;

    private final List<Todo> mTodoList;
    private TodoEngine todoEngine = new TodoEngine(mContext);

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CheckBox todoContent;
        LinearLayout linearLayout;
        TextView todoDate;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view;
            cardView = view.findViewById(R.id.todo_cardview);
            todoContent = view.findViewById(R.id.todo_content);
            todoDate = view.findViewById(R.id.todo_date);
        }
    }

    public TodoAdapter(List<Todo> todoList) {
        mTodoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_todo, parent, false);
        final TodoAdapter.ViewHolder holder = new TodoAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position;
        Todo todo = mTodoList.get(i);

        //????????????????????????
        holder.todoContent.setOnCheckedChangeListener((compoundButton, b) -> {
            todo.setSelected(b);
            if (b) {
                holder.todoContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//???????????????
                holder.todoContent.setTextColor(Color.parseColor("#C0C0C0"));
            } else {
                holder.todoContent.getPaint().setFlags(0);//????????????
                holder.todoContent.setTextColor(Color.parseColor("#FF65696E"));
            }
            new TodoEngine(mContext).updateTodos(todo);
            mTodoList.set(i, todo);
        });

        //?????????????????????????????????cardview
        holder.cardView.setOnClickListener(view -> holder.todoContent.setChecked(!holder.todoContent.isChecked()));

        //??????item
        holder.todoContent.setText(todo.getContent());
        holder.todoContent.setChecked(todo.getSelected());
        if (todo.getYear() != -1 && todo.getHour() != -1 && todo.getMin() > 10)
            holder.todoDate.setText(todo.getMonth() + "???" + todo.getDay() + "???  " + todo.getHour() + ":" + todo.getMin());
        else if (todo.getYear() != -1 && todo.getHour() != -1 && todo.getMin() < 10)
            holder.todoDate.setText(todo.getMonth() + "???" + todo.getDay() + "???  " + todo.getHour() + ":0" + todo.getMin());
        else if (todo.getYear() == -1 && todo.getHour() != -1 && todo.getMin() > 10)
            holder.todoDate.setText(todo.getHour() + ":" + todo.getMin());
        else if (todo.getYear() == -1 && todo.getHour() != -1 && todo.getMin() < 10)
            holder.todoDate.setText(todo.getHour() + ":0" + todo.getMin());
        else
            holder.todoDate.setText(null);
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    //item??????????????????????????????
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //?????????????????????
        Todo todo1 = mTodoList.get(fromPosition);
        Todo todo2 = mTodoList.get(toPosition);
        int tempID = todo1.getTodoID();
        todo1.setTodoID(todo2.getTodoID());
        todo2.setTodoID(tempID);
        todoEngine.updateTodos(todo1, todo2);
        //???UI?????????
        Collections.swap(mTodoList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    //item???????????????????????????????????????
    @Override
    public void onItemDismiss(int position) {
        //?????????????????????
        Todo todo = mTodoList.get(position);
        todoEngine.deleteTodos(todo);
        //???UI?????????
        mTodoList.remove(position);
        notifyItemRemoved(position);
    }

}
