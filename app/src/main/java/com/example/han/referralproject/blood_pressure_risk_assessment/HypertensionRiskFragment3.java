package com.example.han.referralproject.blood_pressure_risk_assessment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.HypertensionDetection;
import com.example.han.referralproject.bean.QuestionChoosed;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HypertensionRiskFragment3 extends Fragment implements View.OnClickListener {


    private View view;
    private RecyclerView mQuestionsList;
    private TextView mTvNext;
    private IFragmentControl iFragmentControl;
    private List<QuestionChoosed> mData;
    private HypertensionDetection.ReceptorBean.ListBeanXX list;

    public HypertensionRiskFragment3() {
        // Required empty public constructor
    }
    public void setOnRiskFragment3Listener(IFragmentControl iFragmentControl) {
        this.iFragmentControl = iFragmentControl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_risk_fragment2, container, false);
        initView(view);
        dealLogic();
        return view;
    }

    private void dealLogic() {
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setSmoothScrollbarEnabled(true);
        mQuestionsList.setLayoutManager(manager);
        mQuestionsList.setNestedScrollingEnabled(false);
        mQuestionsList.setHasFixedSize(true);
        mQuestionsList.setAdapter(new QuestionAdapter(R.layout.item_risk,mData));
    }

    private void initView(View view) {
        mData=new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            list = (HypertensionDetection.ReceptorBean.ListBeanXX) arguments.getSerializable("list");
        }
        if (list != null) {
            mData.add(new QuestionChoosed(list.getKidney(),false));
            mData.add(new QuestionChoosed(list.getHeart(),false));
            mData.add(new QuestionChoosed(list.getEncephalon(),false));
            mData.add(new QuestionChoosed(list.getEye(),false));
        }
        mQuestionsList = (RecyclerView) view.findViewById(R.id.questions_list);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setText("提交");
        mTvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_next:
                iFragmentControl.stepNext(this,mData);
                break;
        }
    }
}
