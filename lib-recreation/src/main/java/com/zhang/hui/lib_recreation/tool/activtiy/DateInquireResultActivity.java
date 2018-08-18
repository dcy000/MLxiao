package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;

public class DateInquireResultActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 日期查询
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    private TextView tvQuestion;
    private TextView tvAnwser;
    private Intent intent;

    public static void startMe(Context context, String quesiton, String anwser) {
        Intent intent = new Intent(context, DateInquireResultActivity.class);
        intent.putExtra("quesiton", quesiton);
        intent.putExtra("anwser", anwser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_inquire_result);
        intent = getIntent();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),intent.getStringExtra("anwser"),false);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvAnwser = (TextView) findViewById(R.id.tv_anwser);

        tvAnwser.setText(intent.getStringExtra("quesiton"));
        String anwser = intent.getStringExtra("anwser");
        tvQuestion.setText(anwser);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_back) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
