package com.gcml.task.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.task.R;
import com.gcml.task.adapter.TaskChoiceAdapter;
import com.gcml.task.bean.get.TaskHealthBean;
import com.gcml.task.widget.GridDividerItemDecoration;
import com.gcml.task.widget.ListDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 依从性调查问卷选择 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskComplyChoiceFragment extends Fragment implements View.OnClickListener {

    // 问题内容
    private static String TITLE_CONTENT = "titleContent";
    // 引导提示
    private static String HINT_CONTENT = "HintContent";
    // 选项数据
    private static String DATA_CONTENT = "dataContent";
    // 是否多选
    private static String IS_MULTIPLE = "isMultiple";

    private TextView mTitle;
    private TextView mHint;
    private TextView mNext;
    private RecyclerView mRecycler;
    private TaskChoiceAdapter mAdapter;
    private TaskHealthBean.QuestionListBean mData;

    public static TaskComplyChoiceFragment newInstance(String title, String hint, TaskHealthBean.QuestionListBean mData, boolean isMultiple) {
        TaskComplyChoiceFragment fragment = new TaskComplyChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_CONTENT, title);
        bundle.putString(HINT_CONTENT, hint);
        bundle.putSerializable(DATA_CONTENT, mData);
        bundle.putBoolean(IS_MULTIPLE, isMultiple);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_task_choice, null);

        Bundle arguments = getArguments();
        mData = (TaskHealthBean.QuestionListBean) arguments.getSerializable(DATA_CONTENT);
        bindView(view);
        bindData(arguments);
        return view;
    }

    private void bindView(View view) {
        mTitle = view.findViewById(R.id.tv_title);
        mHint = view.findViewById(R.id.tv_hint);
        mNext = view.findViewById(R.id.tv_task_next);
        mRecycler = view.findViewById(R.id.rv_task_choice);
    }

    private void bindData(Bundle arguments) {
        mTitle.setText(arguments.getString(TITLE_CONTENT));
        mHint.setText(arguments.getString(HINT_CONTENT));
        mNext.setOnClickListener(this);
        if (arguments.getBoolean(IS_MULTIPLE)) {
            mNext.setVisibility(View.VISIBLE);
            mAdapter = new TaskChoiceAdapter(getContext(), mData.answerList, true);
            mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mRecycler.addItemDecoration(new GridDividerItemDecoration(24, 24));
            mRecycler.setAdapter(mAdapter);
        } else {
            mNext.setVisibility(View.GONE);
            mAdapter = new TaskChoiceAdapter(getContext(), mData.answerList, false);
            mAdapter.setOnSelectListener(new TaskChoiceAdapter.OnSelectListener() {
                @Override
                public void onSelected(int p) {
                    listener.onNextStep(mAdapter.getSelected(), mData);
                }
            });
            mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecycler.addItemDecoration(new ListDividerItemDecoration(
                    getContext(), LinearLayoutManager.VERTICAL, 24, getResources().getColor(R.color.config_color_white)));
            mRecycler.setAdapter(mAdapter);
        }
    }

    private static List<String> getStrings(ArrayList<TaskHealthBean.QuestionListBean> questionList) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            strings.add(questionList.get(i).questionName);
        }
        return strings;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_task_next) {
            if (listener != null) {
                if (mAdapter.getSelected().length == 0){
                    return;
                }
                listener.onNextStep(mAdapter.getSelected(), mData);
            }
        }
    }

    public interface OnNextStepClickListener {
        void onNextStep(String[] selected, TaskHealthBean.QuestionListBean questionList);
    }

    public void setNextStepListener(TaskComplyChoiceFragment.OnNextStepClickListener listener) {
        this.listener = listener;
    }

    private TaskComplyChoiceFragment.OnNextStepClickListener listener;

}
