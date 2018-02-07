package com.example.han.referralproject.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;

public class TheOldVideoActivity extends AppCompatActivity {

    private RadioGroup rgVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_old_video);
        rgVideos = (RadioGroup) findViewById(R.id.old_rg_tabs_video);
        rgVideos.setOnCheckedChangeListener(onCheckedChangeListener);
        findViewById(R.id.old_tv_video_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.old_rb_video == checkedId) {
                VideoListFragment.addOrShow(getSupportFragmentManager(), R.id.old_video_fl_content, 1);
            }
        }
    };
}
