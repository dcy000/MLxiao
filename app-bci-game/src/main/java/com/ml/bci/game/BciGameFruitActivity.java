package com.ml.bci.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BciGameFruitActivity extends AppCompatActivity {

    private TextView tvFruitIndicator;
    private ProgressBar pbAttention;
    private RecyclerView rvFruits;
    private ImageView ivFruitBasketBack;

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


    }

    private SparseIntArray fruitReses = new SparseIntArray();

    private class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }

        public void onBind(int position) {

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
            return 0;
        }
    }
}
