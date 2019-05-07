package com.gcml.module_hypertension_manager.zhongyi;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_hypertension_manager.R;
import com.gcml.module_hypertension_manager.zhongyi.bean.HealthManagementResultBean;

import java.util.List;

public class HealthManagementResultActivity extends ToolbarBaseActivity {

    LinearLayout container;
    private List<HealthManagementResultBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_management_result);
        container=findViewById(R.id.container);
        initTitle();
        initView();
    }

    private void initView() {
        data = (List<HealthManagementResultBean.DataBean>) getIntent().getSerializableExtra("result_data");
        if (data != null & data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                HealthManagementResultBean.DataBean itemBean = data.get(i);
                if (!"否".equals(itemBean.result)) {
                    TextView item = new TextView(this);
                    initTextViewParams(item);
                    item.setText("体质类型:" + itemBean.constitutionName + "       得分:" + itemBean.score+"       "+itemBean.result);
                    container.addView(item);
                }
            }
        }
    }

    private void initTextViewParams(TextView item) {
        item.setTextSize(32);
        item.setPadding(20, 20, 20, 20);

    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测报告");
    }


}
