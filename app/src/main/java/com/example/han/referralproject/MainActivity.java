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

import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo;
import com.example.han.referralproject.facerecognition.OnlineFaceDemo1;
import com.example.han.referralproject.facerecognition.VideoDemo;
import com.example.han.referralproject.login.PerInfoActivity;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
   // SharedPreferences sharedPreferences;
  //  SharedPreferences sharedPreferences1;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mImageView1 = (ImageView) findViewById(R.id.health_test);
        mImageView2 = (ImageView) findViewById(R.id.doctor_remind);
        mImageView3 = (ImageView) findViewById(R.id.person_info);
        mImageView4 = (ImageView) findViewById(R.id.face_to_face);
        mImageView5 = (ImageView) findViewById(R.id.robot_conversion);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(com.megvii.faceppidcardui.util.ConstantData.SHARED_FILE_NAME4, Context.MODE_PRIVATE);

        Log.e("============", sharedPreferences.getString("mAuthid", ""));


        //sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME, Context.MODE_PRIVATE);

     /*   SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "123");
        editor.putString("phone", "123");
        editor.commit();


        sharedPreferences1 = getSharedPreferences(ConstantData.SHARED_FILE_NAMES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.putString("name", "456");
        editor1.putString("phone", "456");
        editor1.commit();*/
        //    showNormal();

    }

    public void showNormal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.user);

        builder.setTitle("虞美人");
        builder.setMessage("雕栏玉砌应犹在，只是朱颜改。问君能有多少愁？恰似一江春水向东流。");


        builder.setNegativeButton("取消", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), i + "--setNegativeButton", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), i + "--setPositiveButton", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        builder.show();

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.health_test:
                intent.setClass(getApplicationContext(), VideoDemo.class);
                startActivity(intent);
                break;
            case R.id.doctor_remind:
                break;
            case R.id.person_info:
                intent.setClass(getApplicationContext(), PerInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.face_to_face:
                break;
            case R.id.robot_conversion:
                intent.setClass(getApplicationContext(), SpeechSynthesisActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  Log.e("=============", sharedPreferences.getString("name", ""));
        Log.e("=============", sharedPreferences.getString("phone", ""));
        Log.e("=============", sharedPreferences1.getString("name", ""));
        Log.e("=============", sharedPreferences1.getString("phone", ""));
*/

    }
}
