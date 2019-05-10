package com.gcml.mall.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.recommend.bean.get.GoodsBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.R;
import com.gcml.mall.bean.PayingOrderBean;
import com.gcml.mall.network.MallRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/mall/goods/detail/activity")
public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    TranslucentToolBar mToolBar;
    ImageView goodsImage, goodsIncrease, goodsDecrease;
    TextView goodsName, goodsDetail, goodsPrice, goodsQuantity, goodsAmount, goodsShopping;
    GoodsBean goods;
    MallRepository mallRepository;
    int mQunatity = 1;
    PayingOrderBean payingOrder = new PayingOrderBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        mContext = this;
        goods = (GoodsBean) getIntent().getSerializableExtra("goods");
        mallRepository = new MallRepository();

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_goods);
        goodsImage = findViewById(R.id.iv_goods_image);
        goodsIncrease = findViewById(R.id.iv_goods_increase);
        goodsDecrease = findViewById(R.id.iv_goods_decrease);
        goodsName = findViewById(R.id.tv_goods_name);
        goodsDetail = findViewById(R.id.tv_goods_pres);
        goodsPrice = findViewById(R.id.tv_goods_price);
        goodsQuantity = findViewById(R.id.tv_goods_quantity);
        goodsAmount = findViewById(R.id.tv_goods_amount);
        goodsShopping = findViewById(R.id.tv_goods_shopping);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请选择选购数量", false);
        mToolBar.setData("商 品 详 情", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });

        goodsIncrease.setOnClickListener(this);
        goodsDecrease.setOnClickListener(this);
        goodsShopping.setOnClickListener(this);

        if (goods != null) {
            ImageLoader.with(this)
                    .load(goods.goodsimage)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(goodsImage);
            goodsName.setText(goods.goodsname);
            goodsPrice.setText("¥" + goods.goodsprice);
            goodsAmount.setText("总价：¥" + String.format("%.2f", Double.parseDouble(goods.goodsprice)));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_goods_increase) {
            mQunatity++;
            if (mQunatity == 1) {
                ImageLoader.with(this)
                        .load(R.drawable.img_goods_decrease)
                        .placeholder(R.drawable.img_goods_decrease)
                        .error(R.drawable.img_goods_decrease)
                        .into(goodsDecrease);
            } else {
                ImageLoader.with(this)
                        .load(R.drawable.img_goods_decrease_2)
                        .placeholder(R.drawable.img_goods_decrease)
                        .error(R.drawable.img_goods_decrease)
                        .into(goodsDecrease);
            }
            goodsQuantity.setText(mQunatity + "");
            double amount1 = Double.parseDouble(goods.goodsprice) * mQunatity;
            goodsAmount.setText("总价：¥" + String.format("%.2f", amount1));
        } else if (i == R.id.iv_goods_decrease) {
            mQunatity--;
            if (mQunatity > 1) {
                ImageLoader.with(this)
                        .load(R.drawable.img_goods_decrease_2)
                        .placeholder(R.drawable.img_goods_decrease_2)
                        .error(R.drawable.img_goods_decrease_2)
                        .into(goodsDecrease);
            } else {
                mQunatity = 1;
                ImageLoader.with(this)
                        .load(R.drawable.img_goods_decrease)
                        .placeholder(R.drawable.img_goods_decrease_2)
                        .error(R.drawable.img_goods_decrease_2)
                        .into(goodsDecrease);
            }
            goodsQuantity.setText(mQunatity + "");
            double amount2 = Double.parseDouble(goods.goodsprice) * mQunatity;
            goodsAmount.setText("总价：¥" + String.format("%.2f", amount2));
        } else if (i == R.id.tv_goods_shopping) {
            Log.e("xxxx", UserSpHelper.getUserId() + ":" + Utils.getDeviceId(getContentResolver()) + ":" + goods.goodsname + ":" + goodsQuantity.getText().toString() + ":" +
                    (Float.parseFloat(goodsQuantity.getText().toString()) * Float.parseFloat(goods.goodsprice)) + ":" + goods.goodsimage + ":" + System.currentTimeMillis());
            mallRepository.shoppingForApi(UserSpHelper.getUserId(), Utils.getDeviceId(getContentResolver()),
                    goods.goodsname, goodsQuantity.getText().toString(),
                    (Float.parseFloat(goodsQuantity.getText().toString()) * Float.parseFloat(goods.goodsprice)) + "",
                    goods.goodsimage, System.currentTimeMillis() + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribeWith(new DefaultObserver<String>() {
                        @Override
                        public void onNext(String body) {
                            super.onNext(body);
                            ShowShopNormal(body);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            ShowLackNormal(throwable.getMessage());
                        }
                    });
        }
    }

    public void ShowShopNormal(final String orderid) {
        new AlertDialog(GoodsDetailActivity.this).builder()
                .setMsg("是否确认购买该商品")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderid", orderid);
                        bundle.putString("from", "Pay");
                        bundle.putInt("requestCode", 1);
                        checkUser(orderid);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(orderid);
                    }
                }).show();
    }

    public void ShowLackNormal(String message) {
        new AlertDialog(mContext).builder()
                .setMsg(message)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @SuppressLint("CheckResult")
    private void cancelOrder(String orderid) {
        mallRepository.payCancelForApi("3", "0", "1", orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String body) {
                        super.onNext(body);
                        ShowLackNormal("取消订单成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ShowLackNormal("取消订单失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void confirmOrder(String orderid) {
        mallRepository.payStatysForApi(UserSpHelper.getUserId(), Utils.getDeviceId(getContentResolver()), orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String body) {
                        super.onNext(body);
                        ShowLackNormal("同步订单成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ShowLackNormal("同步订单失败");
                    }
                });
    }

    private void checkUser(String orderid) {
        Routerfit.register(AppRouter.class)
                .getFaceProvider()
                .getFaceId(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new io.reactivex.observers.DefaultObserver<String>() {
                    @Override
                    public void onNext(String faceId) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        CC.obtainBuilder("com.gcml.auth.face2.signin")
//                .addParam("verify", true)
//                .build()
//                .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        if (result.isSuccess()) {
//                            showPaySuccessDialog(GoodsDetailActivity.this);
//                            confirmOrder(orderid);
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
                                        showPaySuccessDialog(GoodsDetailActivity.this);
                                        confirmOrder(orderid);
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


    public void showPaySuccessDialog(Activity activity) {
        MLVoiceSynthetize.startSynthesize(activity.getApplicationContext(), "恭喜您支付成功", false);
        new AlertDialog(GoodsDetailActivity.this).builder()
                .setMsg("支付成功")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                            Intent intent = new Intent(activity, OrderListActivity.class);
//                            activity.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
