package com.gcml.health.measure.health_report_form;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.health.measure.R;
import com.gcml.health.measure.cc.CCAppActions;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/29 13:56
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormDetailActivity extends ToolbarBaseActivity {
    public static final String KEY_RESULT = "key_result";
    public static final String KEY_ADVICE = "key_advice";
    private TextView mTvTitle;
    private TextView mTvHeadlineInfluencingFactors;
    private TextView mView1;
    private TextView mTvAdvice;

    public static void startActivity(Context context, String result, String advice) {
        Intent intent = new Intent(context, HealthReportFormDetailActivity.class);
        intent.putExtra(KEY_RESULT, result);
        intent.putExtra(KEY_ADVICE, advice);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form_detail);
        initView();
        setData();
    }

    private void setData() {
        String result = getIntent().getStringExtra(KEY_RESULT);
        String advice = getIntent().getStringExtra(KEY_ADVICE);
        mTvTitle.setText(result);
        mTvAdvice.setText(advice);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), result + "，" + UM.getString(R.string.guidance_advice) + advice, false);
    }

    @Override
    protected void backMainActivity() {
        CCAppActions.jump2MainActivity();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvHeadlineInfluencingFactors = (TextView) findViewById(R.id.tv_headline_influencing_factors);
        mView1 = (TextView) findViewById(R.id.view_1);
        mTvAdvice = (TextView) findViewById(R.id.tv_advice);
        mTitleText.setText(R.string.title_evaluation_result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
