package com.gcml.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.device.DeviceUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.mall.R;
import com.gcml.old.GoodsRepository;
import com.google.gson.internal.LinkedTreeMap;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCCache;
import cn.beecloud.BCOfflinePay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCQRCodeResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class QRCodeWXPayActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private static final int REQ_QRCODE_CODE = 1;
    private static final int NOTIFY_RESULT = 10;
    private static final int ERR_CODE = 99;

    ProgressDialog loadingDialog;

    String billNum;
    String billId;
    String billId1;

    String type = "WX_NATIVE";
    BCReqParams.BCChannelTypes channelType;
    String billTitle;

    Bitmap qrCodeBitMap;
    Bitmap qrCodeBitMap1;

    String notify;
    String errMsg;

    ImageView qrcodeImg;
    ImageView qrcodeImg1;

    private String orderId;
    private boolean isSkip;
    private String servicePackageType;

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case REQ_QRCODE_CODE://1 请求二维码 成功回调
                    qrcodeImg1.setImageBitmap(qrCodeBitMap);
                    break;
                case 3:
                    qrcodeImg1.setImageBitmap(qrCodeBitMap1);
                    break;
                case NOTIFY_RESULT:
                    Toast.makeText(QRCodeWXPayActivity.this, notify, Toast.LENGTH_LONG).show();
                    break;
                case ERR_CODE:
                    Toast.makeText(QRCodeWXPayActivity.this, errMsg, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(isSkip, servicePackageType, orderId);
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
                                    Toast.makeText(QRCodeWXPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
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

    public volatile Boolean sign = true;
    public Boolean sign1 = true;
    Date date;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_wx_pay);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("套 餐 购 买");
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "请使用微信扫码进行购买");
        Intent intent = getIntent();
        number = intent.getStringExtra("number");//5000
        isSkip = intent.getBooleanExtra("isSkip", false);
        servicePackageType = intent.getStringExtra("ServicePackage");
        des = intent.getStringExtra("description");
        //对于二维码，微信使用 WX_NATIVE 作为channel参数
        //支付宝使用ALI_OFFLINE_QRCODE

        channelType = BCReqParams.BCChannelTypes.valueOf(type);
        billTitle = "杭州国辰迈联机器人科技有限公司";
        qrcodeImg = this.findViewById(R.id.qrcodeImg);
        qrcodeImg1 = this.findViewById(R.id.qrcodeImg1);
        date = new Date();
    }

    private void prepairedOrder(String des) {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("处理中，请稍候...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(true);

        new GoodsRepository().bugServicePackage(String.format("%.2f", Double.parseDouble(number) / 100.0), des)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        orderId = o + "";
                        reqQrCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepairedOrder(des);
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

    public void QueryOrder() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sign) {
                    new GoodsRepository().getOrderStarte(orderId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<Object>() {
                                @Override
                                public void onNext(Object bean) {

                                    if (bean instanceof LinkedTreeMap) {
                                        if (((LinkedTreeMap) bean).size() != 0) {
                                            Message msg = mHandler.obtainMessage();
                                            sign = false;
                                            msg.what = 2;
                                            mHandler.sendMessage(msg);
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    ToastUtils.showShort(e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                    try {
                        Thread.sleep(1500);
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
        loadingDialog.show();
        Map<String, String> optional = new HashMap<String, String>();
        optional.put("用途", "套餐购买");
        optional.put("testEN", "国辰迈联");
        optional.put("eqid", DeviceUtils.getIMEI());
        optional.put("bid", UserSpHelper.getUserId());
        optional.put("orderid", orderId);
        optional.put("meal_type", servicePackageType);

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
                    //否则通过 bcqrCodeResult.getQrCodeRawContent() 获取二维码的内容，自己去生成对应的二维码
                    msg.what = REQ_QRCODE_CODE;
                    QueryOrder();
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
//        BCCallback callback1 = new BCCallback() {
//            @Override
//            public void done(BCResult bcResult) {
//
//                //此处关闭loading界面
//                loadingDialog.dismiss();
//
//                final BCQRCodeResult bcqrCodeResult = (BCQRCodeResult) bcResult;
//
//                Message msg = mHandler.obtainMessage();
//
//                //resultCode为0表示请求成功
//                if (bcqrCodeResult.getResultCode() == 0) {
//                    billId1 = bcqrCodeResult.getId();
//
//                    //如果你设置了生成二维码参数为true那么此处可以获取二维码
//                    qrCodeBitMap1 = bcqrCodeResult.getQrCodeBitmap();
//
//                    QueryOrders();
//
//
//                    //否则通过 bcqrCodeResult.getQrCodeRawContent() 获取二维码的内容，自己去生成对应的二维码
//
//                    msg.what = 3;
//                } else {
//                    errMsg = "err code:" + bcqrCodeResult.getResultCode() + "; err msg: " + bcqrCodeResult.getResultMsg() + "; err detail: " + bcqrCodeResult.getErrDetail();
//
//                    /**
//                     * 你发布的项目中不需要做如下判断，此处由于支付宝政策原因，
//                     * 不再提供支付宝支付的测试功能，所以给出提示说明
//                     */
//                    if (BCCache.getInstance().appId.equals("c5d1cba1-5e3f-4ba0-941d-9b0a371fe719") && type.equals("ALI") && !bcqrCodeResult.getErrDetail().equals("该功能暂不支持测试模式")) {
//                        errMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
//                    }
//
//                    msg.what = ERR_CODE;
//                }
//
//                mHandler.sendMessage(msg);
//            }
//        };


        //   billNum = BillUtils.genBillNum();

        // BeeCloud微信二维码支付并不是严格意义上的线下支付
//        BCPay.getInstance(PayInfoActivity.this).reqBCNativeAsync(
//                billTitle,  //商品描述
//                Integer.parseInt(number),  //总金额, 以分为单位, 必须是正整数
//                BillUtils.genBillNum(),   //流水号
//                optional,  //扩展参数
//                true,     //是否生成二维码的bitmap
//                480,       //二维码的尺寸, 以px为单位, 如果为null则默认为360
//                callback1);

        BCOfflinePay.PayParams payParam = new BCOfflinePay.PayParams();
        payParam.channelType = BCReqParams.BCChannelTypes.WX_NATIVE;
        payParam.billTitle = billTitle; //商品描述
        payParam.billTotalFee = Integer.parseInt(number); //总金额, 以分为单位, 必须是正整数
        billNum = BillUtils.genBillNum();
        payParam.billNum = billNum;         //流水号
        payParam.optional = optional;   //扩展参数
        payParam.genQRCode = true;      //是否生成二维码的bitmap
        payParam.qrCodeWidth = 480;     //二维码的尺寸, 以px为单位, 如果为null则默认为360

        BCOfflinePay.getInstance().reqQRCodeAsync(payParam, callback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sign = false;
        sign1 = false;
        finish();
    }
}