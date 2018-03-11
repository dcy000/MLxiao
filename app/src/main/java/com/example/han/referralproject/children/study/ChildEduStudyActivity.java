package com.example.han.referralproject.children.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class ChildEduStudyActivity extends BaseActivity {

    private TextView tvTurnOfMind;
    private TextView tvPoems;
    private TextView tvStories;
    private TextView tvWhy;
    private TextView tvWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_study);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("儿  童  幼  教");
        tvTurnOfMind = (TextView) findViewById(R.id.ce_study_tv_indicator_turn_of_mind);
        tvPoems = (TextView) findViewById(R.id.ce_study_tv_indicator_poems);
        tvStories = (TextView) findViewById(R.id.ce_study_tv_indicator_stories);
        tvWhy = (TextView) findViewById(R.id.ce_study_tv_indicator_why);
        tvWords = (TextView) findViewById(R.id.ce_study_tv_indicator_words);

        tvPoems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduPoemListActivity.class);
                startActivity(intent);
            }
        });

        tvWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduWordsActivity.class);
                startActivity(intent);
            }
        });

        tvTurnOfMind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduBrainTeaserActivity.class);
                startActivity(intent);
            }
        });

        tvStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduStoriesActivity.class);
                startActivity(intent);
            }
        });

        tvWhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduWhyActivity.class);
                startActivity(intent);
            }
        });
    }
}
