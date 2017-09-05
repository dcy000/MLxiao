package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;

import java.util.Timer;
import java.util.TimerTask;

public class DoctorMesActivity extends AppCompatActivity {


   // Toolbar mToolBar;
    TextView mTitleText;
    Button mButton;
    TextView mtextview;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mes);
    //    initToolBar();

        mButton = (Button) findViewById(R.id.qianyue);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

        mImageView= (ImageView) findViewById(R.id.icon_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void show() {
        Toast toast = new Toast(getApplicationContext());
        LayoutInflater inflate = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.your_custom_layout, null);
        View vs = v.findViewById(R.id.back);//找到你要设透明背景的layout 的id
        vs.getBackground().setAlpha(100);//0~255透明度值

        // 在这里初始化一下里面的文字啊什么的
        toast.setView(v);
        //   toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        showMyToast(toast, 3000);
        // toast.show();


    }


    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, Toast.LENGTH_LONG);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }

   /* private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.title_content);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    mTitleText.setText(str);
        // mTitleText.setTextSize(25);
        //    mTitleText.setGravity(Gravity.CENTER);

    }*/
}
