package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.imageloader.ImageLoader;
import com.gcml.lib_widget.MixtureTextView;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.xfparsebean.BaiKeBean;

import java.io.Serializable;
import java.util.List;

public class BaikeResultActivity extends AppCompatActivity implements View.OnClickListener {

    private List<BaiKeBean> data;
    /**
     * 百科
     */
    private TextView mTvTitle;
    /**
     * 返回
     */
    private TextView mTvBack;
    private ImageView mImgPic;
    private MixtureTextView mMtResult;
    /**
     * 周星驰的百科
     */
    private TextView mTvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike_result);
        data = (List<BaiKeBean>) getIntent().getSerializableExtra("data");
        initView();
        initdata();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), data.get(0).summary, false);

    }


    public static void startMe(Context context, List<BaiKeBean> data, String question) {
        Intent intent = new Intent(context, BaikeResultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
        MLVoiceRecognize.stop();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setOnClickListener(this);
        mImgPic = (ImageView) findViewById(R.id.img_pic);
        mMtResult = findViewById(R.id.mt_result);
        mTvQuestion = (TextView) findViewById(R.id.tv_question);
    }

    private void initdata() {
        ImageLoader.Options options = ImageLoader.newOptionsBuilder(mImgPic, data.get(0).img).build();
        ImageLoader.instance().load(options);

        mTvQuestion.setText(getIntent().getStringExtra("question"));
        mMtResult.setText(data.get(0).summary);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_back) {
            finish();
        }
    }
}

