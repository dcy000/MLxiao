package com.ml.bci.game;

import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ml.bci.game.common.widget.recyclerview.AutoScrollHelper;
import com.ml.bci.game.common.widget.recyclerview.OverFlyingLayoutManager;

import java.util.ArrayList;

public class BciGameFruitActivity extends AppCompatActivity {

    private TextView tvFruitIndicator;
    private ProgressBar pbAttention;
    private RecyclerView rvFruits;
    private ImageView ivFruitBasketBack;
    private Adapter mAdapter;
    private AutoScrollHelper mAutoScrollHelper;
    private OverFlyingLayoutManager mLayoutManager;
    private AttentionObservable mAttentionObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bci_activity_game_fruit);
        initView();
    }

    private void initView() {
        tvFruitIndicator = (TextView) findViewById(R.id.bci_tv_fruit_indicator);
        pbAttention = (ProgressBar) findViewById(R.id.bci_pb_attention);
        ivFruitBasketBack = (ImageView) findViewById(R.id.bci_iv_fruit_basket_back);
        rvFruits = (RecyclerView) findViewById(R.id.bci_rv_fruits);

        fruitResources.add(R.drawable.bci_ic_fruit_apple);
        fruitResources.add(R.drawable.bci_ic_fruit_peach);
        fruitResources.add(R.drawable.bci_ic_fruit_pear);
        fruitResources.add(R.drawable.bci_ic_fruit_watermelon);

        mLayoutManager = new OverFlyingLayoutManager(this);
        mLayoutManager.setMinScale(1.0f);
        mLayoutManager.setItemSpace(0);
        mLayoutManager.setOrientation(OverFlyingLayoutManager.HORIZONTAL);
        rvFruits.setLayoutManager(mLayoutManager);
        mAdapter = new Adapter();
        rvFruits.setAdapter(mAdapter);
        rvFruits.setEnabled(false);
        rvFruits.addOnItemTouchListener(onItemTouchListener);
        mAutoScrollHelper = new AutoScrollHelper();
        mAutoScrollHelper.attach(rvFruits);
        mAutoScrollHelper.start();
        initAttention();
    }

    private void initAttention() {
        mAttentionObservable = new AttentionObservable();
        mAttentionObservable.registerObserver(new AttentionObservable.Observer() {
            @Override
            public void onAttentionChanged(int intensity) {

            }

            @Override
            public void onBlink() {

            }
        });
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

    private ArrayList<Integer> fruitResources = new ArrayList<>();

    private class VH extends RecyclerView.ViewHolder {

        private ImageView ivFruit;

        public VH(View itemView) {
            super(itemView);
            ivFruit = (ImageView) itemView.findViewById(R.id.bci_iv_item_fruit);
        }

        public void onBind(int position) {
            int fruitRes = fruitResources.get(getAdapterPosition());
            ivFruit.setImageResource(fruitRes);
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
            holder.onBind(position);
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
