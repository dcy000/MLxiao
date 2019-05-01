package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.get.TaskBean;
import com.gcml.task.network.TaskRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 日常任务页面，包括待完成任务和已完成任务 .
 * author: wecent .
 * date: 2018/8/20 .
 */
@Route(path = "/task/task/activity")
public class TaskActivity extends FragmentActivity {

    TranslucentToolBar mToolBar;
    TaskRepository mTaskRepository = new TaskRepository();
    Handler mHandler = new Handler();
    String startType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        // 解决ScrollView默认位置不是最顶部
        getWindow().getDecorView().setFocusable(true);
        getWindow().getDecorView().setFocusableInTouchMode(true);
        getWindow().getDecorView().requestFocus();
        startType = getIntent().getStringExtra("startType");

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到每日任务。", false);
        mToolBar.setData("每 日 任 务", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                if (startType.equals("MLMain")) {
//                    CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                    finish();
                } else if (startType.equals("MLSpeech")) {
                    finish();
                }
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getTaskData() {
        LoadingDialog tipDialog = new LoadingDialog.Builder(TaskActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        mTaskRepository.taskListFromApi(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        tipDialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<TaskBean>() {
                    @Override
                    public void onNext(TaskBean body) {
                        super.onNext(body);
                        if (body.complitionStatus) {
                            TaskFinishFragment instanceFinish = TaskFinishFragment.newInstance(body);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_task, instanceFinish).commit();
                        } else {
                            TaskNormalFragment instanceNoemal = TaskNormalFragment.newInstance(body);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_task, instanceNoemal).commit();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(TaskActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("请求失败")
                                .create();
                        errorDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog.dismiss();
                            }
                        }, 500);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskData();
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
