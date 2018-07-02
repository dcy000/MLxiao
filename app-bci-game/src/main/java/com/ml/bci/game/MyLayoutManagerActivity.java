package com.ml.bci.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ml.bci.game.common.widget.recyclerview.MyLayoutManager;
import com.ml.bci.game.common.widget.recyclerview.RandomLayoutManager;

import java.util.ArrayList;

public class MyLayoutManagerActivity extends AppCompatActivity {

    private RecyclerView rvFruits;
    private RandomLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_layout_manager);
        rvFruits = (RecyclerView) findViewById(R.id.rv_fruits);

        layoutManager = new RandomLayoutManager(rvFruits);
        rvFruits.setLayoutManager(layoutManager);
        rvFruits.setAdapter(adapter);

        initFruits();
        adapter.notifyDataSetChanged();
    }

    private final ArrayList<Integer> fruitResources = new ArrayList<>();

    private void initFruits() {
        fruitResources.clear();
        fruitResources.add(R.drawable.bci_ic_fruit_apple);
        fruitResources.add(R.drawable.bci_ic_fruit_peach);
        fruitResources.add(R.drawable.bci_ic_fruit_pear);
        fruitResources.add(R.drawable.bci_ic_fruit_watermelon);
    }

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
    }

    private RecyclerView.Adapter<VH> adapter = new RecyclerView.Adapter<VH>() {
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
        public int getItemCount() {
            return fruitResources.size();
        }
    };
}
