package com.example.module_child_edu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.module_child_edu.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.wake.MLVoiceWake;

/**
 * 儿童幼教
 */
public class ChildEduStudyActivity extends ToolbarBaseActivity {

    private ImageView ivBrainTeaser;
    private ImageView ivPoems;
    private ImageView ivStories;
    private ImageView ivWhy;
    private ImageView ivWords;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.ce_activity_study;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
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
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    protected void onResume() {
        MLVoiceWake.startWakeUp(null);
        super.onResume();
    }
}
