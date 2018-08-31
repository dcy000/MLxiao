package com.gcml.task.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
    private static String DATA_POSITION = "dataPosition";
    // 选项数据
    private static String DATA_CONTENT = "dataContent";
    // 是否多选
    private static String IS_MULTIPLE = "isMultiple";

    private TextView mTitle;
    private TextView mHint;
    private TextView mNext;
    private RecyclerView mRecycler;
    private TaskChoiceAdapter mAdapter;
    private int mPosition;
    private TaskHealthBean.QuestionListBean mData;
    public List<String> selectedList = new ArrayList<>();

    public static TaskComplyChoiceFragment newInstance(String title, int position, TaskHealthBean.QuestionListBean mData, boolean isMultiple) {
        TaskComplyChoiceFragment fragment = new TaskComplyChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_CONTENT, title);
        bundle.putInt(DATA_POSITION, position);
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
        mPosition = arguments.getInt(DATA_POSITION);
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
        mNext.setOnClickListener(this);
        if (arguments.getBoolean(IS_MULTIPLE)) {
            mNext.setVisibility(View.VISIBLE);
            mAdapter = new TaskChoiceAdapter(R.layout.item_task_choice, mData.answerList);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    TaskHealthBean.QuestionListBean.AnswerListBean myInfo = mData.answerList.get(position);
                    if (myInfo.isChoosed) {
                        myInfo.isChoosed = false;
                        view.setSelected(false);
                    } else {
                        //选中非"无"按钮，需要将"无"的选中状态取消
                        for (TaskHealthBean.QuestionListBean.AnswerListBean bean : mData.answerList) {
                            if (position != 0 && bean.isChoosed) {
                                bean.isChoosed = false;
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //选中"无"按钮，其他的按钮状态都取消
                        if (position == 0) {
                            for (int i = 0; i < mData.answerList.size(); i++) {
                                if (i != position) {
                                    mData.answerList.get(i).isChoosed = false;
                                } else {
                                    mData.answerList.get(i).isChoosed = true;
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        listener.onNextStep(mPosition, mData);
                    }
                }
            });
            mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mRecycler.addItemDecoration(new GridDividerItemDecoration(24, 24));
            mRecycler.setAdapter(mAdapter);
        } else {
            mNext.setVisibility(View.GONE);
            mAdapter = new TaskChoiceAdapter(R.layout.item_task_choice, mData.answerList);
            Log.e("xxxxxxxxxxx", "4");
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.e("xxxxxxxxxxx", "0");
                    TaskHealthBean.QuestionListBean.AnswerListBean myInfo = mData.answerList.get(position);
                    if (myInfo.isChoosed) {
                        myInfo.isChoosed = false;
                        selectedList.remove(position + "");
                        view.setSelected(false);
                        Log.e("xxxxxxxxxxx", "1");
                    } else {
                        for (TaskHealthBean.QuestionListBean.AnswerListBean bean : mData.answerList) {
                            if (myInfo == bean) {
                                bean.isChoosed = true;
                                selectedList.add(position + "");
                            } else {
                                bean.isChoosed = false;
                                selectedList.remove(position + "");
                            }
                            adapter.notifyDataSetChanged();
                        }
                        listener.onNextStep(mPosition, mData);
                        Log.e("xxxxxxxxxxx", "2");
                    }
                }
            });
            mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecycler.addItemDecoration(new ListDividerItemDecoration(
                    getContext(), LinearLayoutManager.VERTICAL, 24, getResources().getColor(R.color.config_color_white)));
            mRecycler.setAdapter(mAdapter);
        }
    }

    public String[] getSelected() {
        String[] result = new String[selectedList.size()];
        for (int i = 0; i < selectedList.size(); i++) {
            result[i] = selectedList.get(i);
        }
        return result;
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
                if (getSelected().length == 0){
                    return;
                }
                listener.onNextStep(mPosition, mData);
            }
        }
    }

    public interface OnNextStepClickListener {
        void onNextStep(int position, TaskHealthBean.QuestionListBean questionList);
    }

    public void setNextStepListener(TaskComplyChoiceFragment.OnNextStepClickListener listener) {
        this.listener = listener;
    }

    private TaskComplyChoiceFragment.OnNextStepClickListener listener;

}
