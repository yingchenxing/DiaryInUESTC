package edu.uestc.diaryinuestc.ui.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;

import edu.uestc.diaryinuestc.R;

public class BillEditActivity extends AppCompatActivity {
    CardView cvEdit;

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
        cvEdit = findViewById(R.id.bill_edit_cv);
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