package com.example.han.referralproject.shopping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.util.Utils;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/app/shopping/goods/detail")
public class GoodDetailActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    Button mButton;
    int i = 1;
    NDialog1 dialog1;
    NDialog2 dialog2;

    GoodBean goods;

    public static Activity mActivity;


    @SuppressLint("StringFormatMatches")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);

        speak(getString(R.string.shop_mount));

        mToolbar.setVisibility(View.VISIBLE);


        mTitleText.setText(getString(R.string.goods_detail));


        mActivity = this;

        dialog1 = new NDialog1(GoodDetailActivity.this);
        dialog2 = new NDialog2(GoodDetailActivity.this);


        Intent intent = getIntent();
        goods = (GoodBean) intent.getSerializableExtra("goods");

        mImageView1 = findViewById(R.id.goods_image);
        mImageView2 = findViewById(R.id.add_mount);
        mImageView3 = findViewById(R.id.reduce_mount);

        mButton = findViewById(R.id.shopping);

        mTextView = findViewById(R.id.goods_name);
        mTextView1 = findViewById(R.id.goods_price);
        mTextView2 = findViewById(R.id.goods_mount);
        mTextView3 = findViewById(R.id.goods_sum_price);


        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mButton.setOnClickListener(this);

        mTextView.setText(goods.goodsname);
        mTextView1.setText(goods.goodsprice.toString());
        mTextView3.setText(String.format(getString(R.string.shop_sum_price), goods.goodsprice.floatValue()));

        Picasso.with(this)
                .load(goods.goodsimage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(this)
                .fit()
                .into(mImageView1);
    }


    /**
     * 返回上一页
     */
    protected void backLastActivity() {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_mount:

                i++;
                mTextView2.setText(i + "");
                float sumPrice = Float.parseFloat(mTextView1.getText().toString()) * i;
                mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice + ""));
                break;

            case R.id.reduce_mount:

                i--;

                if (i >= 1) {

                    mTextView2.setText(i + "");
                    float sumPrice1 = Float.parseFloat(mTextView1.getText().toString()) * i;
                    mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice1 + ""));


                } else {
                    i = 1;
                }
                break;

            case R.id.shopping:
            /*    ToastUtils.showShort("该功能暂未开放");
                speak("该功能暂未开放");*/
                NetworkApi.preparingPay(UserSpHelper.getUserId(), Utils.getDeviceId(), goods.goodsname, mTextView2.getText().toString(), (Float.parseFloat(mTextView2.getText().toString()) * goods.goodsprice.floatValue()) + "", goods.goodsimage, System.currentTimeMillis() + "", new NetworkManager.SuccessCallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        ShowNormals(data);
                    }

                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        if (!TextUtils.isEmpty(message) && message.contains("，")) {
                            String[] split = message.split("，");
                            if (split.length > 1) {
                                ShowNormal(split[0]);
                            }
                        } else {
                            ShowNormal(message);
                        }

                    }
                });


        }


    }


    public void ShowNormals(final String orderid) {
        dialog1.setMessageCenter(true)
                .setMessage("是否确认购买该商品")
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("orderid", orderid);
                            bundle.putString("from", "Pay");
                            bundle.putInt("requestCode", 1);
                            checkUser(orderid);
//                            CCFaceRecognitionActions.jump2FaceRecognitionActivity(GoodDetailActivity.this, bundle);
                        } else if (which == 0) {
                            cancelOrder(orderid);
                        }

                    }
                }).create(NDialog.CONFIRM).show();

    }

    private void cancelOrder(String orderid) {
        NetworkApi.pay_cancel("3", "0", "1", orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                ShowNormal("取消成功");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ShowNormal("取消失败");
            }
        });
    }

    private void syncOrder(String orderid) {
        String deviceId = com.gcml.common.utils.Utils.getDeviceId(getContentResolver());
        NetworkApi.pay_status(UserSpHelper.getUserId(), deviceId, orderid, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Timber.i("同步订单成功");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                Timber.i("同步订单失败");
            }
        });
    }

    private void checkUser(String orderid) {
//        CC.obtainBuilder("com.gcml.auth.face2.signin")
//                .addParam("verify", true)
//                .build()
//                .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        if (result.isSuccess()) {
//                            showPaySuccessDialog(GoodDetailActivity.this);
//                            syncOrder(orderid);
//                        } else {
//                            ToastUtils.showShort(result.getErrorMessage());
//                            cancelOrder(orderid);
//                        }
//                    }
//                });

        Routerfit.register(AppRouter.class)
                .getFaceProvider()
                .getFaceId(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                    @Override
                    public void onNext(String faceId) {
                        Routerfit.register(AppRouter.class).skipFaceBdSignInActivity(false, true, faceId, true, new ActivityCallback() {
                            @Override
                            public void onActivityResult(int result, Object data) {
                                if (result == Activity.RESULT_OK) {
                                    String sResult = data.toString();
                                    if (TextUtils.isEmpty(sResult))
                                        return;
                                    if (sResult.equals("success")) {
                                        showPaySuccessDialog(GoodDetailActivity.this);
                                        syncOrder(orderid);
                                    } else if (sResult.equals("failed")) {
                                        ToastUtils.showShort("支付失败");
                                        cancelOrder(orderid);
                                    }

                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("请先注册人脸！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void showPaySuccessDialog(Activity activity) {
        ToastUtils.showShort("支付成功");
        MLVoiceSynthetize.startSynthesize(activity.getApplicationContext(), "恭喜您支付成功", false);
        NDialog2 dialog2 = new NDialog2(activity);
        dialog2.setMessageCenter(true)
                .setMessage("支付成功")
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            Intent intent = new Intent(activity, OrderListActivity.class);
                            activity.startActivity(intent);
                        }

                    }
                }).create(NDialog.CONFIRM).show();
    }

    public void ShowNormal(String message) {
        if (dialog2 == null) {
            return;
        }
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
                        if (message.contains("余额不足")) {
                            startActivity(new Intent(GoodDetailActivity.this, PayActivity.class));
                        }
                    }
                }).create(NDialog.CONFIRM).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mActivity != null) {
            mActivity = null;
        }


    }


    @Override
    protected void onDestroy() {

        if (dialog2 != null) {

            dialog2.create(NDialog.CONFIRM).cancel();
            dialog2 = null;
        }
        super.onDestroy();

    }
}
