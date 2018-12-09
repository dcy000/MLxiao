package com.example.module_child_edu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.module_child_edu.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.wake.MLVoiceWake;

/**
 * 动画片
 */
public class ChildEduCartoonActivity extends ToolbarBaseActivity {

    private RadioGroup rgTabs;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.ce_activity_cartoon;
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.ce_cartoon_rb_tab_cartoon == checkedId) {
                emitEvent("ShowVideoListFragment",getSupportFragmentManager(), R.id.ce_cartoon_fl_content, 3);
            }
        }
    };

    @Override
    protected void onResume() {
        MLVoiceWake.startWakeUp(null);
        super.onResume();
    }
}
