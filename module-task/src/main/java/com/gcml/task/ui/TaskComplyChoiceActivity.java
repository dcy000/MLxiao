package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.Post.TaskSchemaResultBean;
import com.gcml.task.bean.get.TaskHealthBean;
import com.gcml.task.bean.Post.TaskSchemaBean;
import com.gcml.task.network.TaskRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 依从性调查问卷选择页面 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskComplyChoiceActivity extends AppCompatActivity implements TaskComplyChoiceFragment.OnNextStepClickListener {

    ViewPager mViewPager;
    TranslucentToolBar mToolBar;
    List<Fragment> fragments = new ArrayList<>();
    List<TaskHealthBean.QuestionListBean> mList = new ArrayList<>();
    TaskRepository mTaskRepository = new TaskRepository();
    TaskSchemaBean mPostData = new TaskSchemaBean();
    private LoadingDialog mTipDialog;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getExtras().getBoolean("isFirst")) {
            finish();
            CC.obtainBuilder("app.component.task.comply.choice").addParam("isFirst", false).setContext(TaskComplyChoiceActivity.this).build().callAsync();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_health);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_task_health);
        mViewPager = findViewById(R.id.vp_task_health);
    }

    private void bindData() {
        mToolBar.setData("健 康 问 答", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                if (mViewPager.getCurrentItem() == 0) {
                    CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                    finish();
                    return;
                }
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                if (mViewPager.getCurrentItem() >= 0) {
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                            "主人" + mList.get(mViewPager.getCurrentItem()).questionName,
                            false);
                }
            }

            @Override
            public void onRightClick() {
                new AlertDialog(TaskComplyChoiceActivity.this).builder()
                        .setMsg("您已经开始做题，是否要离开当前页面？")
                        .setPositiveButton("继续做题", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setNegativeButton("确认离开", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                                finish();
                            }
                        }).show();
            }
        });
        getTaskHealthData();
    }

    @SuppressLint("CheckResult")
    private void getTaskHealthData() {
        if (mTipDialog != null) {
            mTipDialog.dismiss();
        }
        mTipDialog = new LoadingDialog.Builder(TaskComplyChoiceActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        mTaskRepository.taskHealthListFromApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        mTipDialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        mTipDialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<TaskHealthBean>() {
                    @Override
                    public void onNext(TaskHealthBean body) {
                        super.onNext(body);
                        mList = body.questionList;
                        //给提交的数据赋值
                        mPostData.equipmentId = Utils.getDeviceId(getContentResolver());
                        mPostData.hmQuestionnaireId = body.hmQuestionnaireId;
                        mPostData.hmQuestionnaireName = body.questionnaireName;
                        mPostData.userId = Integer.parseInt(UserSpHelper.getUserId());
                        mPostData.score = 0;
                        mPostData.answerList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            TaskHealthBean.QuestionListBean questionBean = mList.get(i);
                            TaskComplyChoiceFragment instance = TaskComplyChoiceFragment.newInstance(
                                    mList.get(i).questionName,
                                    i,
                                    mList.get(i),
                                    false);
                            instance.setNextStepListener(TaskComplyChoiceActivity.this);
                            fragments.add(instance);
                            //给提交的数据赋值
                            TaskSchemaBean.AnswerListBean answerBean = new TaskSchemaBean.AnswerListBean();
                            answerBean.questionName = questionBean.questionName;
                            answerBean.hmQuestionId = questionBean.hmQuestionId;
                            mPostData.answerList.add(answerBean);
                        }

                        mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), fragments));
                        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                               "主人" + mList.get(0).questionName,
                                false);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Toast.makeText(TaskComplyChoiceActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onNextStep(int position, TaskHealthBean.QuestionListBean questionList) {
        //更新提交数据的bean
        List<TaskSchemaBean.AnswerListBean> answerList = mPostData.answerList;
        answerList.get(position).answerName = getAnswerInfo(questionList);
        answerList.get(position).hmAnswerId = getHmAnswerId(questionList);
        answerList.get(position).score = getAnswerScores(questionList);
        answerList.get(position).questionSeq = questionList.seq;
        //最后一页
        if (mViewPager.getCurrentItem() + 1 == mViewPager.getAdapter().getCount()) {
            //算总分
            for (int i = 0; i < answerList.size(); i++) {
                mPostData.score += answerList.get(i).score;
            }
            postHealthData();
            return;
        } else {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                    "主人" + mList.get(mViewPager.getCurrentItem() + 1).questionName,
                    false);
        }
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    private String getAnswerInfo(TaskHealthBean.QuestionListBean questionList) {
        String answer = "";
        for (int i = 0; i < questionList.answerList.size(); i++) {
            if (questionList.answerList.get(i).isChoosed) {
                answer = questionList.answerList.get(i).answerInfo;
            }
        }
        return answer;
    }

    private String getHmAnswerId(TaskHealthBean.QuestionListBean questionList) {
        String answerId = "";
        for (int i = 0; i < questionList.answerList.size(); i++) {
            if (questionList.answerList.get(i).isChoosed) {
                answerId = questionList.answerList.get(i).hmAnswerId;
            }
        }
        return answerId;
    }

    private int getAnswerScores(TaskHealthBean.QuestionListBean questionList) {
        int score = 0;
        for (int i = 0; i < questionList.answerList.size(); i++) {
            if (questionList.answerList.get(i).isChoosed) {
                score =  questionList.answerList.get(i).answerScore;
            }
        }
        return score;
    }

    @SuppressLint("CheckResult")
    private void postHealthData() {
        LoadingDialog upDialog = new LoadingDialog.Builder(TaskComplyChoiceActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();
        mTaskRepository.taskHealthListForApi(mPostData, UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        upDialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        upDialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<TaskSchemaResultBean>() {
                    @Override
                    public void onNext(TaskSchemaResultBean body) {
                        super.onNext(body);
                        LoadingDialog successDialog = new LoadingDialog.Builder(TaskComplyChoiceActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("上传成功")
                                .create();
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successDialog.dismiss();
                            }
                        }, 500);
                        CC.obtainBuilder("app.component.task.comply.result")
                                .addParam("resultBean", body)
                                .build()
                                .callAsync();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(TaskComplyChoiceActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("上传失败")
                                .create();
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog.dismiss();
                            }
                        }, 500);
                    }
                });
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> list;

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTipDialog != null) {
            mTipDialog.dismiss();
        }
        MLVoiceSynthetize.stop();
    }

}
