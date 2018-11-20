package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.util.LocalShared;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.register.SelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertHeightActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_height)
    TextView tvSignUpHeight;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_unit)
    TextView tvSignUpUnit;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    protected int selectedPosition = 1;
    protected SelectAdapter adapter;
    protected ArrayList<String> mStrings;
    protected UserInfoBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_height);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改身高");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            initView();
        }
    }

    protected void initView() {
        if (data == null) {
            return;
        }
        tvSignUpUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvSignUpContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select(mStrings == null ? String.valueOf(position) : mStrings.get(position));
            }
        });
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvSignUpContent.smoothScrollToPosition(position);
            }
        });
        rvSignUpContent.setAdapter(adapter);
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    private void select(String text) {
        ToastUtils.showShort(text);
    }

    protected int geTip() {
        return R.string.sign_up_height_tip;
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        final String height = mStrings.get(selectedPosition);

        UserInfoBean user = Box.getSessionManager().getUser();
        Box.getRetrofit(API.class)
                .alertUserInfo(
                        user.bid,
                        height,
                        data.weight,
                        data.eatingHabits,
                        data.smoke,
                        data.drink,
                        data.exerciseHabits,
                        data.mh,
                        data.dz
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        LocalShared.getInstance(AlertHeightActivity.this).setUserHeight(height);
                        ToastUtils.showShort("修改成功");
                        MLVoiceSynthetize.startSynthesize("主人，您的身高已经修改为" + height + "厘米");
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
