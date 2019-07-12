package com.gcml.old;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.face2.VertifyFace2ProviderImp;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.mall.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/app/shopping/goods/detail")
public class GoodDetailActivity extends ToolbarBaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    TextView mTextView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    Button mButton;
    int i = 1;

    GoodBean goods;


    @SuppressLint("StringFormatMatches")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        mTitleText.setText("商  品  详  情");
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请选择购买数量");

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
        mTextView3.setText(String.format("总价：%1$s", goods.goodsprice.floatValue()));

        ImageLoader.with(mImageView1.getContext())
                .load(goods.goodsimage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(mImageView1);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i1 = view.getId();
        if (i1 == R.id.add_mount) {
            i++;
            mTextView2.setText(i + "");
            float sumPrice = Float.parseFloat(mTextView1.getText().toString()) * i;
            mTextView3.setText(String.format("总价：%1$s", sumPrice + ""));

        } else if (i1 == R.id.reduce_mount) {
            i--;

            if (i >= 1) {

                mTextView2.setText(i + "");
                float sumPrice1 = Float.parseFloat(mTextView1.getText().toString()) * i;
                mTextView3.setText(String.format("总价：%1$s", sumPrice1 + ""));


            } else {
                i = 1;
            }

        } else if (i1 == R.id.shopping) {/*    ToastUtils.showShort("该功能暂未开放");
                speak("该功能暂未开放");*/
            GoodsRepository repository = new GoodsRepository();
            repository.shopping(UserSpHelper.getUserId(), DeviceUtils.getIMEI(), goods.goodsname, mTextView2.getText().toString(), (Float.parseFloat(mTextView2.getText().toString()) * goods.goodsprice.floatValue()) + "", goods.goodsimage, System.currentTimeMillis() + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String s) {
                            ShowNormals(s);
                        }

                        @Override
                        public void onError(Throwable e) {
                            String message = e.getMessage();
                            if (!TextUtils.isEmpty(message) && message.contains("，")) {
                                String[] split = message.split("，");
                                if (split.length > 1) {
                                    ShowNormal(split[0]);
                                }
                            } else {
                                ShowNormal(message);
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }


    }


    public void ShowNormals(final String orderid) {
        new AlertDialog(this)
                .builder()
                .setMsg("是否确认购买该商品？")
                .setNegativeButton("取消", ContextCompat.getColor(this,
                        R.color.config_color_base_9), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(orderid);
                    }
                })
                .setPositiveButton("确认", ContextCompat.getColor(this,
                        R.color.market_toolbar_bg), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderid", orderid);
                        bundle.putString("from", "Pay");
                        bundle.putInt("requestCode", 1);
                        checkUser(orderid);
                    }
                }).show();
    }

    private void cancelOrder(String orderid) {
        GoodsRepository repository = new GoodsRepository();
        repository.payCancel("3", "0", "1", orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object s) {
                        Timber.i("取消订单成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ShowNormal("取消失败");
                        Timber.i("取消订单失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void syncOrder(String orderid) {
        String deviceId = com.gcml.common.utils.Utils.getDeviceId(getContentResolver());
        GoodsRepository repository = new GoodsRepository();
        repository.payStatys(UserSpHelper.getUserId(), deviceId, orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        Timber.i("同步订单成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("同步订单失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void checkUser(String orderid) {
        Routerfit.register(AppRouter.class)
                .getVertifyFaceProvider()
                .checkUserEntityAndVertifyFace(false, true, true, new VertifyFace2ProviderImp.VertifyFaceResult() {
                    @Override
                    public void success() {
                        showPaySuccessDialog(GoodDetailActivity.this);
                        syncOrder(orderid);
                    }

                    @Override
                    public void failed(String msg) {
                        Timber.e("支付失败>>>" + msg);
                        ToastUtils.showShort("支付失败");
                        cancelOrder(orderid);
                    }
                });
    }


    public void showPaySuccessDialog(Activity activity) {
        ToastUtils.showShort("支付成功");
        MLVoiceSynthetize.startSynthesize(activity.getApplicationContext(), "恭喜您支付成功", false);

        NiceDialog.init()
                .setLayoutId(R.layout.dialog_show_msg_one_button)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_msg, "支付成功");
                        holder.getView(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Routerfit.register(AppRouter.class).skipOldOrderListActivity();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getSupportFragmentManager());
    }

    public void ShowNormal(String message) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_show_msg_one_button)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_msg, message);
                        holder.getView(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (message.contains("余额不足")) {
                                    Routerfit.register(AppRouter.class).skipPayActivity();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(350)
                .show(getSupportFragmentManager());

    }

}
