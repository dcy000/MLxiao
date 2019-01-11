package com.example.han.referralproject.recharge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.Utils;
import com.gcml.common.data.UserSpHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;

/**
 * 支付紧急应付不能用
 */
public class QRCodeAliPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQ_QRCODE_CODE = 1;
    private static final int NOTIFY_RESULT = 10;
    private static final int ERR_CODE = 99;

    ProgressDialog loadingDialog;

    String billNum;
    String billId;
    String billId1;

    String type = "ALI_QRCODE";
    BCReqParams.BCChannelTypes channelType;
    String billTitle;

    Bitmap qrCodeBitMap;
    Bitmap qrCodeBitMap1;

    String notify;
    String errMsg;

//    ImageView qrcodeImg;
//    ImageView qrcodeImg1;

    private String aliQRHtml;
    private String aliQRURL;

    private String prereturnUrl = "https://unitradeprod.alipay.com/acq/cashierReturn.htm";
    private String returnUrl = "https://gcml.page.pay.com/";

    private WebView wvQRCode;
    private TextView tvPayStatus;

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
                    Double numbers = Double.parseDouble(number) / 100;
                    // 5. 业务充值
                    NetworkApi.PayInfo(Utils.getDeviceId(), numbers + "", date.getTime() + "", UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            // 6. 充值成功
                            if (tvPayStatus != null) {
                                tvPayStatus.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        wvQRCode.setVisibility(View.GONE);
                                        tvPayStatus.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            Toast.makeText(QRCodeAliPayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                            speak(getString(R.string.pay_success));
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            // 6. 充值失败
                            sign = false;
                            Log.e("支付成功同步到我们的后台", "onFailed: " + message);
                        }
                    });

                    break;

                case 0:
                    Double number1 = Double.parseDouble(number) / 100;
                    NetworkApi.PayInfo(Utils.getDeviceId(), number1 + "", date.getTime() + "", UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(QRCodeAliPayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                            speak(getString(R.string.pay_success));
                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            sign1 = false;

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

        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.pay));

        speak(getString(R.string.zhanghu_chongzhi));

    /*    mImageView1 = (ImageView) findViewById(R.id.health_record_icon_back);
        mImageView2 = (ImageView) findViewById(R.id.health_record_icon_home);
*/
        Intent intent = getIntent();
        number = intent.getStringExtra("number");//5000

        //对于二维码，微信使用 WX_NATIVE 作为channel参数
        //支付宝使用ALI_OFFLINE_QRCODE

        channelType = BCReqParams.BCChannelTypes.valueOf(type);
        billTitle = "杭州国辰迈联机器人科技有限公司";

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("处理中，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);

        wvQRCode = (WebView) findViewById(R.id.wvQRCode);
        tvPayStatus = (TextView) findViewById(R.id.tvPayStatus);
        tvPayStatus.setVisibility(View.GONE);
        WebSettings webSettings = wvQRCode.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
//        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        webSettings.setLoadWithOverviewMode(true);
        // 设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);

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

//        aliQRHtml = getIntent().getStringExtra("aliQRHtml");
//        aliQRURL = getIntent().getStringExtra("aliQRURL");
//        //可以
//        wvQRCode.loadUrl(aliQRURL);

        reqQrCode();

        date = new Date();
    }

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("您是否已支付成功?")
                .setMessageSize(35)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(getResources().getColor(R.color.toolbar_bg))
                .setNegativeTextColor(Color.parseColor("#999999"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            finish();
                        }
                    }
                }).create(NDialog1.CONFIRM).show();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("您是否已支付成功?")
                .setMessageSize(35)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        }
                    }
                }).create(NDialog1.CONFIRM).show();
    }


    public void QueryOrder() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sign) {

                    if (type.startsWith("BC")) {
                        // BC的渠道通过id查询结果
                        BCQuery.getInstance().queryBillByIDAsync(billId, new BCCallback() {
                            @Override
                            public void done(BCResult result) {
                                BCQueryBillResult billStatus = (BCQueryBillResult) result;
                                //Logg.e("支付信息",billStatus.getResultMsg() +"错误详情："+billStatus.getErrDetail()+"返回码"+billStatus.getResultCode()+"");
//                                Logg.e("支付信息",billStatus.getBill().getTradeNum() + "\npayresult："+billStatus.getBill().getPayResult()+
//                                        "\nRevertResult"+billStatus.getBill().getRevertResult()+"\nRefundResult"+billStatus.getBill().getRefundResult());
                                //表示支付成功
                                if (billStatus.getResultCode() == 0 && billStatus.getBill().getPayResult()) {

                                    mHandler.sendEmptyMessage(2);
                                    sign = false;

                                }

                            }
                        });
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                }
            }

        }).start();

    }

    public void QueryOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sign1) {
                    if (type.startsWith("BC")) {
                        // BC的渠道通过id查询结果
                        BCQuery.getInstance().queryBillByIDAsync(billId1, new BCCallback() {
                            @Override
                            public void done(BCResult result) {

                                BCQueryBillResult billStatus = (BCQueryBillResult) result;

                                //表示支付成功
                                if (billStatus.getResultCode() == 0 && billStatus.getBill().getPayResult()) {
                                    mHandler.sendEmptyMessage(0);
                                    sign1 = false;
                                }

                            }
                        });
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                }
            }

        }).start();

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
        payParams.billTotalFee = 1;
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
                            aliQRHtml = bcPayResult.getHtml();
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
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sign = false;
        sign1 = false;
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
