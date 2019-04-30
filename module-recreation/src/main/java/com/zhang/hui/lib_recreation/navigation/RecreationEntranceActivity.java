package com.zhang.hui.lib_recreation.navigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.billy.cc.core.component.CC;
import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.activtiy.ToolsActivity;
@Route(path = "/recreation/entrance/activity")
public class RecreationEntranceActivity extends AppCompatActivity implements View.OnClickListener {


    private TranslucentToolBar tbTitle;
    private ImageView llYule;
    private ImageView llYoujiao;
    private ImageView llTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreation_entrance_new);
        initView();
        initTitle();
    }

    private void initTitle() {
        tbTitle.setData("娱 乐 中 心", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
            }
        });

        MLVoiceSynthetize.startSynthesize(this, "欢迎来到娱乐中心");
    }

    private void initView() {
        tbTitle = (TranslucentToolBar) findViewById(R.id.tb_title_fun);
        llYule = (ImageView) findViewById(R.id.ll_yule);
        llYule.setOnClickListener(this);
        llYoujiao = (ImageView) findViewById(R.id.ll_youjiao);
        llYoujiao.setOnClickListener(this);
        llTools = (ImageView) findViewById(R.id.ll_tools);
        llTools.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_yule) {
            //老人娱乐
            Routerfit.register(AppRouter.class).skipTheOldHomeActivity();
        } else if (i == R.id.ll_youjiao) {
            //儿童幼教
            Routerfit.register(AppRouter.class).skipChildEduHomeActivity();
        } else if (i == R.id.ll_tools) {
            startActivity(new Intent(this, ToolsActivity.class));
        }
    }
}
