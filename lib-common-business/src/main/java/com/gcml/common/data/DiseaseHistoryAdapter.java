package com.gcml.common.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.common.business.R;

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
        View view = inflater.inflate(R.layout.common_item_disease, parent, false);
        return new DiseaseHolder(view);
    }

    @Override
    public void onBindViewHolder(DiseaseHolder holder, int position) {
        DiseaseHistoryModel model = mModels.get(position);
        holder.onBind(model);
        holder.tvDisease.setOnClickListener(v -> {
            if (position == mModels.size() - 1) {
                for (int i = 0; i < mModels.size() - 1; i++) {
                    setTvDiseaseFalse( mModels.get(i));
                }
                onTvDiseaseClicked(holder, mModels.get(position));
            } else {
                onTvDiseaseClicked(holder, mModels.get(position));
                setTvDiseaseFalse(mModels.get(mModels.size() - 1));
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mModels == null ? 0 : mModels.size();
    }

    public void onTvDiseaseClicked(DiseaseHolder holder, DiseaseHistoryModel mModel) {
        boolean selected = !mModel.isSelected();
        holder.tvDisease.setSelected(selected);
        mModel.setSelected(selected);
        holder.tvDisease.setBackgroundResource(selected
                ? mModel.getBgSelected()
                : mModel.getBg()
        );
        holder.tvDisease.setTextColor(selected
                ? mModel.getTextColorSelected()
                : mModel.getTextColorUnselected()
        );
    }

    public void setTvDiseaseFalse(DiseaseHistoryModel mModel) {
        mModel.setSelected(false);
    }

}
