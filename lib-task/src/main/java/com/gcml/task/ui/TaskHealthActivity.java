package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
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
 * 原发性高血压问卷
 */
public class TaskHealthActivity extends AppCompatActivity implements TaskChoiceFragment.OnNextStepClickListener {

    ViewPager mViewPager;
    TranslucentToolBar mToolBar;
    List<Fragment> fragments = new ArrayList<>();
    List<TaskHealthBean.QuestionListBean> mList = new ArrayList<>();
    TaskRepository mTaskRepository = new TaskRepository();
    TaskSchemaBean mPostData = new TaskSchemaBean();

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
        mToolBar.setData("健 康 问 答", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                CC.obtainBuilder("app.component.task.test").setContext(TaskHealthActivity.this).build().callAsync();
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        getTaskHealthData();
    }

    @SuppressLint("CheckResult")
    private void getTaskHealthData() {
        LoadingDialog tipDialog = new LoadingDialog.Builder(TaskHealthActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        mTaskRepository.taskHealthListFromApi()
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
                .subscribeWith(new DefaultObserver<TaskHealthBean>() {
                    @Override
                    public void onNext(TaskHealthBean body) {
                        super.onNext(body);
                        mList = body.questionList;
                        //给提交的数据赋值
                        mPostData.equipmentId = Utils.getDeviceId(getContentResolver());
                        mPostData.hmQuestionnaireId = body.hmQuestionnaireId;
                        mPostData.hmQuestionnaireName = body.questionnaireName;
//                        mPostData.userId = LocalShared.getInstance(PrimaryHypertensionActivity.this).getUserId();
                        mPostData.userId = 100206;
                        mPostData.score = 0;
                        mPostData.answerList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            TaskHealthBean.QuestionListBean questionBean = mList.get(i);
                            TaskChoiceFragment instance = TaskChoiceFragment.newInstance(
                                    mList.get(i).questionName,
                                    "请认证阅读",
                                    mList.get(i),
                                    false);
                            instance.setNextStepListener(TaskHealthActivity.this);
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
                        Toast.makeText(TaskHealthActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onNextStep(String[] selected, TaskHealthBean.QuestionListBean questionList) {
        //更新提交数据的bean
        List<TaskSchemaBean.AnswerListBean> answerList = mPostData.answerList;
        for (int i = 0; i < answerList.size(); i++) {
            if (questionList.hmQuestionId.equals(answerList.get(i).hmQuestionId)) {
                answerList.get(i).answerName = getAnswerNames(questionList, selected).toString()
                        .replaceAll("\\[", "")
                        .replaceAll("]", "");
                answerList.get(i).hmAnswerId = questionList.answerList.get(Integer.parseInt(selected[0])).hmAnswerId;
                answerList.get(i).score = getAnswerScores(questionList, selected);//每个项目 得分之后
                answerList.get(i).questionSeq = questionList.seq;
            }
        }
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

    private List<String> getAnswerNames(TaskHealthBean.QuestionListBean questionList, String[] selected) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            strings.add(questionList.answerList.get(Integer.parseInt(selected[i])).answerInfo);
        }
        return strings;
    }

    private int getAnswerScores(TaskHealthBean.QuestionListBean questionList, String[] selected) {
        int score = 0;
        for (int i = 0; i < selected.length; i++) {
            score += questionList.answerList.get(Integer.parseInt(selected[i])).answerScore;
        }
        return score;
    }


    @SuppressLint("CheckResult")
    private void postHealthData() {
        LoadingDialog upDialog = new LoadingDialog.Builder(TaskHealthActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();
        mTaskRepository.taskHealthListForApi(mPostData, "100206")
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
                        LoadingDialog successDialog = new LoadingDialog.Builder(TaskHealthActivity.this)
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("上传成功")
                                .create();
                        mViewPager.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                successDialog.dismiss();
                            }
                        }, 500);
                        CC.obtainBuilder("app.component.task.result")
                                .addParam("resultBean", body)
                                .build()
                                .callAsync();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(TaskHealthActivity.this)
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
