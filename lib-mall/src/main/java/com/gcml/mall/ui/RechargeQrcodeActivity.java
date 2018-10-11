package com.gcml.mall.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.R;
import com.gcml.mall.network.MallRepository;
import com.gcml.mall.utils.BillUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCOfflinePay;
import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCQRCodeResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RechargeQrcodeActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    ImageView alipayQrcode, wxpayQrcode;
    Bitmap alipayQrcodeBitmap, wxpayQrcodeBitmap;
    String alpayBillId, wxpayBillId;
    String billTitle;
    Boolean isAlipaySign = true;
    Boolean isWxpaySign = true;
    int billMoney;// 充值金额
    Date billDate;
    String errorMsg;
    MallRepository mMallRepository = new MallRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_pay);
        billMoney = getIntent().getExtras().getInt("billMoney");

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_recharge_qrcode);
        alipayQrcode = findViewById(R.id.iv_recharge_alipay);
        wxpayQrcode = findViewById(R.id.iv_recharge_wxpay);
    }

    private void bindData() {
        mToolBar.setData("账 户 充 值", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                new AlertDialog(RechargeQrcodeActivity.this).builder()
                        .setMsg("您是否已支付成功?")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CC.obtainBuilder("com.gcml.mall.recharge").setContext(RechargeQrcodeActivity.this).build().callAsync();
                                finish();
                            }
                        }).show();
            }

            @Override
            public void onRightClick() {
                new AlertDialog(RechargeQrcodeActivity.this).builder()
                        .setMsg("您是否已支付成功?")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                                finish();
                            }
                        }).show();
            }
        });

        billTitle = "杭州国辰迈联机器人科技有限公司";
        billDate = new Date();
        getRechargeQrcode();
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case 0:
                    // 支付失败
                    Toast.makeText(RechargeQrcodeActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    // 支付宝扫码充值
//                    Toast.makeText(RechargeQrcodeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                    mMallRepository.rechargeForApi(Utils.getDeviceId(getContentResolver()), String.valueOf(billMoney / 100), billDate.getTime() + "", UserSpHelper.getUserId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .as(RxUtils.autoDisposeConverter(RechargeQrcodeActivity.this))
                            .subscribeWith(new DefaultObserver<Object>() {
                                @Override
                                public void onNext(Object body) {
                                    super.onNext(body);
                                    Toast.makeText(RechargeQrcodeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                                    CC.obtainBuilder("com.gcml.mall.recharge").setContext(RechargeQrcodeActivity.this).build().callAsync();
                                    finish();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    isAlipaySign = false;
                                }
                            });
                break;
                case 2:
                    // 微信扫码充值
//                    Toast.makeText(RechargeQrcodeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                    mMallRepository.rechargeForApi(Utils.getDeviceId(getContentResolver()), String.valueOf(billMoney / 100), billDate.getTime() + "", UserSpHelper.getUserId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .as(RxUtils.autoDisposeConverter(RechargeQrcodeActivity.this))
                            .subscribeWith(new DefaultObserver<Object>() {
                                @Override
                                public void onNext(Object body) {
                                    super.onNext(body);
                                    Toast.makeText(RechargeQrcodeActivity.this, "支付成功", Toast.LENGTH_LONG).show();
                                    CC.obtainBuilder("com.gcml.mall.recharge").setContext(RechargeQrcodeActivity.this).build().callAsync();
                                    finish();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    isWxpaySign = false;
                                }
                            });
                    break;
                case 3:
                    // 支付宝二维码
                    alipayQrcode.setImageBitmap(alipayQrcodeBitmap);
                    break;
                case 4:
                    // 微信二维码
                    wxpayQrcode.setImageBitmap(wxpayQrcodeBitmap);
                    break;
            }
            return true;
        }
    });

    public void queryAlipayResult() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAlipaySign) {
                    BCQuery.getInstance().queryBillByIDAsync(alpayBillId, new BCCallback() {
                        @Override
                        public void done(BCResult result) {
                            BCQueryBillResult billStatus = (BCQueryBillResult) result;
                            //表示支付成功
                            if (billStatus.getResultCode() == 0 && billStatus.getBill().getPayResult()) {
                                Log.e("xxxxxxxxxxxxxx", "支付成功");
                                mHandler.sendEmptyMessage(1);
                                isAlipaySign = false;
                            }
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void queryWxpayResult() {
        Log.e("xxxxxxxxxxxxxx", "支付");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isWxpaySign) {
                    BCQuery.getInstance().queryBillByIDAsync(wxpayBillId, new BCCallback() {
                        @Override
                        public void done(BCResult result) {
                            BCQueryBillResult billStatus = (BCQueryBillResult) result;
                            //表示支付成功
                            if (billStatus.getResultCode() == 0 && billStatus.getBill().getPayResult()) {
                                Log.e("xxxxxxxxxxxxxx", "支付成功");
                                mHandler.sendEmptyMessage(2);
                                isWxpaySign = false;
                            }
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void getRechargeQrcode() {
        LoadingDialog loadingDialog = new LoadingDialog.Builder(RechargeQrcodeActivity.this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        loadingDialog.show();
        Map<String, String> optional = new HashMap<String, String>();
        optional.put("用途", "用户充值");
        optional.put("testEN", "迈联智慧");

        //初始化回调入口
        BCCallback alipayCallback = new BCCallback() {
            @Override
            public void done(BCResult bcResult) {
                //此处关闭loading界面
                loadingDialog.dismiss();
                final BCQRCodeResult bcqrCodeResult = (BCQRCodeResult) bcResult;
                Message msg = mHandler.obtainMessage();
                //resultCode为0表示请求成功
                if (bcqrCodeResult.getResultCode() == 0) {
                    alpayBillId = bcqrCodeResult.getId();
                    alipayQrcodeBitmap = bcqrCodeResult.getQrCodeBitmap();
                    queryAlipayResult();
                    msg.what = 3;
                } else {
                    errorMsg = "err code:" + bcqrCodeResult.getResultCode() +
                            "; err msg: " + bcqrCodeResult.getResultMsg() +
                            "; err detail: " + bcqrCodeResult.getErrDetail();
                    msg.what = 0;
                }
                mHandler.sendMessage(msg);
            }
        };

        BCOfflinePay.PayParams alipayParam = new BCOfflinePay.PayParams();
        alipayParam.channelType = BCReqParams.BCChannelTypes.BC_ALI_QRCODE;
        alipayParam.billTitle = billTitle; //商品描述
        alipayParam.billTotalFee = billMoney; //总金额, 以分为单位, 必须是正整数
        alipayParam.billNum = BillUtils.genBillNum();         //流水号
        alipayParam.optional = optional;   //扩展参数
        alipayParam.genQRCode = true;      //是否生成二维码的bitmap
        alipayParam.qrCodeWidth = 480;     //二维码的尺寸, 以px为单位, 如果为null则默认为360
        BCOfflinePay.getInstance().reqQRCodeAsync(alipayParam, alipayCallback);

        //初始化回调入口
        BCCallback wxpayCallback = new BCCallback() {
            @Override
            public void done(BCResult bcResult) {
                //此处关闭loading界面
                loadingDialog.dismiss();
                final BCQRCodeResult bcqrCodeResult = (BCQRCodeResult) bcResult;
                Message msg = mHandler.obtainMessage();
                if (bcqrCodeResult.getResultCode() == 0) {
                    wxpayBillId = bcqrCodeResult.getId();
                    wxpayQrcodeBitmap = bcqrCodeResult.getQrCodeBitmap();
                    queryWxpayResult();
                    msg.what = 4;
                } else {
                    errorMsg = "err code:" + bcqrCodeResult.getResultCode() +
                            "; err msg: " + bcqrCodeResult.getResultMsg() +
                            "; err detail: " + bcqrCodeResult.getErrDetail();
                    msg.what = 0;
                }
                mHandler.sendMessage(msg);
            }
        };

        // BeeCloud微信二维码支付并不是严格意义上的线下支付
        BCPay.getInstance(RechargeQrcodeActivity.this).reqBCNativeAsync(
                billTitle,  //商品描述
                billMoney,  //总金额, 以分为单位, 必须是正整数
                BillUtils.genBillNum(),   //流水号
                optional,  //扩展参数
                true,     //是否生成二维码的bitmap
                480,       //二维码的尺寸, 以px为单位, 如果为null则默认为360
                wxpayCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isAlipaySign = false;
        isWxpaySign = false;
    }

}
