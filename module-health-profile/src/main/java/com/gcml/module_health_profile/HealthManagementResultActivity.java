package com.gcml.module_health_profile;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_health_profile.bean.TiZhiBean;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

public class HealthManagementResultActivity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private LinearLayout container1;
    private List<TiZhiBean.ConstitutionListBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tizhi_result);
        mToolbar.setVisibility(View.GONE);
        initTitle();
        initView();
    }

    private void initView() {
        container1 = findViewById(R.id.container);

        data = (List<TiZhiBean.ConstitutionListBean>) getIntent().getSerializableExtra("result_data");
        if (data != null & data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                TiZhiBean.ConstitutionListBean itemBean = data.get(i);
                if (!"否".equals(itemBean.result)) {
                    TextView item = new TextView(this);
                    initTextViewParams(item);
                    item.setText("体质类型:" + itemBean.constitutionName + "       得分:" + itemBean.score + "       " + itemBean.result);
                    container1.addView(item);
                }
            }
        }
    }

    private void initTextViewParams(TextView item) {
        item.setTextSize(32);
        item.setPadding(20, 20, 20, 20);

    }

    private void initTitle() {
        tb = findViewById(R.id.tb_tizhi);
        tb.setData("中 医 体 质 辨 识",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });
        setWifiLevel(tb);
    }


}
