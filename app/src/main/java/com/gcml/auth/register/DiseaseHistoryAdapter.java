package com.gcml.auth.register;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;

import java.util.List;

/**
 * Created by lenovo on 2017/10/13.
 */

public class DiseaseHistoryAdapter extends RecyclerView.Adapter<DiseaseHolder> {

    public List<DiseaseHistoryModel> mModels;

    public DiseaseHistoryAdapter(List<DiseaseHistoryModel> models) {
        mModels = models;
    }

    @Override
    public DiseaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_disease, parent, false);
        return new DiseaseHolder(view);
    }

    @Override
    public void onBindViewHolder(DiseaseHolder holder, int position) {
        DiseaseHistoryModel model = mModels.get(position);
        holder.onBind(model);
    }

    @Override
    public int getItemCount() {
        return mModels == null ? 0 : mModels.size();
    }
}
