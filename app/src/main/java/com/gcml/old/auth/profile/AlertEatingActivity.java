package com.gcml.old.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.homepage.MainActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.profile.otherinfo.bean.PUTUserBean;
import com.gcml.old.auth.register.EatAdapter;
import com.gcml.old.auth.register.EatModel;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertEatingActivity extends AppCompatActivity {


    TextView tvSignUpTitle;

    RecyclerView rvSignUpContent;

    TextView tvSignUpGoBack;

    TextView tvSignUpGoForward;
    private UserInfoBean data;
    private GridLayoutManager mLayoutManager;
    public EatAdapter mAdapter;
    public List<EatModel> mModels;
    private String eat = "", smoke = "", drink = "", exercise = "";
    private StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_eating_new);
        tvSignUpTitle = (TextView) findViewById(R.id.tv_sign_up_title);
        rvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);

//        mToolbar.setVisibility(View.VISIBLE);
//        mTitleText.setText("修改饮食情况");
        TranslucentToolBar toolBar = findViewById(R.id.tb_eat_title);
        toolBar.setData("修改饮食情况", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        startActivity(new Intent(AlertEatingActivity.this, MainActivity.class));
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
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        buffer = new StringBuffer();
        initView();
    }

    private void initView() {
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
        if (!TextUtils.isEmpty(data.smoke)) {
            switch (data.smoke) {
                case "经常吸烟":
                    smoke = "1";
                    break;
                case "偶尔吸烟":
                    smoke = "2";
                    break;
                case "从不吸烟":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.drink)) {
            switch (data.drink) {
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
        if (!TextUtils.isEmpty(data.exerciseHabits)) {
            switch (data.exerciseHabits) {
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
        if ("尚未填写".equals(data.mh)) {
            buffer = null;
        } else {
            String[] mhs = data.mh.split("\\s+");
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
            }
        }
        mLayoutManager = new GridLayoutManager(this, 3);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSignUpContent.setLayoutManager(mLayoutManager);
        mModels = eatModals();
        mAdapter = new EatAdapter(mModels);
        mAdapter.setOnItemClickListener(onItemClickListener);
        rvSignUpContent.setAdapter(mAdapter);
    }

    private int positionSelected = -1;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvSignUpContent.getChildAdapterPosition(v);
            if (positionSelected >= 0
                    && positionSelected < mModels.size()) {
                mModels.get(positionSelected).setSelected(false);
                mAdapter.notifyItemChanged(positionSelected);
            }
            positionSelected = position;
            mModels.get(position).setSelected(true);
            mAdapter.notifyItemChanged(position);
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

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        if (positionSelected == -1) {
            ToastUtils.showShort("请选择其中一个");
            return;
        }
//        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, positionSelected + 1 + "", smoke, drink, exercise,
//                TextUtils.isEmpty(buffer) ? "" : buffer.substring(0, buffer.length() - 1), data.dz, new NetworkManager.SuccessCallback<Object>() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        ToastUtils.showShort("修改成功");
//                        switch (positionSelected + 1) {
//                            case 1:
//                                speak("主人，您的饮食情况已经修改为" + "荤素搭配");
//                                break;
//                            case 2:
//                                speak("主人，您的饮食情况已经修改为" + "偏好吃荤");
//                                break;
//                            case 3:
//                                speak("主人，您的饮食情况已经修改为" + "偏好吃素");
//                                break;
//                            case 4:
//                                speak("主人，您的饮食情况已经修改为" + "偏好吃咸");
//                                break;
//                            case 5:
//                                speak("主人，您的饮食情况已经修改为" + "偏好油腻");
//                                break;
//                            case 6:
//                                speak("主人，您的饮食情况已经修改为" + "偏好甜食");
//                                break;
//                        }
//                        finish();
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//                        finish();
//                    }
//                });


        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(UserSpHelper.getUserId());
        bean.eatingHabits = positionSelected + 1 + "";

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(() -> speak("修改成功"));
                        finish();
                    } else {
                        runOnUiThread(() -> speak("修改失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        UserEntity user = new UserEntity();
//        user.eatingHabits = positionSelected + 1 + "";
//        CCResult result = CC.obtainBuilder("com.gcml.auth.putUser")
//                .addParam("user", user)
//                .build()
//                .call();
//        Observable<Object> data = result.getDataItem("data");
//        data.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new DefaultObserver<Object>() {
//                    @Override
//                    public void onNext(Object o) {
//                        super.onNext(o);
//                        runOnUiThread(() -> speak("修改成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        runOnUiThread(() -> speak("修改失败"));
//                    }
//                });
    }

//    @Override
//    protected void onActivitySpeakFinish() {
//        finish();
//    }


    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text);
    }

}
