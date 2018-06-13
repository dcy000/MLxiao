package com.example.han.referralproject.olderhealthmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.olderhealthmanagement.bean.HealthManagementResultBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthManagementResultActivity extends BaseActivity {

    @BindView(R.id.container)
    LinearLayout container;
    private List<HealthManagementResultBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_management_result);
        ButterKnife.bind(this);
        initTitle();
        initView();
    }

    private void initView() {
        data = (List<HealthManagementResultBean.DataBean>) getIntent().getSerializableExtra("result_data");
        if (data != null & data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                HealthManagementResultBean.DataBean itemBean = data.get(i);
                if ("是".equals(itemBean.result)) {
                    TextView item = new TextView(this);
                    initTextViewParams(item);
                    item.setText("体质类型:" + itemBean.constitutionName + "       得分:" + itemBean.score);
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
        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);
    }


}
