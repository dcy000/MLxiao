package com.example.module_person.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.module_person.R;
import com.example.module_person.R2;
import com.example.module_person.service.PersonAPI;
import com.gcml.lib_location.adapter.SelectAdapter;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AlertHeightActivity extends ToolbarBaseActivity {

    @BindView(R2.id.tv_sign_up_height)
    TextView tvSignUpHeight;
    @BindView(R2.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R2.id.tv_sign_up_unit)
    TextView tvSignUpUnit;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    protected int selectedPosition = 1;
    protected SelectAdapter adapter;
    protected ArrayList<String> mStrings;
    protected UserInfoBean data;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_alert_height;
    }

    @Override
    public void initParams(Intent intentArgument) {
        data = (UserInfoBean)intentArgument.getParcelableExtra("data");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("修 改 身 高");
        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
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

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        final String height = mStrings.get(selectedPosition);

        final UserInfoBean user = Box.getSessionManager().getUser();
        Box.getRetrofit(PersonAPI.class)
                .alertUserInfo(
                        user.bid,
                        user.height = height,
                        data.weight,
                        data.eatingHabits,
                        data.smoke,
                        data.drink,
                        data.exerciseHabits,
                        data.mh,
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
                        MLVoiceSynthetize.startSynthesize("主人，您的身高已经修改为" + height + "厘米",voiceListener);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }
    private MLSynthesizerListener voiceListener=new MLSynthesizerListener(){
        @Override
        public void onCompleted(SpeechError speechError) {
            super.onCompleted(speechError);
            finish();
        }
    };
}
