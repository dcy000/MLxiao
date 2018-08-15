package com.gcml.auth.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUp14DiseaseHistoryActivity extends BaseActivity {


    RecyclerView rvContent;

    TextView tvTab1PersonalInfo;

    TextView tvTab2HealthInfo;

    TextView tvGoBack;

    TextView tvGoForward;
    private DiseaseHistoryAdapter mAdapter;
    public List<DiseaseHistoryModel> mModels;
    public GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.auth_activity_sign_up14_disease_history);
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
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvContent.setLayoutManager(mLayoutManager);
        mModels = modals();
        mAdapter = new DiseaseHistoryAdapter(mModels);
        rvContent.setAdapter(mAdapter);
        tvTab1PersonalInfo.setTextColor(Color.parseColor("#3f86fc"));
        tvTab2HealthInfo.setTextColor(getResources().getColor(R.color.textColorSelected));
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

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_disease_history_tip);
    }


    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String mh = getMh();
        if (TextUtils.isEmpty(mh)) {
            mh = "11";
//            navToNext();
//            return;
        }

        showLoadingDialog(getString(R.string.do_uploading));
        NetworkApi.setUserMh(mh, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                hideLoadingDialog();
                navToNext();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
            }
        });
    }

    private void navToNext() {
        Intent intent = new Intent(mContext, RegisterVideoActivity.class);
        startActivity(intent);
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

        //语音选择病史标签
        int size = mModels == null ? 0 : mModels.size();
        for (int i = 0; i < size; i++) {
            DiseaseHistoryModel model = mModels.get(i);
            if (result.contains(model.getName())) {
                View view = rvContent.getChildAt(i);
                DiseaseHolder holder = (DiseaseHolder) rvContent.getChildViewHolder(view);
                holder.onTvDiseaseClicked();
                return;
            }
        }
    }
}
