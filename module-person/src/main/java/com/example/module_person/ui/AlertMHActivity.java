package com.example.module_person.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.module_person.R;
import com.example.module_person.R2;
import com.example.module_person.service.PersonAPI;
import com.gcml.lib_location.adapter.DiseaseHistoryAdapter;
import com.gcml.lib_location.bean.DiseaseHistoryModel;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlertMHActivity extends ToolbarBaseActivity {

    @BindView(R2.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private DiseaseHistoryAdapter mAdapter;
    public List<DiseaseHistoryModel> mModels;
    public GridLayoutManager mLayoutManager;
    protected UserInfoBean data;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_alert_mh;
    }

    @Override
    public void initParams(Intent intentArgument) {
        data = (UserInfoBean) intentArgument.getParcelableExtra("data");
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("修 改 病 史");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        if (data == null) {
            return;
        }
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(mLayoutManager);
        mModels = modals();
        mAdapter = new DiseaseHistoryAdapter(mModels);
        rvSignUpContent.setAdapter(mAdapter);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }


    private List<DiseaseHistoryModel> modals() {
        mModels = new ArrayList<>(9);
        String[] diseaseTypes = getResources().getStringArray(R.array.disease_type);
        for (String diseaseType : diseaseTypes) {
            DiseaseHistoryModel model = new DiseaseHistoryModel(
                    diseaseType,
                    false,
                    R.color.textColorDiseaseSelected,
                    R.color.textColorDiseaseUnselected,
                    R.drawable.bg_tv_disease_selected,
                    R.drawable.bg_tv_disease
            );
            mModels.add(model);
        }
        return mModels;
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String mh = getMh();
        if (TextUtils.isEmpty(mh)) {
            onTvGoBackClicked();
            return;
        }
        final UserInfoBean user = Box.getSessionManager().getUser();
        Box.getRetrofit(PersonAPI.class)
                .alertUserInfo(
                        user.bid,
                        data.height,
                        data.weight,
                        data.eatingHabits,
                        data.smoke,
                        data.drink,
                        data.exerciseHabits,
                        user.mh = mh,
                        data.dz
                )
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Box.getSessionManager().setUser(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("修改成功");
                        MLVoiceSynthetize.startSynthesize("主人，您的病史已经修改成功");
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }


    private String getMh() {
        StringBuilder mhBuilder = new StringBuilder();
        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            if (mModels.get(i).isSelected()) {
                mhBuilder.append(i + 1);
                mhBuilder.append(",");
            }
        }
        int length = mhBuilder.length();
        return length == 0 ? mhBuilder.toString() : mhBuilder.substring(0, length - 1);
    }

}
