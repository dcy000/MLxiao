package com.example.han.referralproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.speechsynthesis.XFSkillApi;
import com.example.han.referralproject.speechsynthesis.xfparsebean.WeatherBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.textView4)
    EditText textView4;
    @BindView(R.id.textView5)
    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textView5)
    public void onViewClicked() {
        XFSkillApi.getSkillData(textView4.getText().toString().trim(), new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(final Object anwser) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView5.setText(anwser.toString());
                    }
                });
            }
        });
    }
}
