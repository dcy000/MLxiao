package com.gcml.task.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcml.common.widget.RecyclerForScrollView;
import com.gcml.task.R;
import com.gcml.task.adapter.TaskMenuAdapter;
import com.gcml.task.bean.get.TaskBean;
import com.gcml.task.widget.GridDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * desc: 待完成任务 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskNormalFragment extends Fragment {

    // 数据
    private static String DATA_CONTENT = "dataContent";

    TextView progressMsg, progressNum, progressLeft, progressRight;
    RecyclerForScrollView mRecycler;
    TaskMenuAdapter mAdapter;
    List<TaskBean.TaskListBean> mList = new ArrayList<>();
    ProgressBar mProgress;

    public static TaskNormalFragment newInstance(List<TaskBean.TaskListBean> mData) {
        TaskNormalFragment fragment = new TaskNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_CONTENT, (Serializable) mData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_task_normal, null);

        Bundle arguments = getArguments();
        mList = (List<TaskBean.TaskListBean>) arguments.getSerializable(DATA_CONTENT);
        bindView(view);
        bindData(mList);
        return view;
    }

    private void bindView(View view) {
        progressMsg = view.findViewById(R.id.tv_task_msg);
        progressNum = view.findViewById(R.id.tv_task_num);
        progressLeft = view.findViewById(R.id.tv_task_left);
        progressRight = view.findViewById(R.id.tv_task_right);
        mRecycler = view.findViewById(R.id.rv_task);
        mProgress = view.findViewById(R.id.pb_task);
    }

    private void bindData(List<TaskBean.TaskListBean> list) {
        progressMsg.setText("完成进度");
        int done = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).complitionStatus.equals("1")) {
                done++;
            }
        }
        int progress = done * 100 / list.size();
        progressNum.setText(progress + "%");
        progressLeft.setText(done + "");
        progressRight.setText("/" + list.size());
        mProgress.setProgress(progress);
        mAdapter = new TaskMenuAdapter(R.layout.item_task_daily, list);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecycler.addItemDecoration(new GridDividerItemDecoration(16, 16));
        mRecycler.setAdapter(mAdapter);
    }

}
