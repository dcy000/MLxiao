package com.gcml.health.assistant.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcml.common.LazyFragment;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.GridViewDividerItemDecoration;
import com.gcml.health.assistant.R;
import com.gcml.health.assistant.model.AssistantRepository;
import com.gcml.health.assistant.model.entity.AllTaskEntity;
import com.gcml.health.assistant.model.entity.AllTaskList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class AbnormalAllTaskFragment extends LazyFragment {

    private TextView progressMsg, progressNum, progressLeft, progressRight;
    private RecyclerView mRecycler;
    private ProgressBar mProgress;

    private int taskId;

    private AssistantRepository repository = new AssistantRepository();


    public AbnormalAllTaskFragment() {
        // Required empty public constructor
    }

    public static AbnormalAllTaskFragment newInstance(int taskId) {
        AbnormalAllTaskFragment fragment = new AbnormalAllTaskFragment();
        Bundle args = new Bundle();
        args.putInt("taskId", taskId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            taskId = arguments.getInt("taskId", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.abnormal_fragment_all_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressMsg = view.findViewById(R.id.tv_task_msg);
        progressNum = view.findViewById(R.id.tv_task_num);
        progressLeft = view.findViewById(R.id.tv_task_left);
        progressRight = view.findViewById(R.id.tv_task_right);
        mProgress = view.findViewById(R.id.pb_task);
        mRecycler = view.findViewById(R.id.rv_task);
        mRecycler.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();
        getData();
    }

    private void getData() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        repository.allTask(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("加载中");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<AllTaskList>() {
                    @Override
                    public void onNext(AllTaskList allTaskList) {
                        showAllTask(allTaskList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    private void showAllTask(AllTaskList taskList) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        progressMsg.setText("完成进度");
        int done = 0;
        List<AllTaskEntity> tasks = taskList.getTaskList();
        int size = tasks == null ? 0 : tasks.size();
        allTasks.clear();
        allTasks.addAll(tasks);
        for (int i = 0; i < size; i++) {
            AllTaskEntity task = tasks.get(i);
            if ("1".equals(task.getStatus())) {
                done++;
            }
        }
        int progress = size == 0 ? 0 : done * 100 / size;
        progressNum.setText(progress + "%");
        progressLeft.setText(done + "");
        progressRight.setText("/" + tasks.size());
        mProgress.setProgress(progress);
        mAdapter = new AllTaskAdapter();
        mRecycler.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecycler.addItemDecoration(new GridViewDividerItemDecoration(16, 16));
        mRecycler.setAdapter(mAdapter);
    }

    private AllTaskAdapter mAdapter;
    private ArrayList<AllTaskEntity> allTasks = new ArrayList<>();

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            int position = mRecycler.getChildAdapterPosition(v);
            AllTaskEntity task = allTasks.get(position);
            confirmOneTask(task);
        }
    };

    private void confirmOneTask(AllTaskEntity task) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        repository.confirmOneTask(task.getUserTasklogId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("加载中");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object obj) {
                        getData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    private class AllTaskHolder extends RecyclerView.ViewHolder {

        private final TextView tv_task_tag;
        private final TextView tv_task_name;
        private final TextView tv_task_name_other;
        private final TextView tv_task_time;
        private final TextView tv_task_action;
        private final ImageView iv_task_action;

        public AllTaskHolder(View itemView) {
            super(itemView);
            tv_task_tag = (TextView) itemView.findViewById(R.id.tv_task_tag);
            tv_task_name = (TextView) itemView.findViewById(R.id.tv_task_name);
            tv_task_name_other = (TextView) itemView.findViewById(R.id.tv_task_name_other);
            tv_task_time = (TextView) itemView.findViewById(R.id.tv_task_time);
            tv_task_action = (TextView) itemView.findViewById(R.id.tv_task_action);
            iv_task_action = (ImageView) itemView.findViewById(R.id.iv_task_action);
            tv_task_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(itemView);
                    }
                }
            });
        }

        public void onBind(int position) {
            tv_task_tag.setVisibility(View.GONE);
            tv_task_name.setVisibility(View.GONE);
            AllTaskEntity task = allTasks.get(position);
            tv_task_name_other.setText(task.getTaskName());
            tv_task_time.setText("(测量时段：" + task.getTaskTime() + ")");
            String taskStatus = task.getStatus();
            if ("0".equals(taskStatus)) {
                iv_task_action.setVisibility(View.GONE);
                tv_task_action.setText("确认");
                tv_task_action.setEnabled(true);
                tv_task_action.setVisibility(View.VISIBLE);
            } else if ("1".equals(taskStatus)) {
                tv_task_action.setVisibility(View.GONE);
                iv_task_action.setVisibility(View.VISIBLE);
            } else if ("2".equals(taskStatus)) {
                iv_task_action.setVisibility(View.GONE);
                tv_task_action.setText("未开启");
                tv_task_action.setEnabled(false);
                tv_task_action.setTextColor(Color.parseColor("#3F86FC"));
                tv_task_action.setBackgroundResource(R.drawable.abnormal_btn_task_action2);
                tv_task_action.setVisibility(View.VISIBLE);
            }
        }
    }

    private class AllTaskAdapter extends RecyclerView.Adapter<AllTaskHolder> {

        @NonNull
        @Override
        public AllTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.abnormal_item_all_task, parent, false);
            return new AllTaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllTaskHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return allTasks == null ? 0 : allTasks.size();
        }
    }

    public void showLoading(String tips) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity instanceof ToolbarBaseActivity) {
            ((ToolbarBaseActivity) activity).showLoading(tips);
        }
    }

    public void dismissLoading() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        if (activity instanceof ToolbarBaseActivity) {
            ((ToolbarBaseActivity) activity).dismissLoading();
        }
    }
}
