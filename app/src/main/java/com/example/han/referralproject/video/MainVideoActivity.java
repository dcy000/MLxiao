package com.example.han.referralproject.video;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

import java.io.File;

public class MainVideoActivity extends BaseActivity implements View.OnClickListener {


    Button mButton;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    LinearLayout mLinearLayout;
    LinearLayout mLinearLayout1;
    LinearLayout mLinearLayout2;
    LinearLayout mLinearLayout3;
    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    ImageView mImageView6;
    ImageView mImageView7;
    ImageView mImageView8;
    ImageView mImageView9;
    ImageView mImageView10;
    ImageView mImageView11;
    ImageView mImageView12;
    ImageView mImageView13;
    ImageView mImageView14;
    ImageView mImageView15;
    ImageView mImageView16;
    ImageView mImageView17;
    ImageView mImageView18;
    ImageView mImageView19;
    ImageView mImageView20;
    ImageView mImageView21;
    ImageView mImageView22;
    ImageView mImageView23;
    ImageView mImageView24;
    ImageView mImageView25;
    ImageView mImageView26;
    ImageView mImageView27;
    ImageView mImageView28;
    ImageView mImageView29;
    ImageView mImageView30;
    ImageView mImageView31;
    //  Toolbar mToolBar;
    TextView mTitleText;
    public ImageView mImageView;

