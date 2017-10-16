package com.medlink.danbogh.register;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/10/13.
 */

public class DiseaseHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_sign_up_item_disease)
    TextView tvDisease;
    public DiseaseHistoryModel mModel;

    public DiseaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.tv_sign_up_item_disease)
    public void onTvDiseaseClicked() {
        boolean selected = !mModel.isSelected();
        tvDisease.setSelected(selected);
        mModel.setSelected(selected);
        tvDisease.setBackgroundResource(selected
                ? mModel.getBgSelected()
                : mModel.getBg()
        );
        tvDisease.setTextColor(selected
                ? mModel.getTextColorSelected()
                : mModel.getTextColorUnselected()
        );
    }

    public void onBind(DiseaseHistoryModel model) {
        mModel = model;
        boolean selected = mModel.isSelected();
        tvDisease.setBackgroundResource(selected
                ? mModel.getBgSelected()
                : mModel.getBg()
        );
        tvDisease.setTextColor(selected
                ? mModel.getTextColorSelected()
                : mModel.getTextColorUnselected()
        );
        tvDisease.setText(mModel.getName());
        tvDisease.setSelected(selected);
    }
}
