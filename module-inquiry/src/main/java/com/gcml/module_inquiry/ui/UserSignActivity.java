package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.dialog.AffirmSignatureDialog;
import com.gcml.module_inquiry.wrap.PainterView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserSignActivity extends AppCompatActivity implements AffirmSignatureDialog.ClickListener {
    private TextView tvSignatrueConfirm;
    private PainterView signature;
    private AffirmSignatureDialog signatureDialog;
    private byte[] bytes;
    private TextView cancel;
    TranslucentToolBar tb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign);
        initTitle();
        initView();
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_user_sign);
        tb.setData("确 认 签 名",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        toUserInfo();
                    }

                    private void toUserInfo() {
                        startActivity(new Intent(UserSignActivity.this, UserInfoListActivity.class));
                    }
                });
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

                signatureDialog.setListener(UserSignActivity.this);
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
//        if (faceData == null) {
//            return;
//        }


        LoadingDialog dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

      /*  NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
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
                hideLoadingDialog();

            }
        });*/

        ToastUtils.showShort("签约成功");
        finish();

    }

    private void qianyue(String imageUrl) {
        final Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String docId = intent.getStringExtra("docId");
//        NetworkApi.bindDoctor(MyApplication.getInstance().userId, Integer.valueOf(docId.replace("null", "")), imageUrl, new NetworkManager.SuccessCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                if (TextUtils.equals(intent.getStringExtra("fromBuildFile"), "is")) {
//                    setResult(RESULT_OK);
//                    finish();
////                    ActivityHelper.finishAll();
//                } else {
//                    startActivity(new Intent(SignatureActivity.this, CheckContractActivity.class));
//                    ActivityHelper.finishAll();
//                }
//            }
//        });
    }
}
