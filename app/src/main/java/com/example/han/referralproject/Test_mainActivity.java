package com.example.han.referralproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.activity.SelectXuetangTimeActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.temperature.TemperatureActivity;
import com.example.han.referralproject.xindian.XinDianDetectActivity;
import com.example.han.referralproject.xindian.XindianActivity;
import com.example.han.referralproject.xuetang.XuetangActivity;
import com.example.han.referralproject.xueya.AttentionActivity;
import com.example.han.referralproject.xueya.XueyaActivity;
import com.example.han.referralproject.xueyang.XueyangActivity;

import java.util.Calendar;

public class Test_mainActivity extends BaseActivity implements View.OnClickListener {

    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    ImageView mImageView6;

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        mToolbar.setVisibility(View.VISIBLE);




        mImageView1 = (ImageView) findViewById(R.id.test_xueya);
        mImageView2 = (ImageView) findViewById(R.id.test_xueyang);
        mImageView3 = (ImageView) findViewById(R.id.test_wendu);
        mImageView4 = (ImageView) findViewById(R.id.test_xuetang);
        mImageView5 = (ImageView) findViewById(R.id.test_xindian);
        mImageView6 = (ImageView) findViewById(R.id.test_qita);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);
        mImageView6.setOnClickListener(this);

        speak(R.string.tips_test);
        registerReceiver(mReceiver, new IntentFilter("change_account"));

    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "change_account":
//                    if (mChangeAccountDialog != null) {
//                        mChangeAccountDialog.dismiss();
//                    }
//                    getData();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;


            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.test_xueya:
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "xueya");
                    startActivity(intent);
                    break;
                case R.id.test_xueyang:
                    intent.setClass(getApplicationContext(), DetectActivity.class);
                    intent.putExtra("type", "xueyang");
                    startActivity(intent);
                    break;
                case R.id.test_wendu:
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "wendu");
                    startActivity(intent);
                    break;
                case R.id.test_xuetang:
                    intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
                    intent.putExtra("type", "xuetang");
                    startActivity(intent);
                    break;
                case R.id.test_xindian:
                    intent.setClass(getApplicationContext(), XinDianDetectActivity.class);
                    startActivity(intent);
                    break;
                case R.id.test_qita:
                    //intent.setClass(getApplicationContext(), XueyaActivity.class);
                    intent.setClass(mContext, DetectActivity.class);
                    intent.putExtra("type", "sanheyi");
                    startActivity(intent);
                    break;
            }
        }


    }

    @Override
    protected void onSpeakListenerResult(String result) {
//        String inSpell = PinYinUtils.converterToSpell(result);
//        Intent intent = new Intent();
//        if(inSpell.matches(".*((xie|xue)ya).*")){
//            intent.setClass(mContext, DetectActivity.class);
//            intent.putExtra("type", "xueya");
//        }else if(inSpell.matches(".*((xie|xue)yang).*")){
//            intent.setClass(getApplicationContext(), DetectActivity.class);
//            intent.putExtra("type", "xueyang");
//        }else if(inSpell.matches(".*(tiwen).*")){
//            intent.setClass(mContext, DetectActivity.class);
//            intent.putExtra("type", "wendu");
//
//        }else if(inSpell.matches(".*((xie|xue)tang).*")){
//            intent.setClass(getApplicationContext(), SelectXuetangTimeActivity.class);
//            intent.putExtra("type", "xuetang");
//        }else if(inSpell.matches(".*((xin|xing)dian).*")){
//            intent.setClass(getApplicationContext(), XinDianDetectActivity.class);
//        }else if(inSpell.matches(".*((san|shan)heyi|(xie|xue)niao(suan|shuan)|dangu(cun|chun)).*")){
//            intent.setClass(mContext, DetectActivity.class);
//            intent.putExtra("type", "sanheyi");
//        }
//        startActivity(intent);
    }
}
