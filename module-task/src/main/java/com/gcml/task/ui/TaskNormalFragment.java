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

public class TaskNormalFragment extends Fragment {

    // 数据
    private static String DATA_CONTENT = "dataContent";

    TextView progressMsg, progressNum, progressLeft, progressRight;
    RecyclerForScrollView mRecycler;
    TaskMenuAdapter mAdapter;
    TaskBean mData;
    ProgressBar mProgress;

    public static TaskNormalFragment newInstance(TaskBean mData) {
        TaskNormalFragment fragment = new TaskNormalFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_CONTENT, mData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_task_normal, null);

        Bundle arguments = getArguments();
        mData = (TaskBean) arguments.getSerializable(DATA_CONTENT);
        bindView(view);
        bindData(mData);
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

    private void bindData(TaskBean data) {
        progressMsg.setText("完成进度");
        int done = 0;
        for (int i = 0; i < data.taskList.size(); i++) {
            TaskBean.TaskListBean taskListBean = data.taskList.get(i);
            if ("1".equals(taskListBean.complitionStatus)) {
                done++;
            }
        }
        int progress = done * 100 / data.taskList.size();
        progressNum.setText(progress + "%");
        progressLeft.setText(done + "");
        progressRight.setText("/" + data.taskList.size());
        mProgress.setProgress(progress);
        mAdapter = new TaskMenuAdapter(R.layout.item_task_daily, data.taskList);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecycler.addItemDecoration(new GridDividerItemDecoration(16, 16));
        mRecycler.setAdapter(mAdapter);
    }

}
