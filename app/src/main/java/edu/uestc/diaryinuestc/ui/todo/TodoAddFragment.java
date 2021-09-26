package edu.uestc.diaryinuestc.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentAddTodoBinding;
import edu.uestc.diaryinuestc.databinding.FragmentTodoBinding;

public class TodoAddFragment extends Fragment {

    private @NonNull FragmentAddTodoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAddTodoBinding.inflate(inflater,container,false);
        //保存按钮
        Button button = binding.addTodoButton;
        EditText editText = getActivity().findViewById(R.id.todo_edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"aaaa",Toast.LENGTH_SHORT).show();
                onStop();
            }
        });
        return inflater.inflate(R.layout.fragment_add_todo,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保存按钮
//        Button button = getActivity().findViewById(R.id.add_todo_button);
//        EditText editText = getActivity().findViewById(R.id.todo_edit);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onDestroy();
//            }
//        });
    }
}
