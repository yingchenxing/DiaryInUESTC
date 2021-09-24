package edu.uestc.diaryinuestc.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.databinding.FragmentTodoBinding;
import edu.uestc.diaryinuestc.ui.diary.DiaryAdapter;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private FragmentTodoBinding binding;
    private List<Todo> todoList = new ArrayList<>();
    private TodoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textTodo;
        todoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        //加载todo的recyclerView
        initTodo();
        RecyclerView recyclerView = binding.todoRecyclerView;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);//
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initTodo(){
        for(int i =0;i<20;i++) {
            Todo todo = new Todo("11111111111");
            todoList.add(todo);
        }
    }
}