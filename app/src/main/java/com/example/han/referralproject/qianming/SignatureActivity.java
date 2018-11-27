package com.example.han.referralproject.qianming;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.qianming.fragment.AffirmSignatureDialog;
import com.example.han.referralproject.qianming.wrap.PainterView;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SignatureActivity extends BaseActivity implements AffirmSignatureDialog.ClickListener {

    /**
     * 确认
     */
    private TextView tvSignatrueConfirm;
    private PainterView signature;
    private AffirmSignatureDialog signatureDialog;
    private byte[] bytes;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ActivityHelper.addActivity(this);
        initTitle();
        initView();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("签 名 确 认");
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请签名确认");
    }


    private void initView() {
        tvSignatrueConfirm = (TextView) findViewById(R.id.tv_signatrue_confirm);
        signature = (PainterView) findViewById(R.id.signature);
        cancel = findViewById(R.id.tv_signatrue_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.clear();
            }
        });
        tvSignatrueConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = signature.creatBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                bytes = stream.toByteArray();

                if (signatureDialog == null) {
                    signatureDialog = new AffirmSignatureDialog();
                }
                Bundle args = new Bundle();
                args.putByteArray("imageData", bytes);
                signatureDialog.setArguments(args);

                signatureDialog.setListener(SignatureActivity.this);
                signatureDialog.show(getFragmentManager(), "signature");

            }
        });
    }

    @Override
    public void onConfirm() {
        //取消
        signature.clear();
    }

    @Override
    public void onCancel() {
        //确认
        uploadHeadToSelf(bytes);
    }

    private UploadManager uploadManager = new UploadManager();

    private void uploadHeadToSelf(final byte[] faceData) {
        if (faceData == null) {
            return;
        }
        NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Date date = new Date();
                SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                StringBuilder str = new StringBuilder();//定义变长字符串
                Random random = new Random();
                for (int i = 0; i < 8; i++) {
                    str.append(random.nextInt(10));
                }
                //将字符串转换为数字并输出
                String key = simple.format(date) + str + ".jpg";

                uploadManager.put(faceData, key, response, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info.isOK()) {
                            String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;
                            qianyue(imageUrl);
                        }
                    }
                }, null);
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    private void qianyue(String imageUrl) {
        final Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String docId = intent.getStringExtra("docId");
        NetworkApi.bindDoctor(MyApplication.getInstance().userId, Integer.valueOf(docId.replace("null", "")), imageUrl, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                if (TextUtils.equals(intent.getStringExtra("fromBuildFile"), "is")) {
                    setResult(RESULT_OK);
                    finish();
//                    ActivityHelper.finishAll();
                } else {
                    startActivity(new Intent(SignatureActivity.this, CheckContractActivity.class));
                    ActivityHelper.finishAll();
                }
            }
        });
    }
}
