package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.other.XFSkillApi;
import com.zhang.hui.lib_recreation.tool.wrapview.VoiceLineView;
import com.zhang.hui.lib_recreation.tool.xfparsebean.BaiKeBean;

import java.util.List;

public class BaikeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 百科
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    /**
     * 互动百科，解决您的问题
     */
    private TextView tvNotice;
    /**
     * 您可以这样提问：
     */
    private TextView tvNoticeDemo;
    /**
     * 周星驰的百科。
     */
    private TextView tvDemo1;
    /**
     * 百科催眠大师。
     */
    private TextView tvDemo2;
    /**
     * 看看成都的百科。
     */
    private TextView tvDemo3;
    private ImageView ivYuyin;
    /**
     * 按下请说话
     */
    private TextView textView4;
    private VoiceLineView vlWave;
    private ConstraintLayout clStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        initView();
        speak("主人,欢迎来到百科");
        initEvent();
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        tvNotice.setOnClickListener(this);
        tvNoticeDemo = (TextView) findViewById(R.id.tv_notice_demo);
        tvNoticeDemo.setOnClickListener(this);
        tvDemo1 = (TextView) findViewById(R.id.tv_demo1);
        tvDemo1.setOnClickListener(this);
        tvDemo2 = (TextView) findViewById(R.id.tv_demo2);
        tvDemo2.setOnClickListener(this);
        tvDemo3 = (TextView) findViewById(R.id.tv_demo3);
        tvDemo3.setOnClickListener(this);
        ivYuyin = (ImageView) findViewById(R.id.iv_yuyin);
        ivYuyin.setOnClickListener(this);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setOnClickListener(this);
        vlWave = (VoiceLineView) findViewById(R.id.vl_wave);
        vlWave.setOnClickListener(this);
        clStart = (ConstraintLayout) findViewById(R.id.cl_start);
        clStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_title) {
        } else if (i == R.id.tv_back) {
        } else if (i == R.id.tv_notice) {
        } else if (i == R.id.tv_notice_demo) {
        } else if (i == R.id.tv_demo1) {
            String demo1 = tvDemo1.getText().toString();
            getDreamData(demo1);

        } else if (i == R.id.tv_demo2) {
        } else if (i == R.id.tv_demo3) {
        } else if (i == R.id.iv_yuyin) {
        } else if (i == R.id.textView4) {
        } else if (i == R.id.vl_wave) {
        } else if (i == R.id.cl_start) {
        } else {
        }
    }

    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"baike".equals(service)) {
                    speak("主人,没有找到" + result);
                    return;
                }
                try {
                    List<BaiKeBean> data = (List<BaiKeBean>) anwser;
//                    BaikeResultActivity.startMe(BaiKeActivtiy.this, data, result);
                } catch (Exception e) {
                    speak("主人,没有找到" + result);
                }
            }
        });
    }
}


