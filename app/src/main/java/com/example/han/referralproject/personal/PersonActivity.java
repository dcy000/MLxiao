package com.example.han.referralproject.personal;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.activity.RecordActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.User;
import com.megvii.faceppidcardui.util.ConstantData;
import com.example.han.referralproject.util.LocalShared;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    public String userId;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUser = (User) msg.obj;
                    mTextView.setText(mUser.getBname());
                    break;
            }
            super.handleMessage(msg);
        }


    };

    User mUser;

    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        userId = MyApplication.getInstance().userId;


        findViewById(R.id.btn_record).setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.per_name);

    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    post();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void post() throws Exception {
        // 创建URL对象
        URL url = new URL(ConstantData.BASE_URL + "/referralProject/UserInfoServlet");
        // 获取该URL与服务器的连接对象
        URLConnection conn = url.openConnection();
        // 设置头信息，请求头信息了解
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");

        // 设置可以操作连接的输入输出流
        conn.setDoOutput(true);// 默认为false，允许使用输出流
        conn.setDoInput(true);// 默认为true，允许使用输入流


        // 传参数
        PrintWriter pw = new PrintWriter(conn.getOutputStream());
        pw.print("datas=" + userId);
        pw.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String lineContent = null;
        String content = null;

        while ((lineContent = br.readLine()) != null) {
            content = lineContent;
            Log.e("=========", content);
        }

        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        msg.obj = content;
        mHandler.sendMessage(msg);


        pw.close();
        br.close();

        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:
                startActivity(new Intent(this, RecordActivity.class));
                break;
            case R.id.btn_logout:
                LocalShared.getInstance(this).loginOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
