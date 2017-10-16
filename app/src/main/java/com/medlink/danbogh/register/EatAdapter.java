package com.medlink.danbogh.register;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lenovo on 2017/10/13.
 */

public class EatAdapter extends RecyclerView.Adapter<EatHolder> {
    public ArrayList<EatModel> mEatModels = new ArrayList<>(6);

    @Override
    public EatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_eat, parent, false);
        return new EatHolder(view);
    }

    @Override
    public void onBindViewHolder(EatHolder holder, int position) {
        EatModel eatModel = mEatModels.get(position);
        holder.onBind(eatModel);
    }

    @Override
    public int getItemCount() {
        return mEatModels.size();
    }

    public void replaceAll(Collection<EatModel> models) {
        this.mEatModels.clear();
        this.mEatModels.addAll(models);
        this.notifyDataSetChanged();
    }
}
