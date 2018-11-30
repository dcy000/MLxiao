package com.example.module_register.ui.normal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.SparseArrayCompat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.adapter.EatAdapter;
import com.example.module_register.adapter.EatModel;
import com.example.module_register.service.RegisterAPI;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public class SignUp13SportsActivity extends VoiceToolBarActivity {

    @BindView(R2.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R2.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R2.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;
    private EatAdapter mAdapter;
    public List<EatModel> mModels;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up13_sports;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvContent.setLayoutManager(layoutManager);
        mModels = eatModals();
        mAdapter = new EatAdapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvContent.setAdapter(mAdapter);

        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab2HealthInfo.setTextColor(getResources().getColor(R.color.textColorSelected));
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private int positionSelected = -1;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvContent.getChildAdapterPosition(v);
            if (position == positionSelected) {
                onTvGoForwardClicked();
                return;
            }
            if (positionSelected >= 0
                    && positionSelected < mModels.size()) {
                mModels.get(positionSelected).setSelected(false);
                mAdapter.notifyItemChanged(positionSelected);
            }
            positionSelected = position;
            mModels.get(position).setSelected(true);
            mAdapter.notifyItemChanged(position);
            onTvGoForwardClicked();
        }
    };

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
        EatModel e = new EatModel(getString(R.string.sometimes_sports),
                R.drawable.ic_sometimes_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference);
        mModels.add(e);
        mModels.add(new EatModel(getString(R.string.never_sports),
                R.drawable.ic_never_sports,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        return mModels;
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_sports_tip);
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        int size = mModels.size();
        for (int i = 0; i < size; i++) {
            EatModel model = mModels.get(i);
            if (model.isSelected()) {
                cacheUserInfo("" + (i + 1));
                signUp();
                return;
            }
        }
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_sports_tip);
    }

    private void cacheUserInfo(String sport) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.exerciseHabits = sport;
        Box.getSessionManager().setUser(user);
    }

    private void navToNext() {
        Intent intent = new Intent(this, SignUp14DiseaseHistoryActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        showLoadingDialog(getString(R.string.do_register));
        UserInfoBean user = Box.getSessionManager().getUser();


        Box.getRetrofit(RegisterAPI.class)
                .registerAccount(
                        "50",
                        user.bname,
                        user.sex,
                        DeviceUtils.getIMEI(),
                        user.tel,
                        user.password,
                        user.dz,
                        user.sfz,
                        user.height,
                        user.weight,
                        user.bloodType,
                        user.eatingHabits,
                        user.smoke,
                        user.drink,
                        user.exerciseHabits
                )
                .compose(RxUtils.<UserInfoBean>httpResponseTransformer())
                .doOnNext(new Consumer<UserInfoBean>() {
                    @Override
                    public void accept(UserInfoBean userInfoBean) throws Exception {
                        Box.getSessionManager().setUser(userInfoBean);
                    }
                })
                .as(RxUtils.<UserInfoBean>autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        MLVoiceSynthetize.startSynthesize("主人，您已注册成功。请点下一步完善相关内容，即可愉快使用！");
                        tvGoBack.setVisibility(View.INVISIBLE);
                        hideLoadingDialog();
                        Box.getSessionManager().setUser(userInfoBean);
                        navToNext();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        hideLoadingDialog();
                        MLVoiceSynthetize.startSynthesize("主人," + ex.message);
                    }
                });
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            String type = map.get(i);
            if (result.contains(type)) {
                onItemClickListener.onClick(rvContent.getChildAt(i));
                return;
            }
        }
    }

    public SparseArrayCompat<String> map;

    {
        map = new SparseArrayCompat<>(4);
        map.put(0, "天");
        map.put(1, "周");
        map.put(2, "偶尔");
        map.put(3, "从不");
    }
}
