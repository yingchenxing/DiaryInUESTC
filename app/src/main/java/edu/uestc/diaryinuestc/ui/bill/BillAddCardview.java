package edu.uestc.diaryinuestc.ui.bill;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.king.view.arcseekbar.ArcSeekBar;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

import edu.uestc.diaryinuestc.MRadioGroup;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.database.BillEngine;
import edu.uestc.diaryinuestc.ui.todo.database.TodoEngine;

public class BillAddCardview extends AppCompatActivity {
    private FloatingActionButton addBillFab;
    private CardView cvAdd;
    private RadioGroup rateRadioGroup;
    private RadioGroup typeRadioGroup1;//作为布局用
    private RadioGroup typeRadioGroup2;//作为布局用
    private ArcSeekBar seekBar;
    private EditText editAmount;
    private TextView inTV;
    private TextView outTV;
    private Bill mBill;
    private double mAmount;
    private MRadioGroup typeSelectGroup;//获取type用
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioButton rb5;
    private RadioButton rb6;
    private RadioButton rb7;
    private RadioButton rb8;
    private RadioButton rb9;
    private RadioButton rb10;
    private RadioButton rb11;
    private RadioButton rb12;
    private RadioButton rb13;
    private RadioButton rb14;
    private RadioButton rb15;
    private boolean isIn;
    private int billType;
    private Button addBillBtn;
    private EditText editBillContent;
    private String mBillContent = null;
    private BillEngine billEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cardview_add_bill);
        initAll();

        //添加动画
        ShowEnterAnimation();
        addBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        //设置输入的模块
        setEditAmount();

        //动态选择滑动范围
        setRateRadioGroup();

    }

    private void initAll() {
        billEngine = new BillEngine(this);
        isIn = false;
        rateRadioGroup = findViewById(R.id.RG_rate);
        seekBar = findViewById(R.id.seek_bar);
        editAmount = findViewById(R.id.edit_amount);
        cvAdd = findViewById(R.id.cv_add_bill);
        addBillFab = findViewById(R.id.add_bill_fab);
        inTV = findViewById(R.id.bill_add_in);
        outTV = findViewById(R.id.bill_add_out);
        initTypeSelectGroup();
        editBillContent = findViewById(R.id.add_bill_content);
        addBillBtn = findViewById(R.id.add_bill_button);
    }

    public void initTypeSelectGroup() {
        rb1 = findViewById(R.id.bill_type1);
        rb2 = findViewById(R.id.bill_type2);
        rb3 = findViewById(R.id.bill_type3);
        rb4 = findViewById(R.id.bill_type4);
        rb5 = findViewById(R.id.bill_type5);
        rb6 = findViewById(R.id.bill_type6);
        rb7 = findViewById(R.id.bill_type7);
        rb8 = findViewById(R.id.bill_type8);
        rb9 = findViewById(R.id.bill_type9);
        rb10 = findViewById(R.id.bill_type10);
        rb11 = findViewById(R.id.bill_type11);
        rb12 = findViewById(R.id.bill_type12);
        rb13 = findViewById(R.id.bill_type13);
        rb14 = findViewById(R.id.bill_type14);
        rb15 = findViewById(R.id.bill_type15);
        typeRadioGroup1 = findViewById(R.id.radioGroup2);
        typeRadioGroup2 = findViewById(R.id.radioGroup3);
        typeSelectGroup = new MRadioGroup(rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12, rb13, rb14, rb15);
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
                typeRadioGroup2.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, addBillFab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), addBillFab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                addBillFab.setImageResource(R.drawable.add);
                BillAddCardview.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public void setRateRadioGroup() {
        rateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb1 = findViewById(R.id.bill_item_rate1);
                RadioButton rb2 = findViewById(R.id.bill_item_rate2);
                RadioButton rb3 = findViewById(R.id.bill_item_rate3);
                switch (checkedId) {
                    case R.id.bill_item_rate1:
                        seekBar.setMax(20);
                        seekBar.setProgress(20);
                        rb1.getPaint().setFlags(FAKE_BOLD_TEXT_FLAG);
                        rb2.getPaint().setFlags(0);
                        rb3.getPaint().setFlags(0);
                        break;
                    case R.id.bill_item_rate2:
                        seekBar.setMax(50);
                        seekBar.setProgress(50);
                        rb2.getPaint().setFlags(FAKE_BOLD_TEXT_FLAG);
                        rb1.getPaint().setFlags(0);
                        rb3.getPaint().setFlags(0);
                        break;
                    case R.id.bill_item_rate3:
                        seekBar.setMax(100);
                        seekBar.setProgress(100);
                        rb3.getPaint().setFlags(FAKE_BOLD_TEXT_FLAG);
                        rb2.getPaint().setFlags(0);
                        rb1.getPaint().setFlags(0);
                        break;
                    default:
                        return;
                }
            }
        });
    }

    //输入模块
    public void setEditAmount() {

        //添加选择账单种类动态颜色变换
        inTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#efb336"));
                outTV.setTextColor(Color.parseColor("#C8C8C8"));
                isIn = true;
                typeRadioGroup1.setVisibility(View.INVISIBLE);
                typeRadioGroup2.setVisibility(View.VISIBLE);
                typeSelectGroup.clearCheck();
            }
        });
        outTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#C8C8C8"));
                outTV.setTextColor(Color.parseColor("#185ADB"));
                isIn = false;
                typeRadioGroup1.setVisibility(View.VISIBLE);
                typeRadioGroup2.setVisibility(View.INVISIBLE);
                typeSelectGroup.clearCheck();
            }
        });

        //圆形seekBar设置
        seekBar.setOnChangeListener(new ArcSeekBar.OnChangeListener() {
            @Override
            public void onStartTrackingTouch(boolean isCanDrag) {

            }

            @Override
            public void onProgressChanged(float progress, float max, boolean fromUser) {
                editAmount.setFocusable(false);
                editAmount.setText(seekBar.getProgress() + "");
                mAmount = seekBar.getProgress();
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
                mAmount = Double.parseDouble(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //手动输入备注
        editBillContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBillContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        //提交按钮
        addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmount != 0) {
                    billType = typeSelectGroup.getType();

                    java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    int year = calendar.get(java.util.Calendar.YEAR);
                    int month = calendar.get(java.util.Calendar.MONTH) + 1;
                    int day1 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                    int day2 = calendar.get(Calendar.DAY_OF_WEEK);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    mBill = new Bill(year, month, day1, day2, hour, min, second,billType, mAmount, isIn, mBillContent);

                    billEngine.insertBill(mBill);
                    Toast.makeText(BillAddCardview.this, "添加成功！", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(BillAddCardview.this, "无法添加数额为0的账单！", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}