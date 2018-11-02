package com.example.han.referralproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.activity.SelectXuetangTimeActivity;
import com.example.han.referralproject.activity.SingleDetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.require2.dialog.DialogTypeEnum;
import com.example.han.referralproject.require2.dialog.NoticePriceDialog;
import com.example.han.referralproject.require2.dialog.SomeCommonDialog;
import com.example.han.referralproject.shopping.Order;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.xindian.XinDianDetectActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Test_mainActivity extends BaseActivity implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    @BindView(R.id.ll_xueya)
    LinearLayout llXueya;
    @BindView(R.id.ll_xueyang)
    LinearLayout llXueyang;
    @BindView(R.id.ll_tiwen)
    LinearLayout llTiwen;
    @BindView(R.id.ll_xuetang)
    LinearLayout llXuetang;
    @BindView(R.id.cl_1)
    ConstraintLayout cl1;
    @BindView(R.id.ll_xindian)
    LinearLayout llXindian;
    @BindView(R.id.ll_tizhong)
    LinearLayout llTizhong;
    @BindView(R.id.ll_san)
    LinearLayout llSan;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.cl_2)
    ConstraintLayout cl2;
    private long lastClickTime = 0;

    private boolean isTest;

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        if (isTest) {
            backMainActivity();
        }
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setVisibility(View.VISIBLE);
        mTitleText.setText("收费项目");

        isTest = getIntent().getBooleanExtra("isTest", false);

        llXueya.setOnClickListener(this);
        llXueyang.setOnClickListener(this);
        llXuetang.setOnClickListener(this);
        llXindian.setOnClickListener(this);
        llTizhong.setOnClickListener(this);
        llTiwen.setOnClickListener(this);
        llSan.setOnClickListener(this);
        llMore.setOnClickListener(this);
        setEnableListeningLoop(false);

        speak(R.string.tips_test);

    }

    @Override
    public void onClick(final View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;

            final Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_xueya:
//                    showDialogAndOder("血压", "血压检测", "1", "");
                    intent.setClass(mContext, SingleDetectActivity.class);
                    intent.putExtra("type", "xueya");
                    startActivity(intent);
                    break;
                case R.id.ll_xueyang:
//                    showDialogAndOder("血氧", "血氧检测", "5", "");
                    intent.setClass(getApplicationContext(), DetectActivity.class);
                    intent.putExtra("type", "xueyang");
                    startActivity(intent);
                    break;
                case R.id.ll_tiwen:
//                    showDialogAndOder("生化综合2", "生化综合检测2", "69", "");
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "wendu");
                    startActivity(intent);
                    break;
                case R.id.ll_xuetang:
//                    showDialogAndOder("血糖", "血糖检测", "3", "");
                    intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
                    intent.putExtra("type", "xuetang");
                    startActivity(intent);
                    break;
                case R.id.ll_xindian:
//                    showDialogAndOder("心电", "心电检测", "10", "");
                    intent.setClass(mContext, XinDianDetectActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_san:
//                    showDialogAndOder("生化综合1", "生化综合检测1", "69", "");
                    intent.setClass(mContext, SelectXuetangTimeActivity.class);
                    intent.putExtra("type", "sanheyi");
                    startActivity(intent);
                    break;
                case R.id.ll_tizhong://体重
//                    showDialogAndOder("体重", "体重检测", "1", "");
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "tizhong");
                    startActivity(intent);
                    break;
                case R.id.ll_more://敬请期待
                    ToastTool.showShort("敬请期待");
                    break;
            }

        }

    }

    private void showDialogAndOder(final String itemName, final String article, final String price, final String photo) {
        showPriceNoticeDialog(price, itemName, new NoticePriceDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm() {
                NetworkApi.DetecteOrderInfo(
                        LocalShared.getInstance(Test_mainActivity.this).getUserId(),
                        Utils.getDeviceId(),
                        article,
                        "1",
                        price,
                        photo,
                        System.currentTimeMillis() + "",
                        new NetworkManager.SuccessCallback<Order>() {
                            @Override
                            public void onSuccess(Order response) {
                                if (response != null && response.orderid != null)
                                    paySuccess(response.orderid, itemName);
                            }
                        },
                        new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {
                                showNoMoneyDialog();
                            }
                        }
                );
            }
        });
    }

    private void showNoMoneyDialog() {
        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.notSufficientFunds);
        dialog.setListener(new SomeCommonDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm(DialogTypeEnum type) {
                startActivity(new Intent(Test_mainActivity.this, PayActivity.class));
            }
        });
        dialog.show(getSupportFragmentManager(), "noMoney");
    }

    private void paySuccess(String orderid, final String itemName) {
        NetworkApi.pay_status(MyApplication.getInstance().userId, Utils.getDeviceId(), orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                final Intent intent = new Intent();
                switch (itemName) {
                    case "体重":
                        intent.setClass(mContext, DetectActivity.class);
                        intent.putExtra("type", "tizhong");
                        startActivity(intent);
                        break;
                    case "血氧":
                        intent.setClass(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xueyang");
                        startActivity(intent);
                        break;
                    case "血压":
                        intent.setClass(mContext, SingleDetectActivity.class);
                        intent.putExtra("type", "xueya");
                        startActivity(intent);
                        break;
                    case "血糖":
                        intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
                        intent.putExtra("type", "xuetang");
                        startActivity(intent);
                        break;
                    case "心电":
                        intent.setClass(mContext, XinDianDetectActivity.class);
                        startActivity(intent);
                        break;
                    case "生化综合1":
                        intent.setClass(mContext, SelectXuetangTimeActivity.class);
                        intent.putExtra("type", "sanheyi");
                        startActivity(intent);
                        break;
                    case "生化综合2":
                        intent.setClass(mContext, DetectActivity.class);
                        intent.putExtra("type", "wendu");
                        startActivity(intent);
                        break;

                }

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }

    NDialog2 dialog2 = new NDialog2(Test_mainActivity.this);

    public void ShowNormal(String message) {
        dialog2.setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                        }
                    }
                }).create(NDialog.CONFIRM).show();

    }

    @NonNull
    private void showPriceNoticeDialog(String price, String itemName, final NoticePriceDialog.OnDialogClickListener listener) {
        NoticePriceDialog dialog = new NoticePriceDialog(price, itemName);
        dialog.setListener(new NoticePriceDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm() {
                if (listener != null) {
                    listener.onClickConfirm();
                }
            }
        });
        dialog.show(getFragmentManager(), "priceDialog");
    }


}
