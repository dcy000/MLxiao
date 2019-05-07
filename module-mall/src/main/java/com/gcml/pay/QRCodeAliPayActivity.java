package com.gcml.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.mall.R;
import com.gcml.old.GoodsRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCReqParams;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 支付紧急应付不能用
 */
public class QRCodeAliPayActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private static final int REQ_QRCODE_CODE = 1;
    private static final int NOTIFY_RESULT = 10;
    private static final int ERR_CODE = 99;

    ProgressDialog loadingDialog;

    String billId;

    String type = "ALI_QRCODE";
    BCReqParams.BCChannelTypes channelType;
    String billTitle;


    String notify;
    String errMsg;
    private String aliQRURL;

    private String prereturnUrl = "https://unitradeprod.alipay.com/acq/cashierReturn.htm";
    private String returnUrl = "https://gcml.page.pay.com/";

    private WebView wvQRCode;

    private String orderId;
    private boolean isSkip;
    private String servicePackageType;
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case REQ_QRCODE_CODE:
//                    qrcodeImg.setImageBitmap(qrCodeBitMap);
                    // 3. 显示网页二维码 ， 用户用支付宝客户端扫码支付
                    wvQRCode.loadUrl(aliQRURL);
                    break;
                case 3:
//                    qrcodeImg1.setImageBitmap(qrCodeBitMap1);
                    break;
                case NOTIFY_RESULT:
                    Toast.makeText(QRCodeAliPayActivity.this, notify, Toast.LENGTH_LONG).show();
                    break;
                case ERR_CODE:
                    Toast.makeText(QRCodeAliPayActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    new GoodsRepository().servicePackageEffective(servicePackageType, orderId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .as(RxUtils.autoDisposeConverter(QRCodeAliPayActivity.this))
                            .subscribe(new DefaultObserver<String>() {
                                @Override
                                public void onNext(String data) {
                                    Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(isSkip, servicePackageType, data);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.e("套餐失效失败" + e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                    break;

                case 0:
                    Double number1 = Double.parseDouble(number) / 100;
                    new GoodsRepository()
                            .PayInfo(DeviceUtils.getIMEI(), number1 + "", date.getTime() + "", UserSpHelper.getUserId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<Object>() {
                                @Override
                                public void onNext(Object o) {
                                    Toast.makeText(QRCodeAliPayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                                    MLVoiceSynthetize.startSynthesize(UM.getApp(), getString(R.string.pay_success));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    sign1 = false;
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                    break;
            }
            return true;
        }
    });

    public String number;
    //  public ImageView mImageView1;
    //  public ImageView mImageView2;

    public Boolean sign = true;
    public Boolean sign1 = true;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_ali_pay);
        mTitleText.setText("套 餐 购 买");
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请使用支付宝扫码进行购买");
        Intent intent = getIntent();
        number = intent.getStringExtra("number");//5000
        isSkip = intent.getBooleanExtra("isSkip", false);
        servicePackageType = intent.getStringExtra("ServicePackage");
        String des = intent.getStringExtra("description");
        mToolbar.setVisibility(View.VISIBLE);
        new GoodsRepository().bugServicePackage(number, des)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        orderId = o + "";
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //对于二维码，微信使用 WX_NATIVE 作为channel参数
        //支付宝使用ALI_OFFLINE_QRCODE

        channelType = BCReqParams.BCChannelTypes.valueOf(type);
        billTitle = "杭州国辰迈联机器人科技有限公司";

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("处理中，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);

        wvQRCode = (WebView) findViewById(R.id.wvQRCode);
        WebSettings webSettings = wvQRCode.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);

        wvQRCode.setOnTouchListener(new View.OnTouchListener() { // 禁止 WebView 点击事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        wvQRCode.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Here put your code
                if (TextUtils.isEmpty(url)) {
                    return true;
                }

                if (url.startsWith(prereturnUrl) || url.startsWith(returnUrl)) {
                    // 4 . 支付完成 调业务充值接口
                    mHandler.sendEmptyMessage(2);
                    return true;
                }

                // return true; //Indicates WebView to NOT load the url;
                return false; //Allow WebView to load url
            }

            //必须重写
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });


        reqQrCode();
        date = new Date();
    }

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        new AlertDialog(this)
                .builder()
                .setMsg("您是否已支付成功?")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
    }

    void reqQrCode() {
        // 支付宝内嵌二维码支付
        loadingDialog.show();
        Map<String, String> optional = new HashMap<String, String>();
        optional.put("用途", "用户充值");
        optional.put("testEN", "迈联智慧");

        BCPay.PayParams payParams = new BCPay.PayParams();
        payParams.channelType = BCReqParams.BCChannelTypes.ALI_QRCODE;
        payParams.billTitle = billTitle;
        payParams.billTotalFee = Integer.parseInt(number);
        payParams.billNum = BillUtils.genBillNum();
        payParams.optional = optional;
        payParams.returnUrl = returnUrl;
        payParams.qrPayMode = "0";
        // 1. 预下单
        BCPay.getInstance(QRCodeAliPayActivity.this).reqPaymentAsync(payParams,
                new BCCallback() {     //回调入口
                    @Override
                    public void done(BCResult bcResult) {
                        if (isFinishing()) {
                            return;
                        }
                        //此处关闭loading界面
                        loadingDialog.dismiss();

                        final BCPayResult bcPayResult = (BCPayResult) bcResult;

                        //resultCode为0表示请求成功
                        if (bcPayResult.getErrCode() == 0) {
                            // 2. 预下单成功， 此处应该在业务服务器记录订单（未设计）， 订单状态：未支付
                            billId = bcPayResult.getId();
                            aliQRURL = bcPayResult.getUrl();
//                            aliQRHtml = bcPayResult.getHtml();
                            mHandler.sendEmptyMessage(REQ_QRCODE_CODE);
                        } else {
                            errMsg = "err code:" + bcPayResult.getErrCode() +
                                    "; err msg: " + bcPayResult.getErrMsg() +
                                    "; err detail: " + bcPayResult.getDetailInfo();
                            mHandler.sendEmptyMessage(ERR_CODE);
                        }
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        sign = false;
        sign1 = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvQRCode != null) {
            wvQRCode.destroy();
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
