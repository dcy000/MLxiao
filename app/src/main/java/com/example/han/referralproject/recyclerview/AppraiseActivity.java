package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.imageview.CircleImageView;
import com.squareup.picasso.Picasso;

public class AppraiseActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView mImageView1;
    public ImageView mImageView2;
    public ImageView mImageView3;
    public ImageView mImageView4;
    public ImageView mImageView5;


    public Button mButton1;
    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;

    NDialog1 dialog;

    public Button mButton;

    public ImageView ImageView1;
    public ImageView ImageView2;

    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;
    public TextView mTextView4;

    SharedPreferences sharedPreferences1;

    CircleImageView mCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);

        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        mCircleImageView = (CircleImageView) findViewById(R.id.circleImageView1);
        mImageView1 = (ImageView) findViewById(R.id.star1);
        mImageView2 = (ImageView) findViewById(R.id.star2);
        mImageView3 = (ImageView) findViewById(R.id.star3);
        mImageView4 = (ImageView) findViewById(R.id.star4);
        mImageView5 = (ImageView) findViewById(R.id.star5);

        mButton = (Button) findViewById(R.id.niming_appraise);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton3 = (Button) findViewById(R.id.button3);
        mButton4 = (Button) findViewById(R.id.button4);
        mButton5 = (Button) findViewById(R.id.button5);
        mButton6 = (Button) findViewById(R.id.button6);

        mTextView1 = (TextView) findViewById(R.id.doctor_name);
        mTextView2 = (TextView) findViewById(R.id.doctor_position);
        mTextView3 = (TextView) findViewById(R.id.doctor_feature);
        mTextView4 = (TextView) findViewById(R.id.pay_standard);


        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);

        mButton.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);

        mButton = (Button) findViewById(R.id.niming_appraise);
        dialog = new NDialog1(AppraiseActivity.this);

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mTextView4.setText("收费标准：" + sharedPreferences1.getString("service_amount", "") + "元/分钟");
        mTextView1.setText(sharedPreferences1.getString("name", ""));
        mTextView2.setText("职级：" + sharedPreferences1.getString("position", ""));
        mTextView3.setText("擅长：" + sharedPreferences1.getString("feature", ""));

        if (!TextUtils.isEmpty(sharedPreferences1.getString("docter_photo", ""))) {
            Picasso.with(this)
                    .load(sharedPreferences1.getString("docter_photo", ""))
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(mCircleImageView);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.star1:

                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));


                break;
            case R.id.star2:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));

                break;
            case R.id.star3:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));


                break;
            case R.id.star4:


                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.stars));


                break;
            case R.id.star5:

                mImageView1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView3.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView4.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));
                mImageView5.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sta));


                break;

            case R.id.button1:

                if (view.isSelected()) {
                    mButton1.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton1.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button2:
                if (view.isSelected()) {
                    mButton2.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton2.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button3:
                if (view.isSelected()) {
                    mButton3.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton3.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button4:

                if (view.isSelected()) {
                    mButton4.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton4.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;
            case R.id.button5:

                if (view.isSelected()) {
                    mButton5.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton5.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;

            case R.id.button6:

                if (view.isSelected()) {
                    mButton6.setTextColor(Color.parseColor("#000000"));
                    view.setSelected(false);
                } else {
                    mButton6.setTextColor(Color.parseColor("#FFFFFF"));

                    view.setSelected(true);
                }

                break;

            case R.id.niming_appraise:


                showNormal();

                break;

        }

    }


    public void showNormal() {
        dialog.setMessageCenter(false)
                .setMessage("您确认要进行评价？")
                .setMessageSize(40)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(getApplicationContext(), DoctorappoActivity.class);
                        startActivity(intent);
                        finish();


                    }
                }).create(NDialog.CONFIRM).show();
    }


}
