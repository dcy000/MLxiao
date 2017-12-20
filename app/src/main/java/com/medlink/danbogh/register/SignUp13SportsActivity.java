package com.medlink.danbogh.register;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp13SportsActivity extends BaseActivity {

    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;
    private EatAdapter mAdapter;
    public List<EatModel> mModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up13_sports);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
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
        setDisableGlobalListen(true);
        speak(R.string.sign_up_sports_tip);
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
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
                        hideLoadingDialog();
                        shared.setUserInfo(response);
                        navToNext();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        T.show(message);
                        speak("主人," + message);
                    }
                }
        );
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

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
