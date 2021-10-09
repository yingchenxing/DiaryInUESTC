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

        //保存todo内容
        EditText editText = view.findViewById(R.id.todo_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("todo content",charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //保存按钮
        Button button = view.findViewById(R.id.add_todo_button);

        button.setOnClickListener(view1 -> {
            Toast.makeText(getContext(),"已保存",Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(editText.getText().toString());

            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
