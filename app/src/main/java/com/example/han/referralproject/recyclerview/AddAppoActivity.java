package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddAppoActivity extends BaseActivity implements View.OnClickListener {


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


    public TextView mTextView1;
    public TextView mTextView2;
    public TextView mTextView3;
    public TextView mTextView4;
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
    public TextView mTextView15;
    public TextView mTextView16;
    public TextView mTextView17;
    public TextView mTextView18;
    public TextView mTextView19;
    public TextView mTextView20;
    public TextView mTextView21;
    public TextView mTextView22;
    public TextView mTextView23;
    public TextView mTextView24;
    public TextView mTextView25;
    public TextView mTextView26;
    public TextView mTextView27;
    public TextView mTextView28;
    public TextView mTextView29;
    public TextView mTextView30;
    public TextView mTextView31;
    public TextView mTextView32;
    public TextView mTextView33;
    public TextView mTextView34;
    public TextView mTextView35;
    public TextView mTextView36;
    public TextView mTextView37;
    public TextView mTextView38;
    public TextView mTextView39;
    public TextView mTextView40;
    public TextView mTextView41;
    public TextView mTextView42;


    public void showNormal(final TextView mTextView, String str1, String str2, String str3) {

        String[] str = str1.split("/");
        String[] strs = str3.split("-");

        final String start_time = str[0] + "-" + str[1] + "-" + str[2] + " " + strs[0];
        final String end_time = str[0] + "-" + str[1] + "-" + str[2] + " " + strs[1];

        speak(String.format(getString(R.string.dialog), str[0], str[1], str[2], str2, str3, sharedPreferences1.getString("name", "")));

        dialog = new NDialog1(AddAppoActivity.this);


        dialog.setMessageCenter(true)
                .setMessage(String.format(getString(R.string.dialog), str[0], str[1], str[2], str2, str3, sharedPreferences1.getString("name", "")))
                .setMessageSize(35)
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
                            mTextView.setText("可预约");
                            mTextView.setTextColor(Color.parseColor("#3F86FC"));
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


    //  public ImageView ImageView1;
    //   public ImageView ImageView2;

    SimpleDateFormat simpl;

    SimpleDateFormat simp2;
    SimpleDateFormat simp3;
    SimpleDateFormat simp4;
    SimpleDateFormat simp5;
    SimpleDateFormat simp6;
    SimpleDateFormat simp7;
    SimpleDateFormat simp8;

    // SimpleDateFormat years;

    List<AlreadyYuyue> list = new ArrayList<AlreadyYuyue>();

    public TextView mMonth;
    public TextView mWeek1;
    public TextView mWeek2;
    public TextView mWeek3;
    public TextView mWeek4;
    public TextView mWeek5;
    public TextView mWeek6;
    public TextView mWeek7;
    public TextView mDay1;
    public TextView mDay2;
    public TextView mDay3;
    public TextView mDay4;
    public TextView mDay5;
    public TextView mDay6;
    public TextView mDay7;


    public TextView mMornings;
    public TextView mAfternoons;
    public LinearLayout LinearLayout1;
    public LinearLayout LinearLayout2;
    public LinearLayout LinearLayout3;
    public LinearLayout LinearLayout4;

    public int sign;

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        startActivity(new Intent(mContext, DoctorappoActivity.class));
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appo);


        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.doctor_yuyue));

        initView();

        speak(getString(R.string.yuyue_times));

        dialog1 = new NDialog2(AddAppoActivity.this);

        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        date = new Date();
        simpl = new SimpleDateFormat("M");


        simple = new SimpleDateFormat("y/M/d");
        simple.format(date);
        simple1 = new SimpleDateFormat("EEE");
        simple1.format(date);
        simp2 = new SimpleDateFormat("dd");
        simp2.format(date);

        date1 = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date1);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date1 = calendar.getTime(); //这个时间就是日期往后推一天的结果
        formatter = new SimpleDateFormat("y/M/d");
        formatter1 = new SimpleDateFormat("EEE");
        simp3 = new SimpleDateFormat("dd");

        formatter.format(date1);
        formatter1.format(date1);
        simp3.format(date1);


        date2 = new Date();
        Calendar calendar1 = new GregorianCalendar();
        calendar1.setTime(date2);
        calendar1.add(calendar1.DATE, 2);//把日期往后增加一天.整数往后推,负数往前移动
        date2 = calendar1.getTime(); //这个时间就是日期往后推一天的结果
        formatte2 = new SimpleDateFormat("y/M/d");
        formatter3 = new SimpleDateFormat("EEE");
        simp4 = new SimpleDateFormat("dd");

        formatte2.format(date2);
        formatter3.format(date2);
        simp4.format(date2);


        date3 = new Date();
        Calendar calendar2 = new GregorianCalendar();
        calendar2.setTime(date3);
        calendar2.add(calendar2.DATE, 3);//把日期往后增加一天.整数往后推,负数往前移动
        date3 = calendar2.getTime(); //这个时间就是日期往后推一天的结果
        formatter4 = new SimpleDateFormat("y/M/d");
        formatter5 = new SimpleDateFormat("EEE");
        simp5 = new SimpleDateFormat("dd");


        formatter4.format(date3);
        formatter5.format(date3);
        simp5.format(date3);


        date4 = new Date();
        Calendar calendar3 = new GregorianCalendar();
        calendar3.setTime(date4);
        calendar3.add(calendar3.DATE, 4);//把日期往后增加一天.整数往后推,负数往前移动
        date4 = calendar3.getTime(); //这个时间就是日期往后推一天的结果
        formatter6 = new SimpleDateFormat("y/M/d");
        formatter7 = new SimpleDateFormat("EEE");
        simp6 = new SimpleDateFormat("dd");


        formatter6.format(date4);
        formatter7.format(date4);
        simp6.format(date4);


        date5 = new Date();
        Calendar calendar4 = new GregorianCalendar();
        calendar4.setTime(date5);
        calendar4.add(calendar4.DATE, 5);//把日期往后增加一天.整数往后推,负数往前移动
        date5 = calendar4.getTime(); //这个时间就是日期往后推一天的结果
        formatter8 = new SimpleDateFormat("y/M/d");
        formatter9 = new SimpleDateFormat("EEE");
        simp7 = new SimpleDateFormat("dd");

        formatter8.format(date5);
        formatter9.format(date5);
        simp7.format(date5);


        date6 = new Date();
        Calendar calendar5 = new GregorianCalendar();
        calendar2.setTime(date6);
        calendar2.add(calendar5.DATE, 6);//把日期往后增加一天.整数往后推,负数往前移动
        date6 = calendar2.getTime(); //这个时间就是日期往后推一天的结果
        formatter10 = new SimpleDateFormat("y/M/d");
        formatter11 = new SimpleDateFormat("EEE");
        simp8 = new SimpleDateFormat("dd");

        formatter10.format(date6);
        formatter11.format(date6);
        simp8.format(date6);

        mMonth.setText(simpl.format(date) + "月");
        mWeek1.setText(simple1.format(date));
        mWeek2.setText(formatter1.format(date1));
        mWeek3.setText(formatter3.format(date2));
        mWeek4.setText(formatter5.format(date3));
        mWeek5.setText(formatter7.format(date4));
        mWeek6.setText(formatter9.format(date5));
        mWeek7.setText(formatter11.format(date6));

        mDay1.setText(simp2.format(date));
        mDay2.setText(simp3.format(date1));
        mDay3.setText(simp4.format(date2));
        mDay4.setText(simp5.format(date3));
        mDay5.setText(simp6.format(date4));
        mDay6.setText(simp7.format(date5));
        mDay7.setText(simp8.format(date6));


        judge();


        NetworkApi.YuYue_already(sharedPreferences1.getString("doctor_id", ""), new NetworkManager.SuccessCallback<ArrayList<AlreadyYuyue>>() {
            @Override
            public void onSuccess(ArrayList<AlreadyYuyue> response) {

                list = response;


                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "09:00:00"))
                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "15:00:00"))
                            ) {

                        mTextView1.setText("已预约");
                        mTextView1.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView1.setEnabled(false);


                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "09:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "15:00:00"))

                            ) {

                        mTextView2.setText("已预约");
                        mTextView2.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView2.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "09:00:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "15:00:00"))

                            ) {

                        mTextView3.setText("已预约");
                        mTextView3.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView3.setEnabled(false);


                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "09:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "15:00:00"))


                            ) {

                        mTextView4.setText("已预约");
                        mTextView4.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView4.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "09:00:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "15:00:00"))


                            ) {


                        mTextView5.setText("已预约");
                        mTextView5.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView5.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "09:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "15:00:00"))


                            ) {


                        mTextView6.setText("已预约");
                        mTextView6.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView6.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "09:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "15:00:00"))

                            ) {

                        mTextView7.setText("已预约");
                        mTextView7.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView7.setEnabled(false);

                    }
                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "09:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "15:20:00"))


                            ) {

                        mTextView8.setText("已预约");
                        mTextView8.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView8.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "09:20:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "15:20:00"))


                            ) {


                        mTextView9.setText("已预约");
                        mTextView9.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView9.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "09:20:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "15:20:00"))


                            ) {


                        mTextView10.setText("已预约");
                        mTextView10.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView10.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "09:20:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "15:20:00"))


                            ) {


                        mTextView11.setText("已预约");
                        mTextView11.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView11.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "09:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "15:20:00"))


                            ) {
                        mTextView12.setText("已预约");
                        mTextView12.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView12.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "09:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "15:20:00"))

                            ) {
                        mTextView13.setText("已预约");
                        mTextView13.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView13.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "09:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "15:20:00"))


                            ) {
                        mTextView14.setText("已预约");
                        mTextView14.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView14.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "09:40:00"))


                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "15:40:00"))


                            ) {
                        mTextView15.setText("已预约");
                        mTextView15.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView15.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "09:40:00"))


                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "15:40:00"))


                            ) {
                        mTextView16.setText("已预约");
                        mTextView16.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView16.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "09:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "15:40:00"))


                            ) {
                        mTextView17.setText("已预约");
                        mTextView17.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView17.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "09:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "15:40:00"))


                            ) {
                        mTextView18.setText("已预约");
                        mTextView18.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView18.setEnabled(false);

                    }
                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "09:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "15:40:00"))


                            ) {

                        mTextView19.setText("已预约");
                        mTextView19.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView19.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "09:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "15:40:00"))

                            ) {


                        mTextView20.setText("已预约");
                        mTextView20.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView20.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "09:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "15:40:00"))

                            ) {

                        mTextView21.setText("已预约");
                        mTextView21.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView21.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "16:00:00"))


                            ) {
                        mTextView22.setText("已预约");
                        mTextView22.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView22.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "16:00:00"))


                            ) {

                        mTextView23.setText("已预约");
                        mTextView23.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView23.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "16:00:00"))


                            ) {

                        mTextView24.setText("已预约");
                        mTextView24.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView24.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "16:00:00"))


                            ) {


                        mTextView25.setText("已预约");
                        mTextView25.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView25.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "16:00:00"))


                            ) {


                        mTextView26.setText("已预约");
                        mTextView26.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView26.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "16:00:00"))


                            ) {


                        mTextView27.setText("已预约");
                        mTextView27.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView27.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "10:00:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "16:00:00"))


                            ) {

                        mTextView28.setText("已预约");
                        mTextView28.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView28.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "16:20:00"))


                            ) {

                        mTextView29.setText("已预约");
                        mTextView29.setTextColor(Color.parseColor("#BBBBBB"));
                        mTextView29.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "16:20:00"))


                            ) {

                        mTextView30.setText("已预约");
                        mTextView30.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView30.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "16:20:00"))


                            ) {

                        mTextView31.setText("已预约");
                        mTextView31.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView31.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "16:20:00"))


                            ) {

                        mTextView32.setText("已预约");
                        mTextView32.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView32.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "16:20:00"))


                            ) {

                        mTextView33.setText("已预约");
                        mTextView33.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView33.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "16:20:00"))


                            ) {

                        mTextView34.setText("已预约");
                        mTextView34.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView34.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "10:20:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "16:20:00"))

                            ) {

                        mTextView35.setText("已预约");
                        mTextView35.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView35.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "10:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(simple.format(date) + "", "16:40:00"))

                            ) {

                        mTextView36.setText("已预约");
                        mTextView36.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView36.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "10:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter.format(date1) + "", "16:40:00"))

                            ) {

                        mTextView37.setText("已预约");
                        mTextView37.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView37.setEnabled(false);

                    }

                    if (list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "10:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatte2.format(date2) + "", "16:40:00"))

                            ) {

                        mTextView38.setText("已预约");
                        mTextView38.setTextColor(Color.parseColor("#BBBBBB"));

                        mTextView38.setEnabled(false);

                    }


                    if (list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "10:40:00"))

                            || list.get(i).getStart_time().equals(changeTime(formatter4.format(date3) + "", "16:40:00"))

                            ) {

                        {
                            mTextView39.setText("已预约");
                            mTextView39.setTextColor(Color.parseColor("#BBBBBB"));

                            mTextView39.setEnabled(false);

                        }

                        if (list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "10:40:00"))

                                || list.get(i).getStart_time().equals(changeTime(formatter6.format(date4) + "", "16:40:00"))

                                ) {

                            mTextView40.setText("已预约");
                            mTextView40.setTextColor(Color.parseColor("#BBBBBB"));

                            mTextView40.setEnabled(false);

                        }

                        if (list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "10:40:00"))

                                || list.get(i).getStart_time().equals(changeTime(formatter8.format(date5) + "", "16:40:00"))

                                ) {

                            mTextView41.setText("已预约");
                            mTextView41.setTextColor(Color.parseColor("#BBBBBB"));

                            mTextView41.setEnabled(false);

                        }


                        if (list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "10:40:00"))

                                || list.get(i).getStart_time().equals(changeTime(formatter10.format(date6) + "", "16:40:00"))

                                ) {

                            mTextView42.setText("已预约");
                            mTextView42.setTextColor(Color.parseColor("#BBBBBB"));

                            mTextView42.setEnabled(false);

                        }

                        // sharedPreferences1.getString("doctor_id", "")

                    }
                }
            }

        }, new NetworkManager.FailedCallback()

        {
            @Override
            public void onFailed(String message) {

            }
        });

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
        String string1 = str1[0] + "-" + str1[1] + "-" + str1[2] + " " + tims;
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
            case R.id.yuyue_1:

                if (sign == 0) {

                    showNormal(mTextView1, simple.format(date), "上午", "09:00:00-09:20:00");

                } else {
                    showNormal(mTextView1, simple.format(date), "下午", "15:00:00-15:20:00");

                }
                mTextView1.setText("已预约");
                mTextView1.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_2:

                if (sign == 0) {

                    showNormal(mTextView2, formatter.format(date1), "上午", "09:00:00-09:20:00");


                } else {
                    showNormal(mTextView2, formatter.format(date1), "下午", "15:00:00-15:20:00");

                }


                mTextView2.setText("已预约");
                mTextView2.setTextColor(Color.parseColor("#BBBBBB"));
                break;
            case R.id.yuyue_3:

                if (sign == 0) {


                    showNormal(mTextView3, formatte2.format(date2), "上午", "09:00:00-09:20:00");

                } else {
                    showNormal(mTextView3, formatte2.format(date2), "下午", "15:00:00-15:20:00");

                }


                mTextView3.setText("已预约");
                mTextView3.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_4:


                if (sign == 0) {


                    showNormal(mTextView4, formatter4.format(date3), "上午", "09:00:00-09:20:00");


                } else {
                    showNormal(mTextView4, formatter4.format(date3), "下午", "15:00:00-15:20:00");

                }

                mTextView4.setText("已预约");
                mTextView4.setTextColor(Color.parseColor("#BBBBBB"));
                break;
            case R.id.yuyue_5:

                if (sign == 0) {


                    showNormal(mTextView5, formatter6.format(date4), "上午", "09:00:00-09:20:00");


                } else {
                    showNormal(mTextView5, formatter6.format(date4), "下午", "15:00:00-15:20:00");

                }


                mTextView5.setText("已预约");
                mTextView5.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_6:


                if (sign == 0) {


                    showNormal(mTextView6, formatter8.format(date5), "上午", "09:00:00-09:20:00");


                } else {
                    showNormal(mTextView6, formatter8.format(date5), "下午", "15:00:00-15:20:00");

                }

                mTextView6.setText("已预约");
                mTextView6.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_7:

                if (sign == 0) {


                    showNormal(mTextView7, formatter10.format(date6), "上午", "09:00:00-09:20:00");


                } else {
                    showNormal(mTextView7, formatter10.format(date6), "下午", "15:00:00-15:20:00");

                }


                mTextView7.setText("已预约");
                mTextView7.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_8:

                if (sign == 0) {


                    showNormal(mTextView8, simple.format(date), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView8, simple.format(date), "下午", "15:20:00-15:40:00");

                }


                mTextView8.setText("已预约");
                mTextView8.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_9:


                if (sign == 0) {


                    showNormal(mTextView9, formatter.format(date1), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView9, formatter.format(date1), "下午", "15:20:00-15:40:00");

                }


                mTextView9.setText("已预约");
                mTextView9.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_10:


                if (sign == 0) {


                    showNormal(mTextView10, formatte2.format(date2), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView10, formatte2.format(date2), "下午", "15:20:00-15:40:00");

                }

                mTextView10.setText("已预约");
                mTextView10.setTextColor(Color.parseColor("#BBBBBB"));

                break;

            case R.id.yuyue_11:


                if (sign == 0) {


                    showNormal(mTextView11, formatter4.format(date3), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView11, formatter4.format(date3), "下午", "15:20:00-15:40:00");

                }


                mTextView11.setText("已预约");
                mTextView11.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_12:


                if (sign == 0) {


                    showNormal(mTextView12, formatter6.format(date4), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView12, formatter6.format(date4), "下午", "15:20:00-15:40:00");

                }

                mTextView12.setText("已预约");
                mTextView12.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_13:


                if (sign == 0) {


                    showNormal(mTextView13, formatter8.format(date5), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView13, formatter8.format(date5), "下午", "15:20:00-15:40:00");

                }

                mTextView13.setText("已预约");
                mTextView13.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_14:


                if (sign == 0) {


                    showNormal(mTextView14, formatter10.format(date6), "上午", "09:20:00-09:40:00");


                } else {
                    showNormal(mTextView14, formatter10.format(date6), "下午", "15:20:00-15:40:00");

                }


                mTextView14.setText("已预约");
                mTextView14.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_15:


                if (sign == 0) {

                    showNormal(mTextView15, simple.format(date), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView15, simple.format(date), "下午", "15:40:00-16:00:00");

                }

                mTextView15.setText("已预约");
                mTextView15.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_16:


                if (sign == 0) {

                    showNormal(mTextView16, formatter.format(date1), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView16, formatter.format(date1), "下午", "15:40:00-16:00:00");

                }

                mTextView16.setText("已预约");
                mTextView16.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_17:


                if (sign == 0) {

                    showNormal(mTextView17, formatte2.format(date2), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView17, formatte2.format(date2), "下午", "15:40:00-16:00:00");

                }


                mTextView17.setText("已预约");
                mTextView17.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_18:


                if (sign == 0) {

                    showNormal(mTextView18, formatter4.format(date3), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView18, formatter4.format(date3), "下午", "15:40:00-16:00:00");

                }


                mTextView18.setText("已预约");
                mTextView18.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_19:


                if (sign == 0) {

                    showNormal(mTextView19, formatter6.format(date4), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView19, formatter6.format(date4), "下午", "15:40:00-16:00:00");

                }

                mTextView19.setText("已预约");
                mTextView19.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_20:


                if (sign == 0) {

                    showNormal(mTextView20, formatter8.format(date5), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView20, formatter8.format(date5), "下午", "15:40:00-16:00:00");

                }

                mTextView20.setText("已预约");
                mTextView20.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_21:


                if (sign == 0) {

                    showNormal(mTextView21, formatter10.format(date6), "上午", "9:40:00-10:00:00");


                } else {
                    showNormal(mTextView21, formatter10.format(date6), "下午", "15:40:00-16:00:00");

                }


                mTextView21.setText("已预约");
                mTextView21.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_22:

                if (sign == 0) {

                    showNormal(mTextView22, simple.format(date), "上午", "10:00:00-10:20:00");


                } else {
                    showNormal(mTextView22, simple.format(date), "下午", "16:00:00-16:20:00");

                }


                mTextView22.setText("已预约");
                mTextView22.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_23:


                if (sign == 0) {

                    showNormal(mTextView23, formatter.format(date1), "上午", "10:00:00-10:20:00");


                } else {


                    showNormal(mTextView23, formatter.format(date1), "下午", "16:00:00-16:20:00");

                }

                mTextView23.setText("已预约");
                mTextView23.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_24:


                if (sign == 0) {

                    showNormal(mTextView24, formatte2.format(date2), "上午", "10:00:00-10:20:00");


                } else {


                    showNormal(mTextView24, formatte2.format(date2), "下午", "16:00:00-16:20:00");

                }

                mTextView24.setText("已预约");
                mTextView24.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_25:


                if (sign == 0) {

                    showNormal(mTextView25, formatter4.format(date3), "上午", "10:00:00-10:20:00");

                } else {
                    showNormal(mTextView25, formatter4.format(date3), "下午", "16:00:00-16:20:00");
                }

                mTextView25.setText("已预约");
                mTextView25.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_26:


                if (sign == 0) {

                    showNormal(mTextView26, formatter6.format(date4), "上午", "10:00:00-10:20:00");


                } else {
                    showNormal(mTextView26, formatter6.format(date4), "下午", "16:00:00-16:20:00");
                }


                mTextView26.setText("已预约");
                mTextView26.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_27:


                if (sign == 0) {

                    showNormal(mTextView27, formatter8.format(date5), "上午", "10:00:00-10:20:00");


                } else {
                    showNormal(mTextView27, formatter8.format(date5), "下午", "16:00:00-16:20:00");
                }


                mTextView27.setText("已预约");
                mTextView27.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_28:

                if (sign == 0) {

                    showNormal(mTextView28, formatter10.format(date6), "上午", "10:00:00-10:20:00");


                } else {
                    showNormal(mTextView28, formatter10.format(date6), "下午", "16:00:00-16:20:00");
                }


                mTextView28.setText("已预约");
                mTextView28.setTextColor(Color.parseColor("#BBBBBB"));

                break;

            case R.id.yuyue_29:


                if (sign == 0) {

                    showNormal(mTextView29, simple.format(date), "上午", "10:20:00-10:40:00");


                } else {
                    showNormal(mTextView29, simple.format(date), "下午", "16:20:00-16:40:00");
                }

                mTextView29.setText("已预约");
                mTextView29.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_30:

                if (sign == 0) {

                    showNormal(mTextView30, formatter.format(date1), "上午", "10:20:00-10:40:00");


                } else {
                    showNormal(mTextView30, formatter.format(date1), "下午", "16:20:00-16:40:00");
                }

                mTextView30.setText("已预约");
                mTextView30.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_31:


                if (sign == 0) {
                    showNormal(mTextView31, formatte2.format(date2), "上午", "10:20:00-10:40:00");
                } else {
                    showNormal(mTextView31, formatte2.format(date2), "下午", "16:20:00-16:40:00");
                }

                mTextView31.setText("已预约");
                mTextView31.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_32:


                if (sign == 0) {
                    showNormal(mTextView32, formatter4.format(date3), "上午", "10:20:00-10:40:00");


                } else {
                    showNormal(mTextView32, formatter4.format(date3), "下午", "16:20:00-16:40:00");
                }
                mTextView32.setText("已预约");
                mTextView32.setTextColor(Color.parseColor("#BBBBBB"));


                break;

            case R.id.yuyue_33:

                if (sign == 0) {
                    showNormal(mTextView33, formatter6.format(date4), "上午", "10:20:00-10:40:00");

                } else {
                    showNormal(mTextView33, formatter6.format(date4), "下午", "16:20:00-16:40:00");
                }
                mTextView33.setText("已预约");
                mTextView33.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_34:


                if (sign == 0) {
                    showNormal(mTextView34, formatter8.format(date5), "上午", "10:20:00-10:40:00");

                } else {
                    showNormal(mTextView34, formatter8.format(date5), "下午", "16:20:00-16:40:00");
                }
                mTextView34.setText("已预约");
                mTextView34.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_35:

                if (sign == 0) {
                    showNormal(mTextView35, formatter10.format(date6), "上午", "10:20:00-10:40:00");


                } else {
                    showNormal(mTextView35, formatter10.format(date6), "下午", "16:20:00-16:40:00");
                }
                mTextView35.setText("已预约");
                mTextView35.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_36:

                if (sign == 0) {
                    showNormal(mTextView36, simple.format(date), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView36, simple.format(date), "下午", "16:40:00-17:00:00");
                }
                mTextView36.setText("已预约");
                mTextView36.setTextColor(Color.parseColor("#BBBBBB"));

                break;
            case R.id.yuyue_37:

                if (sign == 0) {
                    showNormal(mTextView37, formatter.format(date1), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView37, formatter.format(date1), "下午", "16:40:00-17:00:00");
                }


                mTextView37.setText("已预约");
                mTextView37.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_38:

                if (sign == 0) {
                    showNormal(mTextView38, formatte2.format(date2), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView38, formatte2.format(date2), "下午", "16:40:00-17:00:00");
                }

                mTextView38.setText("已预约");
                mTextView38.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_39:


                if (sign == 0) {
                    showNormal(mTextView39, formatter4.format(date3), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView39, formatter4.format(date3), "下午", "16:40:00-17:00:00");
                }

                mTextView39.setText("已预约");
                mTextView39.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_40:


                if (sign == 0) {
                    showNormal(mTextView40, formatter6.format(date4), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView40, formatter6.format(date4), "下午", "16:40:00-17:00:00");
                }

                mTextView40.setText("已预约");
                mTextView40.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_41:

                if (sign == 0) {
                    showNormal(mTextView41, formatter8.format(date5), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView41, formatter8.format(date5), "下午", "16:40:00-17:00:00");
                }

                mTextView41.setText("已预约");
                mTextView41.setTextColor(Color.parseColor("#BBBBBB"));


                break;
            case R.id.yuyue_42:


                if (sign == 0) {
                    showNormal(mTextView42, formatter10.format(date6), "上午", "10:40:00-11:00:00");

                } else {
                    showNormal(mTextView42, formatter10.format(date6), "下午", "16:40:00-17:00:00");
                }

                mTextView42.setText("已预约");
                mTextView42.setTextColor(Color.parseColor("#BBBBBB"));


                break;


        }

    }


    public void initView() {
        mTextView1 = (TextView) findViewById(R.id.yuyue_1);
        mTextView2 = (TextView) findViewById(R.id.yuyue_2);
        mTextView3 = (TextView) findViewById(R.id.yuyue_3);
        mTextView4 = (TextView) findViewById(R.id.yuyue_4);
        mTextView5 = (TextView) findViewById(R.id.yuyue_5);
        mTextView6 = (TextView) findViewById(R.id.yuyue_6);
        mTextView7 = (TextView) findViewById(R.id.yuyue_7);
        mTextView8 = (TextView) findViewById(R.id.yuyue_8);
        mTextView9 = (TextView) findViewById(R.id.yuyue_9);
        mTextView10 = (TextView) findViewById(R.id.yuyue_10);
        mTextView11 = (TextView) findViewById(R.id.yuyue_11);
        mTextView12 = (TextView) findViewById(R.id.yuyue_12);
        mTextView13 = (TextView) findViewById(R.id.yuyue_13);
        mTextView14 = (TextView) findViewById(R.id.yuyue_14);
        mTextView15 = (TextView) findViewById(R.id.yuyue_15);
        mTextView16 = (TextView) findViewById(R.id.yuyue_16);
        mTextView17 = (TextView) findViewById(R.id.yuyue_17);
        mTextView18 = (TextView) findViewById(R.id.yuyue_18);
        mTextView19 = (TextView) findViewById(R.id.yuyue_19);
        mTextView20 = (TextView) findViewById(R.id.yuyue_20);
        mTextView21 = (TextView) findViewById(R.id.yuyue_21);
        mTextView22 = (TextView) findViewById(R.id.yuyue_22);
        mTextView23 = (TextView) findViewById(R.id.yuyue_23);
        mTextView24 = (TextView) findViewById(R.id.yuyue_24);
        mTextView25 = (TextView) findViewById(R.id.yuyue_25);
        mTextView26 = (TextView) findViewById(R.id.yuyue_26);
        mTextView27 = (TextView) findViewById(R.id.yuyue_27);
        mTextView28 = (TextView) findViewById(R.id.yuyue_28);
        mTextView29 = (TextView) findViewById(R.id.yuyue_29);
        mTextView30 = (TextView) findViewById(R.id.yuyue_30);
        mTextView31 = (TextView) findViewById(R.id.yuyue_31);
        mTextView32 = (TextView) findViewById(R.id.yuyue_32);
        mTextView33 = (TextView) findViewById(R.id.yuyue_33);
        mTextView34 = (TextView) findViewById(R.id.yuyue_34);
        mTextView35 = (TextView) findViewById(R.id.yuyue_35);
        mTextView36 = (TextView) findViewById(R.id.yuyue_36);
        mTextView37 = (TextView) findViewById(R.id.yuyue_37);
        mTextView38 = (TextView) findViewById(R.id.yuyue_38);
        mTextView39 = (TextView) findViewById(R.id.yuyue_39);
        mTextView40 = (TextView) findViewById(R.id.yuyue_40);
        mTextView41 = (TextView) findViewById(R.id.yuyue_41);
        mTextView42 = (TextView) findViewById(R.id.yuyue_42);
        mMonth = (TextView) findViewById(R.id.yuyue_month);
        mWeek1 = (TextView) findViewById(R.id.yuyue_week1);
        mWeek2 = (TextView) findViewById(R.id.yuyue_week2);
        mWeek3 = (TextView) findViewById(R.id.yuyue_week3);
        mWeek4 = (TextView) findViewById(R.id.yuyue_week4);
        mWeek5 = (TextView) findViewById(R.id.yuyue_week5);
        mWeek6 = (TextView) findViewById(R.id.yuyue_week6);
        mWeek7 = (TextView) findViewById(R.id.yuyue_week7);
        mDay1 = (TextView) findViewById(R.id.yuyue_day1);
        mDay2 = (TextView) findViewById(R.id.yuyue_day2);
        mDay3 = (TextView) findViewById(R.id.yuyue_day3);
        mDay4 = (TextView) findViewById(R.id.yuyue_day4);
        mDay5 = (TextView) findViewById(R.id.yuyue_day5);
        mDay6 = (TextView) findViewById(R.id.yuyue_day6);
        mDay7 = (TextView) findViewById(R.id.yuyue_day7);


        mMornings = (TextView) findViewById(R.id.mornings);
        mAfternoons = (TextView) findViewById(R.id.afternoons);

        LinearLayout1 = (LinearLayout) findViewById(R.id.morning1);
        LinearLayout2 = (LinearLayout) findViewById(R.id.morning2);
        LinearLayout3 = (LinearLayout) findViewById(R.id.afternoon1);
        LinearLayout4 = (LinearLayout) findViewById(R.id.afternoon2);


        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mTextView5.setOnClickListener(this);
        mTextView6.setOnClickListener(this);
        mTextView7.setOnClickListener(this);
        mTextView8.setOnClickListener(this);
        mTextView9.setOnClickListener(this);
        mTextView10.setOnClickListener(this);
        mTextView11.setOnClickListener(this);
        mTextView12.setOnClickListener(this);
        mTextView13.setOnClickListener(this);
        mTextView14.setOnClickListener(this);
        mTextView15.setOnClickListener(this);
        mTextView16.setOnClickListener(this);
        mTextView17.setOnClickListener(this);
        mTextView18.setOnClickListener(this);
        mTextView19.setOnClickListener(this);
        mTextView20.setOnClickListener(this);
        mTextView21.setOnClickListener(this);
        mTextView22.setOnClickListener(this);
        mTextView23.setOnClickListener(this);
        mTextView24.setOnClickListener(this);
        mTextView25.setOnClickListener(this);
        mTextView26.setOnClickListener(this);
        mTextView27.setOnClickListener(this);
        mTextView28.setOnClickListener(this);
        mTextView29.setOnClickListener(this);
        mTextView30.setOnClickListener(this);
        mTextView31.setOnClickListener(this);
        mTextView32.setOnClickListener(this);
        mTextView33.setOnClickListener(this);
        mTextView34.setOnClickListener(this);
        mTextView35.setOnClickListener(this);
        mTextView36.setOnClickListener(this);
        mTextView37.setOnClickListener(this);
        mTextView38.setOnClickListener(this);
        mTextView39.setOnClickListener(this);
        mTextView40.setOnClickListener(this);
        mTextView41.setOnClickListener(this);
        mTextView42.setOnClickListener(this);

        mMornings.setTextColor(Color.parseColor("#3F86FC"));
        LinearLayout1.setVisibility(View.VISIBLE);
        LinearLayout2.setVisibility(View.VISIBLE);
        LinearLayout3.setVisibility(View.GONE);
        LinearLayout4.setVisibility(View.GONE);


        mMornings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sign = 0;


                mMornings.setTextColor(Color.parseColor("#3F86FC"));
                mAfternoons.setTextColor(Color.parseColor("#757575"));

                LinearLayout1.setVisibility(View.VISIBLE);
                LinearLayout2.setVisibility(View.VISIBLE);
                LinearLayout3.setVisibility(View.GONE);
                LinearLayout4.setVisibility(View.GONE);


                judge();


            }
        });


        mAfternoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sign = 1;


                mMornings.setTextColor(Color.parseColor("#757575"));
                mAfternoons.setTextColor(Color.parseColor("#3F86FC"));
                LinearLayout3.setVisibility(View.VISIBLE);
                LinearLayout4.setVisibility(View.VISIBLE);
                LinearLayout1.setVisibility(View.GONE);
                LinearLayout2.setVisibility(View.GONE);


                mTextView1.setEnabled(true);
                mTextView1.setText("可预约");
                mTextView1.setTextColor(Color.parseColor("#3F86FC"));

                mTextView8.setEnabled(true);
                mTextView8.setText("可预约");
                mTextView8.setTextColor(Color.parseColor("#3F86FC"));

                mTextView15.setEnabled(true);
                mTextView15.setText("可预约");
                mTextView15.setTextColor(Color.parseColor("#3F86FC"));


                mTextView22.setEnabled(true);
                mTextView22.setText("可预约");
                mTextView22.setTextColor(Color.parseColor("#3F86FC"));

                mTextView29.setEnabled(true);
                mTextView29.setText("可预约");
                mTextView29.setTextColor(Color.parseColor("#3F86FC"));

                mTextView36.setEnabled(true);
                mTextView36.setText("可预约");
                mTextView36.setTextColor(Color.parseColor("#3F86FC"));

                mTextView1.setEnabled(true);
                mTextView1.setText("可预约");
                mTextView1.setTextColor(Color.parseColor("#3F86FC"));


                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "15:20:00"))) {

                    mTextView1.setText("已预约");
                    mTextView1.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView1.setEnabled(false);


                }

                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "15:40:00"))) {

                    mTextView8.setText("已预约");
                    mTextView8.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView8.setEnabled(false);


                }

                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "16:00:00"))) {

                    mTextView15.setText("已预约");
                    mTextView15.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView15.setEnabled(false);

                }

                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "16:20:00"))) {

                    mTextView22.setText("已预约");
                    mTextView22.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView22.setEnabled(false);

                }

                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "16:40:00"))) {

                    mTextView29.setText("已预约");
                    mTextView29.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView29.setEnabled(false);

                }

                if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "17:00:00"))) {

                    mTextView36.setText("已预约");
                    mTextView36.setTextColor(Color.parseColor("#BBBBBB"));
                    mTextView36.setEnabled(false);

                }


            }
        });


    }


    public void judge() {


        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "09:20:00"))) {

            mTextView1.setText("已预约");
            mTextView1.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView1.setEnabled(false);


        }

        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "09:40:00"))) {

            mTextView8.setText("已预约");
            mTextView8.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView8.setEnabled(false);

        }

        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "10:00:00"))) {


            mTextView15.setText("已预约");
            mTextView15.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView15.setEnabled(false);


        }


        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "10:20:00"))) {


            mTextView22.setText("已预约");
            mTextView22.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView22.setEnabled(false);


        }


        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "10:40:00"))) {


            mTextView29.setText("已预约");
            mTextView29.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView29.setEnabled(false);


        }

        if (System.currentTimeMillis() > Long.parseLong(changeTime(simple.format(date) + "", "11:00:00"))) {


            mTextView36.setText("已预约");
            mTextView36.setTextColor(Color.parseColor("#BBBBBB"));
            mTextView36.setEnabled(false);


        }


    }


}
