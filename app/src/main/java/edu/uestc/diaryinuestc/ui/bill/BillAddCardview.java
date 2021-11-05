package edu.uestc.diaryinuestc.ui.bill;

import static android.graphics.Paint.FAKE_BOLD_TEXT_FLAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.king.view.arcseekbar.ArcSeekBar;

import edu.uestc.diaryinuestc.R;

public class BillAddCardview extends AppCompatActivity {
    private FloatingActionButton addBillFab;
    private CardView cvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cardview_add_bill);

        //添加动画

        cvAdd = findViewById(R.id.cv_add_bill);
        addBillFab = findViewById(R.id.add_bill_fab);
        ShowEnterAnimation();
        addBillFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        //添加选择账单种类动态颜色变换
        Switch switchBtn = findViewById(R.id.bill_add_switch);
        TextView inTV = findViewById(R.id.bill_add_in);
        TextView outTV = findViewById(R.id.bill_add_out);
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
        ArcSeekBar seekBar = findViewById(R.id.seek_bar);
        EditText editAmount = findViewById(R.id.edit_amount);
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

        //动态选择滑动范围
        RadioGroup radioGroup = findViewById(R.id.RG_rate);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb1 = findViewById(R.id.bill_item_rate1);
                RadioButton rb2 = findViewById(R.id.bill_item_rate2);
                RadioButton rb3 = findViewById(R.id.bill_item_rate3);
                switch (checkedId){
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

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
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
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), addBillFab.getWidth() / 2);
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


}