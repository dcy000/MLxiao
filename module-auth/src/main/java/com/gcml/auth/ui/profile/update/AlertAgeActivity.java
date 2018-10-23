package com.gcml.auth.ui.profile.update;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;

import com.gcml.auth.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.picker.SelectAdapter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;

import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;


import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertAgeActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTbAgeTitle;
    /**
     * 您的年龄
     */
    private TextView mTvSignUpHeight;
    private RecyclerView mRvSignUpContent;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_alert_age);
        initView();
        initData();
        initRV();
    }

    private int currentPositon;

    private void initRV() {
        GalleryLayoutManager manager = new GalleryLayoutManager(1);
        manager.attach(mRvSignUpContent, 20);
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View view, int positon) {
                currentPositon = positon;
                ToastUtils.showShort(strings.get(positon));
            }
        });

        SelectAdapter adapter = new SelectAdapter();
        adapter.setStrings(strings);
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mRvSignUpContent.smoothScrollToPosition(position);
            }
        });

        mRvSignUpContent.setAdapter(adapter);
    }

    List<String> strings = new ArrayList<>();

    private void initData() {
        for (int i = 10; i < 150; i++) {
            strings.add(String.valueOf(i));
        }
    }

    private void initView() {
        mTbAgeTitle = (TranslucentToolBar) findViewById(R.id.tb_age_title);
        mTvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        mRvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        mTvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTbAgeTitle.setData("修改年龄", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home")
                                .build().callAsync();
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_sign_up_go_back) {
            finish();
        } else if (i == R.id.tv_sign_up_go_forward) {
            nextStep();
        }
    }

    private void nextStep() {
        String seletedAge = strings.get(currentPositon);

        UserEntity user = new UserEntity();
        user.age = seletedAge;
        Observable<UserEntity> data = CC.obtainBuilder("com.gcml.auth.putUser")
                .addParam("user", user)
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        speak("修改成功");
                        ToastUtils.showShort("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        speak("修改失败");
                        ToastUtils.showShort("修改失败");
                    }
                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text, false);
    }

}
