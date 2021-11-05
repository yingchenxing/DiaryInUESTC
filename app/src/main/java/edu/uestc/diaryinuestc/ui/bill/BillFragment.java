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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.king.view.arcseekbar.ArcSeekBar;

import java.util.ArrayList;
import java.util.List;

import edu.uestc.diaryinuestc.MainActivity;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentBillBinding;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.bill.BillAdapter;
import edu.uestc.diaryinuestc.ui.bill.day.bill.BillDay;
import edu.uestc.diaryinuestc.ui.bill.month.bill.BillMonth;
import edu.uestc.diaryinuestc.ui.todo.TodoAdapter;

public class BillFragment extends Fragment {

    //    private BillViewModel billViewModel;
    private FragmentBillBinding binding;
    private List<Bill> billList;
    private List<BillDay> billDayList;
    private BillMonth billMonth;
    private BillAdapter adapter;
    private FloatingActionButton addBillFab;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        billViewModel =
//                new ViewModelProvider(this).get(BillViewModel.class);

        binding = FragmentBillBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initView();

        //设置fab
        addBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setExitTransition(null);
                getActivity().getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),addBillFab,addBillFab.getTransitionName());
                startActivity(new Intent(getContext(),BillAddCardview.class),options.toBundle());
            }
        });


        //加载todo的recyclerView
        initBillList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BillAdapter(billList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void initView() {
        recyclerView = binding.billRecyclerview;
        addBillFab = binding.fab;
    }


    //初始化账单组
    private void initBillList() {
        billList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Bill bill = new Bill(2001, i, 100, true, "红包");
            billList.add(bill);
        }
        for (int i = 0; i < 5; i++) {
            Bill bill = new Bill(2001, i, 100, false, "红包");
            billList.add(bill);
        }

        for (Bill bill : billList) {
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        addBillFab.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onResume() {
        super.onResume();
        addBillFab.setVisibility(View.VISIBLE);
    }
}