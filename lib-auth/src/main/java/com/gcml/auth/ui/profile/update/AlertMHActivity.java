package com.gcml.auth.ui.profile.update;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.auth.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.common.data.DiseaseHistoryAdapter;
import com.gcml.common.data.DiseaseHistoryModel;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertMHActivity extends AppCompatActivity {

    RecyclerView rvSignUpContent;

    TextView tvSignUpGoBack;

    TextView tvSignUpGoForward;
    private DiseaseHistoryAdapter mAdapter;
    public List<DiseaseHistoryModel> mModels;
    public GridLayoutManager mLayoutManager;
    protected UserEntity data;
    protected String eat = "", smoke = "", drink = "", exercise = "";
    private StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_alert_mh);
        rvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        TranslucentToolBar toolBar = findViewById(R.id.tb_mh_title);
        toolBar.setData("修改病史", R.drawable.common_icon_back, "返回",
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


        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        tvSignUpGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvSignUpGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        data = getIntent().getParcelableExtra("data");
        buffer = new StringBuffer();
        if (data != null) {
            initView();
        }
    }


    protected void initView() {
        if (!TextUtils.isEmpty(data.eatingHabits)) {
            switch (data.eatingHabits) {
                case "荤素搭配":
                    eat = "1";
                    break;
                case "偏好吃荤":
                    eat = "2";
                    break;
                case "偏好吃素":
                    eat = "3";
                    break;
                case "偏好吃咸":
                    break;
                case "偏好油腻":
                    break;
                case "偏好甜食":
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.smokeHabits)) {
            switch (data.smokeHabits) {
                case "经常抽烟":
                    smoke = "1";
                    break;
                case "偶尔抽烟":
                    smoke = "2";
                    break;
                case "从不抽烟":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.drinkHabits)) {
            switch (data.drinkHabits) {
                case "经常喝酒":
                    smoke = "1";
                    break;
                case "偶尔喝酒":
                    smoke = "2";
                    break;
                case "从不喝酒":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.sportsHabits)) {
            switch (data.sportsHabits) {
                case "每天一次":
                    exercise = "1";
                    break;
                case "每周几次":
                    exercise = "2";
                    break;
                case "偶尔运动":
                    exercise = "3";
                    break;
                case "从不运动":
                    exercise = "4";
                    break;
            }
        }
        if ("暂未填写".equals(data.deseaseHistory)) {
            buffer = null;
        } else {
            String[] mhs = data.deseaseHistory.split(",");
            for (int i = 0; i < mhs.length; i++) {
                if (mhs[i].equals("高血压"))
                    buffer.append(1 + ",");
                else if (mhs[i].equals("糖尿病"))
                    buffer.append(2 + ",");
                else if (mhs[i].equals("冠心病"))
                    buffer.append(3 + ",");
                else if (mhs[i].equals("慢阻肺"))
                    buffer.append(4 + ",");
                else if (mhs[i].equals("孕产妇"))
                    buffer.append(5 + ",");
                else if (mhs[i].equals("痛风"))
                    buffer.append(6 + ",");
                else if (mhs[i].equals("甲亢"))
                    buffer.append(7 + ",");
                else if (mhs[i].equals("高血脂"))
                    buffer.append(8 + ",");
                else if (mhs[i].equals("其他"))
                    buffer.append(9 + ",");
                else if (mhs[i].equals("无"))
                    buffer.append(11 + ",");
            }
        }
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(mLayoutManager);
        mModels = modals();
        mAdapter = new DiseaseHistoryAdapter(mModels);
        rvSignUpContent.setAdapter(mAdapter);
    }


    private List<DiseaseHistoryModel> modals() {
        mModels = new ArrayList<>(10);
        String[] diseaseTypes = getResources().getStringArray(R.array.common_disease_type);
        for (String diseaseType : diseaseTypes) {
            DiseaseHistoryModel model = new DiseaseHistoryModel(
                    diseaseType,
                    false,
                    R.color.commonTextColorDiseaseSelected,
                    R.color.commonTextColorDiseaseUnselected,
                    R.drawable.common_bg_tv_disease_selected,
                    R.drawable.common_bg_tv_disease_unselected
            );
            mModels.add(model);
        }
        return mModels;
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String mh = getMh();
        if (TextUtils.isEmpty(mh)) {
            onTvGoBackClicked();
            return;
        }
        
        UserEntity user = new UserEntity();
        user.deseaseHistory = mh;
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


    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text);
    }

}
