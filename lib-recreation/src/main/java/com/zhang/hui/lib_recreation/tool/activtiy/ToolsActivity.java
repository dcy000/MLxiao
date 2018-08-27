package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.zhang.hui.lib_recreation.R;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar tlbTitle;
    private ImageView ivJiemeng;
    private ImageView ivJinjintian;
    private ImageView ivRiqiChaxun;
    private ImageView ivCaipu;
    private ImageView ivJisuan;
    private ImageView ivBaike;
    private ImageView ivCaimi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreation_tool);
        initView();
    }


    private void initView() {
        tlbTitle = (TranslucentToolBar) findViewById(R.id.tb_tool_title);
        ivJiemeng = (ImageView) findViewById(R.id.iv_jiemeng);
        ivJiemeng.setOnClickListener(this);
        ivJinjintian = (ImageView) findViewById(R.id.iv_jinjintian);
        ivJinjintian.setOnClickListener(this);
        ivRiqiChaxun = (ImageView) findViewById(R.id.iv_riqi_chaxun);
        ivRiqiChaxun.setOnClickListener(this);
        ivCaipu = (ImageView) findViewById(R.id.iv_caipu);
        ivCaipu.setOnClickListener(this);
        ivJisuan = (ImageView) findViewById(R.id.iv_jisuan);
        ivJisuan.setOnClickListener(this);
        ivBaike = (ImageView) findViewById(R.id.iv_baike);
        ivBaike.setOnClickListener(this);
        ivCaimi = (ImageView) findViewById(R.id.iv_caimi);
        ivCaimi.setOnClickListener(this);

        tlbTitle.setData("症 状 自 查", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_jiemeng) {
            startActivity(new Intent(this, JieMengActivity.class));
        } else if (i == R.id.iv_jinjintian) {
            startActivity(new Intent(this, HistoryTodayActivity.class));
        } else if (i == R.id.iv_riqi_chaxun) {
            startActivity(new Intent(this, DateInquireActivity.class));
        } else if (i == R.id.iv_caipu) {
            startActivity(new Intent(this, CookBookActivity.class));
        } else if (i == R.id.iv_jisuan) {
            startActivity(new Intent(this, BaikeActivity.class));
        } else if (i == R.id.iv_baike) {
            startActivity(new Intent(this, CalculationActivity.class));
        } else if (i == R.id.iv_jisuan) {
            startActivity(new Intent(this, RiddleActivit.class));
        }
    }


}
