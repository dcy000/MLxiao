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
import com.gcml.lib_utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HypertensionRiskFragment2 extends Fragment implements View.OnClickListener {


    private View view;
    private RecyclerView mQuestionsList;
    private TextView mTvNext;
    private IFragmentControl iFragmentControl;
    private List<QuestionChoosed> mData;
    private HypertensionDetection.SecondaryBean.ListBeanX list;

    public HypertensionRiskFragment2() {
        // Required empty public constructor
    }
    public void setOnRiskFragment2Listener(IFragmentControl iFragmentControl) {
        this.iFragmentControl = iFragmentControl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_risk_fragment2, container, false);
        initView();
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

    private void initView() {
        mData=new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments!=null){
            list = (HypertensionDetection.SecondaryBean.ListBeanX) arguments.getSerializable("list");
        }
        if (list != null) {
            mData.add(new QuestionChoosed(list.getRenalIllness(),-1,6));
            mData.add(new QuestionChoosed(list.getEndocrineIllness(),-1,7));
            mData.add(new QuestionChoosed(list.getPrrs(),-1,8));
            mData.add(new QuestionChoosed(list.getDrugInduced(),-1,9));
        }
        mQuestionsList = view.findViewById(R.id.questions_list);
        mTvNext = view.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            ((HypertensionRiskActivity) getActivity()).speak("测试二");
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
                        ToastUtils.showShort("您好，您还有未回答的题目");
                        ((HypertensionRiskActivity) getActivity()).speak("您好，您还有未回答的题目");
                        return;
                    }
                }
                if (iFragmentControl != null) {
                    iFragmentControl.stepNext(this,mData);
                }
                break;
        }
    }
}
