package com.example.han.referralproject.yunlian;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.tool.BaiKeActivtiy;
import com.example.han.referralproject.tool.CookBookActivity;
import com.example.han.referralproject.tool.DateInquireActivity;
import com.example.han.referralproject.tool.HistoryTodayActivity;
import com.example.han.referralproject.tool.JieMengActivity;
import com.example.han.referralproject.tool.RiddleActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.ml.edu.OldRouter;

/**
 * Created by Administrator on 2018/4/26.
 */

public class EntertainmentCenterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView oldManHappy;
    private ImageView childrenHappy;
    private ImageView healthClassroom;
    private ImageView zhougongjiemeng;
    private ImageView lishijintian;
    private ImageView riqichaxun;
    private ImageView caipu;
    private ImageView baike;
    private ImageView caimi;
    private LinearLayout linearlayou2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_center);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("娱乐中心");

    }

    private void initView() {
        oldManHappy = (ImageView) findViewById(R.id.old_man_happy);
        oldManHappy.setOnClickListener(this);
        childrenHappy = (ImageView) findViewById(R.id.children_happy);
        childrenHappy.setOnClickListener(this);
        healthClassroom = (ImageView) findViewById(R.id.health_classroom);
        healthClassroom.setOnClickListener(this);
        zhougongjiemeng = (ImageView) findViewById(R.id.zhougongjiemeng);
        zhougongjiemeng.setOnClickListener(this);
        lishijintian = (ImageView) findViewById(R.id.lishijintian);
        lishijintian.setOnClickListener(this);
        riqichaxun = (ImageView) findViewById(R.id.riqichaxun);
        riqichaxun.setOnClickListener(this);
        caipu = (ImageView) findViewById(R.id.caipu);
        caipu.setOnClickListener(this);
        baike = (ImageView) findViewById(R.id.baike);
        baike.setOnClickListener(this);
        caimi = (ImageView) findViewById(R.id.caimi);
        caimi.setOnClickListener(this);
        linearlayou2 = (LinearLayout) findViewById(R.id.linearlayou2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.old_man_happy:
                OldRouter.routeToOldHomeActivity(this);
                break;
            case R.id.children_happy:
                startActivity(new Intent(this,ChildEduHomeActivity.class));
                break;
            case R.id.health_classroom:
                startActivity(new Intent(this,VideoListActivity.class));
                break;
            case R.id.zhougongjiemeng:
                startActivity(new Intent(this, JieMengActivity.class));
                break;
            case R.id.lishijintian:
                startActivity(new Intent(this, HistoryTodayActivity.class));
                break;
            case R.id.riqichaxun:
                startActivity(new Intent(this, DateInquireActivity.class));
                break;
            case R.id.caipu:
                startActivity(new Intent(this, CookBookActivity.class));
                break;
            case R.id.baike:
                startActivity(new Intent(this, BaiKeActivtiy.class));
                break;
            case R.id.caimi:
                startActivity(new Intent(this, RiddleActivity.class));
                break;
        }
    }
}
