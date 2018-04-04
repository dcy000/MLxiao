package com.example.han.referralproject.children.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class ChildEduStudyActivity extends BaseActivity {

    private ImageView ivBrainTeaser;
    private ImageView ivPoems;
    private ImageView ivStories;
    private ImageView ivWhy;
    private ImageView ivWords;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_study);
        ivBack = (ImageView) findViewById(R.id.ce_common_iv_back);
        ivBrainTeaser = (ImageView) findViewById(R.id.ce_study_iv_tab_brain_teaser);
        ivPoems = (ImageView) findViewById(R.id.ce_study_iv_tab_poems);
        ivStories = (ImageView) findViewById(R.id.ce_study_tv_tab_stories);
        ivWhy = (ImageView) findViewById(R.id.ce_study_iv_tab_why);
        ivWords = (ImageView) findViewById(R.id.ce_study_iv_tab_words);

        ivWhy.setEnabled(false);
        ivWords.setEnabled(false);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivPoems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduPoemListActivity.class);
                startActivity(intent);
            }
        });

        ivWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduWordsActivity.class);
                startActivity(intent);
            }
        });

        ivBrainTeaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduBrainTeaserActivity.class);
                startActivity(intent);
            }
        });

        ivStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduStories2Activity.class);
                startActivity(intent);
            }
        });

        ivWhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = intent.setClass(ChildEduStudyActivity.this, ChildEduWhyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        setEnableListeningLoop(false);
        setDisableGlobalListen(false);
        super.onResume();
    }
}
