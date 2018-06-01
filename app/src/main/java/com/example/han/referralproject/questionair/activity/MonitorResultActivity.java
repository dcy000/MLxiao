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
    private static final String FEATURE = "feature";
    private static final String TIAOYANG = "tiaoyang";
    private String resultScores;
    private String resultType;
    private String featrue;
    private String tiaoyang;

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
        tiaoyang = getIntent().getStringExtra(TIAOYANG);
        featrue= getIntent().getStringExtra(FEATURE);
    }


    public static void starMe(Context context,  String resultType,String scores,String feature,String tiaoyao) {
        Intent intent = new Intent(context, MonitorResultActivity.class);
        intent.putExtra(SCORES, scores);
        intent.putExtra(RESULT_TYPE, resultType);
        intent.putExtra(FEATURE, feature);
        intent.putExtra(TIAOYANG, tiaoyao);
        context.startActivity(intent);
    }

    private void initView() {
        TextView result = findViewById(R.id.tv_result);
        TextView description = findViewById(R.id.tv_description);
        result.setText(resultType);
        description.setText(featrue);


        TextView scor = findViewById(R.id.tv_score);
        TextView tiaoyang = findViewById(R.id.tv_tiaoyang);
        scor.setText(resultScores);
        tiaoyang.setText(this.tiaoyang);

    }

}
