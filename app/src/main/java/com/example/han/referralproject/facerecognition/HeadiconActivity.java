package com.example.han.referralproject.facerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HeadiconActivity extends BaseActivity {

    ImageView mCircleImageView;
    Button mButton;
    Button mButton1;

    private UploadManager uploadManager;

    String imageData1;

    ImageView mImageView1;
    ImageView mImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headicon);

        Log.e("=====================", LocalShared.getInstance(getApplicationContext()).getXunfeiId());
        uploadManager = new UploadManager();

        mCircleImageView = (CircleImageView) findViewById(R.id.per_image);

        mButton = (Button) findViewById(R.id.cancel);
        mButton1 = (Button) findViewById(R.id.trues);

        mImageView1 = (ImageView) findViewById(R.id.icon_back);
        mImageView2 = (ImageView) findViewById(R.id.icon_home);

        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetworkApi.get_token(new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        getImageUrl(response);
                    }

                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {


                    }
                });


            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterVideoActivity.class);
                startActivity(intent);
                finish();
            }
        });


        imageData1 = LocalShared.getInstance(getApplicationContext()).getUserImg();

        if (imageData1 != null) {
            byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mCircleImageView.setImageBitmap(bitmap);
        }

        speak(R.string.head_icon);

    }


    public void getImageUrl(String token) {
        byte[] data = Base64.decode(imageData1.getBytes(), 1);
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        String key = simple.format(date) + str + ".jpg";

        uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                if (info.isOK()) {

                    String imageUrl = "http://oyptcv2pb.bkt.clouddn.com/" + key;

                    NetworkApi.return_imageUrl(imageUrl, MyApplication.getInstance().userId, LocalShared.getInstance(getApplicationContext()).getXunfeiId(),
                            new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    Intent intent = new Intent(getApplicationContext(), RecoDocActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {

                                }
                            });
                } else {

                }
            }
        }, null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        startListening();
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(".*(queding|wancheng|xiayibu).*")) {
            mButton1.performClick();
            return;
        }
        if (inSpell.matches(".*(quxiao|chongxin|zhongxin|zhongpai|zaipai|chongpai|zhongpai).*")) {
            mButton.performClick();
        }
    }
}
