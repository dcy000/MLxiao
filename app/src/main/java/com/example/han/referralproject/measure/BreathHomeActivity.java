package com.example.han.referralproject.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.BreathHomeResultBean;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.han.referralproject.util.WeakHandler;
import com.example.han.referralproject.view.InputDialog;
import com.google.gson.Gson;
import com.gzq.lib_bluetooth.BaseBluetooth;

public class BreathHomeActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 哮喘严重程度(PEF)
     */
    private TextView mTitle1;
    /**
     * ≥80%
     */
    private TextView mTitle11;
    /**
     * &lt;80%
     */
    private TextView mTitle12;
    /**
     * &lt;60%
     */
    private TextView mTitle13;
    private ConstraintLayout mCl1;
    /**
     * 阻塞指数(FEV1)
     */
    private TextView mTitle2;
    /**
     * ≥80%
     */
    private TextView mTitle21;
    /**
     * &lt;80%
     */
    private TextView mTitle22;
    /**
     * &lt;30%
     */
    private TextView mTitle23;
    private ConstraintLayout mCl2;
    /**
     * COPD分级(FEV1/FVC)
     */
    private TextView mTitle3;
    /**
     * &gt;70%
     */
    private TextView mTitle31;
    /**
     * &lt;70%
     */
    private TextView mTitle32;
    private ConstraintLayout mCl3;
    /**
     * 健康数据
     */
    private TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    private TextView mBtnVideoDemo;
    /**
     * 0
     */
    private TextView mTvGaoya;
    /**
     * 0%
     */
    private TextView mTvGaoyaPercent;
    /**
     * 流量值
     */
    private TextView mTvTitle1;
    private ConstraintLayout mCl4;
    /**
     * 0.00
     */
    private TextView mTvDiya;
    /**
     * 0%
     */
    private TextView mTvDiyaPercent;
    /**
     * 呼气量
     */
    private TextView mTvTitle2;
    private ConstraintLayout mCl5;
    /**
     * 0.00
     */
    private TextView mTvMaibo;
    /**
     * 0%
     */
    private TextView mTvMaiboPercent;
    /**
     * 肺活量
     */
    private TextView mTvTitle3;
    private ConstraintLayout mCl6;
    private ImageView mIvBack;
    private ImageView mIconHome;
    private BaseBluetooth baseBluetooth;
    private int sex = 0;
    private int age = 25;
    private int height = 170;
    private int weight = 65;
    private String bluetoothName;
    private String bluetoothAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathhome);
        initView();
        initData();
        showDialog(this);
    }

    private void initData() {
        sex = LocalShared.getInstance(this).getSex().equals("男") ? 0 : 1;
        String userAge = LocalShared.getInstance(this).getUserAge();
        age = userAge.equals("0") ? 25 : Integer.parseInt(userAge);
        height = (int) Float.parseFloat(LocalShared.getInstance(this).getUserHeight());
        weight = (int) LocalShared.getInstance(this).getSignUpWeight();

    }

    private void showDialog(final FragmentActivity activity) {
        String breathHomeMacAndName = LocalShared.getInstance(this).getBreathHomeMac();
        if (!TextUtils.isEmpty(breathHomeMacAndName)) {
            String[] split = breathHomeMacAndName.split(",");
            if (split.length == 2) {
                baseBluetooth = new BreathHomePresenter(activity, bluetoothName = split[0],
                        bluetoothAddress = split[1], sex, age, height, weight);
            }
        } else {
            new InputDialog(this).builder()
                    .setMsg("请输入仪器后背唯一识别码\n" + "字母请大写")
                    .setMsgColor(R.color.config_color_base_3)
                    .setEditHint("请输入设备唯一识别码")
                    .setPositiveButton("连接", new InputDialog.OnInputChangeListener() {
                        @Override
                        public void onInput(final String s) {
                            new WeakHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    baseBluetooth = new BreathHomePresenter(activity,
                                            bluetoothName = s.trim(),
                                            null,
                                            sex,
                                            age,
                                            height,
                                            weight);
                                }
                            }, 1000);
                        }
                    }).show();
        }


    }

    private void initView() {
        mTitle1 = (TextView) findViewById(R.id.title1);
        mTitle11 = (TextView) findViewById(R.id.title1_1);
        mTitle12 = (TextView) findViewById(R.id.title1_2);
        mTitle13 = (TextView) findViewById(R.id.title1_3);
        mCl1 = (ConstraintLayout) findViewById(R.id.cl1);
        mTitle2 = (TextView) findViewById(R.id.title2);
        mTitle21 = (TextView) findViewById(R.id.title2_1);
        mTitle22 = (TextView) findViewById(R.id.title2_2);
        mTitle23 = (TextView) findViewById(R.id.title2_3);
        mCl2 = (ConstraintLayout) findViewById(R.id.cl2);
        mTitle3 = (TextView) findViewById(R.id.title3);
        mTitle31 = (TextView) findViewById(R.id.title3_1);
        mTitle32 = (TextView) findViewById(R.id.title3_2);
        mCl3 = (ConstraintLayout) findViewById(R.id.cl3);
        mBtnHealthHistory = (TextView) findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = (TextView) findViewById(R.id.tv_gaoya);
        mTvGaoyaPercent = (TextView) findViewById(R.id.tv_gaoya_percent);
        mTvTitle1 = (TextView) findViewById(R.id.tv_title1);
        mCl4 = (ConstraintLayout) findViewById(R.id.cl4);
        mTvDiya = (TextView) findViewById(R.id.tv_diya);
        mTvDiyaPercent = (TextView) findViewById(R.id.tv_diya_percent);
        mTvTitle2 = (TextView) findViewById(R.id.tv_title2);
        mCl5 = (ConstraintLayout) findViewById(R.id.cl5);
        mTvMaibo = (TextView) findViewById(R.id.tv_maibo);
        mTvMaiboPercent = (TextView) findViewById(R.id.tv_maibo_percent);
        mTvTitle3 = (TextView) findViewById(R.id.tv_title3);
        mCl6 = (ConstraintLayout) findViewById(R.id.cl6);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_health_history:
                ToastTool.showShort("暂无数据");
                break;
            case R.id.btn_video_demo:
                ToastTool.showShort("暂无视频");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.icon_home:
                if (baseBluetooth != null) {
                    baseBluetooth.onDestroy();
                    baseBluetooth = new BreathHomePresenter(this, bluetoothName, bluetoothAddress, sex, age, height, weight);
                }
                break;
        }
    }

    public void updateData(String... datas) {
        BreathHomeResultBean breathHomeResultBean = new Gson().fromJson(datas[0], BreathHomeResultBean.class);
        mTvGaoya.setText(breathHomeResultBean.getPef());
        mTvDiya.setText(breathHomeResultBean.getFev1());
        mTvMaibo.setText(breathHomeResultBean.getFvc());
        mTvGaoyaPercent.setText(breathHomeResultBean.getPercentPEF());
        mTvDiyaPercent.setText(breathHomeResultBean.getPercentFEV1());
        mTvMaiboPercent.setText(breathHomeResultBean.getPercentFEV1_FVC());
    }


}
