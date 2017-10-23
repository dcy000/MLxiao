package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.constant.ConstantData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class AddAppoActivity extends BaseActivity implements View.OnClickListener {

    public Button mButton;
    public Button mButton1;

    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;
    public Button mButton7;
    public Button mButton8;

    public Button mButton9;
    public Button mButton10;
    public Button mButton11;
    public Button mButton12;
    public Button mButton13;
    public Button mButton14;
    public Button mButton15;

    public Button mButton16;
    public Button mButton17;
    public Button mButton18;
    public Button mButton19;
    public Button mButton20;
    public Button mButton21;
    public Button mButton22;

    public Button mButton23;
    public Button mButton24;
    public Button mButton25;
    public Button mButton26;
    public Button mButton27;
    public Button mButton28;
    public Button mButton29;

    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;
    public TextView mTextView4;

    public LinearLayout mLinearLayout1;
    public LinearLayout mLinearLayout2;
    public LinearLayout mLinearLayout3;
    public LinearLayout mLinearLayout4;
    public LinearLayout mLinearLayout5;
    public LinearLayout mLinearLayout6;
    public LinearLayout mLinearLayout7;
    public LinearLayout mLinearLayout8;


    public Button mButton30;
    public Button mButton31;
    public Button mButton32;
    public Button mButton33;
    public Button mButton34;
    public Button mButton35;
    public Button mButton36;
    public Button mButton37;
    public Button mButton38;
    public Button mButton39;
    public Button mButton40;
    public Button mButton41;
    public Button mButton42;
    public Button mButton43;
    public Button mButton44;
    public Button mButton45;
    public Button mButton46;
    public Button mButton47;
    public Button mButton48;
    public Button mButton49;
    public Button mButton50;
    public Button mButton51;
    public Button mButton52;
    public Button mButton53;
    public Button mButton54;
    public Button mButton55;
    public Button mButton56;
    public Button mButton57;

    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
    public TextView textView4;
    public TextView mTextView5;
    public TextView mTextView6;
    public TextView mTextView7;
    public TextView mTextView8;
    public TextView mTextView9;
    public TextView mTextView10;
    public TextView mTextView11;
    public TextView mTextView12;
    public TextView mTextView13;
    public TextView mTextView14;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;

    SimpleDateFormat simple;
    SimpleDateFormat simple1;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    SimpleDateFormat formatte2;
    SimpleDateFormat formatter3;
    SimpleDateFormat formatter4;
    SimpleDateFormat formatter5;
    SimpleDateFormat formatter6;
    SimpleDateFormat formatter7;
    SimpleDateFormat formatter8;
    SimpleDateFormat formatter9;
    SimpleDateFormat formatter10;
    SimpleDateFormat formatter11;
    Date date;
    Date date1;
    Date date2;
    Date date3;
    Date date4;
    Date date5;
    Date date6;

    Button mButtons;
    NDialog2 dialog;

    ImageView mImageView;

    ImageView mCircleImageView;
    TextView TextView1;
    TextView TextView2;
    TextView TextView3;


    public void showNormal(String message) {
        dialog.setMessageCenter(false)
                .setMessage(message)
                .setMessageSize(40)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(getApplicationContext(), DoctorappoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create(NDialog.CONFIRM).show();
    }

    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appo);

        speak(R.string.yuyue);

        sharedPreferences = getSharedPreferences(ConstantData.SHARED_FILE_NAME1, Context.MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(ConstantData.SHARED_FILE_NAME2, Context.MODE_PRIVATE);
        sharedPreferences3 = getSharedPreferences(ConstantData.SHARED_FILE_NAME3, Context.MODE_PRIVATE);


        mImageView = (ImageView) findViewById(R.id.icon_back);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mButtons = (Button) findViewById(R.id.yuyue_true);

        mButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new NDialog2(AddAppoActivity.this);
                showNormal(String.format(getString(R.string.dialog), "张医生"));
            }
        });

        mButton = (Button) findViewById(R.id.add_afternoon);
        mButton1 = (Button) findViewById(R.id.add_morning);

        mButton2 = (Button) findViewById(R.id.yuyue1);
        mButton3 = (Button) findViewById(R.id.yuyue2);
        mButton4 = (Button) findViewById(R.id.yuyue3);
        mButton5 = (Button) findViewById(R.id.yuyue4);
        mButton6 = (Button) findViewById(R.id.yuyue5);
        mButton7 = (Button) findViewById(R.id.yuyue6);
        mButton8 = (Button) findViewById(R.id.yuyue7);

        mButton9 = (Button) findViewById(R.id.yuyue8);
        mButton10 = (Button) findViewById(R.id.yuyue9);
        mButton11 = (Button) findViewById(R.id.yuyue10);
        mButton12 = (Button) findViewById(R.id.yuyue11);
        mButton13 = (Button) findViewById(R.id.yuyue12);
        mButton14 = (Button) findViewById(R.id.yuyue13);
        mButton15 = (Button) findViewById(R.id.yuyue14);


        mButton16 = (Button) findViewById(R.id.yuyue15);
        mButton17 = (Button) findViewById(R.id.yuyue16);
        mButton18 = (Button) findViewById(R.id.yuyue17);
        mButton19 = (Button) findViewById(R.id.yuyue18);
        mButton20 = (Button) findViewById(R.id.yuyue19);
        mButton21 = (Button) findViewById(R.id.yuyue20);
        mButton22 = (Button) findViewById(R.id.yuyue21);

        mButton23 = (Button) findViewById(R.id.yuyue22);
        mButton24 = (Button) findViewById(R.id.yuyue23);
        mButton25 = (Button) findViewById(R.id.yuyue24);
        mButton26 = (Button) findViewById(R.id.yuyue25);
        mButton27 = (Button) findViewById(R.id.yuyue26);
        mButton28 = (Button) findViewById(R.id.yuyue27);
        mButton29 = (Button) findViewById(R.id.yuyue28);

        mButton30 = (Button) findViewById(R.id.yuyue29);
        mButton31 = (Button) findViewById(R.id.yuyue30);
        mButton32 = (Button) findViewById(R.id.yuyue31);
        mButton33 = (Button) findViewById(R.id.yuyue32);
        mButton34 = (Button) findViewById(R.id.yuyue33);
        mButton35 = (Button) findViewById(R.id.yuyue34);
        mButton36 = (Button) findViewById(R.id.yuyue35);
        mButton37 = (Button) findViewById(R.id.yuyue36);
        mButton38 = (Button) findViewById(R.id.yuyue37);
        mButton39 = (Button) findViewById(R.id.yuyue38);
        mButton40 = (Button) findViewById(R.id.yuyue39);
        mButton41 = (Button) findViewById(R.id.yuyue40);
        mButton42 = (Button) findViewById(R.id.yuyue41);
        mButton43 = (Button) findViewById(R.id.yuyue42);
        mButton44 = (Button) findViewById(R.id.yuyue43);
        mButton45 = (Button) findViewById(R.id.yuyue44);
        mButton46 = (Button) findViewById(R.id.yuyue45);
        mButton47 = (Button) findViewById(R.id.yuyue46);
        mButton48 = (Button) findViewById(R.id.yuyue47);
        mButton49 = (Button) findViewById(R.id.yuyue48);
        mButton50 = (Button) findViewById(R.id.yuyue49);
        mButton51 = (Button) findViewById(R.id.yuyue50);
        mButton52 = (Button) findViewById(R.id.yuyue51);
        mButton53 = (Button) findViewById(R.id.yuyue52);
        mButton54 = (Button) findViewById(R.id.yuyue53);
        mButton55 = (Button) findViewById(R.id.yuyue54);
        mButton56 = (Button) findViewById(R.id.yuyue55);
        mButton57 = (Button) findViewById(R.id.yuyue56);


        mTextView1 = (TextView) findViewById(R.id.time1);
        mTextView2 = (TextView) findViewById(R.id.time2);
        mTextView3 = (TextView) findViewById(R.id.time3);
        mTextView4 = (TextView) findViewById(R.id.time4);


        mLinearLayout1 = (LinearLayout) findViewById(R.id.linearlayou5);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.linearlayou6);
        mLinearLayout3 = (LinearLayout) findViewById(R.id.linearlayou7);
        mLinearLayout4 = (LinearLayout) findViewById(R.id.linearlayou8);

        mLinearLayout5 = (LinearLayout) findViewById(R.id.linearlayou9);
        mLinearLayout6 = (LinearLayout) findViewById(R.id.linearlayou10);
        mLinearLayout7 = (LinearLayout) findViewById(R.id.linearlayou11);
        mLinearLayout8 = (LinearLayout) findViewById(R.id.linearlayou12);

        textView1 = (TextView) findViewById(R.id.date1);
        textView2 = (TextView) findViewById(R.id.date2);
        textView3 = (TextView) findViewById(R.id.date3);
        textView4 = (TextView) findViewById(R.id.date4);
        mTextView5 = (TextView) findViewById(R.id.date5);
        mTextView6 = (TextView) findViewById(R.id.date6);
        mTextView7 = (TextView) findViewById(R.id.date7);
        mTextView8 = (TextView) findViewById(R.id.week1);
        mTextView9 = (TextView) findViewById(R.id.week2);
        mTextView10 = (TextView) findViewById(R.id.week3);
        mTextView11 = (TextView) findViewById(R.id.week4);
        mTextView12 = (TextView) findViewById(R.id.week5);
        mTextView13 = (TextView) findViewById(R.id.week6);
        mTextView14 = (TextView) findViewById(R.id.week7);


        date = new Date();
        simple = new SimpleDateFormat("M/d");
        simple.format(date);
        simple1 = new SimpleDateFormat("EEE");
        simple1.format(date);


        date1 = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date1 = calendar.getTime(); //这个时间就是日期往后推一天的结果
        formatter = new SimpleDateFormat("M/d");
        formatter1 = new SimpleDateFormat("EEE");
        formatter.format(date1);
        formatter1.format(date1);


        date2 = new Date();
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(date2);
        calendar1.add(calendar1.DATE, 2);//把日期往后增加一天.整数往后推,负数往前移动
        date2 = calendar1.getTime(); //这个时间就是日期往后推一天的结果
        formatte2 = new SimpleDateFormat("M/d");
        formatter3 = new SimpleDateFormat("EEE");
        formatte2.format(date2);
        formatter3.format(date2);

        date3 = new Date();
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date3);
        calendar2.add(calendar2.DATE, 3);//把日期往后增加一天.整数往后推,负数往前移动
        date3 = calendar2.getTime(); //这个时间就是日期往后推一天的结果
        formatter4 = new SimpleDateFormat("M/d");
        formatter5 = new SimpleDateFormat("EEE");
        formatter4.format(date3);
        formatter5.format(date3);


        date4 = new Date();
        Calendar calendar3 = new GregorianCalendar();
        calendar3.setTime(date4);
        calendar3.add(calendar3.DATE, 4);//把日期往后增加一天.整数往后推,负数往前移动
        date4 = calendar3.getTime(); //这个时间就是日期往后推一天的结果
        formatter6 = new SimpleDateFormat("M/d");
        formatter7 = new SimpleDateFormat("EEE");
        formatter6.format(date4);
        formatter7.format(date4);


        date5 = new Date();
        Calendar calendar4 = new GregorianCalendar();
        calendar4.setTime(date5);
        calendar4.add(calendar4.DATE, 5);//把日期往后增加一天.整数往后推,负数往前移动
        date5 = calendar4.getTime(); //这个时间就是日期往后推一天的结果
        formatter8 = new SimpleDateFormat("M/d");
        formatter9 = new SimpleDateFormat("EEE");
        formatter8.format(date5);
        formatter9.format(date5);


        date6 = new Date();
        Calendar calendar5 = new GregorianCalendar();
        calendar2.setTime(date6);
        calendar2.add(calendar5.DATE, 6);//把日期往后增加一天.整数往后推,负数往前移动
        date6 = calendar2.getTime(); //这个时间就是日期往后推一天的结果
        formatter10 = new SimpleDateFormat("M/d");
        formatter11 = new SimpleDateFormat("EEE");
        formatter10.format(date6);
        formatter11.format(date6);


        textView1.setText(simple.format(date));
        textView2.setText(formatter.format(date1));
        textView3.setText(formatte2.format(date2));
        textView4.setText(formatter4.format(date3));
        mTextView5.setText(formatter6.format(date4));
        mTextView6.setText(formatter8.format(date5));
        mTextView7.setText(formatter10.format(date6));
        mTextView8.setText(simple1.format(date));
        mTextView9.setText(formatter1.format(date1));
        mTextView10.setText(formatter3.format(date2));
        mTextView11.setText(formatter5.format(date3));
        mTextView12.setText(formatter7.format(date4));
        mTextView13.setText(formatter9.format(date5));
        mTextView14.setText(formatter11.format(date6));


        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);

        mButton9.setOnClickListener(this);
        mButton10.setOnClickListener(this);
        mButton11.setOnClickListener(this);
        mButton12.setOnClickListener(this);
        mButton13.setOnClickListener(this);
        mButton14.setOnClickListener(this);
        mButton15.setOnClickListener(this);

        mButton16.setOnClickListener(this);
        mButton17.setOnClickListener(this);
        mButton18.setOnClickListener(this);
        mButton19.setOnClickListener(this);
        mButton20.setOnClickListener(this);
        mButton21.setOnClickListener(this);
        mButton22.setOnClickListener(this);

        mButton23.setOnClickListener(this);
        mButton24.setOnClickListener(this);
        mButton25.setOnClickListener(this);
        mButton26.setOnClickListener(this);
        mButton27.setOnClickListener(this);
        mButton28.setOnClickListener(this);
        mButton29.setOnClickListener(this);

        mButton30.setOnClickListener(this);
        mButton31.setOnClickListener(this);
        mButton33.setOnClickListener(this);
        mButton34.setOnClickListener(this);
        mButton35.setOnClickListener(this);
        mButton36.setOnClickListener(this);
        mButton37.setOnClickListener(this);
        mButton38.setOnClickListener(this);
        mButton39.setOnClickListener(this);
        mButton40.setOnClickListener(this);
        mButton41.setOnClickListener(this);
        mButton42.setOnClickListener(this);
        mButton43.setOnClickListener(this);
        mButton44.setOnClickListener(this);
        mButton45.setOnClickListener(this);
        mButton46.setOnClickListener(this);
        mButton47.setOnClickListener(this);
        mButton48.setOnClickListener(this);
        mButton49.setOnClickListener(this);
        mButton50.setOnClickListener(this);
        mButton51.setOnClickListener(this);
        mButton52.setOnClickListener(this);
        mButton53.setOnClickListener(this);
        mButton54.setOnClickListener(this);
        mButton55.setOnClickListener(this);
        mButton56.setOnClickListener(this);
        mButton57.setOnClickListener(this);


        mButton1.setSelected(true);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                } else {
                    mTextView1.setText("14:30-15:30");
                    mTextView2.setText("15:50-16:20");
                    mTextView3.setText("16:40-17:30");
                    mTextView4.setText("17:50-18:30");

                    mLinearLayout1.setVisibility(View.GONE);
                    mLinearLayout2.setVisibility(View.GONE);
                    mLinearLayout3.setVisibility(View.GONE);
                    mLinearLayout4.setVisibility(View.GONE);

                    mLinearLayout5.setVisibility(View.VISIBLE);
                    mLinearLayout6.setVisibility(View.VISIBLE);
                    mLinearLayout7.setVisibility(View.VISIBLE);
                    mLinearLayout8.setVisibility(View.VISIBLE);


                    mButton1.setSelected(false);
                    v.setSelected(true);
                }
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    v.setSelected(false);
                } else {

                    mLinearLayout1.setVisibility(View.VISIBLE);
                    mLinearLayout2.setVisibility(View.VISIBLE);
                    mLinearLayout3.setVisibility(View.VISIBLE);
                    mLinearLayout4.setVisibility(View.VISIBLE);

                    mLinearLayout5.setVisibility(View.GONE);
                    mLinearLayout6.setVisibility(View.GONE);
                    mLinearLayout7.setVisibility(View.GONE);
                    mLinearLayout8.setVisibility(View.GONE);


                    mTextView1.setText("9:00-9:20");
                    mTextView2.setText("9:40-9:50");
                    mTextView3.setText("10:00-10:30");
                    mTextView4.setText("10:40-11:30");
                    mButton.setSelected(false);
                    v.setSelected(true);
                }
            }
        });

        mCircleImageView = (ImageView) findViewById(R.id.circleImageView2);
        TextView1 = (TextView) findViewById(R.id.doctor_name);
        TextView2 = (TextView) findViewById(R.id.doctor_position);
        TextView3 = (TextView) findViewById(R.id.doctor_feature);


        Picasso.with(this)
                .load(ConstantData.BASE_URL + "/referralProject/" + sharedPreferences1.getString("image", ""))
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(mCircleImageView);

        TextView1.setText("姓名：" + sharedPreferences1.getString("name", ""));
        TextView2.setText("职级：" + sharedPreferences1.getString("position", ""));
        TextView3.setText("擅长：" + sharedPreferences1.getString("feature", ""));


        mTextView = (TextView) findViewById(R.id.service_amount);
        mTextView.setText("收费标准：" + sharedPreferences1.getString("service_amount", "") + "元/分钟");


    }

    public void SharePerfence(SharedPreferences sharedPreferences, String month, String day, String time) {

        SharedPreferences.Editor editor7 = sharedPreferences.edit();

        editor7.putString("month", month);
        editor7.putString("day", day);
        editor7.putString("time", time);
        editor7.commit();

    }

    public void SharePerfence1(SharedPreferences sharedPreferences) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("month", "");
        editor.putString("day", "");
        editor.putString("time", "");
        editor.commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yuyue1:


                if (view.isSelected()) {

                    mButton2.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "上午", "9:00-9:20");


                    }
                    view.setSelected(true);
                    mButton2.setText("已预约");

                }

                break;
            case R.id.yuyue2:

                if (view.isSelected()) {


                    mButton3.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {


                        SharePerfence(sharedPreferences, formatter.format(date1), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "上午", "9:00-9:20");


                    }


                  /*  SharedPreferences.Editor editor7 = sharedPreferences.edit();

                    editor7.putString("month", formatter.format(date1));

                    editor7.putString("day", "上午");
                    editor7.putString("time", "9:00-9:20");
                    editor7.commit();*/

                    view.setSelected(true);
                    mButton3.setText("已预约");

                }
                break;
            case R.id.yuyue3:


                if (view.isSelected()) {

                    mButton4.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "上午", "9:00-9:20");

                    }


                   /* SharedPreferences.Editor editor2 = sharedPreferences.edit();
                    editor2.putString("month", formatte2.format(date2));
                    editor2.putString("day", "上午");
                    editor2.putString("time", "9:00-9:20");
                    editor2.commit();*/

                    view.setSelected(true);
                    mButton4.setText("已预约");

                }
                break;
            case R.id.yuyue4:


                if (view.isSelected()) {

                    mButton5.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "上午", "9:00-9:20");

                    }


                  /*  SharedPreferences.Editor editor3 = sharedPreferences.edit();
                    editor3.putString("month", formatter4.format(date3));
                    editor3.putString("day", "上午");
                    editor3.putString("time", "9:00-9:20");
                    editor3.commit();*/

                    view.setSelected(true);
                    mButton5.setText("已预约");

                }
                break;
            case R.id.yuyue5:


                if (view.isSelected()) {

                    mButton6.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "上午", "9:00-9:20");

                    }


                   /* SharedPreferences.Editor editor4 = sharedPreferences.edit();
                    editor4.putString("month", formatter6.format(date4));
                    editor4.putString("day", "上午");
                    editor4.putString("time", "9:00-9:20");
                    editor4.commit();*/

                    view.setSelected(true);
                    mButton6.setText("已预约");

                }
                break;
            case R.id.yuyue6:

                if (view.isSelected()) {

                    mButton7.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "上午", "9:00-9:20");

                    }


                 /*   SharedPreferences.Editor editor5 = sharedPreferences.edit();
                    editor5.putString("month", formatter8.format(date5));
                    editor5.putString("day", "上午");
                    editor5.putString("time", "9:00-9:20");
                    editor5.commit();*/

                    view.setSelected(true);
                    mButton7.setText("已预约");

                }
                break;

            case R.id.yuyue7:


                if (view.isSelected()) {

                    mButton8.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "上午", "9:00-9:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "上午", "9:00-9:20");

                    }


                /*    SharedPreferences.Editor editor6 = sharedPreferences.edit();
                    editor6.putString("month", formatter10.format(date6));
                    editor6.putString("day", "上午");
                    editor6.putString("time", "9:00-9:20");
                    editor6.commit();*/


                    view.setSelected(true);
                    mButton8.setText("已预约");

                }
                break;
            case R.id.yuyue8:


                if (view.isSelected()) {
                    mButton9.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "上午", "9:40-9:50");

                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "上午", "9:40-9:50");
                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "上午", "9:40-9:50");

                    }


                   /* SharedPreferences.Editor editor7 = sharedPreferences.edit();

                    editor7.putString("month", simple.format(date));
                    editor7.putString("day", "上午");
                    editor7.putString("time", "9:40-9:50");
                    editor7.commit();*/

                    view.setSelected(true);
                    mButton9.setText("已预约");

                }
                break;

            case R.id.yuyue9:


                if (view.isSelected()) {
                    mButton10.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "上午", "9:40-9:50");

                    }

                /*    SharedPreferences.Editor editor8 = sharedPreferences.edit();


                    editor8.putString("month", formatter.format(date1));
                    editor8.putString("day", "上午");
                    editor8.putString("time", "9:40-9:50");
                    editor8.commit();*/

                    view.setSelected(true);
                    mButton10.setText("已预约");

                }
                break;

            case R.id.yuyue10:


                if (view.isSelected()) {
                    mButton11.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "上午", "9:40-9:50");

                    }

                  /*  SharedPreferences.Editor editor9 = sharedPreferences.edit();

                    editor9.putString("month", formatte2.format(date2));
                    editor9.putString("day", "上午");
                    editor9.putString("time", "9:40-9:50");
                    editor9.commit();*/

                    view.setSelected(true);
                    mButton11.setText("已预约");

                }
                break;

            case R.id.yuyue11:


                if (view.isSelected()) {
                    mButton12.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "上午", "9:40-9:50");

                    }

                  /*  SharedPreferences.Editor editor10 = sharedPreferences.edit();


                    editor10.putString("month", formatter4.format(date3));
                    editor10.putString("day", "上午");
                    editor10.putString("time", "9:40-9:50");
                    editor10.commit();*/

                    view.setSelected(true);
                    mButton12.setText("已预约");

                }
                break;

            case R.id.yuyue12:


                if (view.isSelected()) {
                    mButton13.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "上午", "9:40-9:50");

                    }


                 /*   SharedPreferences.Editor editor11 = sharedPreferences.edit();

                    editor11.putString("month", formatter6.format(date4));
                    editor11.putString("day", "上午");
                    editor11.putString("time", "9:40-9:50");
                    editor11.commit();*/

                    view.setSelected(true);
                    mButton13.setText("已预约");

                }
                break;

            case R.id.yuyue13:

                if (view.isSelected()) {
                    mButton14.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "上午", "9:40-9:50");

                    }

                  /*  SharedPreferences.Editor editor12 = sharedPreferences.edit();

                    editor12.putString("month", formatter8.format(date5));
                    editor12.putString("day", "上午");
                    editor12.putString("time", "9:40-9:50");
                    editor12.commit();*/

                    view.setSelected(true);
                    mButton14.setText("已预约");

                }
                break;

            case R.id.yuyue14:


                if (view.isSelected()) {
                    mButton15.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "上午", "9:40-9:50");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "上午", "9:40-9:50");

                    }

                   /* SharedPreferences.Editor editor13 = sharedPreferences.edit();

                    editor13.putString("month", formatter10.format(date6));
                    editor13.putString("day", "上午");
                    editor13.putString("time", "9:40-9:50");
                    editor13.commit();*/

                    view.setSelected(true);
                    mButton15.setText("已预约");

                }
                break;

            case R.id.yuyue15:


                if (view.isSelected()) {
                    mButton16.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "上午", "10:00-10:30");

                    }


                   /* SharedPreferences.Editor editor14 = sharedPreferences.edit();

                    editor14.putString("month", simple.format(date));
                    editor14.putString("day", "上午");
                    editor14.putString("time", "10:00-10:30");
                    editor14.commit();*/

                    view.setSelected(true);
                    mButton16.setText("已预约");

                }
                break;

            case R.id.yuyue16:


                if (view.isSelected()) {
                    mButton17.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "上午", "10:00-10:30");

                    }

                   /* SharedPreferences.Editor editor15 = sharedPreferences.edit();

                    editor15.putString("month", formatter.format(date1));
                    editor15.putString("day", "上午");
                    editor15.putString("time", "10:00-10:30");
                    editor15.commit();*/

                    view.setSelected(true);
                    mButton17.setText("已预约");

                }
                break;
            case R.id.yuyue17:


                if (view.isSelected()) {
                    mButton18.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "上午", "10:00-10:30");

                    }


                  /*  SharedPreferences.Editor editor16 = sharedPreferences.edit();

                    editor16.putString("month", (formatte2.format(date2)));
                    editor16.putString("day", "上午");
                    editor16.putString("time", "10:00-10:30");
                    editor16.commit();*/

                    view.setSelected(true);
                    mButton18.setText("已预约");

                }
                break;

            case R.id.yuyue18:


                if (view.isSelected()) {
                    mButton19.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "上午", "10:00-10:30");

                    }


                   /* SharedPreferences.Editor editor17 = sharedPreferences.edit();

                    editor17.putString("month", formatter4.format(date3));
                    editor17.putString("day", "上午");
                    editor17.putString("time", "10:00-10:30");
                    editor17.commit();*/

                    view.setSelected(true);
                    mButton19.setText("已预约");

                }
                break;

            case R.id.yuyue19:


                if (view.isSelected()) {
                    mButton20.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "上午", "10:00-10:30");

                    }

                   /* SharedPreferences.Editor editor18 = sharedPreferences.edit();

                    editor18.putString("month", formatter6.format(date4));
                    editor18.putString("day", "上午");
                    editor18.putString("time", "10:00-10:30");
                    editor18.commit();*/

                    view.setSelected(true);
                    mButton20.setText("已预约");

                }
                break;

            case R.id.yuyue20:


                if (view.isSelected()) {
                    mButton21.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "上午", "10:00-10:30");

                    }


                  /*  SharedPreferences.Editor editor19 = sharedPreferences.edit();


                    editor19.putString("month", formatter8.format(date5));
                    editor19.putString("day", "上午");
                    editor19.putString("time", "10:00-10:30");
                    editor19.commit();*/

                    view.setSelected(true);
                    mButton21.setText("已预约");

                }
                break;

            case R.id.yuyue21:


                if (view.isSelected()) {
                    mButton22.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "上午", "10:00-10:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "上午", "10:00-10:30");

                    }


                   /* SharedPreferences.Editor editor20 = sharedPreferences.edit();


                    editor20.putString("month", formatter10.format(date6));
                    editor20.putString("day", "上午");
                    editor20.putString("time", "10:00-10:30");
                    editor20.commit();*/

                    view.setSelected(true);
                    mButton22.setText("已预约");

                }
                break;
            case R.id.yuyue22:


                if (view.isSelected()) {
                    mButton23.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "上午", "10:40-11:30");

                    }


                   /* SharedPreferences.Editor editor21 = sharedPreferences.edit();


                    editor21.putString("month", simple.format(date));
                    editor21.putString("day", "上午");
                    editor21.putString("time", "10:40-11:30");
                    editor21.commit();*/

                    view.setSelected(true);
                    mButton23.setText("已预约");

                }
                break;
            case R.id.yuyue23:


                if (view.isSelected()) {
                    mButton24.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "上午", "10:40-11:30");

                    }

                   /* SharedPreferences.Editor editor22 = sharedPreferences.edit();

                    editor22.putString("month", formatter.format(date1));
                    editor22.putString("day", "上午");
                    editor22.putString("time", "10:40-11:30");
                    editor22.commit();*/

                    view.setSelected(true);
                    mButton24.setText("已预约");

                }
                break;
            case R.id.yuyue24:


                if (view.isSelected()) {
                    mButton25.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "上午", "10:40-11:30");

                    }

                   /* SharedPreferences.Editor editor23 = sharedPreferences.edit();

                    editor23.putString("month", formatte2.format(date2));
                    editor23.putString("day", "上午");
                    editor23.putString("time", "10:40-11:30");
                    editor23.commit();*/

                    view.setSelected(true);
                    mButton25.setText("已预约");

                }
                break;
            case R.id.yuyue25:


                if (view.isSelected()) {
                    mButton26.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "上午", "10:40-11:30");

                    }
