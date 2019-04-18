package com.gcml.task.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.widget.RecyclerForScrollView;
import com.gcml.task.R;
import com.gcml.task.adapter.TaskMenuAdapter;
import com.gcml.task.bean.get.TaskBean;
import com.gcml.task.widget.ListDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc: 已完成任务 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskFinishFragment extends Fragment {

    // 数据
    private static String DATA_CONTENT = "dataContent";

    TextView progressTitle, progressNumber, progressMessage, progressTag;
    ImageView imagePeople1, imagePeople2, imagePeople3, imagePeople4, imagePeople5;
    RecyclerForScrollView mRecycler;
    TaskMenuAdapter mAdapter;
    TaskBean mdata;

    public static TaskFinishFragment newInstance(TaskBean mData) {
        TaskFinishFragment fragment = new TaskFinishFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_CONTENT, mData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_task_finish, null);

        Bundle arguments = getArguments();
        mdata = (TaskBean) arguments.getSerializable(DATA_CONTENT);
        bindView(view);
        bindData(mdata);
        return view;
    }

    private void bindView(View view) {
        progressTitle = view.findViewById(R.id.tv_task_title);
        progressNumber = view.findViewById(R.id.tv_task_progress);
        progressMessage = view.findViewById(R.id.tv_task_message);
        imagePeople1 = view.findViewById(R.id.iv_task_people1);
        imagePeople2 = view.findViewById(R.id.iv_task_people2);
        imagePeople3 = view.findViewById(R.id.iv_task_people3);
        imagePeople4 = view.findViewById(R.id.iv_task_people4);
        imagePeople5 = view.findViewById(R.id.iv_task_people5);
        progressTag = view.findViewById(R.id.tv_task_tag);
        mRecycler = view.findViewById(R.id.rv_task);
    }

    private void bindData(TaskBean data) {
        int progress = Integer.parseInt(data.surpassHuman);
        if (progress <= 20) {
            imagePeople1.setVisibility(View.VISIBLE);
            imagePeople2.setVisibility(View.GONE);
            imagePeople3.setVisibility(View.GONE);
            imagePeople4.setVisibility(View.GONE);
            imagePeople5.setVisibility(View.GONE);
        } else if (progress <= 40) {
            imagePeople1.setVisibility(View.GONE);
            imagePeople2.setVisibility(View.VISIBLE);
            imagePeople3.setVisibility(View.GONE);
            imagePeople4.setVisibility(View.GONE);
            imagePeople5.setVisibility(View.GONE);
        } else if (progress <= 60) {
            imagePeople1.setVisibility(View.GONE);
            imagePeople2.setVisibility(View.GONE);
            imagePeople3.setVisibility(View.VISIBLE);
            imagePeople4.setVisibility(View.GONE);
            imagePeople5.setVisibility(View.GONE);
        } else if (progress <= 80) {
            imagePeople1.setVisibility(View.GONE);
            imagePeople2.setVisibility(View.GONE);
            imagePeople3.setVisibility(View.GONE);
            imagePeople4.setVisibility(View.VISIBLE);
            imagePeople5.setVisibility(View.GONE);
        } else if (progress <= 100) {
            imagePeople1.setVisibility(View.GONE);
            imagePeople2.setVisibility(View.GONE);
            imagePeople3.setVisibility(View.GONE);
            imagePeople4.setVisibility(View.GONE);
            imagePeople5.setVisibility(View.VISIBLE);
        }
        progressTitle.setText(R.string.today_mission_has_been_completed);
        progressNumber.setText(progress + "%");
        progressMessage.setText(R.string.people_are_overtaken_by_you);
        progressTag.setText(R.string.you_can_also);
        List<TaskBean.TaskListBean> list = new ArrayList<>();
        for (int i = 0; i < data.taskList.size(); i++) {
            if (!data.taskList.get(i).mustStatus.equals("1")) {
                list.add(data.taskList.get(i));
            }
        }
        mAdapter = new TaskMenuAdapter(R.layout.item_task_daily, list);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new ListDividerItemDecoration(
                getContext(), LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.config_color_white)));
        mRecycler.setAdapter(mAdapter);
    }
}
