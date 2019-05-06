package com.gcml.module_edu_child.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_edu_child.R;
import com.sjtu.yifei.route.Routerfit;

public class ChildEduCartoonActivity extends ToolbarBaseActivity {

    private RadioGroup rgTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce_activity_cartoon);
        mToolbar.setVisibility(View.GONE);
        findViewById(R.id.ce_common_iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rgTabs = findViewById(R.id.ce_cartoon_rg_tabs);
        rgTabs.setOnCheckedChangeListener(onCheckedChangeListener);
        rgTabs.check(R.id.ce_cartoon_rb_tab_cartoon);
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (R.id.ce_cartoon_rb_tab_cartoon == checkedId) {
                Fragment videoListFragment = Routerfit.register(AppRouter.class).getVideoListFragmentProvider().getVideoListFragment(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.ce_cartoon_fl_content, videoListFragment).commitAllowingStateLoss();
            }
        }
    };

}