/*
                    SharedPreferences.Editor editor24 = sharedPreferences.edit();

                    editor24.putString("month", formatter4.format(date3));
                    editor24.putString("day", "上午");
                    editor24.putString("time", "10:40-11:30");
                    editor24.commit();*/

                    view.setSelected(true);
                    mButton26.setText("已预约");

                }
                break;
            case R.id.yuyue26:


                if (view.isSelected()) {
                    mButton27.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "上午", "10:40-11:30");

                    }

                  /*  SharedPreferences.Editor editor25 = sharedPreferences.edit();

                    editor25.putString("month", formatter6.format(date4));
                    editor25.putString("day", "上午");
                    editor25.putString("time", "10:40-11:30");
                    editor25.commit();*/

                    view.setSelected(true);
                    mButton27.setText("已预约");

                }
                break;
            case R.id.yuyue27:


                if (view.isSelected()) {
                    mButton28.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "上午", "10:40-11:30");

                    }


                  /*  SharedPreferences.Editor editor26 = sharedPreferences.edit();

                    editor26.putString("month", formatter8.format(date5));
                    editor26.putString("day", "上午");
                    editor26.putString("time", "10:40-11:30");
                    editor26.commit();*/

                    view.setSelected(true);
                    mButton28.setText("已预约");

                }
                break;
            case R.id.yuyue28:


                if (view.isSelected()) {
                    mButton29.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "上午", "10:40-11:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "上午", "10:40-11:30");

                    }


                  /*  SharedPreferences.Editor editor27 = sharedPreferences.edit();

                    editor27.putString("month", formatter10.format(date6));
                    editor27.putString("day", "上午");
                    editor27.putString("time", "10:40-11:30");
                    editor27.commit();*/

                    view.setSelected(true);
                    mButton29.setText("已预约");

                }
                break;

            case R.id.yuyue29:
                if (view.isSelected()) {
                    mButton30.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton30.setText("已预约");

                }
                break;
            case R.id.yuyue30:
                if (view.isSelected()) {
                    mButton31.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton31.setText("已预约");

                }
                break;
            case R.id.yuyue31:
                if (view.isSelected()) {
                    mButton32.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton32.setText("已预约");

                }
                break;

            case R.id.yuyue32:
                if (view.isSelected()) {
                    mButton33.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton33.setText("已预约");

                }
                break;

            case R.id.yuyue33:
                if (view.isSelected()) {
                    mButton34.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton34.setText("已预约");

                }
                break;
            case R.id.yuyue34:
                if (view.isSelected()) {
                    mButton35.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton35.setText("已预约");

                }
                break;
            case R.id.yuyue35:
                if (view.isSelected()) {
                    mButton36.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "下午", "14:30-15:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "下午", "14:30-15:30");

                    }

                    view.setSelected(true);
                    mButton.setText("已预约");

                }
                break;
            case R.id.yuyue36:
                if (view.isSelected()) {
                    mButton37.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton37.setText("已预约");

                }
                break;
            case R.id.yuyue37:
                if (view.isSelected()) {
                    mButton38.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton38.setText("已预约");

                }
                break;
            case R.id.yuyue38:
                if (view.isSelected()) {
                    mButton39.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton39.setText("已预约");

                }
                break;
            case R.id.yuyue39:
                if (view.isSelected()) {
                    mButton40.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton40.setText("已预约");

                }
                break;
            case R.id.yuyue40:
                if (view.isSelected()) {
                    mButton41.setText("未预约");
                    view.setSelected(false);
                } else {


                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton41.setText("已预约");

                }
                break;
            case R.id.yuyue41:
                if (view.isSelected()) {
                    mButton42.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton42.setText("已预约");

                }
                break;
            case R.id.yuyue42:
                if (view.isSelected()) {
                    mButton43.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "下午", "15:50-16:20");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "下午", "15:50-16:20");

                    }

                    view.setSelected(true);
                    mButton43.setText("已预约");

                }
                break;
            case R.id.yuyue43:
                if (view.isSelected()) {
                    mButton44.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton44.setText("已预约");

                }
                break;
            case R.id.yuyue44:
                if (view.isSelected()) {
                    mButton45.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton45.setText("已预约");

                }
                break;
            case R.id.yuyue45:
                if (view.isSelected()) {
                    mButton46.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton46.setText("已预约");

                }
                break;
            case R.id.yuyue46:
                if (view.isSelected()) {
                    mButton47.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton47.setText("已预约");

                }
                break;
            case R.id.yuyue47:
                if (view.isSelected()) {
                    mButton48.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton48.setText("已预约");

                }
                break;
            case R.id.yuyue48:
                if (view.isSelected()) {
                    mButton49.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton49.setText("已预约");

                }
                break;
            case R.id.yuyue49:
                if (view.isSelected()) {
                    mButton50.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "下午", "16:40-17:30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "下午", "16:40-17:30");

                    }

                    view.setSelected(true);
                    mButton50.setText("已预约");

                }
                break;
            case R.id.yuyue50:
                if (view.isSelected()) {
                    mButton51.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, simple.format(date), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, simple.format(date), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, simple.format(date), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton51.setText("已预约");

                }
                break;
            case R.id.yuyue51:
                if (view.isSelected()) {
                    mButton52.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter.format(date1), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter.format(date1), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter.format(date1), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton52.setText("已预约");

                }
                break;
            case R.id.yuyue52:
                if (view.isSelected()) {
                    mButton53.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatte2.format(date2), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatte2.format(date2), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatte2.format(date2), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton53.setText("已预约");

                }
                break;
            case R.id.yuyue53:
                if (view.isSelected()) {
                    mButton54.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter4.format(date3), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter4.format(date3), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter4.format(date3), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton54.setText("已预约");

                }
                break;
            case R.id.yuyue54:
                if (view.isSelected()) {
                    mButton55.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter6.format(date4), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter6.format(date4), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter6.format(date4), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton55.setText("已预约");

                }
                break;
            case R.id.yuyue55:
                if (view.isSelected()) {
                    mButton56.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter8.format(date5), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter8.format(date5), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter8.format(date5), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton56.setText("已预约");

                }
                break;
            case R.id.yuyue56:
                if (view.isSelected()) {
                    mButton57.setText("未预约");
                    view.setSelected(false);
                } else {

                    if ("".equals(sharedPreferences.getString("month", ""))) {

                        SharePerfence(sharedPreferences, formatter10.format(date6), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences2.getString("month", ""))) {

                        SharePerfence(sharedPreferences2, formatter10.format(date6), "下午", "17:50-18；30");


                    } else if ("".equals(sharedPreferences3.getString("month", ""))) {

                        SharePerfence(sharedPreferences3, formatter10.format(date6), "下午", "17:50-18；30");

                    }

                    view.setSelected(true);
                    mButton57.setText("已预约");

                }
                break;


        }
    }
}
