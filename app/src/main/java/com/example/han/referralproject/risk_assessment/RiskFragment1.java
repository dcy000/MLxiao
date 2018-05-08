package com.example.han.referralproject.risk_assessment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.HypertensionDetection;
import com.example.han.referralproject.bean.QuestionChoosed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class RiskFragment1 extends Fragment implements View.OnClickListener {
    private static final String title = "本次风险评估需要您先回答<font color='#FF2D2D'>13</font>道题目，大约<font color='#FF2D2D'>3</font>分钟";
    private View view;
    private TextView mTitle;
    private RecyclerView mQuestionsList;
    private HypertensionDetection.PrimaryBean.ListBean list;
    private List<QuestionChoosed> mData;
    private IFragmentControl iFragmentControl;
    private TextView mTvNext;

    public void setOnRiskFragment1Listener(IFragmentControl iFragmentControl) {
        this.iFragmentControl = iFragmentControl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_risk1, container, false);
        initView(view);
        dealLogic();
        return view;
    }

    private void dealLogic() {
        mTitle.setText(Html.fromHtml(title));
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setSmoothScrollbarEnabled(true);
        mQuestionsList.setLayoutManager(layout);
        mQuestionsList.setHasFixedSize(true);
        mQuestionsList.setNestedScrollingEnabled(false);
        mQuestionsList.setAdapter(new QuestionAdapter(R.layout.item_risk,mData));
    }

    private void initView(View view) {
        mData = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            list = (HypertensionDetection.PrimaryBean.ListBean) arguments.getSerializable("list");
        }
        if (list != null) {
            mData.add(new QuestionChoosed(list.getGenetic(), false));
            mData.add(new QuestionChoosed(list.getDrinkWine(), false));
            mData.add(new QuestionChoosed(list.getMentalStress(), false));
            mData.add(new QuestionChoosed(list.getNaSalt(), false));
            mData.add(new QuestionChoosed(list.getSport(), false));
        }
        mTitle = (TextView) view.findViewById(R.id.title);
        mQuestionsList = (RecyclerView) view.findViewById(R.id.questions_list);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_next:
                if (iFragmentControl != null)
                    iFragmentControl.stepNext(this,mData);
                break;
        }
    }
}
