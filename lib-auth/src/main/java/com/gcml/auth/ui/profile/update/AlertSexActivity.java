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
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.common.widget.picker.SelectAdapter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertSexActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTbSex;
    /**
     * 您的性别
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
    private int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_alert_sex);
        initView();
        initData();
        initRV();
    }

    private List<String> strings = new ArrayList<>();

    private void initData() {
        strings = Arrays.asList("男", "女");
    }

    private void initView() {
        mTbSex = (TranslucentToolBar) findViewById(R.id.tb_sex_title);
        mTvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        mRvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        mTvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTbSex.setData("修改性别", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home")
                                .build()
                                .callAsync();
                        finish();
                    }
                });


    }

    private void initRV() {
        GalleryLayoutManager manager = new GalleryLayoutManager(1);
        manager.attach(mRvSignUpContent, 1);
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener((recyclerView, view, positon) -> {
            currentPositon = positon;
            ToastUtils.showShort(strings.get(positon));
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
        String seletedSex = strings.get(currentPositon);

        UserEntity user = new UserEntity();
        user.sex = seletedSex;
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
                        ToastUtils.showShort("修改失败");
                        speak("修改失败");
                    }
                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }
}
