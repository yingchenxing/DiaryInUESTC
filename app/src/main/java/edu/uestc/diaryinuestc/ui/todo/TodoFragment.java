package edu.uestc.diaryinuestc.ui.todo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentTodoBinding;
import edu.uestc.diaryinuestc.ui.Transluncy;
import edu.uestc.diaryinuestc.ui.todo.database.TodoEngine;

public class TodoFragment extends Fragment implements Transluncy {


    private FragmentTodoBinding binding;
    private List<Todo> todoList = new ArrayList<>();
    private TodoAdapter adapter;
    private TodoEngine todoEngine ;
    private PopupWindow popupTodoAdd;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        todoEngine = new TodoEngine(getContext());
        //加载todo的recyclerView
        initTodo();
        //判断是否显示添加文本
        textView = binding.addTodoText;
        if(todoList.size()!=0){
            textView.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = binding.todoRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,true);

        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);


        //实现todo的拖拽
        SimpleItemTouchHelper callback = new SimpleItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //加载添加todo的fragment
        FloatingActionButton fab = binding.todoFab;
        fab.setOnClickListener(view -> {
            //通过popupWindow实现
            load_popupWindow();
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initTodo() {
        todoList = todoEngine.queryAllTodos();
        Collections.reverse(todoList);//将新的todo排在前面
    }

//    public void refreshTodo() {
//        adapter.notifyDataSetChanged();
//    }

    public void load_popupWindow() {
        View popView = getLayoutInflater().inflate(R.layout.cardview_add_todo, null);
        popupTodoAdd = new PopupWindow(popView, (int) (binding.getRoot().getWidth() * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTodoAdd.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline, null));
        popupTodoAdd.setOutsideTouchable(true);//点击其它退出
        popupTodoAdd.setFocusable(true);//可编辑todo
        popupTodoAdd.setAnimationStyle(R.style.popupWindow_anim_style);//设置动画

        //设置点击事件
        EditText editText = popView.findViewById(R.id.todo_edit);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("todo content", charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        Button button = popView.findViewById(R.id.add_todo_button);

        button.setOnClickListener(view1 -> {
            String content = editText.getText().toString();

            if (content.length()!=0) {
                Todo todo = new Todo(editText.getText().toString());
                //将todo添加到数据库
                todoEngine.insertTodo(todo);
                //将todo添加到UI
                todoList.add(todo);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), 1);
                Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                popupTodoAdd.dismiss();
            } else {
                Toast.makeText(this.getContext(), "无法保存空todo", Toast.LENGTH_SHORT).show();
            }
            if(todoList.size()!=0){
                textView.setVisibility(View.GONE);
            }
        });

        popupTodoAdd.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

//淡化背景
        backgroundAlpha(0.5f);
        popupTodoAdd.setOnDismissListener(() -> backgroundAlpha(1.0f));
    }

    @Override
    public void backgroundAlpha(float bg_alpha) {
        Activity activity = requireActivity();
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }


}