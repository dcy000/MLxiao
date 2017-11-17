package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    SharedPreferences sharedPreferences1;


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

    NDialog1 dialog;
    NDialog2 dialog1;

    ImageView mImageView;

    ImageView mCircleImageView;
    TextView TextView1;
    TextView TextView2;
    TextView TextView3;
    //SharedPreferences sharedPreference;


    public void showNormal(final Button button, String str1, String str2, String str3) {

        String[] str = str1.split("/");
        String[] strs = str3.split("-");

        final String start_time = years.format(date) + "-" + str[0] + "-" + str[1] + " " + strs[0];
        final String end_time = years.format(date) + "-" + str[0] + "-" + str[1] + " " + strs[1];

        speak(String.format(getString(R.string.dialog), str[0], str[1], str2, str3, sharedPreferences1.getString("name", "")));

        dialog = new NDialog1(AddAppoActivity.this);


        dialog.setMessageCenter(false)
                .setMessage(String.format(getString(R.string.dialog), str[0], str[1], str2, str3, sharedPreferences1.getString("name", "")))
                .setMessageSize(40)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {

                            NetworkApi.YuYue(start_time, end_time, MyApplication.getInstance().userId, sharedPreferences1.getString("doctor_id", ""), new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    //sharedPreference.getString("doctor_id", "")

                                    //   SharePerfence(sharedPreferences, str1, str2, str3);

                                    ShowNormals("预约成功");


                                }
                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {
                                    ShowNormals("预约失败");

                                }
                            });


                        } else if (which == 0) {
                            button.setText("未预约");
                            button.setSelected(false);

                            dialog.create(NDialog.CONFIRM).cancel();
                            dialog = null;
                        }

                    }
                })
                .create(NDialog.CONFIRM).show();
    }

    public void ShowNormals(String str) {
        dialog1.setMessageCenter(true)
                .setMessage(str)
                .setMessageSize(40)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {

                            Intent intent = new Intent(getApplicationContext(), DoctorappoActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                }).create(NDialog.CONFIRM).show();

    }


    public TextView mTextView;

    public ImageView ImageView1;
    public ImageView ImageView2;

    SimpleDateFormat years;

    List<AlreadyYuyue> list = new ArrayList<AlreadyYuyue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appo);

        dialog1 = new NDialog2(AddAppoActivity.this);


        //   speak(R.string.yuyue);

        //sharedPreference = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);


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


        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);


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


        years = new SimpleDateFormat("y");


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
                    mTextView1.setText("14:00-14:15");
                    mTextView2.setText("14:30-14:45");
                    mTextView3.setText("15:00-15:15");
                    mTextView4.setText("15:30-15:45");

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


                    mTextView1.setText("9:00-9:15");
                    mTextView2.setText("9:30-9:45");
                    mTextView3.setText("10:00-10:15");
                    mTextView4.setText("10:30-10:45");
                    mButton.setSelected(false);
                    v.setSelected(true);
                }
            }
        });


        NetworkApi.YuYue_already(sharedPreferences1.getString("doctor_id", ""), new NetworkManager.SuccessCallback<ArrayList<AlreadyYuyue>>() {
            @Override
            public void onSuccess(ArrayList<AlreadyYuyue> response) {

                list = response;


                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "09:00:00"))) {
                        mButton2.setEnabled(false);
                        mButton2.setSelected(true);
                        mButton2.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "09:00:00"))) {
                        mButton3.setEnabled(false);
                        mButton3.setSelected(true);
                        mButton3.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "09:00:00"))) {
                        mButton4.setEnabled(false);
                        mButton4.setSelected(true);
                        mButton4.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "09:00:00"))) {
                        mButton5.setEnabled(false);
                        mButton5.setSelected(true);
                        mButton5.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "09:00:00"))) {
                        mButton6.setEnabled(false);
                        mButton6.setSelected(true);
                        mButton6.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "09:00:00"))) {
                        mButton7.setEnabled(false);
                        mButton7.setSelected(true);
                        mButton7.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "09:00:00"))) {
                        mButton8.setEnabled(false);
                        mButton8.setSelected(true);
                        mButton8.setText("已预约");
                    }
                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "09:30:00"))) {
                        mButton9.setEnabled(false);
                        mButton9.setSelected(true);
                        mButton9.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "09:30:00"))) {
                        mButton10.setEnabled(false);
                        mButton10.setSelected(true);
                        mButton10.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "09:30:00"))) {
                        mButton11.setEnabled(false);
                        mButton11.setSelected(true);
                        mButton11.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "09:30:00"))) {
                        mButton12.setEnabled(false);
                        mButton12.setSelected(true);
                        mButton12.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "09:30:00"))) {
                        mButton13.setEnabled(false);
                        mButton13.setSelected(true);
                        mButton13.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "09:30:00"))) {
                        mButton14.setEnabled(false);
                        mButton14.setSelected(true);
                        mButton14.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "09:30:00"))) {
                        mButton15.setEnabled(false);
                        mButton15.setSelected(true);
                        mButton15.setText("已预约");
                    }


                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "10:00:00"))) {
                        mButton16.setEnabled(false);
                        mButton16.setSelected(true);
                        mButton16.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "10:00:00"))) {
                        mButton17.setEnabled(false);
                        mButton17.setSelected(true);
                        mButton17.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "10:00:00"))) {
                        mButton18.setEnabled(false);
                        mButton18.setSelected(true);
                        mButton18.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "10:00:00"))) {
                        mButton19.setEnabled(false);
                        mButton19.setSelected(true);
                        mButton19.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "10:00:00"))) {
                        mButton20.setEnabled(false);
                        mButton20.setSelected(true);
                        mButton20.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "10:00:00"))) {
                        mButton21.setEnabled(false);
                        mButton21.setSelected(true);
                        mButton21.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "10:00:00"))) {
                        mButton22.setEnabled(false);
                        mButton22.setSelected(true);
                        mButton22.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "10:30:00"))) {
                        mButton23.setEnabled(false);
                        mButton23.setSelected(true);
                        mButton23.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "10:30:00"))) {
                        mButton24.setEnabled(false);
                        mButton24.setSelected(true);
                        mButton24.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "10:30:00"))) {
                        mButton25.setEnabled(false);
                        mButton25.setSelected(true);
                        mButton25.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "10:30:00"))) {
                        mButton26.setEnabled(false);
                        mButton26.setSelected(true);
                        mButton26.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "10:30:00"))) {
                        mButton27.setEnabled(false);
                        mButton27.setSelected(true);
                        mButton27.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "10:30:00"))) {
                        mButton28.setEnabled(false);
                        mButton28.setSelected(true);
                        mButton28.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "10:30:00"))) {
                        mButton29.setEnabled(false);
                        mButton29.setSelected(true);
                        mButton29.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "14:00:00"))) {
                        mButton30.setEnabled(false);
                        mButton30.setSelected(true);
                        mButton30.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "14:00:00"))) {
                        mButton31.setEnabled(false);
                        mButton31.setSelected(true);
                        mButton31.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "14:00:00"))) {
                        mButton32.setEnabled(false);
                        mButton32.setSelected(true);
                        mButton32.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "14:00:00"))) {
                        mButton33.setEnabled(false);
                        mButton33.setSelected(true);
                        mButton33.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "14:00:00"))) {
                        mButton34.setEnabled(false);
                        mButton34.setSelected(true);
                        mButton34.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "14:00:00"))) {
                        mButton35.setEnabled(false);
                        mButton35.setSelected(true);
                        mButton35.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "14:00:00"))) {
                        mButton36.setEnabled(false);
                        mButton36.setSelected(true);
                        mButton36.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", " 14:30:00"))) {
                        mButton37.setEnabled(false);
                        mButton37.setSelected(true);
                        mButton37.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "14:30:00"))) {
                        mButton38.setEnabled(false);
                        mButton38.setSelected(true);
                        mButton38.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "14:30:00"))) {
                        mButton39.setEnabled(false);
                        mButton39.setSelected(true);
                        mButton39.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "14:30:00"))) {
                        mButton40.setEnabled(false);
                        mButton40.setSelected(true);
                        mButton40.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "14:30:00"))) {
                        mButton41.setEnabled(false);
                        mButton41.setSelected(true);
                        mButton41.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "14:30:00"))) {
                        mButton42.setEnabled(false);
                        mButton42.setSelected(true);
                        mButton42.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "14:30:00"))) {
                        mButton43.setEnabled(false);
                        mButton43.setSelected(true);
                        mButton43.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "15:00:00"))) {
                        mButton44.setEnabled(false);
                        mButton44.setSelected(true);
                        mButton44.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "15:00:00"))) {
                        mButton45.setEnabled(false);
                        mButton45.setSelected(true);
                        mButton45.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "15:00:00"))) {
                        mButton46.setEnabled(false);
                        mButton46.setSelected(true);
                        mButton46.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "15:00:00"))) {
                        mButton47.setEnabled(false);
                        mButton47.setSelected(true);
                        mButton47.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "15:00:00"))) {
                        mButton48.setEnabled(false);
                        mButton48.setSelected(true);
                        mButton48.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "15:00:00"))) {
                        mButton49.setEnabled(false);
                        mButton49.setSelected(true);
                        mButton49.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "15:00:00"))) {
                        mButton50.setEnabled(false);
                        mButton50.setSelected(true);
                        mButton50.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "15:30:00"))) {
                        mButton51.setEnabled(false);
                        mButton51.setSelected(true);
                        mButton51.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "15:30:00"))) {
                        mButton52.setEnabled(false);
                        mButton52.setSelected(true);
                        mButton52.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "15:30:00"))) {
                        mButton53.setEnabled(false);
                        mButton53.setSelected(true);
                        mButton53.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "15:30:00"))) {
                        mButton54.setEnabled(false);
                        mButton54.setSelected(true);
                        mButton54.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "15:30:00"))) {
                        mButton55.setEnabled(false);
                        mButton55.setSelected(true);
                        mButton55.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "15:30:00"))) {
                        mButton56.setEnabled(false);
                        mButton56.setSelected(true);
                        mButton56.setText("已预约");
                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "15:30:00"))) {
                        mButton57.setEnabled(false);
                        mButton57.setSelected(true);
                        mButton57.setText("已预约");
                    }

                }

                // sharedPreferences1.getString("doctor_id", "")
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });


        mCircleImageView = (ImageView) findViewById(R.id.circleImageView2);
        TextView1 = (TextView) findViewById(R.id.doctor_name);
        TextView2 = (TextView) findViewById(R.id.doctor_position);
        TextView3 = (TextView) findViewById(R.id.doctor_feature);


        if (!"".equals(sharedPreferences1.getString("docter_photo", ""))) {
            Picasso.with(this)
                    .load(sharedPreferences1.getString("docter_photo", ""))
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(mCircleImageView);
        }


        TextView1.setText("姓名：" + sharedPreferences1.getString("name", ""));
        TextView2.setText("职级：" + sharedPreferences1.getString("position", ""));
        TextView3.setText("擅长：" + sharedPreferences1.getString("feature", ""));


        mTextView = (TextView) findViewById(R.id.service_amount);
        mTextView.setText("收费标准：" + sharedPreferences1.getString("service_amount", "") + "元/分钟");


    }

    public String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public String changeTime(String tim, String tims) {

        String time = null;
        String[] str1 = tim.split("/");
        String string1 = years.format(date) + "-" + str1[0] + "-" + str1[1] + " " + tims;
        try {
            time = dateToStamp(string1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yuyue1:
                if (view.isSelected()) {
                    mButton2.setText("未预约");
                    view.setSelected(false);
                } else {
                    showNormal(mButton2, simple.format(date), "上午", "09:00:00-09:15:00");

                    view.setSelected(true);
                    mButton2.setText("已预约");

                }

                break;
            case R.id.yuyue2:

                if (view.isSelected()) {


                    mButton3.setText("未预约");
                    view.setSelected(false);
                } else {

                    showNormal(mButton3, formatter.format(date1), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton3.setText("已预约");

                }
                break;
            case R.id.yuyue3:


                if (view.isSelected()) {

                    mButton4.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton4, formatte2.format(date2), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton4.setText("已预约");

                }
                break;
            case R.id.yuyue4:


                if (view.isSelected()) {

                    mButton5.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton5, formatter4.format(date3), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton5.setText("已预约");

                }
                break;
            case R.id.yuyue5:


                if (view.isSelected()) {

                    mButton6.setText("未预约");
                    view.setSelected(false);
                } else {

                    showNormal(mButton6, formatter6.format(date4), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton6.setText("已预约");

                }
                break;
            case R.id.yuyue6:

                if (view.isSelected()) {

                    mButton7.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton7, formatter8.format(date5), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton7.setText("已预约");

                }
                break;

            case R.id.yuyue7:


                if (view.isSelected()) {

                    mButton8.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton8, formatter10.format(date6), "上午", "09:00:00-09:15:00");


                    view.setSelected(true);
                    mButton8.setText("已预约");

                }
                break;
            case R.id.yuyue8:


                if (view.isSelected()) {
                    mButton9.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton9, simple.format(date), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton9.setText("已预约");

                }
                break;

            case R.id.yuyue9:


                if (view.isSelected()) {
                    mButton10.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton10, formatter.format(date1), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton10.setText("已预约");

                }
                break;

            case R.id.yuyue10:


                if (view.isSelected()) {
                    mButton11.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton11, formatte2.format(date2), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton11.setText("已预约");

                }
                break;

            case R.id.yuyue11:


                if (view.isSelected()) {
                    mButton12.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton12, formatter4.format(date3), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton12.setText("已预约");

                }
                break;

            case R.id.yuyue12:


                if (view.isSelected()) {
                    mButton13.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton13, formatter6.format(date4), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton13.setText("已预约");

                }
                break;

            case R.id.yuyue13:

                if (view.isSelected()) {
                    mButton14.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton14, formatter8.format(date5), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton14.setText("已预约");

                }
                break;

            case R.id.yuyue14:


                if (view.isSelected()) {
                    mButton15.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton15, formatter10.format(date6), "上午", "09:30:00-09:45:00");


                    view.setSelected(true);
                    mButton15.setText("已预约");

                }
                break;

            case R.id.yuyue15:


                if (view.isSelected()) {
                    mButton16.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton16, simple.format(date), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton16.setText("已预约");

                }
                break;

            case R.id.yuyue16:


                if (view.isSelected()) {
                    mButton17.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton17, formatter.format(date1), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton17.setText("已预约");

                }
                break;
            case R.id.yuyue17:


                if (view.isSelected()) {
                    mButton18.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton18, formatte2.format(date2), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton18.setText("已预约");

                }
                break;

            case R.id.yuyue18:


                if (view.isSelected()) {
                    mButton19.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton19, formatter4.format(date3), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton19.setText("已预约");

                }
                break;

            case R.id.yuyue19:


                if (view.isSelected()) {
                    mButton20.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton20, formatter6.format(date4), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton20.setText("已预约");

                }
                break;

            case R.id.yuyue20:


                if (view.isSelected()) {
                    mButton21.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton21, formatter8.format(date5), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton21.setText("已预约");

                }
                break;

            case R.id.yuyue21:


                if (view.isSelected()) {
                    mButton22.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton22, formatter10.format(date6), "上午", "10:00:00-10:15:00");


                    view.setSelected(true);
                    mButton22.setText("已预约");

                }
                break;
            case R.id.yuyue22:


                if (view.isSelected()) {
                    mButton23.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton23, simple.format(date), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton23.setText("已预约");

                }
                break;
            case R.id.yuyue23:


                if (view.isSelected()) {
                    mButton24.setText("未预约");
                    view.setSelected(false);
                } else {

                    showNormal(mButton24, formatter.format(date1), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton24.setText("已预约");

                }
                break;
            case R.id.yuyue24:


                if (view.isSelected()) {
                    mButton25.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton25, formatte2.format(date2), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton25.setText("已预约");

                }
                break;
            case R.id.yuyue25:


                if (view.isSelected()) {
                    mButton26.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton26, formatter4.format(date3), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton26.setText("已预约");

                }
                break;
            case R.id.yuyue26:


                if (view.isSelected()) {
                    mButton27.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton27, formatter6.format(date4), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton27.setText("已预约");

                }
                break;
            case R.id.yuyue27:


                if (view.isSelected()) {
                    mButton28.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton28, formatter8.format(date5), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton28.setText("已预约");

                }
                break;
            case R.id.yuyue28:


                if (view.isSelected()) {
                    mButton29.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton29, formatter10.format(date6), "上午", "10:30:00-10:45:00");


                    view.setSelected(true);
                    mButton29.setText("已预约");

                }
                break;

            case R.id.yuyue29:
                if (view.isSelected()) {
                    mButton30.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton30, simple.format(date), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton30.setText("已预约");

                }
                break;
            case R.id.yuyue30:
                if (view.isSelected()) {
                    mButton31.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton31, formatter.format(date1), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton31.setText("已预约");

                }
                break;
            case R.id.yuyue31:
                if (view.isSelected()) {
                    mButton32.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton32, formatte2.format(date2), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton32.setText("已预约");

                }
                break;

            case R.id.yuyue32:
                if (view.isSelected()) {
                    mButton33.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton33, formatter4.format(date3), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton33.setText("已预约");

                }
                break;

            case R.id.yuyue33:
                if (view.isSelected()) {
                    mButton34.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton34, formatter6.format(date4), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton34.setText("已预约");

                }
                break;
            case R.id.yuyue34:
                if (view.isSelected()) {
                    mButton35.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton35, formatter8.format(date5), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton35.setText("已预约");

                }
                break;
            case R.id.yuyue35:
                if (view.isSelected()) {
                    mButton36.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton36, formatter10.format(date6), "下午", "14:00:00-14:15:00");


                    view.setSelected(true);
                    mButton.setText("已预约");

                }
                break;
            case R.id.yuyue36:
                if (view.isSelected()) {
                    mButton37.setText("未预约");
                    view.setSelected(false);
                } else {

                    showNormal(mButton37, simple.format(date), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton37.setText("已预约");

                }
                break;
            case R.id.yuyue37:
                if (view.isSelected()) {
                    mButton38.setText("未预约");
                    view.setSelected(false);
                } else {

                    showNormal(mButton38, formatter.format(date1), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton38.setText("已预约");

                }
                break;
            case R.id.yuyue38:
                if (view.isSelected()) {
                    mButton39.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton39, formatte2.format(date2), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton39.setText("已预约");

                }
                break;
            case R.id.yuyue39:
                if (view.isSelected()) {
                    mButton40.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton40, formatter4.format(date3), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton40.setText("已预约");

                }
                break;
            case R.id.yuyue40:
                if (view.isSelected()) {
                    mButton41.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton41, formatter6.format(date4), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton41.setText("已预约");

                }
                break;
            case R.id.yuyue41:
                if (view.isSelected()) {
                    mButton42.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton42, formatter8.format(date5), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton42.setText("已预约");

                }
                break;
            case R.id.yuyue42:
                if (view.isSelected()) {
                    mButton43.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton43, formatter10.format(date6), "下午", "14:30:00-14:45:00");


                    view.setSelected(true);
                    mButton43.setText("已预约");

                }
                break;
            case R.id.yuyue43:
                if (view.isSelected()) {
                    mButton44.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton44, simple.format(date), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton44.setText("已预约");

                }
                break;
            case R.id.yuyue44:
                if (view.isSelected()) {
                    mButton45.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton45, formatter.format(date1), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton45.setText("已预约");

                }
                break;
            case R.id.yuyue45:
                if (view.isSelected()) {
                    mButton46.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton46, formatte2.format(date2), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton46.setText("已预约");

                }
                break;
            case R.id.yuyue46:
                if (view.isSelected()) {
                    mButton47.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton47, formatter4.format(date3), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton47.setText("已预约");

                }
                break;
            case R.id.yuyue47:
                if (view.isSelected()) {
                    mButton48.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton48, formatter6.format(date4), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton48.setText("已预约");

                }
                break;
            case R.id.yuyue48:
                if (view.isSelected()) {
                    mButton49.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton49, formatter8.format(date5), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton49.setText("已预约");

                }
                break;
            case R.id.yuyue49:
                if (view.isSelected()) {
                    mButton50.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton50, formatter10.format(date6), "下午", "15:00:00-15:15:00");


                    view.setSelected(true);
                    mButton50.setText("已预约");

                }
                break;
            case R.id.yuyue50:
                if (view.isSelected()) {
                    mButton51.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton51, simple.format(date), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton51.setText("已预约");

                }
                break;
            case R.id.yuyue51:
                if (view.isSelected()) {
                    mButton52.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton52, formatter.format(date1), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton52.setText("已预约");

                }
                break;
            case R.id.yuyue52:
                if (view.isSelected()) {
                    mButton53.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton53, formatte2.format(date2), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton53.setText("已预约");

                }
                break;
            case R.id.yuyue53:
                if (view.isSelected()) {
                    mButton54.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton54, formatter4.format(date3), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton54.setText("已预约");

                }
                break;
            case R.id.yuyue54:
                if (view.isSelected()) {
                    mButton55.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton55, formatter6.format(date4), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton55.setText("已预约");

                }
                break;
            case R.id.yuyue55:
                if (view.isSelected()) {
                    mButton56.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton56, formatter8.format(date5), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton56.setText("已预约");

                }
                break;
            case R.id.yuyue56:
                if (view.isSelected()) {
                    mButton57.setText("未预约");
                    view.setSelected(false);
                } else {


                    showNormal(mButton57, formatter10.format(date6), "下午", "15:30:00-15:45:00");


                    view.setSelected(true);
                    mButton57.setText("已预约");

                }
                break;


        }
    }
}
