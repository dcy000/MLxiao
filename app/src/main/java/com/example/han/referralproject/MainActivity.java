package com.example.han.referralproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo1;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.facerecognition.VideoDemo;
import com.example.han.referralproject.login.PerInfoActivity;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.example.han.referralproject.video.MainVideoActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView1 = (ImageView) findViewById(R.id.robot_con);
        mImageView2 = (ImageView) findViewById(R.id.person_info);
        mImageView3 = (ImageView) findViewById(R.id.health_test);
        mImageView4 = (ImageView) findViewById(R.id.doctor_ask);
        mImageView5 = (ImageView) findViewById(R.id.health_class);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.robot_con:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
            case R.id.person_info:
                intent.setClass(getApplicationContext(), PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.health_test:
                intent.setClass(getApplicationContext(), VideoDemo.class);
                startActivity(intent);
                break;
            case R.id.doctor_ask:
                intent.setClass(getApplicationContext(), MainVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.health_class:
                intent.setClass(getApplicationContext(), MainVideoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
