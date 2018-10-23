package com.gcml.old.auth.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.data.EatAdapter;
import com.gcml.common.data.EatModel;
import com.gcml.common.utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUp12DrinkActivity extends BaseActivity {


    RecyclerView rvContent;

    TextView tvTab1PersonalInfo;

    TextView tvTab2HealthInfo;

    TextView tvGoBack;

    TextView tvGoForward;
    private EatAdapter mAdapter;
    private List<EatModel> mModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.auth_activity_sign_up12_drink);
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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
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
        mModels = new ArrayList<>(3);
        mModels.add(new EatModel(getString(R.string.common_always_drink),
                R.drawable.common_ic_always_drink,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference));
        EatModel e = new EatModel(getString(R.string.common_sometimes_drink),
                R.drawable.common_ic_sometimes_drink,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference);
        mModels.add(e);
        mModels.add(new EatModel(getString(R.string.common_never_drink),
                R.drawable.common_ic_never_drink,
                R.color.commonColorSaltyPreference,
                R.drawable.common_bg_tv_salty_preference_selected,
                R.drawable.common_bg_tv_salty_preference));
        return mModels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_drink_tip);
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        int size = mModels.size();
        for (int i = 0; i < size; i++) {
            EatModel model = mModels.get(i);
            if (model.isSelected()) {
                LocalShared.getInstance(this.getApplicationContext()).setSignUpDrink("" + (i + 1));
                Intent intent = new Intent(this, SignUp13SportsActivity.class);
                startActivity(intent);
                return;
            }
        }
        speak(R.string.sign_up_drink_tip);
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
        map = new SparseArrayCompat<>(3);
        map.put(0, "经常");
        map.put(1, "偶尔");
        map.put(2, "从不");
    }
}