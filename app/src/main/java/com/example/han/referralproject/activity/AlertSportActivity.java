package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.register.EatAdapter;
import com.medlink.danbogh.register.EatModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertSportActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_title)
    TextView tvSignUpTitle;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    private EatAdapter mAdapter;
    public List<EatModel> mModels;
    private int positionSelected = -1;
    private UserInfoBean data;
    private String eat = "", smoke = "", drink = "", exercise = "", mh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_sport);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改运动情况");
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (data == null) {
            data = new UserInfoBean();
        }
        eat = data.eatingHabits;
        smoke = data.smoke;
        drink = data.drink;
        exercise = data.exerciseHabits;
        mh = data.mh;
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(layoutManager);
        mModels = eatModals();
        mAdapter = new EatAdapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvSignUpContent.setAdapter(mAdapter);

    }

    private List<EatModel> eatModals() {
        mModels = new ArrayList<>(4);
        mModels.add(new EatModel(getString(R.string.everyday_sports),
                R.drawable.ic_everyday_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.few_times_per_week_sports),
                R.drawable.ic_few_times_per_week_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.sometimes_sports),
                R.drawable.ic_sometimes_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.never_sports),
                R.drawable.ic_never_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        return mModels;
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvSignUpContent.getChildAdapterPosition(v);
            if (positionSelected >= 0
                    && positionSelected < mModels.size()) {
                mModels.get(positionSelected).setSelected(false);
                mAdapter.notifyItemChanged(positionSelected);
            }
            positionSelected = position;
            mModels.get(position).setSelected(true);
            mAdapter.notifyItemChanged(position);
        }
    };

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        if (positionSelected == -1) {
            ToastUtils.showShort("请选择其中一个");
            return;
        }
        UserInfoBean user = Box.getSessionManager().getUser();
        Box.getRetrofit(API.class)
                .alertUserInfo(
                        user.bid,
                        data.height,
                        data.weight,
                        eat,
                        smoke,
                        drink,
                        positionSelected + 1 + "",
                        mh,
                        data.dz)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("修改成功");
                        switch (positionSelected + 1) {
                            case 1:
                                MLVoiceSynthetize.startSynthesize("主人，您的运动情况已经修改为" + "每天一次");
                                break;
                            case 2:
                                MLVoiceSynthetize.startSynthesize("主人，您的运动情况已经修改为" + "每周几次");
                                break;
                            case 3:
                                MLVoiceSynthetize.startSynthesize("主人，您的运动情况已经修改为" + "偶尔运动");
                                break;
                            case 4:
                                MLVoiceSynthetize.startSynthesize("主人，您的运动情况已经修改为" + "从不运动");
                                break;
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }

    @Override
    protected void onActivitySpeakFinish() {
        finish();
    }
}
