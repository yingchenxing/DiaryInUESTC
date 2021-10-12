package edu.uestc.diaryinuestc.ui.todo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import edu.uestc.diaryinuestc.R;

public class TodoAddFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cardview_add_todo,container,false);

        //保存按钮
        Button button = view.findViewById(R.id.add_todo_button);

        button.setOnClickListener(view1 -> {
            Toast.makeText(getContext(),"已保存",Toast.LENGTH_SHORT).show();

            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
