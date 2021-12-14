package edu.uestc.diaryinuestc.ui.bill;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.king.view.arcseekbar.ArcSeekBar;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import edu.uestc.diaryinuestc.MainActivity;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentBillBinding;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.bill.BillAdapter;
import edu.uestc.diaryinuestc.ui.bill.database.BillEngine;
import edu.uestc.diaryinuestc.ui.bill.day.bill.BillDay;
import edu.uestc.diaryinuestc.ui.bill.day.bill.BillDayAdapter;
import edu.uestc.diaryinuestc.ui.bill.month.bill.BillMonth;
import edu.uestc.diaryinuestc.ui.todo.TodoAdapter;

public class BillFragment extends Fragment {
    private FragmentBillBinding binding;
    private List<Bill> billList;
    private List<BillDay> billDayList;
    private BillMonth billMonth;
    public BillDayAdapter adapter;
    private FloatingActionButton addBillFab;
    private RecyclerView recyclerView;
    private BillEngine billEngine;
    private int mMonth;
    private TextView timeToolbar;
    private TextView outToolbar;
    private TextView inToolbar;
    private Calendar calendar;
    private Button staticBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBillBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initView();

        setListener();

        //加载todo的recyclerView
        initBillList(calendar.get(Calendar.MONTH)+1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BillDayAdapter(billDayList);
        recyclerView.setAdapter(adapter);

        //加载toolbar
        initToolbar();

        return root;
    }

    public void setListener() {
        //设置fab
        addBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setExitTransition(null);
                getActivity().getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), addBillFab, addBillFab.getTransitionName());
                startActivity(new Intent(getContext(), BillAddCardview.class), options.toBundle());
            }
        });

        //设置统计图按钮
        staticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BillStaticActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        int month = calendar.get(Calendar.MONTH) + 1;
        timeToolbar.setText("2021年" + month + "月");
        int inAmount = 0;
        int outAmount = 0;
        for (Bill bill : billList) {
            if (bill.getMonth() == month) {
                if (bill.isIn())
                    inAmount += bill.getAmount();
                else
                    outAmount += bill.getAmount();
            }
        }
        outToolbar.setText("总支出¥" + outAmount);
        inToolbar.setText("总收入¥" + inAmount);
    }

    //初始化变量
    private void initView() {
        staticBtn = binding.staticBtn;
        billEngine = new BillEngine(getContext());
        recyclerView = binding.billDayRv;
        addBillFab = binding.fab;
        timeToolbar = binding.timeToolbar;
        inToolbar = binding.inToolbar;
        outToolbar = binding.outToolbar;
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    }

    //输出本月的账单
    private List<Bill> showBillOfMonth(int month){
        List<Bill> bills = new ArrayList<>();
        for(Bill bill :billList){
            if(bill.getMonth()==month){
                bills.add(bill);
            }
        }
        return bills;
    }

    //初始化账单组
    private void initBillList(int month) {
        billList = billEngine.queryAllBills();
        Collections.sort(billList);
        billDayList = new ArrayList<>();




        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);

            month = bill.getMonth();//不根据月份
//            if(bill.getMonth()!=month)
//                continue;

            int day1 = bill.getDay1();
            int day2 = bill.getDay2();
            BillDay billDay = new BillDay(month, day1, day2);
            while (i < billList.size()) {
                bill = billList.get(i);
                if (day1 == bill.getDay1()
                        && day2 == bill.getDay2()
                        && month == bill.getMonth()) {
                    billDay.billList.add(billList.get(i));
                    i++;
                } else {
                    break;
                }
            }
            i--;
            billDayList.add(billDay);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        addBillFab.setVisibility(View.VISIBLE);

        //重新刷新recyclerView
        initBillList(calendar.get(Calendar.MONTH)+1);
        initToolbar();

        adapter = new BillDayAdapter(billDayList);
        recyclerView.setAdapter(adapter);
    }
}