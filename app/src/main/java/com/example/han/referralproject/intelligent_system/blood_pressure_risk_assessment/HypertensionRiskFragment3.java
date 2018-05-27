package com.example.han.referralproject.intelligent_system.blood_pressure_risk_assessment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.ToastTool;

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
            mData.add(new QuestionChoosed(list.getKidney(),-1,10));
            mData.add(new QuestionChoosed(list.getHeart(),-1,11));
            mData.add(new QuestionChoosed(list.getEncephalon(),-1,12));
            mData.add(new QuestionChoosed(list.getEye(),-1,13));
        }
        mQuestionsList = (RecyclerView) view.findViewById(R.id.questions_list);
        mTvNext = (TextView) view.findViewById(R.id.tv_next);
        mTvNext.setText("提交");
        mTvNext.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            ((HypertensionRiskActivity) getActivity()).speak("测试三");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_next:
                for (QuestionChoosed question:mData){
                    if (question.isChoosed()==-1){
                        ToastTool.showShort("主人，您还有未回答的题目");
                        ((HypertensionRiskActivity) getActivity()).speak("主人，您还有未回答的题目");
                        return;
                    }
                }
                if (iFragmentControl!=null) {
                    iFragmentControl.stepNext(this, mData);
                }
                break;
        }
    }
}
