package com.gcml.common.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.business.R;


/**
 * Created by lenovo on 2017/10/13.
 */

public class DiseaseHolder extends RecyclerView.ViewHolder {
    TextView tvDisease;
    public DiseaseHistoryModel mModel;

    public DiseaseHolder(View itemView) {
        super(itemView);
        tvDisease = (TextView) itemView.findViewById(R.id.tv_sign_up_item_disease);
//        tvDisease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                onTvDiseaseClicked();
//            }
//        });
    }

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
