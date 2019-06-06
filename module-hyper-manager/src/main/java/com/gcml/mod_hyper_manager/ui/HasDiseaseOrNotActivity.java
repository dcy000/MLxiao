package com.gcml.mod_hyper_manager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.mod_hyper_manager.R;
import com.gcml.mod_hyper_manager.net.HyperRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class HasDiseaseOrNotActivity extends ToolbarBaseActivity implements MultipleChoiceStringFragment.OnButtonClickListener {

    public static final String CONTENT = " 您是否已经存在以下疾病：肾脏疾病、内分泌疾病、心血管疾病、颅脑病变？";
    String[] itmes = {"是", "否"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_disease_or_not);
        initTitle();
        initView();
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您是否存在以下疾病：肾脏疾病，内分泌疾病，心血管疾病，颅脑病变？");
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MultipleChoiceStringFragment fragment = MultipleChoiceStringFragment
                .getInstance(CONTENT, "",
                        Arrays.asList("是", "否")
                        , true);
        fragment.setListener(this);
        transaction.replace(R.id.fl_container, fragment).commitAllowingStateLoss();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 调 查");
        mRightText.setVisibility(View.GONE);
//        mRightView.setImageResource(R.drawable.white_wifi_3);
//        mRightView.setOnClickListener(v -> startActivity(new Intent(HasDiseaseOrNotActivity.this, WifiConnectActivity.class)));
    }

    @Override
    public void onNextStep(int[] checked) {
        if ("是".equals(itmes[checked[0]])) {
            postTargetState("1");
        } else {
            //跳转问卷
            postTargetState("0");
        }
    }

    private void toSolution() {
        Routerfit.register(AppRouter.class).skipTreatmentPlanActivity();
    }

    /**
     * 更新靶细胞
     *
     * @param state
     */
    private void postTargetState(String state) {
        new HyperRepository()
                .postTargetHypertension(UserSpHelper.getUserId(), state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if ("0".equals(state)) {
                            startActivity(new Intent(HasDiseaseOrNotActivity.this, HypertensionActivity.class));
                        } else if ("1".equals(state)) {
                            toSolution();
                        }
                        toSolution();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
