package com.example.han.referralproject.questionair.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;


public class MonitorResultActivity extends BaseActivity {
    private static final String SCORES = "scores";
    private static final String RESULT_TYPE = "result_type";
    private String resultScores;
    private String resultType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_result);
        initTitle();
        initData();
        initView();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测报告");
    }

    private void initData() {
        resultScores = getIntent().getStringExtra(SCORES);
        resultType = getIntent().getStringExtra(RESULT_TYPE);
    }


    public static void starMe(Context context, String scores, String resultType) {
        Intent intent = new Intent(context, MonitorResultActivity.class);
        intent.putExtra(SCORES, scores);
        intent.putExtra(RESULT_TYPE, resultType);
        context.startActivity(intent);
    }

    private void initView() {
        TextView result = findViewById(R.id.tv_result);
        TextView description = findViewById(R.id.tv_description);

//        Random random = new Random();
//        int index = random.nextInt(9);
//
//        String resultInfo = results[index];
//        result.setText(resultInfo.split("-")[0]);

        result.setText(resultType);
        description.setText(resultScores);
    }

}
