package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.bean.YuYueInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.recharge.PayInfoActivity;
import com.example.han.referralproject.util.Utils;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call.EMUIHelper;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.call2.NimCallActivity;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoctorappoActivity extends BaseActivity  implements View.OnClickListener{

    SharedPreferences sharedPreferences1;

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


                        mTextView.setText(list.get(0).getStart_time());
                        mTextView1.setText(list.get(0).getEnd_time());


                        mTextView2.setText(list.get(1).getStart_time());
                        mTextView6.setText(list.get(1).getEnd_time());


                        mTextView7.setText(list.get(2).getStart_time());
                        mTextView8.setText(list.get(2).getEnd_time());


                    /*    setAlarmClock(0);
                        setAlarmClock(1);
                        setAlarmClock(2);*/


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


                        long time = 0;
                        long time1 = 0;

                        try {
                            time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                            time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        enableVideo(time1, time - 60000);

                        long time2 = 0;
                        long time3 = 0;

                        try {
                            time2 = Long.parseLong(dateToStamp(list.get(1).getStart_time()));
                            time3 = Long.parseLong(dateToStamp(list.get(1).getEnd_time()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        enableVideo(time3, time2 - 60000);


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


                    } else if (list.size() == 2) {


                        models = DataSupport.findAll(AlarmModel.class);

                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.VISIBLE);
                        mLinearLayout3.setVisibility(View.GONE);


                        mTextView.setText(list.get(0).getStart_time());
                        mTextView1.setText(list.get(0).getEnd_time());

                        mTextView2.setText(list.get(1).getStart_time());
                        mTextView6.setText(list.get(1).getEnd_time());

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


                        long time = 0;
                        long time1 = 0;

                        try {
                            time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                            time1 = Long.parseLong(dateToStamp(list.get(0).getEnd_time()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        enableVideo(time1, time - 60000);

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


                    } else if (list.size() == 1) {

                        models = DataSupport.findAll(AlarmModel.class);

                        mLinearLayout1.setVisibility(View.VISIBLE);
                        mLinearLayout2.setVisibility(View.GONE);
                        mLinearLayout3.setVisibility(View.GONE);

                        mTextView.setText(list.get(0).getStart_time());
                        mTextView1.setText(list.get(0).getEnd_time());

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

                    break;
            }

            return true;
        }
    });


    public void enableVideo(long time, long time1) {


        if (System.currentTimeMillis() < time && System.currentTimeMillis() >= time1) {
            mButtons.setEnabled(true);
            mButtons.setSelected(true);

        } else {
            mButtons.setEnabled(false);
            mButtons.setSelected(false);
        }

    }

    public void setAlarmClock(int i) {

        if (!"".equals(list.get(i).getStart_time())) {
            long time = 0;
            try {
                time = Long.parseLong(dateToStamp(list.get(i).getStart_time()));
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
                    } else {

                        if (sign == true && a == list.size() - 1) {

                            AlarmHelper.setupAlarm(DoctorappoActivity.this, Long.parseLong(times), getString(R.string.doctor_alarm), 1 + "");

                        }
                    }


                }

            }


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String nimUserId = MyApplication.getInstance().nimUserId();
        NimAccountHelper.getInstance().login(nimUserId, "123456", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorappo);


        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.doctor_qianyue));



        //  speak(R.string.yuyue_1);

        dialog1 = new NDialog2(DoctorappoActivity.this);

        mButtons = (Button) findViewById(R.id.video_doctor);
        mButtons.setEnabled(false);
        mButtons.setSelected(false);

        mButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NimCallActivity.launch(DoctorappoActivity.this, "docter_" + doctorId);
                finish();
            }
        });






        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);

        doctorId = sharedPreferences1.getString("doctor_id", "");

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

        if (!TextUtils.isEmpty(sharedPreferences1.getString("docter_photo", ""))) {
            Picasso.with(this)
                    .load(sharedPreferences1.getString("docter_photo", ""))
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .tag(this)
                    .fit()
                    .into(circleImageView);
        }

        mTextView3.setText(sharedPreferences1.getString("name", ""));
        mTextView4.setText("职级：" + sharedPreferences1.getString("position", ""));
        mTextView5.setText("擅长：" + sharedPreferences1.getString("feature", ""));

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NimCallActivity.launch(DoctorappoActivity.this, "docter_" + doctorId);
                finish();
            }
        });

        mButton1 = (Button) findViewById(R.id.add_yuyue);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(mTextView.getText().toString()) ||
                        "".equals(mTextView2.getText().toString()) ||
                        "".equals(mTextView7.getText().toString())) {
                    Intent intent = new Intent(getApplicationContext(), AddAppoActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "签约已达上限", Toast.LENGTH_SHORT).show();
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



    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    List<YuYueInfo> list = new ArrayList<YuYueInfo>();

    private String doctorId;

    @Override
    protected void onStart() {
        super.onStart();

        yuYueDoctor();

    }

    public void yuYueDoctor() {


        NetworkApi.YuYue_info(MyApplication.getInstance().userId, doctorId, new NetworkManager.SuccessCallback<ArrayList<YuYueInfo>>() {
            @Override
            public void onSuccess(ArrayList<YuYueInfo> response) {

                // MyApplication.getInstance().userId
                // sharedPreferences1.getString("doctor_id", "")


                list.clear();
                list.addAll(response);
                mHandler.sendEmptyMessage(0);

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });


    }


    NDialog1 dialog;
    NDialog2 dialog1;


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


                                NetworkApi.YuYue_cancel(list.get(0).getRid() + "", new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {

                                        long time = 0;
                                        try {
                                            time = Long.parseLong(dateToStamp(list.get(0).getStart_time()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        String times = String.valueOf(time - 60000);


                                        /*DataSupport.deleteAllAsync(AlarmModel.class, "timestamp=?", times)
                                                .listen(new UpdateOrDeleteCallback() {
                                                    @Override
                                                    public void onFinish(int rowsAffected) {
                                                        if (rowsAffected >= 1) {

                                                            mTextView.setText("");
                                                            mTextView1.setText("");
                                                            mLinearLayout1.setVisibility(View.INVISIBLE);
                                                            ShowNormals("取消"成功);

                                                        }

                                                    }
                                                });*/

                                        models = DataSupport.findAll(AlarmModel.class);


                                        if (models.size() != 0) {

                                            for (int i = 0; i < models.size(); i++) {
                                                if (times.equals(models.get(i).getTimestamp() + "")) {
                                                    models.get(i).delete();
                                                    break;
                                                }
                                            }



                                        }


                                        mTextView.setText("");
                                        mTextView1.setText("");
                                        mLinearLayout1.setVisibility(View.INVISIBLE);
                                        yuYueDoctor();

                                        ShowNormals("取消成功");


                                    }

                                }, new NetworkManager.FailedCallback() {
                                    @Override
                                    public void onFailed(String message) {
                                        ShowNormals("取消失败");


                                    }
                                });


                            } else if (sign == 2) {


                                NetworkApi.YuYue_cancel(list.get(1).getRid() + "", new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {

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

                                        mTextView2.setText("");
                                        mTextView6.setText("");
                                        mLinearLayout2.setVisibility(View.INVISIBLE);
                                        yuYueDoctor();


                                        ShowNormals("取消成功");

                                        /*DataSupport.deleteAllAsync(AlarmModel.class, "timestamp=?", times)
                                                .listen(new UpdateOrDeleteCallback() {
                                                    @Override
                                                    public void onFinish(int rowsAffected) {
                                                        if (rowsAffected >= 1) {

                                                            mTextView2.setText("");
                                                            mTextView6.setText("");
                                                            mLinearLayout2.setVisibility(View.INVISIBLE);
                                                            ShowNormals("取消成功");

                                                        }

                                                    }
                                                });*/


                                    }

                                }, new NetworkManager.FailedCallback() {
                                    @Override
                                    public void onFailed(String message) {
                                        ShowNormals("取消失败");


                                    }
                                });


                            } else if (sign == 3) {


                                NetworkApi.YuYue_cancel(list.get(2).getRid() + "", new NetworkManager.SuccessCallback<String>() {
                                    @Override
                                    public void onSuccess(String response) {

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

                                        mTextView7.setText("");
                                        mTextView8.setText("");
                                        mLinearLayout3.setVisibility(View.INVISIBLE);

                                        yuYueDoctor();


                                        ShowNormals("取消成功");

                                       /* DataSupport.deleteAllAsync(AlarmModel.class, "timestamp=?", times)
                                                .listen(new UpdateOrDeleteCallback() {
                                                    @Override
                                                    public void onFinish(int rowsAffected) {
                                                        if (rowsAffected >= 1) {

                                                            mTextView7.setText("");
                                                            mTextView8.setText("");
                                                            mLinearLayout3.setVisibility(View.INVISIBLE);
                                                            ShowNormals("取消成功");
                                                        }

                                                    }
                                                });*/


                                    }

                                }, new NetworkManager.FailedCallback() {
                                    @Override
                                    public void onFailed(String message) {

                                        ShowNormals("取消失败");

                                    }
                                });


                            }

                        }

                    }
                }).create(NDialog.CONFIRM).show();
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

                    }
                }).create(NDialog.CONFIRM).show();

    }


}
