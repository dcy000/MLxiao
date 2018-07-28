package com.example.han.referralproject.recharge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCCache;
import cn.beecloud.BCOfflinePay;
import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCQRCodeResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;

public class PayInfoActivity extends BaseActivity implements View.OnClickListener {


    private static final int REQ_QRCODE_CODE = 1;
    private static final int NOTIFY_RESULT = 10;
    private static final int ERR_CODE = 99;

    ProgressDialog loadingDialog;

    String billNum;
    String billId;
    String billId1;

    String type = "BC_ALI_QRCODE";
    BCReqParams.BCChannelTypes channelType;
    String billTitle;

    Bitmap qrCodeBitMap;
    Bitmap qrCodeBitMap1;

    String notify;
    String errMsg;

    ImageView qrcodeImg;
    ImageView qrcodeImg1;


    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case REQ_QRCODE_CODE:

                    qrcodeImg.setImageBitmap(qrCodeBitMap);

                    break;

                case 3:

                    qrcodeImg1.setImageBitmap(qrCodeBitMap1);

                    break;

                case NOTIFY_RESULT:
                    Toast.makeText(PayInfoActivity.this, notify, Toast.LENGTH_LONG).show();
                    break;

                case ERR_CODE:
                    Toast.makeText(PayInfoActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Double numbers = Double.parseDouble(number) / 100;

                    NetworkApi.PayInfo(Utils.getDeviceId(), numbers + "", date.getTime() + "", MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(PayInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            speak(getString(R.string.pay_success));

                        }
                    }, new NetworkManager.FailedCallback() {
                        @Override
                        public void onFailed(String message) {
                            sign = false;
                            Log.e("支付成功同步到我们的后台", "onFailed: "+message);
                        }
                    });

                    break;

                case 0:
                    Double number1 = Double.parseDouble(number) / 100;

                    NetworkApi.PayInfo(Utils.getDeviceId(), number1 + "", date.getTime() + "", MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(PayInfoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_pay_info);

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

        qrcodeImg = (ImageView) this.findViewById(R.id.qrcodeImg);
        qrcodeImg1 = (ImageView) this.findViewById(R.id.qrcodeImg1);


        reqQrCode();

      /*  mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);*/
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
                }).create(NDialog.CONFIRM).show();
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
                }).create(NDialog.CONFIRM).show();
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
                        Thread.sleep(1000);
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
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                }
            }

        }).start();

    }


    void reqQrCode() {


        loadingDialog.show();

        Map<String, String> optional = new HashMap<String, String>();
        optional.put("用途", "用户充值");
        optional.put("testEN", "迈联智慧");

        //初始化回调入口
        BCCallback callback = new BCCallback() {
            @Override
            public void done(BCResult bcResult) {

                //此处关闭loading界面
                loadingDialog.dismiss();

                final BCQRCodeResult bcqrCodeResult = (BCQRCodeResult) bcResult;

                Message msg = mHandler.obtainMessage();

                //resultCode为0表示请求成功
                if (bcqrCodeResult.getResultCode() == 0) {
                    billId = bcqrCodeResult.getId();


                    //如果你设置了生成二维码参数为true那么此处可以获取二维码
                    qrCodeBitMap = bcqrCodeResult.getQrCodeBitmap();

                    QueryOrder();


                    //否则通过 bcqrCodeResult.getQrCodeRawContent() 获取二维码的内容，自己去生成对应的二维码

                    msg.what = REQ_QRCODE_CODE;
                } else {
                    errMsg = "err code:" + bcqrCodeResult.getResultCode() + "; err msg: " + bcqrCodeResult.getResultMsg() + "; err detail: " + bcqrCodeResult.getErrDetail();

                    /**
                     * 你发布的项目中不需要做如下判断，此处由于支付宝政策原因，
                     * 不再提供支付宝支付的测试功能，所以给出提示说明
                     */
                    if (BCCache.getInstance().appId.equals("c5d1cba1-5e3f-4ba0-941d-9b0a371fe719") && type.equals("ALI") && !bcqrCodeResult.getErrDetail().equals("该功能暂不支持测试模式")) {
                        errMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
                    }

                    msg.what = ERR_CODE;
                }

                mHandler.sendMessage(msg);
            }
        };


        //初始化回调入口
        BCCallback callback1 = new BCCallback() {
            @Override
            public void done(BCResult bcResult) {

                //此处关闭loading界面
                loadingDialog.dismiss();

                final BCQRCodeResult bcqrCodeResult = (BCQRCodeResult) bcResult;

                Message msg = mHandler.obtainMessage();

                //resultCode为0表示请求成功
                if (bcqrCodeResult.getResultCode() == 0) {
                    billId1 = bcqrCodeResult.getId();

                    //如果你设置了生成二维码参数为true那么此处可以获取二维码
                    qrCodeBitMap1 = bcqrCodeResult.getQrCodeBitmap();

                    QueryOrders();


                    //否则通过 bcqrCodeResult.getQrCodeRawContent() 获取二维码的内容，自己去生成对应的二维码

                    msg.what = 3;
                } else {
                    errMsg = "err code:" + bcqrCodeResult.getResultCode() + "; err msg: " + bcqrCodeResult.getResultMsg() + "; err detail: " + bcqrCodeResult.getErrDetail();

                    /**
                     * 你发布的项目中不需要做如下判断，此处由于支付宝政策原因，
                     * 不再提供支付宝支付的测试功能，所以给出提示说明
                     */
                    if (BCCache.getInstance().appId.equals("c5d1cba1-5e3f-4ba0-941d-9b0a371fe719") && type.equals("ALI") && !bcqrCodeResult.getErrDetail().equals("该功能暂不支持测试模式")) {
                        errMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
                    }

                    msg.what = ERR_CODE;
                }

                mHandler.sendMessage(msg);
            }
        };


        //   billNum = BillUtils.genBillNum();

        // BeeCloud微信二维码支付并不是严格意义上的线下支付
        BCPay.getInstance(PayInfoActivity.this).reqBCNativeAsync(
                billTitle,  //商品描述
                Integer.parseInt(number),  //总金额, 以分为单位, 必须是正整数
                BillUtils.genBillNum(),   //流水号
                optional,  //扩展参数
                true,     //是否生成二维码的bitmap
                480,       //二维码的尺寸, 以px为单位, 如果为null则默认为360
                callback1);


        BCOfflinePay.PayParams payParam = new BCOfflinePay.PayParams();
        payParam.channelType = channelType;
        payParam.billTitle = billTitle; //商品描述
        payParam.billTotalFee = Integer.parseInt(number); //总金额, 以分为单位, 必须是正整数
        payParam.billNum = BillUtils.genBillNum();         //流水号
        payParam.optional = optional;   //扩展参数
        payParam.genQRCode = true;      //是否生成二维码的bitmap
        payParam.qrCodeWidth = 480;     //二维码的尺寸, 以px为单位, 如果为null则默认为360

        BCOfflinePay.getInstance().reqQRCodeAsync(payParam, callback);
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
}
