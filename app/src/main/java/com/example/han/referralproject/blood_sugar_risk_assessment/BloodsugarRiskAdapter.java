package com.example.han.referralproject.blood_sugar_risk_assessment;

import android.graphics.Color;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class BloodsugarRiskAdapter extends BaseMultiItemQuickAdapter<BloodSugarRisk, BaseViewHolder> {
    public BloodsugarRiskAdapter(List<BloodSugarRisk> data) {
        super(data);
        addItemType(BloodSugarRisk.FOUR_BUTTON, R.layout.item_risk_four_button);
        addItemType(BloodSugarRisk.THREE_BUTTON_HORIZONTAL, R.layout.item_risk_three_horizontal);
        addItemType(BloodSugarRisk.TWO_BUTTON, R.layout.item_risk);
        addItemType(BloodSugarRisk.THREE_BUTTON_VERTICAL, R.layout.item_risk_three_vertical);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final BloodSugarRisk testBean) {
        if (baseViewHolder.getAdapterPosition() % 2 == 0) {
            baseViewHolder.getView(R.id.item_risk).setBackgroundColor(Color.parseColor("#f5f5f5"));
        } else {
            baseViewHolder.getView(R.id.item_risk).setBackgroundColor(Color.WHITE);
        }
        switch (testBean.getItemType()) {
            case BloodSugarRisk.FOUR_BUTTON:
                baseViewHolder.setText(R.id.four_answer1, testBean.getAnswerList().get(0).getAnswerInfo());
                baseViewHolder.setText(R.id.four_answer2, testBean.getAnswerList().get(1).getAnswerInfo());
                baseViewHolder.setText(R.id.four_answer3, testBean.getAnswerList().get(2).getAnswerInfo());
                baseViewHolder.setText(R.id.four_answer4, testBean.getAnswerList().get(3).getAnswerInfo());
                baseViewHolder.setText(R.id.four_question, baseViewHolder.getAdapterPosition()+"."+testBean.getQuestionName());
                baseViewHolder.getView(R.id.four_answer1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(0);
                    }
                });
                baseViewHolder.getView(R.id.four_answer2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(1);
                    }
                });
                baseViewHolder.getView(R.id.four_answer3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(2);
                    }
                });
                baseViewHolder.getView(R.id.four_answer4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(3);
                    }
                });
                ((RadioGroup) baseViewHolder.getView(R.id.four_question_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        testBean.setChoosed(true);
                    }
                });
                break;
            case BloodSugarRisk.THREE_BUTTON_HORIZONTAL:
                baseViewHolder.setText(R.id.threeh_answer1, testBean.getAnswerList().get(0).getAnswerInfo());
                baseViewHolder.setText(R.id.threeh_answer2, testBean.getAnswerList().get(1).getAnswerInfo());
                baseViewHolder.setText(R.id.threeh_answer3, testBean.getAnswerList().get(2).getAnswerInfo());
                baseViewHolder.setText(R.id.threeh_question, baseViewHolder.getAdapterPosition()+"."+testBean.getQuestionName());
                baseViewHolder.getView(R.id.threeh_answer1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(0);
                    }
                });
                baseViewHolder.getView(R.id.threeh_answer2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(1);
                    }
                });
                baseViewHolder.getView(R.id.threeh_answer3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(2);
                    }
                });
                ((RadioGroup) baseViewHolder.getView(R.id.three_horizontal_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        testBean.setChoosed(true);
                    }
                });

                break;
            case BloodSugarRisk.TWO_BUTTON:
                baseViewHolder.setText(R.id.answer_true, testBean.getAnswerList().get(0).getAnswerInfo());
                baseViewHolder.setText(R.id.answer_false, testBean.getAnswerList().get(1).getAnswerInfo());
                baseViewHolder.setText(R.id.question, baseViewHolder.getAdapterPosition()+"."+testBean.getQuestionName());
                baseViewHolder.getView(R.id.answer_true).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(0);
                    }
                });
                baseViewHolder.getView(R.id.answer_false).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(1);
                    }
                });
                ((RadioGroup) baseViewHolder.getView(R.id.two_bg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        testBean.setChoosed(true);
                    }
                });
                break;
            case BloodSugarRisk.THREE_BUTTON_VERTICAL:
                baseViewHolder.setText(R.id.threev_answer1, testBean.getAnswerList().get(0).getAnswerInfo());
                baseViewHolder.setText(R.id.threev_answer2, testBean.getAnswerList().get(1).getAnswerInfo());
                baseViewHolder.setText(R.id.threev_answer3, testBean.getAnswerList().get(2).getAnswerInfo());
                baseViewHolder.setText(R.id.threev_question, baseViewHolder.getAdapterPosition()+"."+testBean.getQuestionName());
                baseViewHolder.getView(R.id.threev_answer1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(0);
                    }
                });
                baseViewHolder.getView(R.id.threev_answer2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(1);
                    }
                });

                baseViewHolder.getView(R.id.threev_answer3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testBean.setChoosedPosition(2);

                    }
                });
                ((RadioGroup) baseViewHolder.getView(R.id.three_vertical_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        testBean.setChoosed(true);
                    }
                });
                break;
        }
    }
}
