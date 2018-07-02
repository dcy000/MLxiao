package com.ml.bci.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ml.bci.game.common.utils.FragmentUtils;
import com.ml.bci.game.common.widget.recyclerview.AutoScrollHelper;

import java.util.ArrayList;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class BciGameFruitActivity extends AppCompatActivity {

    private ConstraintLayout clRoot;
    private TextView tvFruitIndicator;
    private ProgressBar pbAttention;
    private RecyclerView rvFruits;

    private ImageView ivFruitChoose;
    private Adapter mAdapter;
    private AutoScrollHelper mAutoScrollHelper;
    private GalleryLayoutManager mLayoutManager;

    private BciDeviceControllerFragment mBciDeviceControllerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bci_activity_game_fruit);
        initView();
        initAttention();
    }

    private void initView() {
        clRoot = (ConstraintLayout) findViewById(R.id.bci_cl_root);
        tvFruitIndicator = (TextView) findViewById(R.id.bci_tv_fruit_indicator);
        pbAttention = (ProgressBar) findViewById(R.id.bci_pb_attention);
        ivFruitChoose = (ImageView) findViewById(R.id.bci_iv_choose_fruit);
        rvFruits = (RecyclerView) findViewById(R.id.bci_rv_fruits);

        mLayoutManager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        mLayoutManager.attach(rvFruits, fruitResources.size() - 1);
        mLayoutManager.setCallbackInFling(true);
        mLayoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                int curSelectedPosition = mLayoutManager.getCurSelectedPosition();
                tvFruitIndicator.setText(getFruit(curSelectedPosition));
            }
        });
        rvFruits.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter();
        rvFruits.setAdapter(mAdapter);
        rvFruits.setEnabled(false);
        rvFruits.addOnItemTouchListener(onItemTouchListener);
        mAutoScrollHelper = new AutoScrollHelper();
        mAutoScrollHelper.attach(rvFruits);

        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFruitChoose.setVisibility(View.GONE);
                mBciDeviceControllerFragment.closeDevice();
                initFruits();
                mAdapter.notifyDataSetChanged();
                rvFruits.smoothScrollToPosition(fruitResources.size() - 1);
                mBciDeviceControllerFragment.connectDevice();
            }
        });
    }

    private void initFruits() {
        fruitResources.clear();
        fruitResources.add(R.drawable.bci_ic_fruit_apple);
        fruitResources.add(R.drawable.bci_ic_fruit_peach);
        fruitResources.add(R.drawable.bci_ic_fruit_pear);
        fruitResources.add(R.drawable.bci_ic_fruit_watermelon);
    }

    private void initAttention() {
        mBciDeviceControllerFragment = FragmentUtils.get(
                getSupportFragmentManager(),
                BciDeviceControllerFragment.class
        );
        mBciDeviceControllerFragment.register(new BciSignalObservable.Observer() {
            @Override
            public void onAttentionChanged(int intensity) {
                pbAttention.setProgress(intensity);
                if (!mAutoScrollHelper.isStarted()) {
                    mAutoScrollHelper.start();
                }
                mAutoScrollHelper.setOffsetX((int) (mAutoScrollHelper.baseOffset() * intensity / 100f));
            }

            @Override
            public void onBlinkChanged(int intensity) {
                if (intensity > 88) {
                    animateDown();
                }
            }
        });
    }

    private void animateDown() {
        rvFruits.post(new Runnable() {
            @Override
            public void run() {
                int currentPosition = fruitResources.size() == 0 ? 0 : mLayoutManager.getCurSelectedPosition();
                Log.d("fruit", "currentPosition: " + currentPosition);
                Log.d("fruit", getFruit(currentPosition));
                if (fruitResources.size() != 0) {
                    final int position;
                    position = currentPosition % fruitResources.size();
                    final Integer removed;
                    synchronized (fruitResources) {
                        removed = fruitResources.remove(position);
                    }
                    final int[] startLocation = new int[2];
                    View view = mLayoutManager.getChildAt(position);
                    view.getLocationInWindow(startLocation);
                    mAdapter.notifyItemRemoved(position);
                    rvFruits.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(startLocation, position, removed);
                        }
                    }, 16);
                }
            }
        });
    }

    private void animate(int[] startLocation, int position, final int removed) {
        final ImageView imageView = new ImageView(this);
        imageView.setImageResource(removed);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        clRoot.addView(imageView, layoutParams);

        int[] parentLocation = new int[2];
        clRoot.getLocationInWindow(parentLocation);

        int[] endLocation = new int[2];
        ivFruitChoose.getLocationInWindow(endLocation);

        int startX = startLocation[0] - parentLocation[0] + ivFruitChoose.getWidth() / 2;
        int startY = startLocation[1] - parentLocation[1] + ivFruitChoose.getHeight() / 2;
        int endX = endLocation[0] - parentLocation[0] + ivFruitChoose.getWidth() / 2;
        int endY = endLocation[1] - parentLocation[1];

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo((startX + endX) / 2, startY, endX, endY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);

        ValueAnimator animator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float[] pos = new float[2];
                pathMeasure.getPosTan(value, pos, null);
                imageView.setTranslationX(pos[0]);
                imageView.setTranslationY(pos[1]);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clRoot.removeView(imageView);
                ivFruitChoose.setImageResource(removed);
                ivFruitChoose.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    private String getFruit(int currentPosition) {
        int res;
        synchronized (fruitResources) {
            int position = currentPosition % fruitResources.size();
            res = fruitResources.get(position);
        }
        String fruit = "苹果";
        switch (res) {
            case R.drawable.bci_ic_fruit_apple:
                fruit = "苹果";
                break;
            case R.drawable.bci_ic_fruit_peach:
                fruit = "桃子";
                break;
            case R.drawable.bci_ic_fruit_pear:
                fruit = "梨子";
                break;
            case R.drawable.bci_ic_fruit_watermelon:
                fruit = "西瓜";
                break;
            default:
                break;
        }
        return fruit;
    }

    private RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    private final ArrayList<Integer> fruitResources = new ArrayList<>();

    private class VH extends RecyclerView.ViewHolder {

        private ImageView ivFruit;

        public VH(View itemView) {
            super(itemView);
            ivFruit = (ImageView) itemView.findViewById(R.id.bci_iv_item_fruit);
        }

        public void onBind() {
            synchronized (fruitResources) {
                int fruitRes = fruitResources.get(getAdapterPosition());
                ivFruit.setImageResource(fruitRes);
            }
        }

        public void onRecycled() {

        }
    }

    private class Adapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.bci_item_fruit, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.onBind();
        }

        @Override
        public void onViewRecycled(VH holder) {
            super.onViewRecycled(holder);
            holder.onRecycled();
        }

        @Override
        public int getItemCount() {
            return fruitResources.size();
        }
    }
}
