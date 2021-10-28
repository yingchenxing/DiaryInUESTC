package edu.uestc.diaryinuestc.ui.bill;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.king.view.arcseekbar.ArcSeekBar;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.databinding.FragmentBillBinding;

public class BillFragment extends Fragment {

    //    private BillViewModel billViewModel;
    private FragmentBillBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        billViewModel =
//                new ViewModelProvider(this).get(BillViewModel.class);

        binding = FragmentBillBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //添加选择账单种类动态颜色变换
        Switch switchBtn = root.findViewById(R.id.bill_add_switch);
        TextView inTV = root.findViewById(R.id.bill_add_in);
        TextView outTV = root.findViewById(R.id.bill_add_out);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inTV.setTextColor(Color.parseColor("#efb336"));
                    outTV.setTextColor(Color.parseColor("#FF9499A0"));
                } else {
                    inTV.setTextColor(Color.parseColor("#FF9499A0"));
                    outTV.setTextColor(Color.parseColor("#1296db"));
                }
            }
        });
        inTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#efb336"));
                outTV.setTextColor(Color.parseColor("#FF9499A0"));
                switchBtn.setChecked(true);
            }
        });
        outTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#FF9499A0"));
                outTV.setTextColor(Color.parseColor("#1296db"));
                switchBtn.setChecked(false);
            }
        });

        //圆形seekBar设置
        ArcSeekBar seekBar = root.findViewById(R.id.seek_bar);
        EditText editAmount = root.findViewById(R.id.edit_amount);
        seekBar.setOnChangeListener(new ArcSeekBar.OnChangeListener() {
            @Override
            public void onStartTrackingTouch(boolean isCanDrag) {

            }

            @Override
            public void onProgressChanged(float progress, float max, boolean fromUser) {
                editAmount.setFocusable(false);
                editAmount.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStopTrackingTouch(boolean isCanDrag) {
            }

            @Override
            public void onSingleTapUp() {

            }
        });
        //手动输入
        editAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAmount.setFocusable(true);
                editAmount.setFocusableInTouchMode(true);
                editAmount.requestFocus();
                editAmount.requestFocusFromTouch();
            }
        });
        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    public void editAmount(View view) {
        EditText editAmount = binding.getRoot().findViewById(R.id.edit_amount);
        editAmount.setFocusable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}