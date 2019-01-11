package com.gcml.old.auth.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.common.data.EatAdapter;
import com.gcml.common.data.EatModel;
import com.gcml.common.data.UserSpHelper;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUp13SportsActivity extends BaseActivity {


    RecyclerView rvContent;

    TextView tvTab1PersonalInfo;

    TextView tvTab2HealthInfo;

    TextView tvGoBack;

    TextView tvGoForward;
    private EatAdapter mAdapter;
    public List<EatModel> mModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.auth_activity_sign_up13_sports);
        rvContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvTab1PersonalInfo = (TextView) findViewById(R.id.tv_tab1_personal_info);
        tvTab2HealthInfo = (TextView) findViewById(R.id.tv_tab2_health_info);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mToolbar.setVisibility(View.GONE);
        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        initView();
    }

    private void initView() {
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
        mModels.add(new EatModel(getString(R.string.common_everyday_sports),
                R.drawable.common_ic_everyday_sports,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.common_few_times_per_week_sports),
                R.drawable.common_ic_few_times_per_week_sports,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference));
        EatModel e = new EatModel(getString(R.string.common_sometimes_sports),
                R.drawable.common_ic_sometimes_sports,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference);
        mModels.add(e);
        mModels.add(new EatModel(getString(R.string.common_never_sports),
                R.drawable.common_ic_never_sports,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference));
        return mModels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_sports_tip);
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        int size = mModels.size();
        for (int i = 0; i < size; i++) {
            EatModel model = mModels.get(i);
            if (model.isSelected()) {
                LocalShared.getInstance(this.getApplicationContext()).setSignUpSport("" + (i + 1));
                signUp();
                return;
            }
        }
        speak(R.string.sign_up_sports_tip);
    }

    private void navToNext() {
        Intent intent = new Intent(this, SignUp14DiseaseHistoryActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        showLoadingDialog(getString(R.string.do_register));
        final LocalShared shared = LocalShared.getInstance(this);
        String name = shared.getSignUpName();
        String gender = shared.getSignUpGender();
        String address = shared.getSignUpAddress();
        String idCard = shared.getSignUpIdCard();
        String phone = shared.getSignUpPhone();
        String password = shared.getSignUpPassword();
        float height = shared.getSignUpHeight();
        float weight = shared.getSignUpWeight();
        String bloodType = shared.getSignUpBloodType();
        String eat = shared.getSignUpEat();
        String smoke = shared.getSignUpSmoke();
        String drink = shared.getSignUpDrink();
        String sport = shared.getSignUpSport();
        NetworkApi.registerUser(
                name,
                gender,
                address,
                idCard,
                phone,
                password,
                height,
                weight,
                bloodType,
                eat,
                smoke,
                drink,
                sport,
                new NetworkManager.SuccessCallback<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean response) {
                        speak("您好，您已注册成功。请点下一步完善相关内容，即可愉快使用！");
                        tvGoBack.setVisibility(View.INVISIBLE);
                        hideLoadingDialog();
                        shared.setUserInfo(response);
                        LocalShared.getInstance(mContext).setSex(response.sex);
                        LocalShared.getInstance(mContext).setUserPhoto(response.userPhoto);
                        LocalShared.getInstance(mContext).setUserAge(response.age);
                        LocalShared.getInstance(mContext).setUserHeight(response.height);
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", UserSpHelper.getUserId())
                                .build()
                                .callAsync();
                        navToNext();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        ToastUtils.showShort(message);
                        speak("您好，" + message);
                    }
                }
        );
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
