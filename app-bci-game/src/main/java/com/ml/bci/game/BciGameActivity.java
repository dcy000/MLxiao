package com.ml.bci.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class BciGameActivity extends AppCompatActivity {

    private ConstraintLayout mClRoot;
    private View mTargetApple;
    private View mTargetBasket;

    private BciSignalObservable mBciSignalObservable;

    private ConstraintSet constraintSet = new ConstraintSet();
    private ConstraintSet endConstraintSet = new ConstraintSet();

    private int velocity;
    private int marginLeft;
    private boolean hasRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bci_activity_game);
        initView();
        initAttention();
    }

    private void initAttention() {
        mBciSignalObservable = new BciSignalObservable();
        mBciSignalObservable.registerObserver(new BciSignalObservable.Observer() {
            @Override
            public void onAttentionChanged(int intensity) {
                int width = getResources().getDisplayMetrics().widthPixels;
                if (mTargetApple.getRight() >= width) {
                    onEnd(null);
                    return;
                }

                TransitionManager.beginDelayedTransition(mClRoot);
                constraintSet.clone(mClRoot);
                int newMarginLeft = calculateAppleMarginLeft(intensity);
                constraintSet.setMargin(R.id.bci_target_apple, ConstraintSet.LEFT, newMarginLeft);
                constraintSet.applyTo(mClRoot);
            }

            @Override
            public void onBlinkChanged(int intensity) {
                TransitionManager.beginDelayedTransition(mClRoot);
                endConstraintSet.clone(mClRoot);
                endConstraintSet.connect(
                        R.id.bci_target_apple, ConstraintSet.TOP,
                        R.id.bci_target_basket, ConstraintSet.TOP, 0
                );
                endConstraintSet.connect(
                        R.id.bci_target_apple, ConstraintSet.BOTTOM,
                        R.id.bci_target_basket, ConstraintSet.BOTTOM, 0
                );
                endConstraintSet.applyTo(mClRoot);
            }
        });
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int calculateAppleMarginLeft(int intensity) {
        int newVelocity = intensity + velocity;
        velocity = newVelocity;

        marginLeft = (int) (marginLeft + newVelocity * 0.5);
        return marginLeft;
    }

    private void initView() {
        mClRoot = (ConstraintLayout) findViewById(R.id.bci_cl_game_root);
        mTargetApple = findViewById(R.id.bci_target_apple);
        mTargetBasket = findViewById(R.id.bci_target_basket);
    }

    public void onEnd(View view) {
        if (!hasRun) {
            return;
        }
        hasRun = false;
        mBciSignalObservable.notifyBlinkChanged(100);
        mHandler.removeCallbacksAndMessages(null);

        float left = mTargetApple.getX();
        int width = mTargetApple.getWidth();
        float right = left + width;
        float leftBasket = mTargetBasket.getX();
        int widthBasket = mTargetBasket.getWidth();
        float rightBasket = leftBasket + widthBasket;

        boolean hasCongratulations = left >= leftBasket && right <= rightBasket;

        String text = hasCongratulations ? "Congratulations !!!" : "Oh no !!!";
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private Random mRandom = new Random();

    public void onStart(View view) {
        if (hasRun) {
            return;
        }
        hasRun = true;
        marginLeft = 0;
        velocity = 0;
        constraintSet.clone(mClRoot);
        constraintSet.connect(
                R.id.bci_target_apple, ConstraintSet.BOTTOM,
                R.id.bci_cl_game_root, ConstraintSet.BOTTOM, marginLeft
        );
        constraintSet.connect(
                R.id.bci_target_apple, ConstraintSet.TOP,
                R.id.bci_cl_game_root, ConstraintSet.TOP, marginLeft
        );
        constraintSet.connect(
                R.id.bci_target_apple, ConstraintSet.LEFT,
                R.id.bci_cl_game_root, ConstraintSet.LEFT, marginLeft
        );
        constraintSet.applyTo(mClRoot);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mBciSignalObservable.notifyAttentionChanged(mRandom.nextInt(100));
                mHandler.postDelayed(this, 500);
            }
        });
    }
}
