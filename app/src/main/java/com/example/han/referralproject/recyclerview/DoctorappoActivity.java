package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.medlink.danbogh.call.EMUIHelper;
import com.megvii.faceppidcardui.util.ConstantData;

public class DoctorappoActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public TextView mTextView;
    public TextView mTextView1;
    public TextView mTextView2;
    public Button mButton1;
    public ImageView circleImageView;
    public ImageView mImageView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappo);

        mTextView = (TextView) findViewById(R.id.yuyue_time);
        mTextView1 = (TextView) findViewById(R.id.yuyue_time1);
        mTextView2 = (TextView) findViewById(R.id.yuyue_time2);

        mImageView = (ImageView) findViewById(R.id.icon_back);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        circleImageView = (ImageView) findViewById(R.id.circleImageView);

        circleImageView = (ImageView) findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMUIHelper.callVideo(MyApplication.getInstance(), MyApplication.getInstance().emDoctorId);
                finish();
            }
        });

        mButton1 = (Button) findViewById(R.id.add_yuyue);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAppoActivity.class);
                startActivity(intent);

            }
        });


        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME5, Context.MODE_PRIVATE);


    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextView.setText(sharedPreferences.getString("month", ""));
        mTextView1.setText(sharedPreferences.getString("day", ""));
        mTextView2.setText(sharedPreferences.getString("time", ""));

    }
}
