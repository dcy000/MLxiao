package com.example.module_register.ui.normal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.gcml.lib_location.adapter.EatAdapter;
import com.gcml.lib_location.bean.EatModel;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp10EatActivity extends VoiceToolBarActivity {

    @BindView(R2.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    @BindView(R2.id.tv_sign_up_title)
    TextView mTvSignUpEatPreference;
    @BindView(R2.id.tv_tab1_personal_info)
    TextView tvTab1PersonalInfo;
    @BindView(R2.id.tv_tab2_health_info)
    TextView tvTab2HealthInfo;
    public Unbinder mUnbinder;
    public EatAdapter mAdapter;
    public List<EatModel> mModels;
    public GridLayoutManager mLayoutManager;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up10_eat;
    }

    @Override
    public void initParams(Intent intentArgument) {

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
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_eat_tip);
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvContent.setLayoutManager(mLayoutManager);
        mModels = eatModals();
        mAdapter = new EatAdapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvContent.setAdapter(mAdapter);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab2HealthInfo.setTextColor(getResources().getColor(R.color.textColorSelected));
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
        mModels = new ArrayList<>(6);
        mModels.add(new EatModel(getString(R.string.meat_collocation),
                R.drawable.ic_meat_collocation,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.meat_preference),
                R.drawable.ic_meat_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.vegetatian_preference),
                R.drawable.ic_vegetarian_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.salty_preference),
                R.drawable.ic_salty_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.greasy_preferece),
                R.drawable.ic_greasy_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        mModels.add(new EatModel(getString(R.string.sweet_preferemce),
                R.drawable.ic_sweet_preference,
                R.color.colorSaltyPreference,
                R.drawable.bg_tv_salty_preference_selected,
                R.drawable.bg_tv_salty_preference));
        return mModels;
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
                cacheUserInfo((i + 1) + "");
                Intent intent = new Intent(this, SignUp11SmokeActivity.class);
                startActivity(intent);
                return;
            }
        }
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_eat_tip);
    }

    private void cacheUserInfo(String eat) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.eatingHabits = eat;
        Box.getSessionManager().setUser(user);
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
        map = new SparseArrayCompat<>(6);
        map.put(0, "荤素");
        map.put(1, "荤");
        map.put(2, "素");
        map.put(3, "咸");
        map.put(4, "油");
        map.put(5, "甜");
    }
}
