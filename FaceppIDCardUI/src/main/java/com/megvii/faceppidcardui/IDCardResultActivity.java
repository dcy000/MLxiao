package com.megvii.faceppidcardui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.faceppidcardui.util.ConUtil;
import com.megvii.faceppidcardui.util.ConstantData;
import com.megvii.faceppidcardui.util.Util;
import com.megvii.faceppidcardui.view.MyView;
import com.megvii.idcard.sdk.IDCard.IDCardQuality;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class IDCardResultActivity extends Activity {
    private IDCardQuality iCardQuality;
    private Bitmap bitmap;
    private ProgressBar mBar;
    private TextView contentText, debugText;
    float clear;
    float is_idcard;
    float in_bound;
    SharedPreferences sharedPreferences;

    String address;
    String birthday;
    String gender;
    String id_card_number;
    String name;
    String race;
    String side;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "识别成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME, Context.MODE_PRIVATE);

        init();
    }

    void init() {
        clear = getIntent().getFloatExtra("clear", 0.9f);
        is_idcard = getIntent().getFloatExtra("is_idcard", 0.9f);
        in_bound = getIntent().getFloatExtra("in_bound", 0.9f);
        iCardQuality = (IDCardQuality) getIntent().getSerializableExtra("iCardQuality");
        String path = getIntent().getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);

        mBar = (ProgressBar) findViewById(R.id.result_bar);
        contentText = (TextView) findViewById(R.id.result_idcard_contentText);
        debugText = (TextView) findViewById(R.id.result_idcard_debugText);
        debugText.setText("clear: " + clear + ", is_idcard: " + is_idcard + ", in_bound: " + in_bound);
        MyView myView = (MyView) findViewById(R.id.result_myimage);
        myView.setiCardQuality(bitmap, iCardQuality);
        doOCR(path);
    }


    /**
     * 对身份证照片做ocr，然后发现是正面照片，那么利用 face/extract 接口进行人脸检测，如果是背面，直接弹出对话框
     */
    public void doOCR(final String path) {
        mBar.setVisibility(View.VISIBLE);
        try {
            String url = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
            RequestParams rParams = new RequestParams();
            Log.w("ceshi", "Util.API_OCRKEY===" + Util.API_SECRET + ", Util.API_OCRSECRET===" + Util.API_SECRET);
            rParams.put("api_key", Util.API_KEY);
            rParams.put("api_secret", Util.API_SECRET);
            rParams.put("image_file", new File(path));
            rParams.put("legality", 1 + "");
            AsyncHttpClient asyncHttpclient = new AsyncHttpClient();
            asyncHttpclient.post(url, rParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseByte) {
                    mBar.setVisibility(View.GONE);
                    try {
                        String successStr = new String(responseByte);
                        Log.w("ceshi", "ocr  onSuccess: " + successStr);
                        String info = "";
                        JSONObject jObject = new JSONObject(successStr).getJSONArray("cards").getJSONObject(0);
                        if ("back".equals(jObject.getString("side"))) {
                            String officeAdress = jObject.getString("issued_by");
                            String useful_life = jObject.getString("valid_date");
                            info = info + "officeAdress:  " + officeAdress + "\nuseful_life:  " + useful_life;
                        } else {
                            address = jObject.getString("address");
                            birthday = jObject.getString("birthday");
                            gender = jObject.getString("gender");
                            id_card_number = jObject.getString("id_card_number");
                            name = jObject.getString("name");
                            race = jObject.getString("race");
                            side = jObject.getString("side");
                            JSONObject legalityObject = jObject.getJSONObject("legality");
                            info = info + "name:  " + name + "\nid_card_number:  " + id_card_number + "\ngender:  " + gender + "\nrace:  " + race + "\nbirthday:  " + birthday + "\naddress:  " + address;
                            //   String checkError = "\n";
                            try {
                                float edited = Float.parseFloat(legalityObject.getString("Edited"));
                                float ID_Photo = Float.parseFloat(legalityObject.getString("ID Photo"));
                                float Photocopy = Float.parseFloat(legalityObject.getString("Photocopy"));
                                float Screen = Float.parseFloat(legalityObject.getString("Screen"));
                                float Temporary_ID_Photo = Float.parseFloat(legalityObject.getString("Temporary ID Photo"));
                                //      checkError = checkError + "\nedited:  " + edited + "\nID_Photo:  " + ID_Photo + "\nPhotocopy:  " + Photocopy + "\nScreen:  " + Screen + "\nTemporary_ID_Photo:  " + Temporary_ID_Photo;
                            } catch (Exception e) {
                            }
                            info = info;

                        }
                        contentText.setText(contentText.getText().toString() + info);

                       /* new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    post(name, id_card_number, gender);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();*/

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", name);
                        editor.putString("id_card_number", id_card_number);
                        editor.putString("gender", gender);
                        editor.putString("birthday", birthday);
                        editor.commit();
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        Log.w("ceshi", "responseBody===" + new String(responseBody));
                    }
                    mBar.setVisibility(View.GONE);
                    ConUtil.showToast(IDCardResultActivity.this, "识别失败，请重新识别！");
                }
            });
        } catch (FileNotFoundException e1) {
            mBar.setVisibility(View.GONE);
            e1.printStackTrace();
            ConUtil.showToast(IDCardResultActivity.this, "识别失败，请重新识别！");
        }
    }


}