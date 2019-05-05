package com.gcml.module_health_record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IECG_PDF_FragmentProvider;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
@Route(path = "/health/record/show/pdf")
public class ShowPDFActivity extends ToolbarBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);
        mTitleText.setText("心 电 报 告");
        IECG_PDF_FragmentProvider url = Routerfit.register(AppRouter.class).getECG_PDF_Fragment(getIntent().getStringExtra("url"));
        Fragment fragment = url.getECG_PDF_Fragment();
        if (fragment == null) {
            finish();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
        }
    }
}
