package edu.uestc.diaryinuestc.ui.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import edu.uestc.diaryinuestc.R;

public class TodoAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_todo);
        EditText editText = findViewById(R.id.todo_edit);
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
        Button button = findViewById(R.id.add_todo_button);

        button.setOnClickListener(view1 -> {
            Toast.makeText(this,"已保存",Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(editText.getText().toString());
            onBackPressed();

        });
    }

}