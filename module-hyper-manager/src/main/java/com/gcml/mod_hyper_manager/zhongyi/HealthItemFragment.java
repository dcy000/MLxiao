package com.gcml.mod_hyper_manager.zhongyi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.zhongyi.bean.OlderHealthManagementBean;

import java.util.List;


/**
 * Created by lenovo on 2018/5/8.
 */

public class HealthItemFragment extends Fragment {
    public static final String KEY_INDEX = "key_index";
    public static final String KEY_DATA = "key_data";
    private String index;
    private View view;
    private OlderHealthManagementSerciveActivity activity;
    private int lastXChecedId;
    OlderHealthManagementBean.DataBean.QuestionListBean data;


    public static HealthItemFragment getInstance(String index, OlderHealthManagementBean.DataBean.QuestionListBean data) {
        HealthItemFragment fragment = new HealthItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_INDEX, index);
        args.putSerializable(KEY_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (OlderHealthManagementSerciveActivity) getActivity();
        if (getArguments() != null) {
            index = getArguments().getString(KEY_INDEX);
            data = (OlderHealthManagementBean.DataBean.QuestionListBean) getArguments().getSerializable(KEY_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_health_monitor, container, false);
        initRadiogroup();
        return view;
    }

    private void initRadiogroup() {
        //题目
        TextView question = view.findViewById(R.id.tv_question);
        question.setText(data.questionName);

        //答案 动态添加
        final RadioGroup rgAnwser = view.findViewById(R.id.rg_anwser);
        List<OlderHealthManagementBean.DataBean.QuestionListBean.AnswerListBean> answerList = data.answerList;
        if (answerList != null && answerList.size() != 0) {
            for (int i = 0; i < answerList.size(); i++) {
                RadioButton button = new RadioButton(getContext());
                setRadioButtonParams(button);
                button.setText(answerList.get(i).answerInfo);
//                //返回上一步显示=========开始  由于viewpager缓存步长限制
//                if (data.getSelected&&data.hmAnswerId.equals(answerList.get(i).hmAnswerId)){
//                    button.setChecked(true);
//                }else{
//                    button.setChecked(false);
//                }
//                //返回上一步显示=========结束
                rgAnwser.addView(button, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

        //监听radiogrouup
        rgAnwser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                List<OlderHealthManagementBean.DataBean.QuestionListBean.AnswerListBean> list = data.answerList;
                for (int i = 0; i < list.size(); i++) {
                    RadioButton button = (RadioButton) rgAnwser.getChildAt(i);
                    //设置选中
                    if (button.isChecked()) {
                        data.isSelected = true;

                        //赋值
                        data.answerScore = list.get(i).answerScore;//得分
                        data.hmQuestionId = list.get(i).hmQuestionId;//问题id
                        data.hmAnswerId = list.get(i).hmAnswerId;//答案id
                        data.questionSeq = data.seq;//序号
                    }
                }


//                点击自动翻页
                if (lastXChecedId != checkedId) {
                    ((OlderHealthManagementSerciveActivity) getActivity()).nextCurrentPage();
                }
                lastXChecedId = checkedId;

            }
        });

    }


    private void setRadioButtonParams(RadioButton radioButton) {
        radioButton.setButtonDrawable(R.drawable.bg_item_selete_set);
        radioButton.setTextSize(32);
        radioButton.setTextColor(getActivity().getResources()
                .getColorStateList(R.color.older_healh_management_color_set));
        radioButton.setPadding(20, 20, 20, 20);

    }

}
