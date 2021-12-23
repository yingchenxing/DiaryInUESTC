package edu.uestc.diaryinuestc.ui.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.uestc.diaryinuestc.MRadioGroup;
import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.Transluncy;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.database.BillEngine;

public class BillEditActivity extends AppCompatActivity implements Transluncy {
    public static final String Bill_ID = "bId";
    private CardView cvEdit;
    private BillEngine billEngine;
    private TextView mAmount;
    private TextView mTime;
    private TextView mTypeText;
    private TextView mContent;
    private int billId;
    private Bill mBill;
    private LinearLayout deleteBtn;
    private LinearLayout editBtn;
    private PopupWindow popupEditBill;
    RelativeLayout rl;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    RadioButton rb5;
    RadioButton rb6;
    RadioButton rb7;
    RadioButton rb8;
    RadioButton rb9;
    RadioButton rb10;
    RadioButton rb11;
    RadioButton rb12;
    RadioButton rb13;
    RadioButton rb14;
    RadioButton rb15;
    EditText editAmount;
    EditText editContent;
    MRadioGroup typeSelectGroup;
    RadioGroup rg;
    RadioGroup typeRg1;
    RadioGroup typeRg2;
    private TextView inTV;
    private TextView outTV;
    private boolean isIn;
    View popView;
    Button saveBtn;
    private String mAmountStr;
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bill_edit);
        initAll();

        cvEdit.setVisibility(View.INVISIBLE);
        animateRevealShow();//动画


    }

    public void initAll() {
        billEngine = new BillEngine(this);
        mAmount = findViewById(R.id.edit_bill_amount);
        mTime = findViewById(R.id.edit_bill_time);
        mContent = findViewById(R.id.edit_bill_content);
        mTypeText = findViewById(R.id.edit_bill_type_text);
        cvEdit = findViewById(R.id.bill_edit_cv);
        deleteBtn = findViewById(R.id.delete_bill_btn);
        editBtn = findViewById(R.id.edit_bill_btn);
        Intent intent = getIntent();
        billId = intent.getIntExtra(Bill_ID, 0);
        mBill = billEngine.queryById(billId);
        rl = findViewById(R.id.edit_rl);
        setText();
        setListener();
    }

    public void initPop() {

        rb1 = popView.findViewById(R.id.edit_bill_type1);
        rb2 = popView.findViewById(R.id.edit_bill_type2);
        rb3 = popView.findViewById(R.id.edit_bill_type3);
        rb4 = popView.findViewById(R.id.edit_bill_type4);
        rb5 = popView.findViewById(R.id.edit_bill_type5);
        rb6 = popView.findViewById(R.id.edit_bill_type6);
        rb7 = popView.findViewById(R.id.edit_bill_type7);
        rb8 = popView.findViewById(R.id.edit_bill_type8);
        rb9 = popView.findViewById(R.id.edit_bill_type9);
        rb10 = popView.findViewById(R.id.edit_bill_type10);
        rb11 = popView.findViewById(R.id.edit_bill_type11);
        rb12 = popView.findViewById(R.id.edit_bill_type12);
        rb13 = popView.findViewById(R.id.edit_bill_type13);
        rb14 = popView.findViewById(R.id.edit_bill_type14);
        rb15 = popView.findViewById(R.id.edit_bill_type15);
        Drawable drawable = getResources().getDrawable(R.drawable.rmb);
        drawable.setBounds(0,0,40,40);
        editAmount = popView.findViewById(R.id.edit_pop_amount);
        editAmount.setCompoundDrawables(drawable,null,null,null);
        editContent = popView.findViewById(R.id.edit_content);
        typeSelectGroup = new MRadioGroup(rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12, rb13, rb14, rb15);
        typeRg1 = popView.findViewById(R.id.editRG1);
        typeRg2 = popView.findViewById(R.id.editRG2);
        inTV = popView.findViewById(R.id.edit_bill_in);
        outTV = popView.findViewById(R.id.edit_bill_out);
        saveBtn = popView.findViewById(R.id.save_bill_btn);
    }

    private void setListener() {
        //删除button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBill != null) {
                    billEngine.deleteBills(mBill);
                    Toast.makeText(BillEditActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(BillEditActivity.this, "该记录已经删除，请刷新页面！", Toast.LENGTH_SHORT).show();
                }
                ;
            }
        });

        //编辑button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getWindow().setExitTransition(null);
