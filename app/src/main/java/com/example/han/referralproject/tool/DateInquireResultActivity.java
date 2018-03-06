package com.example.han.referralproject.tool;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.han.referralproject.R;

import java.io.Serializable;

public class DateInquireResultActivity extends AppCompatActivity {

    private Intent intent;

    public static void startMe(Context context, String quesiton, String anwser){
        Intent intent = new Intent(context, CookBookResultActivity.class);
        intent.putExtra("quesiton",quesiton);
        intent.putExtra("anwser",anwser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_inquire_result);
        intent = getIntent();
    }


}
