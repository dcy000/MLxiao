package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by afirez on 2018/6/22.
 */

public class AutoScrollHelper {
    public static final long INTERVAL_AUTO_SCROLL = 16;

    private WeakReference<RecyclerView> mWeakRecyclerView;
    private volatile boolean running;
    private volatile boolean canRun;


    public void attach(RecyclerView view) {
        mWeakRecyclerView = new WeakReference<>(view);
//        view.setOnTouchListener(onTouchListener);
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (running) {
                        stop();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    if (canRun) {
                        start();
                    }
                    break;
            }
            return false;
        }
    };

    public void start() {
        if (running) {
            stop();
        }
        canRun = true;
        running = true;
        RecyclerView recyclerView = mWeakRecyclerView.get();
        if (recyclerView == null) {
            return;
        }
        recyclerView.postDelayed(autoScrollRunnable, INTERVAL_AUTO_SCROLL);
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            RecyclerView recyclerView = mWeakRecyclerView.get();
            if (recyclerView == null) {
                return;
            }
            if (!canRun || !running) {
                return;
            }
            recyclerView.scrollBy(offsetX(), offsetY());
            recyclerView.postDelayed(autoScrollRunnable, INTERVAL_AUTO_SCROLL);
        }
    };


    public int offsetX() {
        return offsetX;
    }

    public int offsetY() {
        return offsetY;
    }

    public int baseOffset() {
        return baseOffset;
    }

    private int baseOffset = -10;

    private int offsetX = -2;

    private int offsetY = -2;

    public void setBaseOffset(int baseOffset) {
        this.baseOffset = baseOffset;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isStarted() {
        return running;
    }

    public void stop() {
        running = false;
        RecyclerView recyclerView = mWeakRecyclerView.get();
        if (recyclerView != null) {
            recyclerView.removeCallbacks(autoScrollRunnable);
        }
    }

}
