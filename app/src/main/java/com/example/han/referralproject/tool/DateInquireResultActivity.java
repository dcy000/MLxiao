package com.example.han.referralproject.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateInquireResultActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_anwser)
    TextView tvAnwser;
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
        ButterKnife.bind(this);
        intent = getIntent();
        initView();
        initEvent();
        speak(intent.getStringExtra("anwser"));
    }

    private void initEvent() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tvAnwser.setText(intent.getStringExtra("quesiton"));
        String anwser = intent.getStringExtra("anwser");
        tvQuestion.setText(anwser);
    }

}