    public ImageView ImageView1;
    public ImageView ImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);

        mToolbar.setVisibility(View.GONE);

        //initToolBar();

        ImageView1 = (ImageView) findViewById(R.id.icon_back);
        ImageView2 = (ImageView) findViewById(R.id.icon_home);

        ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WifiConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });


       /* mImageView = (ImageView) findViewById(R.id.icon_back);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        mButton = (Button) findViewById(R.id.pressure);
        mButton1 = (Button) findViewById(R.id.xue_tang);
        mButton2 = (Button) findViewById(R.id.zhong_feng);
        mButton3 = (Button) findViewById(R.id.nao_tan);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout1);
        mLinearLayout1 = (LinearLayout) findViewById(R.id.linearlayout2);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.linearlayout3);
        mLinearLayout3 = (LinearLayout) findViewById(R.id.linearlayout4);

        mImageView = (ImageView) findViewById(R.id.image1);
        mImageView1 = (ImageView) findViewById(R.id.image2);
        mImageView2 = (ImageView) findViewById(R.id.image3);
        mImageView3 = (ImageView) findViewById(R.id.image4);
        mImageView4 = (ImageView) findViewById(R.id.image5);
        mImageView5 = (ImageView) findViewById(R.id.image6);
        mImageView6 = (ImageView) findViewById(R.id.image7);
        mImageView7 = (ImageView) findViewById(R.id.image8);
        mImageView8 = (ImageView) findViewById(R.id.image9);
        mImageView9 = (ImageView) findViewById(R.id.image10);
        mImageView10 = (ImageView) findViewById(R.id.image11);
        mImageView11 = (ImageView) findViewById(R.id.image12);
        mImageView12 = (ImageView) findViewById(R.id.image13);
        mImageView13 = (ImageView) findViewById(R.id.image14);
        mImageView14 = (ImageView) findViewById(R.id.image15);
        mImageView15 = (ImageView) findViewById(R.id.image16);
        mImageView16 = (ImageView) findViewById(R.id.image17);
        mImageView17 = (ImageView) findViewById(R.id.image18);
        mImageView18 = (ImageView) findViewById(R.id.image19);
        mImageView19 = (ImageView) findViewById(R.id.image20);
        mImageView20 = (ImageView) findViewById(R.id.image21);
        mImageView21 = (ImageView) findViewById(R.id.image22);
        mImageView22 = (ImageView) findViewById(R.id.image23);
        mImageView23 = (ImageView) findViewById(R.id.image24);
        mImageView24 = (ImageView) findViewById(R.id.image25);
        mImageView25 = (ImageView) findViewById(R.id.image26);
        mImageView26 = (ImageView) findViewById(R.id.image27);
        mImageView27 = (ImageView) findViewById(R.id.image28);
        mImageView28 = (ImageView) findViewById(R.id.image29);
        mImageView29 = (ImageView) findViewById(R.id.image30);
        mImageView30 = (ImageView) findViewById(R.id.image31);
        mImageView31 = (ImageView) findViewById(R.id.image32);

        mImageView.setOnClickListener(this);
        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);
        mImageView7.setOnClickListener(this);
        mImageView8.setOnClickListener(this);
        mImageView9.setOnClickListener(this);
        mImageView10.setOnClickListener(this);
        mImageView11.setOnClickListener(this);
        mImageView12.setOnClickListener(this);
        mImageView13.setOnClickListener(this);
        mImageView14.setOnClickListener(this);
        mImageView15.setOnClickListener(this);
        mImageView16.setOnClickListener(this);
        mImageView17.setOnClickListener(this);
        mImageView18.setOnClickListener(this);
        mImageView19.setOnClickListener(this);
        mImageView20.setOnClickListener(this);
        mImageView21.setOnClickListener(this);
        mImageView22.setOnClickListener(this);
        mImageView23.setOnClickListener(this);
        mImageView24.setOnClickListener(this);
        mImageView25.setOnClickListener(this);
        mImageView26.setOnClickListener(this);
        mImageView27.setOnClickListener(this);
        mImageView28.setOnClickListener(this);
        mImageView29.setOnClickListener(this);
        mImageView30.setOnClickListener(this);
        mImageView31.setOnClickListener(this);


        mButton.setSelected(true);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    mButton.setTextColor(Color.parseColor("#00CD79"));
                    v.setSelected(false);

                } else {
                    mButton.setTextColor(Color.parseColor("#FFFFFF"));
                    mButton1.setTextColor(Color.parseColor("#FD695D"));
                    mButton2.setTextColor(Color.parseColor("#FEB70F"));
                    mButton3.setTextColor(Color.parseColor("#5599FC"));
                    mButton1.setSelected(false);
                    mButton2.setSelected(false);
                    mButton3.setSelected(false);
                    v.setSelected(true);
                }

                mLinearLayout.setVisibility(View.VISIBLE);
                mLinearLayout1.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.GONE);

            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    mButton1.setTextColor(Color.parseColor("#FD695D"));
                    v.setSelected(false);


                } else {
                    mButton.setTextColor(Color.parseColor("#00CD79"));
                    mButton2.setTextColor(Color.parseColor("#FEB70F"));
                    mButton3.setTextColor(Color.parseColor("#5599FC"));
                    mButton1.setTextColor(Color.parseColor("#FFFFFF"));
                    mButton.setSelected(false);
                    mButton2.setSelected(false);
                    mButton3.setSelected(false);
                    v.setSelected(true);
                }

                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.GONE);

            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    mButton2.setTextColor(Color.parseColor("#FEB70F"));

                    v.setSelected(false);
                } else {
                    mButton2.setTextColor(Color.parseColor("#FFFFFF"));
                    mButton.setTextColor(Color.parseColor("#00CD79"));
                    mButton1.setTextColor(Color.parseColor("#FD695D"));
                    mButton3.setTextColor(Color.parseColor("#5599FC"));

                    mButton.setSelected(false);
                    mButton1.setSelected(false);
                    mButton3.setSelected(false);
                    v.setSelected(true);
                }

                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout1.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                mLinearLayout3.setVisibility(View.GONE);

            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    mButton3.setTextColor(Color.parseColor("#5599FC"));
                    v.setSelected(false);
                } else {
                    mButton.setTextColor(Color.parseColor("#00CD79"));
                    mButton1.setTextColor(Color.parseColor("#FD695D"));
                    mButton2.setTextColor(Color.parseColor("#FEB70F"));
                    mButton3.setTextColor(Color.parseColor("#FFFFFF"));
                    mButton.setSelected(false);
                    mButton1.setSelected(false);
                    mButton2.setSelected(false);
                    v.setSelected(true);
                }

                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout1.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getApplicationContext(), PlayVideoActivity.class);
        switch (v.getId()) {
            case R.id.image1:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure1.mp4");
                break;
            case R.id.image2:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure2.mp4");

                break;
            case R.id.image3:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure3.mp4");

                break;
            case R.id.image4:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure4.mp4");

                break;
            case R.id.image5:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure5.mp4");

                break;
            case R.id.image6:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure6.mp4");

                break;
            case R.id.image7:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure7.mp4");

                break;
            case R.id.image8:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure8.mp4");

                break;
            case R.id.image9:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin1.mp4");

                break;
            case R.id.image10:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin2.mp4");

                break;
            case R.id.image11:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin3.mp4");

                break;
            case R.id.image12:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin4.mp4");

                break;
            case R.id.image13:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin5.mp4");

                break;
            case R.id.image14:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin6.mp4");

                break;
            case R.id.image15:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin7.mp4");

                break;
            case R.id.image16:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/jinshengbin8.mp4");

                break;
            case R.id.image17:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure5.mp4");

                break;
            case R.id.image18:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure6.mp4");

                break;
            case R.id.image19:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure7.mp4");

                break;
            case R.id.image20:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure8.mp4");

                break;
            case R.id.image21:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/xuetang1.mp4");

                break;
            case R.id.image22:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/xuetang2.mp4");

                break;
            case R.id.image23:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/xuetang3.mp4");

                break;
            case R.id.image24:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure1.mp4");

                break;
            case R.id.image25:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure2.mp4");

                break;
            case R.id.image26:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure3.mp4");

                break;
            case R.id.image27:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure4.mp4");

                break;
            case R.id.image28:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure5.mp4");

                break;
            case R.id.image29:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure6.mp4");

                break;
            case R.id.image30:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure7.mp4");

                break;
            case R.id.image31:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure8.mp4");

                break;
            case R.id.image32:
                intent.putExtra("url", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.example.han.referralproject" + "/pressure1.mp4");

                break;

        }

        startActivity(intent);

    }


    /*private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (TextView) findViewById(R.id.title_content);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        // 给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //    mTitleText.setText(str);
        mTitleText.setTextSize(25);
        //    mTitleText.setGravity(Gravity.CENTER);

    }*/

    // 处理点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MainVideoActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
