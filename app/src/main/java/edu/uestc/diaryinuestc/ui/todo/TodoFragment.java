package edu.uestc.diaryinuestc.ui.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentTodoBinding;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private FragmentTodoBinding binding;
    private List<Todo> todoList = new ArrayList<>();
    private TodoAdapter adapter;
    //    private SwipeRefreshLayout swipeRefresh;
    private PopupWindow popupTodoAdd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

////        final TextView textView = binding.textTodo;
//        todoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
////                textView.setText(s);
//            }
//        });

        //加载todo的recyclerView
        initTodo();
        RecyclerView recyclerView = binding.todoRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);//
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        //实现todo的拖拽
        SimpleItemTouchHelper callback = new SimpleItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //加载添加todo的fragment
        FloatingActionButton fab = binding.todoFab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment_todo,new TodoAddFragment());
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
////                transaction.addToBackStack(null);//添加到返回栈
//                transaction.commit();

                //通过popupwindow实现
                load_popupwindow();

                //淡化背景
//                backgroundAlpha(0.5f);
//                popupPhotoSelectorWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));


                //通过activity实现
//                Intent intent = new Intent(getContext(),TodoAddActivity.class);
//                startActivity(intent);
            }
        });

        //滑动刷新
//        swipeRefresh = binding.todoSwipeRefresh;
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshTodo();
//            }
//        });

        //传递数据
//        EventBus.getDefault().register(this);


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        EventBus.getDefault().unregister(this);
    }

    public void initTodo() {
        for (int i = 0; i < 10; i++) {
            Todo todo = new Todo(i + "");
            todoList.add(todo);
        }
        for (int i = 0; i < 10; i++) {
            Todo todo = new Todo(i + "");
            todo.setSelected(true);
            todoList.add(todo);
        }
    }


    public void refreshTodo() {
        adapter.notifyDataSetChanged();
    }


    public void load_popupwindow() {
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
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("todo content", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button button = popView.findViewById(R.id.add_todo_button);

        button.setOnClickListener(view1 -> {

//            EventBus.getDefault().post(editText.getText().toString());
            String content = editText.getText().toString();
            if (content.length()!=0) {
                Todo todo = new Todo(editText.getText().toString());
                todoList.add(todo);//将todo添加
                adapter.notifyItemRangeInserted(adapter.getItemCount(), 1);
                Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                popupTodoAdd.dismiss();
            } else {
                Toast.makeText(this.getContext(), "无法保存空todo", Toast.LENGTH_SHORT).show();
            }
        });


        popupTodoAdd.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
    }


}