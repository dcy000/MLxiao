package com.example.han.referralproject.risk_assessment;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.QuestionChoosed;

import java.util.List;

/**
 * Created by Administrator on 2018/5/5.
 */

public class QuestionAdapter extends BaseQuickAdapter<QuestionChoosed,BaseViewHolder>{
    public QuestionAdapter(int layoutResId, @Nullable List<QuestionChoosed> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, QuestionChoosed questionChoosed) {
        if (baseViewHolder.getAdapterPosition() % 2 == 0) {
            baseViewHolder.getView(R.id.item_risk).setBackgroundColor(Color.WHITE);
        } else {
            baseViewHolder.getView(R.id.item_risk).setBackgroundColor(Color.parseColor("#f5f5f5"));
        }
        baseViewHolder.setText(R.id.question, questionChoosed.getQuestion());
//        if (questionChoosed.isChoosed()) {
//            ((RadioButton) baseViewHolder.getView(R.id.answer_true)).setChecked(true);
//        } else {
//            ((RadioButton) baseViewHolder.getView(R.id.answer_false)).setChecked(true);
//        }
        baseViewHolder.getView(R.id.answer_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(baseViewHolder.getAdapterPosition()).setChoosed(true);
                Log.e(TAG, "onClick: " + mData.toString());
            }
        });

        baseViewHolder.getView(R.id.answer_false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(baseViewHolder.getAdapterPosition()).setChoosed(false);
                Log.e(TAG, "onClick: " + mData.toString());
            }
        });
    }
}
