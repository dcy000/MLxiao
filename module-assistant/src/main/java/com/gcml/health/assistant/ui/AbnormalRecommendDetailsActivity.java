package com.gcml.health.assistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.health.assistant.R;
import com.gcml.health.assistant.model.AssistantRepository;
import com.gcml.health.assistant.model.entity.AbnormalEntity;
import com.gcml.health.assistant.model.entity.AbnormalRecommendEntity;
import com.gcml.health.assistant.model.entity.AbnormalTaskEntity;
import com.gcml.health.assistant.model.entity.AbnormalTaskList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AbnormalRecommendDetailsActivity extends ToolbarBaseActivity {

    private AbnormalRecommendEntity abnormalRecommend;
    private AbnormalEntity abnormal;
    private TextView tvTips;
    private TextView tvTask1;
    private TextView tvTask2;
    private TextView tvTask3;
    private TextView tvTask4;
    private TextView tvConfirmTask;
    private TextView tvAllTask;

    private ArrayList<TextView> tvTasks = new ArrayList<>();
    private ArrayList<ImageView> ivTasks = new ArrayList<>();

    private AssistantRepository repository = new AssistantRepository();
    private AbnormalTaskList tasks;
    private int taskId;
    private ImageView ivTask1;
    private ImageView ivTask2;
    private ImageView ivTask3;
    private ImageView ivTask4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abnormal_activity_recommend_details);
        abnormal = getIntent().getParcelableExtra("abnormal");
        abnormalRecommend = getIntent().getParcelableExtra("abnormalRecommend");
        taskId = getIntent().getIntExtra("taskId", 0);

        mTitleText.setText("智 能 建 议");
//        mRightView.setImageResource(R.drawable.common_icon_home);

        tvTips = (TextView) findViewById(R.id.tvTips);
        tvTask1 = (TextView) findViewById(R.id.tvTask1);
        tvTask2 = (TextView) findViewById(R.id.tvTask2);
        tvTask3 = (TextView) findViewById(R.id.tvTask3);
        tvTask4 = (TextView) findViewById(R.id.tvTask4);
        ivTask1 = (ImageView) findViewById(R.id.ivTask1);
        ivTask2 = (ImageView) findViewById(R.id.ivTask2);
        ivTask3 = (ImageView) findViewById(R.id.ivTask3);
        ivTask4 = (ImageView) findViewById(R.id.ivTask4);
        tvTasks.add(tvTask1);
        tvTasks.add(tvTask2);
        tvTasks.add(tvTask3);
        tvTasks.add(tvTask4);

        ivTasks.add(ivTask1);
        ivTasks.add(ivTask2);
        ivTasks.add(ivTask3);
        ivTasks.add(ivTask4);

        String adviceMsg = abnormalRecommend == null ? "" : abnormalRecommend.getAdviceMsg();
        tvTips.setText(adviceMsg);

        for (TextView tvTask : tvTasks) {
            tvTask.setVisibility(View.GONE);
        }
        for (ImageView ivTask : ivTasks) {
            ivTask.setVisibility(View.GONE);
        }
        tvConfirmTask = (TextView) findViewById(R.id.tvConfirmTask);
        tvConfirmTask.setVisibility(View.GONE);
        tvAllTask = (TextView) findViewById(R.id.tvAllTask);
        tvAllTask.setVisibility(View.GONE);

        tvConfirmTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmTask();
            }
        });

        tvAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTask();
            }
        });
        getData();
    }

    private void allTask() {
        startActivity(new Intent(this, AbnormalAllTaskActivity.class)
                .putExtra("taskId", taskId)
        );
    }

    private void confirmTask() {
        if (abnormal == null || abnormalRecommend == null || tasks == null) {
            ToastUtils.showShort("请求异常");
            return;
        }

        repository.confirmTask(tasks.getTaskId())
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
                        ToastUtils.showShort("确认成功");
                        getData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }


    private void getData() {
        if (abnormal == null || abnormalRecommend == null) {
            ToastUtils.showShort("请求异常");
            return;
        }

        for (TextView tvTask : tvTasks) {
            tvTask.setVisibility(View.GONE);
        }
        for (ImageView ivTask : ivTasks) {
            ivTask.setVisibility(View.GONE);
        }
        tvConfirmTask.setVisibility(View.GONE);
        tvAllTask.setVisibility(View.GONE);

        repository.abnormalTasks(abnormal.getId(), abnormalRecommend.getAdviceId())
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
                .subscribe(new DefaultObserver<AbnormalTaskList>() {
                    @Override
                    public void onNext(AbnormalTaskList tasks) {
                        showTasks(tasks);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }

    private void showTasks(AbnormalTaskList tasks) {
        this.tasks = tasks;
        taskId = tasks.getTaskId();
        List<AbnormalTaskEntity> taskList = tasks.getTaskList();
        if (taskList == null || taskList.size() == 0) {
            ToastUtils.showShort("当前没有任务!");
            return;
        }
        int size = taskList.size();

        for (int i = 0; i < size; i++) {
            if (i >= 4) {
                break;
            }
            AbnormalTaskEntity task = taskList.get(i);
            TextView tvTask = tvTasks.get(i);
            String taskTime = task.getTaskTime();
            if (TextUtils.isEmpty(taskTime)) {
                taskTime = "(" + taskTime + ")";
            } else {
                taskTime = "";
            }
            tvTask.setText(task.getTaskName() + taskTime);
            tvTask.setVisibility(View.VISIBLE);
            ivTasks.get(i).setVisibility(View.VISIBLE);
        }

        boolean taskConfirm = "0".equals(tasks.getStatus());
        tvConfirmTask.setEnabled(taskConfirm);
        tvConfirmTask.setVisibility(View.VISIBLE);
        tvAllTask.setVisibility(View.VISIBLE);
    }

//    @Override
//    protected void backMainActivity() {
//        Routerfit.register(AppRouter.class).skipMainActivity();
//    }
}
