package com.example.han.referralproject.shopping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.util.Utils;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    Goods goods;

    public static Activity mActivity;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);

        MLVoiceSynthetize.startSynthesize(R.string.shop_mount);

        mToolbar.setVisibility(View.VISIBLE);


        mTitleText.setText(getString(R.string.goods_detail));


        mActivity = this;

        dialog1 = new NDialog1(GoodDetailActivity.this);
        dialog2 = new NDialog2(GoodDetailActivity.this);


        Intent intent = getIntent();
        goods = (Goods) intent.getSerializableExtra("goods");

        mImageView1 = (ImageView) findViewById(R.id.goods_image);
        mImageView2 = (ImageView) findViewById(R.id.add_mount);
        mImageView3 = (ImageView) findViewById(R.id.reduce_mount);

        mButton = (Button) findViewById(R.id.shopping);

        mTextView = (TextView) findViewById(R.id.goods_name);
        mTextView1 = (TextView) findViewById(R.id.goods_price);
        mTextView2 = (TextView) findViewById(R.id.goods_mount);
        mTextView3 = (TextView) findViewById(R.id.goods_sum_price);


        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mButton.setOnClickListener(this);

        mTextView.setText(goods.getGoodsname());
        mTextView1.setText(goods.getGoodsprice());
        mTextView3.setText(String.format(getString(R.string.shop_sum_price), goods.getGoodsprice()));

        Picasso.with(this)
                .load(goods.getGoodsimage())
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
                int sumPrice = Integer.parseInt(mTextView1.getText().toString()) * i;
                mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice + ""));
                break;

            case R.id.reduce_mount:

                i--;

                if (i >= 1) {

                    mTextView2.setText(i + "");
                    int sumPrice1 = Integer.parseInt(mTextView1.getText().toString()) * i;
                    mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice1 + ""));


                } else {
                    i = 1;
                }
                break;

            case R.id.shopping:
                UserInfoBean user = Box.getSessionManager().getUser();
                Box.getRetrofit(API.class)
                        .orderInfo(
                                user.bid,
                                Utils.getDeviceId(),
                                goods.getGoodsname(),
                                mTextView2.getText().toString(),
                                (Integer.parseInt(mTextView2.getText().toString()) * Integer.parseInt(goods.getGoodsprice())) + "",
                                goods.getGoodsimage(),
                                System.currentTimeMillis() + ""
                        )
                        .compose(RxUtils.httpResponseTransformer())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new CommonObserver<Object>() {
                            @Override
                            public void onNext(Object order) {
                                ShowNormals( order+"");
                            }

                            @Override
                            protected void onError(ApiException ex) {
                                super.onError(ex);
                                ShowNormal(ex.getMessage());
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
                            Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                            intent.putExtra("orderid", orderid);
                            intent.putExtra("from", "Pay");
                            startActivityForResult(intent, 1);


                        } else if (which == 0) {


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

                    }
                }).create(NDialog.CONFIRM).show();

    }


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

    public void showPaySuccessDialog() {
        MLVoiceSynthetize.startSynthesize(R.string.shop_success);
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
                            Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }).create(NDialog.CONFIRM).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                showPaySuccessDialog();
            }
        }
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
