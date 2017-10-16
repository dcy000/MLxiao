package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.constant.ConstantData;
import com.medlink.danbogh.call.EMUIHelper;
import com.squareup.picasso.Picasso;

public class DoctorappoActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;


    public TextView mTextView;
    public TextView mTextView1;
    public TextView mTextView2;
    public Button mButton1;
    public ImageView circleImageView;
    public ImageView mImageView;

    public TextView mTextView3;
    public TextView mTextView4;
    public TextView mTextView5;

    public Button mButton;
    public Button mButton_1;
    public Button mButton_2;

    public LinearLayout mLinearLayout1;
    public LinearLayout mLinearLayout2;
    public LinearLayout mLinearLayout3;

    public TextView mTextView6;
    public TextView mTextView7;
    public TextView mTextView8;

    public TextView mTextView9;
    public TextView mTextView10;
    public TextView mTextView11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappo);

        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME1, Context.MODE_PRIVATE);

        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(ConstantData.SHARED_FILE_NAME2, Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences(ConstantData.SHARED_FILE_NAME3, Context.MODE_PRIVATE);


        mTextView = (TextView) findViewById(R.id.yuyue_time);
        mTextView1 = (TextView) findViewById(R.id.yuyue_time1);
        mTextView2 = (TextView) findViewById(R.id.yuyue_time2);

        mImageView = (ImageView) findViewById(R.id.icon_back);

        mTextView3 = (TextView) findViewById(R.id.docotor_name);
        mTextView4 = (TextView) findViewById(R.id.docotor_position);
        mTextView5 = (TextView) findViewById(R.id.docotor_feature);

        mTextView6 = (TextView) findViewById(R.id.yuyue_time3);
        mTextView7 = (TextView) findViewById(R.id.yuyue_time4);
        mTextView8 = (TextView) findViewById(R.id.yuyue_time5);


        mTextView9 = (TextView) findViewById(R.id.yuyue_time6);
        mTextView10 = (TextView) findViewById(R.id.yuyue_time7);
        mTextView11 = (TextView) findViewById(R.id.yuyue_time8);


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        circleImageView = (ImageView) findViewById(R.id.circleImageView1);

        Picasso.with(this)
                .load(ConstantData.BASE_URL + "/referralProject/" + sharedPreferences1.getString("image", ""))
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(circleImageView);

        mTextView3.setText(sharedPreferences1.getString("name", ""));
        mTextView4.setText("职级：" + sharedPreferences1.getString("position", ""));
        mTextView5.setText("擅长：" + sharedPreferences1.getString("feature", ""));


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
                if ("".equals(sharedPreferences.getString("month", "")) || "".equals(sharedPreferences2.getString("month", ""))
                        || "".equals(sharedPreferences3.getString("month", ""))) {

                    Intent intent = new Intent(getApplicationContext(), AddAppoActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "您预约已达上限", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mLinearLayout1 = (LinearLayout) findViewById(R.id.linearlayou7);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.linearlayou8);
        mLinearLayout3 = (LinearLayout) findViewById(R.id.linearlayou9);


        mButton = (Button) findViewById(R.id.cancel_yuyue);
        mButton_1 = (Button) findViewById(R.id.cancel_yuyue1);
        mButton_2 = (Button) findViewById(R.id.cancel_yuyue2);

        mButton.setOnClickListener(this);
        mButton_1.setOnClickListener(this);
        mButton_2.setOnClickListener(this);

        if ("".equals(sharedPreferences.getString("month", ""))) {

            mLinearLayout1.setVisibility(View.INVISIBLE);

        }
        if ("".equals(sharedPreferences2.getString("month", ""))) {

            mLinearLayout2.setVisibility(View.INVISIBLE);

        }

        if ("".equals(sharedPreferences3.getString("month", ""))) {

            mLinearLayout3.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextView.setText(sharedPreferences.getString("month", ""));
        mTextView1.setText(sharedPreferences.getString("day", ""));
        mTextView2.setText(sharedPreferences.getString("time", ""));


        mTextView6.setText(sharedPreferences2.getString("month", ""));
        mTextView7.setText(sharedPreferences2.getString("day", ""));
        mTextView8.setText(sharedPreferences2.getString("time", ""));


        mTextView9.setText(sharedPreferences3.getString("month", ""));
        mTextView10.setText(sharedPreferences3.getString("day", ""));
        mTextView11.setText(sharedPreferences3.getString("time", ""));


    }

    NDialog1 dialog;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_yuyue:

                dialog = new NDialog1(DoctorappoActivity.this);
                showNormal(1);
                break;

            case R.id.cancel_yuyue1:

                dialog = new NDialog1(DoctorappoActivity.this);
                showNormal(2);
                break;

            case R.id.cancel_yuyue2:

                dialog = new NDialog1(DoctorappoActivity.this);
                showNormal(3);
                break;
        }
    }

    public void showNormal(final int sign) {
        dialog.setMessageCenter(false)
                .setMessage("您确认要取消预约？")
                .setMessageSize(40)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            if (sign == 1) {
                                mLinearLayout1.setVisibility(View.INVISIBLE);
                                SharedPreferences.Editor editor7 = sharedPreferences.edit();
                                editor7.putString("month", "");
                                editor7.putString("day", "");
                                editor7.putString("time", "");
                                editor7.commit();
                            } else if (sign == 2) {
                                mLinearLayout2.setVisibility(View.INVISIBLE);
                                SharedPreferences.Editor editor7 = sharedPreferences2.edit();
                                editor7.putString("month", "");
                                editor7.putString("day", "");
                                editor7.putString("time", "");
                                editor7.commit();
                            } else if (sign == 3) {
                                mLinearLayout3.setVisibility(View.INVISIBLE);
                                SharedPreferences.Editor editor7 = sharedPreferences3.edit();
                                editor7.putString("month", "");
                                editor7.putString("day", "");
                                editor7.putString("time", "");
                                editor7.commit();
                            }

                        }

                    }
                }).create(NDialog.CONFIRM).show();
    }

}
