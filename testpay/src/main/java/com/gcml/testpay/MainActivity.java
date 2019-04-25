package com.gcml.testpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtUserId;
    private EditText mEtAmount;
    private EditText mEtDesc;
    private EditText mEtStoreId;
    private Button mBtnPay;
    private Button mBtnPayBack;
    private TextView mTvCallback;
    private ImageView mIvQrCode;
    private boolean isPaySuccess = false;
    private PayCallBackBean payCallBackBean;
    private static final String KEY_OUT_TRADE_NO = "key_out_trade_no";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        OkGo.getInstance().init(getApplication());
        SPUtil.init(getApplication());
    }

    private void initView() {
        mEtUserId = (EditText) findViewById(R.id.et_userId);
        mEtAmount = (EditText) findViewById(R.id.et_amount);
        mEtDesc = (EditText) findViewById(R.id.et_desc);
        mEtStoreId = (EditText) findViewById(R.id.et_storeId);
        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
        mBtnPayBack = (Button) findViewById(R.id.btn_pay_back);
        mBtnPayBack.setOnClickListener(this);
        mTvCallback = (TextView) findViewById(R.id.tv_callback);
        mIvQrCode = (ImageView) findViewById(R.id.iv_qr_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_pay:
                pay();
                break;
            case R.id.btn_pay_back:
                paycallback();
                break;
        }
    }

    private void paycallback() {
        final String out_trade_no = (String) SPUtil.get(KEY_OUT_TRADE_NO, "");
        if (TextUtils.isEmpty(out_trade_no)) {
            Toast.makeText(this, "订单号异常", Toast.LENGTH_SHORT).show();
            return;
        }
        OkGo.<String>get("http://192.168.200.235:8080/mybatisPlus/Alipay/get")
                .params("no", out_trade_no)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        GoodsBean goodsBean = new Gson().fromJson(body, GoodsBean.class);
                        OkGo.<String>post("http://192.168.200.235:8080/mybatisPlus/Alipay/aliPayRefund")
                                .params("out_trade_no", out_trade_no)
                                .params("trade_no", goodsBean.getTrade_no())
                                .params("refund_amount", goodsBean.getBuyer_pay_amount())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Toast.makeText(MainActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        super.onError(response);
                                        Toast.makeText(MainActivity.this, "退款失败：" + response.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void pay() {
        OkGo.<String>get("http://192.168.200.235:8080/mybatisPlus/Alipay/pay2")
                .params("userid", mEtUserId.getText().toString().trim())
                .params("totalAmount", mEtAmount.getText().toString().trim())
                .params("subject", mEtDesc.getText().toString().trim())
                .params(" storeId", mEtStoreId.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mTvCallback.append(response.body());
                        payCallBackBean = new Gson().fromJson(response.body(), PayCallBackBean.class);
                        SPUtil.put(KEY_OUT_TRADE_NO, payCallBackBean.getDate().getAlipay_trade_precreate_response().getOut_trade_no());
                        String qr_code = payCallBackBean.getDate().getAlipay_trade_precreate_response().getQr_code();
                        QRCodeUtils.createQRCode(qr_code, 600, 600, mIvQrCode);
                    }
                });
    }

}
