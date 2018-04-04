package com.example.han.referralproject.children.cartoon;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.video.VideoListFragment;

public class ChildEduCartoonActivity extends BaseActivity {

    private RadioGroup rgTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_cartoon);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rgTabs = (RadioGroup) findViewById(R.id.ce_cartoon_rg_tabs);
        rgTabs.setOnCheckedChangeListener(onCheckedChangeListener);
        rgTabs.check(R.id.ce_cartoon_rb_tab_cartoon);
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.ce_cartoon_rb_tab_cartoon == checkedId) {
                VideoListFragment.addOrShow(getSupportFragmentManager(), R.id.ce_cartoon_fl_content, 3);
            }
        }
    };

    @Override
    protected void onResume() {
        setDisableGlobalListen(false);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
