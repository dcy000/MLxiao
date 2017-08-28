package com.example.han.referralproject.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.LoadingActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo1;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.facerecognition.VideoDemo;
import com.example.han.referralproject.imageview.CircleImageView;
import com.megvii.faceppidcardui.*;
import com.megvii.faceppidcardui.util.ConstantData;

import java.util.Calendar;

public class PerInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public CircleImageView mCircleImageView;
    public CircleImageView mCircleImageView1;
    public CircleImageView mCircleImageView2;
    public ImageView mImageView;
    public ImageView mImageView1;
    public ImageView mImageView2;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    SharedPreferences sharedPreference;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_info);
        mCircleImageView = (CircleImageView) findViewById(R.id.person_info1);
        mCircleImageView1 = (CircleImageView) findViewById(R.id.person_info2);
        mCircleImageView2 = (CircleImageView) findViewById(R.id.person_info3);
        mImageView = (ImageView) findViewById(R.id.register_new);
        mImageView1 = (ImageView) findViewById(R.id.returns);
        mImageView2 = (ImageView) findViewById(R.id.homes);

        sharedPreference = getSharedPreferences(ConstantData.SHARED_FILE_NAME4, Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(ConstantData.SHARED_FILE_NAME1, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(ConstantData.SHARED_FILE_NAME2, Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences(ConstantData.SHARED_FILE_NAME3, Context.MODE_PRIVATE);


        mCircleImageView.setOnClickListener(this);
        mCircleImageView1.setOnClickListener(this);
        mCircleImageView2.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.person_info1:
                    if (sharedPreferences1.getString("mAuthid", "").equals(sharedPreference.getString("mAuthid", ""))) {
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    } else {
                        showNormal("确定切换账户？", sharedPreferences1);
                    }

                    break;
                case R.id.person_info2:
                    if (sharedPreferences2.getString("mAuthid", "").equals(sharedPreference.getString("mAuthid", ""))) {
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    } else {
                        showNormal("确定切换账户？", sharedPreferences2);
                    }
                    break;
                case R.id.person_info3:
                    if (sharedPreferences3.getString("mAuthid", "").equals(sharedPreference.getString("mAuthid", ""))) {
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    } else {
                        showNormal("确定切换账户？", sharedPreferences3);
                    }
                    break;
                case R.id.register_new:
                    if (sharedPreferences1.getString("mAuthid", "").equals("") ||
                            sharedPreferences2.getString("mAuthid", "").equals("") ||
                            sharedPreferences3.getString("mAuthid", "").equals("")) {

                        intent.setClass(getApplicationContext(), RegisterVideoActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "注册用户已达上限", Toast.LENGTH_SHORT).show();


                    }

                    break;
                case R.id.returns:
                    finish();
                    break;
                case R.id.homes:
                    finish();
                    break;
            }
        }

    }


    public void update(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreference.edit();

        editor.putString("doctor", sharedPreferences.getString("doctor", ""));
        editor.putString("phone", sharedPreferences.getString("phone", ""));
        editor.putString("address", sharedPreferences.getString("address", ""));
        editor.putString("mAuthid", sharedPreferences.getString("mAuthid", ""));
        editor.putString("imageData", sharedPreferences.getString("imageData", ""));
        editor.putString("name", sharedPreferences.getString("name", ""));
        editor.putString("id_card_number", sharedPreferences.getString("id_card_number", ""));
        editor.putString("gender", sharedPreferences.getString("gender", ""));
        editor.putString("age", sharedPreferences.getString("age", ""));
        editor.commit();
    }

    NDialog1 dialog = new NDialog1(this);

    public void showNormal(String message, final SharedPreferences sharedPreferences) {

        dialog.setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(25)
                .setButtonCenter(false)
                .setButtonSize(25)
                .setPositiveTextColor(Color.parseColor("#0000FF"))
                .setNegativeTextColor(Color.parseColor("#0000FF"))
                .setCancleable(false)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            update(sharedPreferences);
                            dialog.create(NDialog.CONFIRM).dismiss();
                            Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).create(NDialog.CONFIRM).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

       /* SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imageData", "");
        editor.commit();*/

        String imageData1 = sharedPreferences1.getString("imageData", "");
        if (imageData1 != null) {
            byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                mCircleImageView.setImageBitmap(bitmap);
            }
        }

        String imageData2 = sharedPreferences2.getString("imageData", "");
        if (imageData2 != null) {
            byte[] bytes = Base64.decode(imageData2.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                mCircleImageView1.setImageBitmap(bitmap);
            }
        }

        String imageData3 = sharedPreferences3.getString("imageData", "");
        if (imageData3 != null) {
            byte[] bytes = Base64.decode(imageData3.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                mCircleImageView2.setImageBitmap(bitmap);
            }
        }


    }
}
