package com.example.module_mall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.module_mall.R;
import com.example.module_mall.bean.Goods;
import com.example.module_mall.service.MallAPI;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gcml.lib_widget.dialog.SingleButtonDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    Goods goods;

    public static Activity mActivity;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_good_detail;
    }

    @Override
    public void initParams(Intent intentArgument) {
        goods = (Goods) intentArgument.getSerializableExtra("goods");
        MLVoiceSynthetize.startSynthesize(R.string.shop_mount);
    }

    @Override
    public void initView() {
        mTitleText.setText(getString(R.string.goods_detail));

        mActivity = this;

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

        Glide.with(Box.getApp())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.mall_placeholder)
                        .error(R.drawable.mall_placeholder))
                .load(goods.getGoodsimage())
                .into(mImageView1);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i1 = view.getId();
        if (i1 == R.id.add_mount) {
            i++;
            mTextView2.setText(i + "");
            int sumPrice = Integer.parseInt(mTextView1.getText().toString()) * i;
            mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice + ""));

        } else if (i1 == R.id.reduce_mount) {
            i--;

            if (i >= 1) {

                mTextView2.setText(i + "");
                int sumPrice1 = Integer.parseInt(mTextView1.getText().toString()) * i;
                mTextView3.setText(String.format(getString(R.string.shop_sum_price), sumPrice1 + ""));


            } else {
                i = 1;
            }

        } else if (i1 == R.id.shopping) {
            String num = mTextView2.getText().toString();
            String price = goods.getGoodsprice();
            UserInfoBean user = Box.getSessionManager().getUser();
            Box.getRetrofit(MallAPI.class)
                    .orderInfo(
                            user.bid,
                            DeviceUtils.getIMEI(),
                            goods.getGoodsname(),
                            num,
                            (Integer.parseInt(num) * Float.parseFloat(price)),
                            goods.getGoodsimage(),
                            System.currentTimeMillis() + ""
                    )
                    .compose(RxUtils.httpResponseTransformer())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onNext(Object order) {
                            ShowNormals(order + "");
                        }

                        @Override
                        protected void onError(ApiException ex) {
                            ShowNormal(ex.message);
                        }
                    });
        }


    }


    public void ShowNormals(final String orderid) {
        new AlertDialog(this)
                .builder()
                .setMsg("是否确认购买该商品")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Box.getRetrofit(MallAPI.class)
                                .cancelPayOrder("3", "0", "1", orderid)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CommonObserver<Object>() {
                                    @Override
                                    public void onNext(Object o) {
                                        ShowNormal("取消成功");
                                    }

                                    @Override
                                    protected void onError(ApiException ex) {
                                        ShowNormal("取消失败");
                                    }
                                });
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vertifyWithFace();
                    }
                }).show();
    }

    private void vertifyWithFace() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<UserInfoBean> usersFromRoom = Box.getUsersFromRoom();
                if (usersFromRoom == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();
                for (UserInfoBean user : usersFromRoom) {
                    userIds.append(user.bid);
                }
                String substring = userIds.substring(0, userIds.length() - 1);
                emitter.onNext(substring);
            }
        }).flatMap(new Function<String, ObservableSource<HttpResult<ArrayList<UserInfoBean>>>>() {
            @Override
            public ObservableSource<HttpResult<ArrayList<UserInfoBean>>> apply(String s) throws Exception {
                return Box.getRetrofit(CommonAPI.class)
                        .queryAllLocalUsers(s);
            }
        })
                .compose(RxUtils.<ArrayList<UserInfoBean>>httpResponseTransformer())
                .as(RxUtils.<ArrayList<UserInfoBean>>autoDisposeConverter(this))
                .subscribe(new CommonObserver<ArrayList<UserInfoBean>>() {
                    @Override
                    public void onNext(ArrayList<UserInfoBean> users) {
                        Intent intent = new Intent();
                        intent.setClass(GoodDetailActivity.this, FaceSignInActivity.class);
                        intent.putExtra("skip", false);
                        intent.putExtra("currentUser", true);
                        intent.putParcelableArrayListExtra("users", users);
                        startActivityForResult(intent, 1001);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort(ex.getMessage());
                        emitEvent("skip2MainActivity");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            int faceResult = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
            switch (faceResult) {
                case FaceConstants.AUTH_FACE_SUCCESS:
                    showPaySuccessDialog();
                    break;
                case FaceConstants.AUTH_FACE_FAIL:
                    break;
            }
        }
    }

    public void ShowNormal(String message) {
        new SingleButtonDialog(this)
                .builder()
                .setMsg(message)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    public void showPaySuccessDialog() {
        MLVoiceSynthetize.startSynthesize(R.string.shop_success);

        new SingleButtonDialog(this)
                .builder()
                .setMsg("支付成功")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mActivity != null) {
            mActivity = null;
        }


    }
}
