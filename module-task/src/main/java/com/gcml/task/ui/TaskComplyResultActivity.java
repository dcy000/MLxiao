package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.Post.TaskSchemaResultBean;
import com.gcml.task.network.TaskRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: TaskComplyResultActivity .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskComplyResultActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    RelativeLayout mLayoutSport;
    TextView resultTitle;
    TextView totalContent1, totalContent2, totalContent3, totalContent4;
    TextView testContent1, testContent2, testContent3;
    TextView sportContent1, sportContent2, sportContent3;
    TextView dineContent1, dineContent2, dineContent3, dineContent4;
    TextView resultAgain, resultAffirm;
    TaskSchemaResultBean resultBean;
    TaskRepository mTaskRepository = new TaskRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_result);
        resultBean = (TaskSchemaResultBean) getIntent().getExtras().getSerializable("resultBean");

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task_result);
        mLayoutSport = findViewById(R.id.rl_task_result_sport);
        resultTitle = findViewById(R.id.tv_task_result_title);
        resultTitle.setClickable(true);

        totalContent1 = findViewById(R.id.tv_total_content1);
        totalContent2 = findViewById(R.id.tv_total_content2);
        totalContent3 = findViewById(R.id.tv_total_content3);
        totalContent4 = findViewById(R.id.tv_total_content4);

        testContent1 = findViewById(R.id.tv_test_content1);
        testContent2 = findViewById(R.id.tv_test_content2);
        testContent3 = findViewById(R.id.tv_test_content3);

        sportContent1 = findViewById(R.id.tv_sport_content1);
        sportContent2 = findViewById(R.id.tv_sport_content2);
        sportContent3 = findViewById(R.id.tv_sport_content3);

        dineContent1 = findViewById(R.id.tv_dine_content1);
        dineContent2 = findViewById(R.id.tv_dine_content2);
        dineContent3 = findViewById(R.id.tv_dine_content3);
        dineContent4 = findViewById(R.id.tv_dine_content4);

        resultAgain = findViewById(R.id.tv_task_result_again);
        resultAffirm = findViewById(R.id.tv_task_result_affirm);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), resultBean.result, false);
        mToolBar.setData(UM.getString(R.string.title_health_report), R.drawable.common_btn_back, UM.getString(R.string.toolbar_back), R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                new AlertDialog(TaskComplyResultActivity.this).builder()
                        .setMsg(UM.getString(R.string.leave_this_page))
                        .setNegativeButton(UM.getString(R.string.dialog_button_cancel), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton(UM.getString(R.string.dialog_button_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });
        resultTitle.setText(resultBean.result);

        totalContent1.setText(resultBean.initiative == null ? UM.getString(R.string.none) : resultBean.initiative);
        totalContent2.setText(resultBean.compliance == null ? UM.getString(R.string.none) : resultBean.compliance);
        totalContent3.setText(resultBean.attitude == null ? UM.getString(R.string.none) : resultBean.attitude);
        totalContent4.setText(resultBean.awareness == null ? UM.getString(R.string.none) : resultBean.awareness);

        testContent1.setText(resultBean.detectionPlan.HTN == null ? UM.getString(R.string.none) : resultBean.detectionPlan.HTN);
        testContent2.setText(resultBean.detectionPlan.weight == null ? UM.getString(R.string.none) : resultBean.detectionPlan.weight);
        testContent3.setText(resultBean.detectionPlan.GLU == null ? UM.getString(R.string.none) : resultBean.detectionPlan.GLU);

        if (resultBean.sportRecommend != null) {
            mLayoutSport.setVisibility(View.VISIBLE);
            sportContent1.setText(UM.getString(R.string.peer_weak_times,resultBean.sportRecommend.weekCount));
            sportContent2.setText(resultBean.sportRecommend.timeCost + "min");
            sportContent3.setText(String.format("%.2f", resultBean.sportRecommend.weightTarget) + "kg");
        } else {
            mLayoutSport.setVisibility(View.GONE);
        }

        dineContent1.setText(resultBean.intake.naSalt == null ? UM.getString(R.string.none) : "<" + resultBean.intake.naSalt);
        dineContent2.setText(resultBean.intake.grease == null ? UM.getString(R.string.none) : "<" + resultBean.intake.grease);
        dineContent3.setText(resultBean.intake.drink == null ? UM.getString(R.string.none) : "<" + resultBean.intake.drink);
        dineContent4.setText(resultBean.intake.smoke == null ? UM.getString(R.string.none) : resultBean.intake.smoke);

        resultAgain.setOnClickListener(this);
        resultAffirm.setOnClickListener(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_task_result_again) {
            new AlertDialog(TaskComplyResultActivity.this).builder()
                    .setMsg(UM.getString(R.string.will_it_continue))
                    .setPositiveButton(UM.getString(R.string.Continue), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTaskRepository.taskHealthDeleteForApi(UserSpHelper.getUserId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .as(RxUtils.autoDisposeConverter(TaskComplyResultActivity.this))
                                    .subscribeWith(new DefaultObserver<Object>() {
                                        @Override
                                        public void onNext(Object body) {
                                            super.onNext(body);
                                            CC.obtainBuilder("app.component.task.comply.choice").addParam("isFirst", true).setContext(TaskComplyResultActivity.this).build().callAsync();
                                            finish();
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            super.onError(throwable);
                                            ToastUtils.showShort("网络错误，请重试");
                                        }
                                    });
                        }
                    })
                    .setNegativeButton(UM.getString(R.string.toolbar_back), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();

        } else if (v.getId() == R.id.tv_task_result_affirm) {
            CC.obtainBuilder("app.component.task").addParam("startType", "MLMain").build().callAsync();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
