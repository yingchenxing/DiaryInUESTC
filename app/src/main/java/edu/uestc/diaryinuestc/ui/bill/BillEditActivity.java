package edu.uestc.diaryinuestc.ui.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.uestc.diaryinuestc.R;
import edu.uestc.diaryinuestc.ui.bill.bill.Bill;
import edu.uestc.diaryinuestc.ui.bill.database.BillEngine;

public class BillEditActivity extends AppCompatActivity {
    public static final String Bill_ID = "bId";
    private CardView cvEdit;
    private BillEngine billEngine;
    private TextView mAmount;
    private TextView mTime;
    //    private TextView mSource;
    private TextView mTypeText;
    private ImageView mTypeImg;
    private TextView mContent;
    private int billId;
    private Bill mBill;
    private LinearLayout deleteBtn;
    private LinearLayout editBtn;


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
        mTypeImg = findViewById(R.id.edit_bill_type_img);
        cvEdit = findViewById(R.id.bill_edit_cv);
        deleteBtn = findViewById(R.id.delete_bill_btn);
        editBtn = findViewById(R.id.edit_bill_btn);
        Intent intent = getIntent();
        billId = intent.getIntExtra(Bill_ID, 0);
        mBill = billEngine.queryById(billId);
        setText();
        setListener();
    }

    private void setListener() {
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


}