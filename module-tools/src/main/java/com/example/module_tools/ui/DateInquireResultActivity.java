package com.example.module_tools.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateInquireResultActivity extends ToolbarBaseActivity {


    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_question)
    TextView tvQuestion;
    @BindView(R2.id.tv_anwser)
    TextView tvAnwser;
    @BindView(R2.id.tv_back)
    TextView tvBack;
    private Intent intent;

    public static void startMe(Context context, String quesiton, String anwser) {
        Intent intent = new Intent(context, DateInquireResultActivity.class);
        intent.putExtra("quesiton", quesiton);
        intent.putExtra("anwser", anwser);
        context.startActivity(intent);
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_date_inquire_result;
    }

    @Override
    public void initParams(Intent intentArgument) {
        intent = intentArgument;
        MLVoiceSynthetize.startSynthesize(intent.getStringExtra("anwser"));
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        tvAnwser.setText(intent.getStringExtra("quesiton"));
        String anwser = intent.getStringExtra("anwser");
        tvQuestion.setText(anwser);
        initEvent();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
