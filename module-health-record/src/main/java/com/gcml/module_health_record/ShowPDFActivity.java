package com.gcml.module_health_record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_health_record.cc.CCAppActions;

public class ShowPDFActivity extends ToolbarBaseActivity {
    public static void startActivity(Context context, String url) {
        context.startActivity(new Intent(context, ShowPDFActivity.class)
                .putExtra("url", url));
    }

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        mTitleText.setText("心 电 报 告");
        CC.obtainBuilder("health_measure")
                .setActionName("getECG_PDF_Fragment")
                .addParam("url", getIntent().getStringExtra("url"))
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result != null) {
                            fragment = result.getDataItem("fragment");
                        }
                        if (fragment == null) {
                            finish();
                        } else {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frame, fragment)
                                    .commit();
                        }
                    }
                });

    }

    @Override
    protected void backMainActivity() {
        super.backMainActivity();
//        CCAppActions.jump2MainActivity();
    }
}
