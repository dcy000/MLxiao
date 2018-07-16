package com.ml.bci.game.common.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by afirez on 2018/6/22.
 */

public class AutoScrollHelper {

    private static final String TAG = "AutoScrollHelper";

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
            return;
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

            int x = offsetX();
            int y = offsetY();

            recyclerView.scrollBy(x, y);
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

    public void setOffsetX(final int offsetX) {
        RecyclerView recyclerView = mWeakRecyclerView.get();
        if (recyclerView == null) {
            return;
        }
        if (!canRun || !running) {
            return;
        }
        if (updateOffsetXRunnable == null) {
            updateOffsetXRunnable = new UpdateOffsetXRunnable();
        }
        updateOffsetXRunnable.setOffsetX(offsetX);
        recyclerView.post(updateOffsetXRunnable);
    }

    private UpdateOffsetXRunnable updateOffsetXRunnable;

    private class UpdateOffsetXRunnable implements Runnable {
        private int mOffsetX;

        public void setOffsetX(int offsetX) {
            mOffsetX = offsetX;
        }

        @Override
        public void run() {
            AutoScrollHelper.this.offsetX = mOffsetX;
        }
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