//                getWindow().setEnterTransition(null);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(BillEditActivity.this, editBtn, editBtn.getTransitionName());
//                startActivity(new Intent(BillEditActivity.this, BillAddCardview.class), options.toBundle());
                load_popupWindow();
            }
        });


    }

    private void setPopupListener() {

        //添加选择账单种类动态颜色变换
        typeRg1.setVisibility(View.INVISIBLE);
        typeRg2.setVisibility(View.VISIBLE);
        inTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#efb336"));
                outTV.setTextColor(Color.parseColor("#C8C8C8"));
                isIn = true;
                typeRg1.setVisibility(View.VISIBLE);
                typeRg2.setVisibility(View.INVISIBLE);
                typeSelectGroup.clearCheck();
            }
        });
        outTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inTV.setTextColor(Color.parseColor("#C8C8C8"));
                outTV.setTextColor(Color.parseColor("#FF2195F3"));
                isIn = false;
                typeRg1.setVisibility(View.INVISIBLE);
                typeRg2.setVisibility(View.VISIBLE);
                typeSelectGroup.clearCheck();
            }
        });

        editAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAmount.setFocusable(true);
                editAmount.setFocusableInTouchMode(true);
                editAmount.requestFocus();
                editAmount.requestFocusFromTouch();
            }
        });

        editContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContent.setFocusable(true);
                editContent.setFocusableInTouchMode(true);
                editContent.requestFocus();
                editContent.requestFocusFromTouch();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBill.setAmount(Double.parseDouble(editAmount.getText().toString()));
                mBill.setIn(isIn);
                mBill.setType(typeSelectGroup.getType());
                mBill.setContent(editContent.getText().toString());
                billEngine.updateBills(mBill);
                popupEditBill.dismiss();
                setText();
            }
        });

        //设置初始值
        if (mBill.isIn()) {
            inTV.callOnClick();
        } else {
            outTV.callOnClick();
        }

        editAmount.setText(mBill.getAmount()+"");
        //初始化备注
        if (mBill.getContent() != null)
            editContent.setText(mBill.getContent());
        typeSelectGroup.setCheck(mBill.getType());

    }

    private void load_popupWindow() {
        //设置弹窗
        popView = getLayoutInflater().inflate(R.layout.cardview_edit_bill, null);
        popupEditBill = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupEditBill.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                R.drawable.round_outline, null));
        popupEditBill.setOutsideTouchable(true);//点击其它退出
        popupEditBill.setFocusable(true);//可编辑todo
        popupEditBill.setAnimationStyle(R.style.popupBill_anim_style);//设置动画
        initPop();
        setPopupListener();
        int mHeightPixels = new DisplayMetrics().heightPixels;
        int popHeight = popupEditBill.getContentView().getMeasuredHeight();
        popupEditBill.showAtLocation(rl, Gravity.BOTTOM, 0, -500);


        //淡化背景
        backgroundAlpha(0.5f);
        popupEditBill.setOnDismissListener(() -> backgroundAlpha(1.0f));
    }


    public void setText() {
        if (mBill != null) {
            if (mBill.isIn())
                mAmount.setText("+" + String.format("%.2f", mBill.getAmount()));
            else
                mAmount.setText("-" + String.format("%.2f", mBill.getAmount()));

            mTypeText.setText(mBill.getTypeName());
            mContent.setText(mBill.getContent());
            if (mBill.getMin() < 10)
                mTime.setText(mBill.getMonth() + "月" + mBill.getDay1() + "日  " + mBill.getHour() + ":0" + mBill.getMin());
            else
                mTime.setText(mBill.getMonth() + "月" + mBill.getDay1() + "日  " + mBill.getHour() + ":" + mBill.getMin());
        }
    }

    //入场动画
    public void animateRevealShow() {
        cvEdit.post(new Runnable() {
            @Override
            public void run() {
                Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvEdit, cvEdit.getWidth() / 2, 0, 0, cvEdit.getHeight());
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new AccelerateInterpolator());
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        cvEdit.setVisibility(View.VISIBLE);
                        super.onAnimationStart(animation);
                    }
                });
                mAnimator.start();
            }
        });

    }

    //退出动画
    public void animateRevealClose() {
        cvEdit.post(new Runnable() {
            @Override
            public void run() {
                Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvEdit, cvEdit.getWidth() / 2, 0, cvEdit.getHeight(), 0);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new AccelerateInterpolator());
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cvEdit.setVisibility(View.INVISIBLE);
                        super.onAnimationEnd(animation);
                        BillEditActivity.super.onBackPressed();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }
                });
                mAnimator.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void backgroundAlpha(float bg_alpha) {
        Activity activity = BillEditActivity.this;
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bg_alpha; //0.0-1.0
        window.setAttributes(lp);
    }


}