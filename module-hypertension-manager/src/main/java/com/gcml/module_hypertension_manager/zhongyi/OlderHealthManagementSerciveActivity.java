package com.gcml.module_hypertension_manager.zhongyi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_hypertension_manager.R;
import com.gcml.module_hypertension_manager.net.HyperRepository;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementAnwserBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementResultBean;
import com.gcml.module_hypertension_manager.zhongyi.bean.OlderHealthManagementBean;
import com.sjtu.yifei.annotation.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
@Route(path = "hypertension/manager/older/health/management/sercive")
public class OlderHealthManagementSerciveActivity extends ToolbarBaseActivity {

    LinearLayout llOperator;
    private List<OlderHealthManagementBean.DataBean.QuestionListBean> questionList = new ArrayList<>();
    MonitorViewPager vp;
    TextView tvPreviousItem;
    TextView tvCurrentItem;
    TextView tvNextItem;
    private int count;
    private String hmQuestionnaireId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_older_health_management_sercive);
        llOperator = findViewById(R.id.ll_operator);
        vp = findViewById(R.id.vp);
        tvPreviousItem = findViewById(R.id.tv_previous_item);
        tvPreviousItem.setOnClickListener(this);
        tvCurrentItem = findViewById(R.id.tv_current_item);
        tvCurrentItem.setOnClickListener(this);
        tvNextItem = findViewById(R.id.tv_next_item);
        tvNextItem.setOnClickListener(this);
        initTitle();
        initData();
    }

    private void initData() {
        showLoading("正在加载中...");
        new HyperRepository().getHealthManagementForOlder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<OlderHealthManagementBean.DataBean>() {
                    @Override
                    public void onNext(OlderHealthManagementBean.DataBean dataBean) {
                        OlderHealthManagementSerciveActivity.this.hmQuestionnaireId = dataBean.hmQuestionnaireId;
                        if (dataBean != null) {
                            questionList = dataBean.questionList;
                            if (questionList != null && questionList.size() != 0) {
                                count = questionList.size();
                                llOperator.setVisibility(View.VISIBLE);
                                initView();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    private ArrayList<Fragment> fragments;
    private int index;

    private void initView() {
        tvCurrentItem.setText(1 + "/" + count);

        fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fragments.add(HealthItemFragment.getInstance((i + 1) + "", questionList.get(i)));
        }
        vp.setOffscreenPageLimit(count - 1);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                OlderHealthManagementSerciveActivity.this.index = position;
                tvCurrentItem.setText((index + 1) + "/" + count);

                if (isLastPager()) {
                    tvNextItem.setText("提交");
                } else {
                    tvNextItem.setText("下一题");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_previous_item) {
            preCurrentPage();
        } else if (v.getId() == R.id.tv_next_item) {
            if (questionList == null) {
                return;
            }
            if (!questionList.get(index).isSelected) {
                ToastUtils.showShort("请选择答案");
                return;
            }

            if (isLastPager()) {
                submit();
                return;
            }
            nextCurrentPage();
        }
    }

    private void submit() {
        showLoading("正在提交...");
        HealthManagementAnwserBean anwserBean = new HealthManagementAnwserBean();
        anwserBean.equipmentId = DeviceUtils.getIMEI();
        anwserBean.userId = UserSpHelper.getUserId();
        anwserBean.hmQuestionnaireId = this.hmQuestionnaireId;
        anwserBean.answerList = new ArrayList<>();

        if (questionList != null && questionList.size() != 0) {

            for (int i = 0; i < questionList.size(); i++) {
                HealthManagementAnwserBean.AnswerListBean anwser = new HealthManagementAnwserBean.AnswerListBean();
                anwser.score = questionList.get(i).answerScore;
                anwser.hmAnswerId = questionList.get(i).hmAnswerId;
                anwser.hmQuestionId = questionList.get(i).hmQuestionId;
                anwser.questionSeq = questionList.get(i).questionSeq;

                anwserBean.answerList.add(anwser);

            }
        }
        new HyperRepository()
                .postHealthManagementAnwser(anwserBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<HealthManagementResultBean.DataBean>>() {
                    @Override
                    public void onNext(List<HealthManagementResultBean.DataBean> dataBeans) {
                        ToastUtils.showShort("提交成功");
                        gotoResultPage(dataBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    /**
     * 跳转到结果页面
     *
     * @param data
     */
    private void gotoResultPage(List<HealthManagementResultBean.DataBean> data) {
        startActivity(new Intent(this, HealthManagementResultActivity.class).putExtra("result_data", (Serializable) data));
        finish();
    }

    private boolean isLastPager() {
        return index == count - 1;
    }

    public void nextCurrentPage() {
        vp.setCurrentItem(index + 1, true);
    }

    public void preCurrentPage() {
        vp.setCurrentItem(index - 1, true);
    }


}
