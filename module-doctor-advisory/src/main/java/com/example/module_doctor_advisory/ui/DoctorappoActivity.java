package com.example.module_doctor_advisory.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lib_alarm_clock.AlarmHelper;
import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.bean.Doctor;
import com.example.module_doctor_advisory.bean.YuYueInfo;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.AlertDialog;
import com.gcml.lib_widget.dialog.SingleButtonDialog;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.AlarmModel;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.apache.commons.lang.StringUtils;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DoctorappoActivity extends ToolbarBaseActivity implements View.OnClickListener {


    public TextView mTextView;
    public TextView mTextView1;
    public TextView mTextView2;
    public Button mButton1;
    public ImageView circleImageView;

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


    public TextView mTextView12;

    // public ImageView ImageView1;
    //  public ImageView ImageView2;
    List<AlarmModel> models = new ArrayList<AlarmModel>();

    public Button mButtons;


    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(final Message msg) {


            switch (msg.what) {
                case 1:
                    break;

                case 0:
                    models = DataSupport.findAll(AlarmModel.class);


                    if (list.size() == 3) {
                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.VISIBLE);
                        mLinearLayout3.setVisibility(View.VISIBLE);


                        if ("已接受".equals(list.get(0).getState())) {
                            mButton2.setText("已预约");
                            mButton2.setSelected(true);
                            mButton2.setEnabled(false);


                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            if (!"".equals(list.get(0).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time = 0;
                            long time1 = 0;

                            try {
                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time1, time - 60000);


                        } else if ("已过期".equals(list.get(0).getState()) ||
                                "已拒绝".equals(list.get(0).getState())) {
                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(0).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout1.setVisibility(View.GONE);
                                        }
                                    });


                        } else {

                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            if (!"".equals(list.get(0).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time = 0;
                            long time1 = 0;

                            try {
                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time1, time - 60000);


                        }


                        if ("已接受".equals(list.get(1).getState())) {
                            mButton3.setText("已预约");
                            mButton3.setSelected(true);
                            mButton3.setEnabled(false);


                            mTextView2.setText(StringUtils.substringBeforeLast(list.get(1).getStart_time(), ":"));
                            mTextView6.setText(StringUtils.substringBeforeLast(list.get(1).getEnd_time(), ":"));

                            if (!"".equals(list.get(1).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time2 = 0;
                            long time3 = 0;

                            try {
                                time2 = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                time3 = Long.parseLong(dateToStamp(list.get(1).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time3, time2 - 60000);


                        } else if ("已过期".equals(list.get(1).getState())

                                || "已拒绝".equals(list.get(1).getState())) {
                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(0).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout2.setVisibility(View.GONE);
                                        }
                                    });

                        } else {

                            mTextView2.setText(StringUtils.substringBeforeLast(list.get(1).getStart_time(), ":"));
                            mTextView6.setText(StringUtils.substringBeforeLast(list.get(1).getEnd_time(), ":"));

                            if (!"".equals(list.get(1).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time2 = 0;
                            long time3 = 0;

                            try {
                                time2 = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                time3 = Long.parseLong(dateToStamp(list.get(1).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time3, time2 - 60000);


                        }


                        if ("已接受".equals(list.get(2).getState())) {
                            mButton4.setText("已预约");
                            mButton4.setSelected(true);
                            mButton4.setEnabled(false);

                            mTextView7.setText(StringUtils.substringBeforeLast(list.get(2).getStart_time(), ":"));
                            mTextView8.setText(StringUtils.substringBeforeLast(list.get(2).getEnd_time(), ":"));

                            if (!"".equals(list.get(2).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time4 = 0;
                            long time5 = 0;

                            try {
                                time4 = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                                time5 = Long.parseLong(dateToStamp(list.get(2).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time5, time4 - 60000);

                            models = DataSupport.findAll(AlarmModel.class);


                        } else if ("已过期".equals(list.get(2).getState())
                                || "已拒绝".equals(list.get(2).getState())) {

                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(0).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout3.setVisibility(View.GONE);
                                        }
                                    });


                        } else {

                            mTextView7.setText(StringUtils.substringBeforeLast(list.get(2).getStart_time(), ":"));
                            mTextView8.setText(StringUtils.substringBeforeLast(list.get(2).getEnd_time(), ":"));

                            if (!"".equals(list.get(2).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }


                            long time4 = 0;
                            long time5 = 0;

                            try {
                                time4 = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                                time5 = Long.parseLong(dateToStamp(list.get(2).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time5, time4 - 60000);

                            models = DataSupport.findAll(AlarmModel.class);


                        }


                    } else if (list.size() == 2) {

                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.VISIBLE);
                        mLinearLayout3.setVisibility(View.GONE);


                        if ("已接受".equals(list.get(0).getState())) {
                            mButton2.setText("已预约");
                            mButton2.setSelected(true);
                            mButton2.setEnabled(false);


                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            models = DataSupport.findAll(AlarmModel.class);

                            if (!"".equals(list.get(0).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }

                            long time = 0;
                            long time1 = 0;

                            try {
                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time1, time - 60000);


                        } else if ("已过期".equals(list.get(0).getState())
                                || "已拒绝".equals(list.get(0).getState())) {
                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(0).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout1.setVisibility(View.GONE);
                                        }
                                    });

                        } else {
                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            models = DataSupport.findAll(AlarmModel.class);

                            //   setAlarmClock(0);
                            if (!"".equals(list.get(0).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }


                                    }
                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }
                                }


                            }

                            long time = 0;
                            long time1 = 0;

                            try {
                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time1, time - 60000);


                        }


                        if ("已接受".equals(list.get(1).getState())) {
                            mButton3.setText("已预约");
                            mButton3.setSelected(true);
                            mButton3.setEnabled(false);


                            mTextView2.setText(StringUtils.substringBeforeLast(list.get(1).getStart_time(), ":"));
                            mTextView6.setText(StringUtils.substringBeforeLast(list.get(1).getEnd_time(), ":"));


                            //  setAlarmClock(1);


                            if (!"".equals(list.get(1).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }

                                    }

                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");
                                    }

                                }


                            }


                            long time2 = 0;
                            long time3 = 0;

                            try {
                                time2 = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                time3 = Long.parseLong(dateToStamp(list.get(1).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time3, time2 - 60000);

                            models = DataSupport.findAll(AlarmModel.class);


                        } else if ("已过期".equals(list.get(1).getState())
                                || "已拒绝".equals(list.get(1).getState())) {

                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(1).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout2.setVisibility(View.GONE);
                                        }
                                    });

                        } else {

                            mTextView2.setText(StringUtils.substringBeforeLast(list.get(1).getStart_time(), ":"));
                            mTextView6.setText(StringUtils.substringBeforeLast(list.get(1).getEnd_time(), ":"));


                            //  setAlarmClock(1);


                            if (!"".equals(list.get(1).getStart_time())) {
                                long time = 0;
                                try {
                                    time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }

                                    }

                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");
                                    }

                                }


                            }


                            long time2 = 0;
                            long time3 = 0;

                            try {
                                time2 = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                                time3 = Long.parseLong(dateToStamp(list.get(1).getEnd_time()));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            enableVideo(time3, time2 - 60000);

                            models = DataSupport.findAll(AlarmModel.class);

                        }

                    } else if (list.size() == 1) {

                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.GONE);
                        mLinearLayout3.setVisibility(View.GONE);


                        if ("已接受".equals(list.get(0).getState())) {
                            mButton2.setText("已预约");
                            mButton2.setSelected(true);
                            mButton2.setEnabled(false);


                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            models = DataSupport.findAll(AlarmModel.class);


                            long time = 0;
                            long time1 = 0;
                            if (!"".equals(list.get(0).getStart_time())) {

                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                    time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }

                                    }

                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }


                                }


                            }

                            enableVideo(time1, time - 60000);


                            models = DataSupport.findAll(AlarmModel.class);


                        } else if ("已过期".equals(list.get(0).getState())
                                || "已拒绝".equals(list.get(0).getState())) {

                            Box.getRetrofit(DoctorAPI.class)
                                    .cancelReservation(list.get(0).getRid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CommonObserver<Object>() {
                                        @Override
                                        public void onNext(Object o) {
                                            long time = 0;
                                            try {
                                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String times = String.valueOf(time - 60000);


                                            if (models.size() != 0) {

                                                for (int i = 0; i < models.size(); i++) {
                                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                                        models.get(i).delete();
                                                        break;
                                                    }
                                                }
                                            }
                                            mLinearLayout1.setVisibility(View.GONE);
                                        }
                                    });


                        } else {

                            mTextView.setText(StringUtils.substringBeforeLast(list.get(0).getStart_time(), ":"));
                            mTextView1.setText(StringUtils.substringBeforeLast(list.get(0).getEnd_time(), ":"));


                            models = DataSupport.findAll(AlarmModel.class);


                            long time = 0;
                            long time1 = 0;
                            if (!"".equals(list.get(0).getStart_time())) {

                                try {
                                    time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                    time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String times = String.valueOf(time - 60000);

                                if (models.size() == 0) {
                                    AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                } else {
                               /* for (AlarmModel itemModel : models) {

                                    if (String.valueOf(itemModel.getTimestamp()).equals(times)) {
                                        break;
                                    } else {
                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");
                                        break;
                                    }

                                }*/

                                    boolean sign = true;

                                    for (int a = 0; a < models.size(); a++) {

                                        if (String.valueOf(models.get(a).getTimestamp()).equals(times)) {
                                            sign = false;
                                        }

                                    }

                                    if (sign == true) {

                                        AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                                    }


                                }


                            }

                            enableVideo(time1, time - 60000);


                            models = DataSupport.findAll(AlarmModel.class);


                        }


                    }

                    break;
            }

            return true;
        }
    });
    static ArrayList allReservationHistory = new ArrayList();


    public void enableVideo(long time, long time1) {


        if (System.currentTimeMillis() < time && System.currentTimeMillis() >= time1) {
            mButtons.setEnabled(true);
            mButtons.setSelected(true);

        } else {
            mButtons.setEnabled(false);
            mButtons.setSelected(false);
        }

    }


    public String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    Button mButton2;
    Button mButton3;
    Button mButton4;

    TextView mHistroy;
    private UserInfoBean user;
    private Doctor doctor;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_doctorappo;
    }

    @Override
    public void initParams(Intent intentArgument) {
        user = Box.getSessionManager().getUser();
    }

    @Override
    public void initView() {
        mButton2 = (Button) findViewById(R.id.cancel_yuyue);
        mButton3 = (Button) findViewById(R.id.cancel_yuyue1);
        mButton4 = (Button) findViewById(R.id.cancel_yuyue2);

        mHistroy = (TextView) findViewById(R.id.yuyue_lishi);

        mHistroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DoctorappoActivity.this, HistoryActivity.class);
                startActivity(intent);

            }
        });

        MLVoiceSynthetize.startSynthesize(R.string.qianyue_doctor);

        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.doctor_qianyue));

        mButtons = (Button) findViewById(R.id.video_doctor);
        mButtons.setEnabled(false);
        mButtons.setSelected(false);
        mButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emitEvent("NimCall","docter_" + user.doid);
            }
        });


        mTextView = (TextView) findViewById(R.id.yuyue_time);
        mTextView1 = (TextView) findViewById(R.id.yuyue_time1);
        mTextView2 = (TextView) findViewById(R.id.yuyue_time2);


        mTextView3 = (TextView) findViewById(R.id.docotor_name);
        mTextView4 = (TextView) findViewById(R.id.docotor_position);
        mTextView5 = (TextView) findViewById(R.id.docotor_feature);

        mTextView6 = (TextView) findViewById(R.id.yuyue_time3);
        mTextView7 = (TextView) findViewById(R.id.yuyue_time4);
        mTextView8 = (TextView) findViewById(R.id.yuyue_time5);


        mTextView12 = (TextView) findViewById(R.id.service_amount);

        //   mTextView12.setText("收费标准：" + sharedPreferences1.getString("service_amount", "") + "元/分钟");


        circleImageView = (ImageView) findViewById(R.id.circleImageView1);

        Box.getRetrofit(DoctorAPI.class)
                .queryDoctorInfo(user.doid)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<Doctor>() {
                    @Override
                    public void onNext(Doctor doctor) {
                        DoctorappoActivity.this.doctor = doctor;
                        if (!TextUtils.isEmpty(doctor.docter_photo)) {
                            Glide.with(Box.getApp())
                                    .applyDefaultRequestOptions(new RequestOptions()
                                            .placeholder(R.drawable.avatar_placeholder)
                                            .error(R.drawable.avatar_placeholder))
                                    .load(doctor.docter_photo)
                                    .into(circleImageView);
                        }
                        mTextView3.setText(String.format(getString(R.string.doctor_name), doctor.doctername));
                        mTextView4.setText(String.format(getString(R.string.doctor_zhiji), doctor.duty));
                        mTextView5.setText(String.format(getString(R.string.doctor_shanchang), doctor.department));
                        mTextView12.setText(String.format(getString(R.string.doctor_shoufei), doctor.service_amount));
                    }
                });



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NimCallActivity.launch(DoctorappoActivity.this, "docter_" + doctorId);
//                finish();
            }
        });

        mButton1 = (Button) findViewById(R.id.add_yuyue);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    Log.e("==============", list.toString());
                if (list.size() < 3) {
                    Intent intent = new Intent(getApplicationContext(), AddAppoActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    MLVoiceSynthetize.startSynthesize(R.string.yuyue_limit);

                    Toast.makeText(getApplicationContext(), "主人，您预约已达到最大次数", Toast.LENGTH_SHORT).show();
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


        if ("".equals(mTextView.getText().toString())) {
            mLinearLayout1.setVisibility(View.INVISIBLE);

        }

        if ("".equals(mTextView2.getText().toString())) {
            mLinearLayout2.setVisibility(View.INVISIBLE);

        }

        if ("".equals(mTextView7.getText().toString())) {
            mLinearLayout3.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }


    List<YuYueInfo> list = new ArrayList<YuYueInfo>();


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        yuYueDoctor();
    }

    public void yuYueDoctor() {
        Box.getRetrofit(DoctorAPI.class)
                .querySignedDoctorInfo(user.bid, user.doid)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<YuYueInfo>>() {
                    @Override
                    public void onNext(List<YuYueInfo> yuYueInfos) {
                        allReservationHistory.clear();
                        allReservationHistory.addAll(yuYueInfos);
                        list.clear();
                        for (int i = 0; i < yuYueInfos.size(); i++) {
                            if ("未接受".equals(yuYueInfos.get(i).getState())) {
                                list.add(yuYueInfos.get(i));
                            }
                        }

                        mHandler.sendEmptyMessage(0);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.cancel_yuyue) {
            MLVoiceSynthetize.startSynthesize(R.string.cancel_yuyue);

            showNormal(1);


        } else if (i == R.id.cancel_yuyue1) {
            MLVoiceSynthetize.startSynthesize(R.string.cancel_yuyue);

            showNormal(2);

        } else if (i == R.id.cancel_yuyue2) {
            MLVoiceSynthetize.startSynthesize(R.string.cancel_yuyue);

            showNormal(3);

        }
    }

    public void showNormal(final int sign) {

        new AlertDialog(this)
                .builder()
                .setMsg("您确认要取消预约？")
                .setCancelable(false)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelReserversion(sign);
                    }
                }).show();
    }

    private void cancelReserversion(int sign) {
        if (sign == 1) {

            Box.getRetrofit(DoctorAPI.class)
                    .cancelReservation(list.get(0).getRid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            long time = 0;
                            try {
                                time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String times = String.valueOf(time - 60000);


                            models = DataSupport.findAll(AlarmModel.class);


                            if (models.size() != 0) {

                                for (int i = 0; i < models.size(); i++) {
                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                        models.get(i).delete();
                                        break;
                                    }
                                }


                            }


                            mTextView.setText(null);
                            mTextView1.setText(null);
                            mLinearLayout1.setVisibility(View.INVISIBLE);
                            yuYueDoctor();

                            ShowNormals("取消成功");
                            MLVoiceSynthetize.startSynthesize(R.string.cancel_success);
                        }
                    });

        } else if (sign == 2) {
            Box.getRetrofit(DoctorAPI.class)
                    .cancelReservation(list.get(1).getRid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onNext(Object o) {

                            long time = 0;
                            try {
                                time = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String times = String.valueOf(time - 60000);


                            if (models.size() != 0) {

                                for (int i = 0; i < models.size(); i++) {
                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                        models.get(i).delete();
                                        break;
                                    }
                                }
                            }

                            mTextView2.setText(null);
                            mTextView6.setText(null);
                            mLinearLayout2.setVisibility(View.INVISIBLE);
                            yuYueDoctor();


                            ShowNormals("取消成功");

                            MLVoiceSynthetize.startSynthesize(R.string.cancel_success);
                        }
                    });

        } else if (sign == 3) {
            Box.getRetrofit(DoctorAPI.class)
                    .cancelReservation(list.get(2).getRid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onNext(Object o) {
                            long time = 0;
                            try {
                                time = Long.parseLong(dateToStamp(list.get(2).getStart_time()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String times = String.valueOf(time - 60000);

                            if (models.size() != 0) {

                                for (int i = 0; i < models.size(); i++) {
                                    if (times.equals(models.get(i).getTimestamp() + "")) {
                                        models.get(i).delete();
                                        break;
                                    }
                                }
                            }

                            mTextView7.setText(null);
                            mTextView8.setText(null);
                            mLinearLayout3.setVisibility(View.INVISIBLE);

                            yuYueDoctor();


                            ShowNormals("取消成功");

                            MLVoiceSynthetize.startSynthesize(R.string.cancel_success);
                        }

                        @Override
                        protected void onError(ApiException ex) {
                            super.onError(ex);
                            ShowNormals("取消失败");
                        }
                    });
        }
    }

    private void cancelReservation(int rid) {
        Box.getRetrofit(DoctorAPI.class)
                .cancelReservation(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    public void ShowNormals(String str) {
        new SingleButtonDialog(this)
                .builder()
                .setMsg(str)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }


}
