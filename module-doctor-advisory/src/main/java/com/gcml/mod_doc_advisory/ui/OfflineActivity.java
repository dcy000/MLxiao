package com.gcml.mod_doc_advisory.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.mod_doc_advisory.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
@Route(path = "/app/activity/offline/activity")
public class OfflineActivity extends ToolbarBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mToolbar.setVisibility(View.VISIBLE);
        mLeftText.setText("线下绑定");
        mRightText.setText("暂不绑定");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),getString(R.string.user_help));
    }

}
