package edu.uestc.diaryinuestc.ui.todo;

import android.app.Activity;
import android.os.Bundle;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentTodoBinding;
import edu.uestc.diaryinuestc.ui.Transluncy;
import edu.uestc.diaryinuestc.ui.todo.database.TodoEngine;

public class TodoFragment extends Fragment implements Transluncy, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private FragmentTodoBinding binding;
    private List<Todo> todoList = new ArrayList<>();
    private TodoAdapter adapter;
    private TodoEngine todoEngine;
    private PopupWindow popupTodoAdd;
    private TextView textView;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;

    private int mHour = -1;
    private int mMin = -1;
    private Todo mTodo;
    private View popView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        todoEngine = new TodoEngine(getContext());
        //加载todo的recyclerView
        initTodo();
        //判断是否显示添加文本
        textView = binding.addTodoText;
        if (todoList.size() != 0) {
            textView.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = binding.todoRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);

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

    public void load_popupWindow() {
        initDdl();

        popView = getLayoutInflater().inflate(R.layout.cardview_add_todo, null);
        popupTodoAdd = new PopupWindow(popView, (int) (binding.getRoot().getWidth() * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupTodoAdd.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline, null));
        popupTodoAdd.setOutsideTouchable(true);//点击其它退出
        popupTodoAdd.setFocusable(true);//可编辑todo
        popupTodoAdd.setAnimationStyle(R.style.popupWindow_anim_style);//设置动画

        //设置点击事件
        EditText editText = popView.findViewById(R.id.todo_edit);
        TextView tvDdl = popView.findViewById(R.id.add_todo_ddl);
        tvDdl.setVisibility(View.GONE);
        Button button = popView.findViewById(R.id.add_todo_button);
        Button selectDate = popView.findViewById(R.id.add_todo_date);

        button.setOnClickListener(view1 -> {
            String content = editText.getText().toString();

            if (content.length() != 0) {
                mTodo = new Todo(editText.getText().toString(), mYear, mMonth, mDay, mHour, mMin);
                //将todo添加到数据库
                todoEngine.insertTodo(mTodo);
                //将todo添加到UI
                todoList.add(mTodo);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), 1);
                Toast.makeText(getContext(), "已保存", Toast.LENGTH_SHORT).show();
                popupTodoAdd.dismiss();
            } else {
                Toast.makeText(this.getContext(), "无法保存空todo", Toast.LENGTH_SHORT).show();
            }
            if (todoList.size() != 0) {
                textView.setVisibility(View.GONE);
            }
        });
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDateDialog();
            }
        });

        popupTodoAdd.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

//淡化背景
        backgroundAlpha(0.5f);
        popupTodoAdd.setOnDismissListener(() -> backgroundAlpha(1.0f));
    }

    private void loadDateDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Initial day selection
        );
        dpd.show(getFragmentManager(), "选择时间");
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setVersion(TimePickerDialog.Version.VERSION_1);
        tpd.show(getFragmentManager(), "选择时间");
    }

    @Override
    public void backgroundAlpha(float bg_alpha) {
        Activity activity = requireActivity();
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear + 1;
        mDay = dayOfMonth;
        setDDL();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMin = minute;
        setDDL();
    }

    public void setDDL() {
        TextView tvDdl = popView.findViewById(R.id.add_todo_ddl);
        if (mMin < 10 && mMonth >= 0)
            tvDdl.setText(mMonth + "月" + mDay + "日  " + mHour + ":0" + mMin);
        else if (mMin >= 10 && mMonth >= 0)
            tvDdl.setText(mMonth + "月" + mDay + "日  " + mHour + ":" + mMin);
        tvDdl.setVisibility(View.VISIBLE);
    }

    public void initDdl() {
        mYear = -1;
        mMonth = -1;
        mDay = -1;

        mHour = -1;
        mMin = -1;
    }
}