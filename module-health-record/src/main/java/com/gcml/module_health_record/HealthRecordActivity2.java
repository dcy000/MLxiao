package com.gcml.module_health_record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_record.cc.CCAppActions;
import com.gcml.module_health_record.fragments.HealthRecordBUAFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodoxygenFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gcml.module_health_record.fragments.HealthRecordBloodsugarFragment;
import com.gcml.module_health_record.fragments.HealthRecordCholesterolFragment;
import com.gcml.module_health_record.fragments.HealthRecordECGFragment;
import com.gcml.module_health_record.fragments.HealthRecordHeartrateFragment;
import com.gcml.module_health_record.fragments.HealthRecordTemperatureFragment;
import com.gcml.module_health_record.fragments.HealthRecordWeightFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/5 17:48
 * created by: gzq
 * description: TODO
 */
public class HealthRecordActivity2 extends AppCompatActivity implements View.OnClickListener, HealthRecordBloodsugarFragment.BloodsugarSelectTime {
    private LinearLayout mLlBack;
    private TextView mTvTopTitle;
    private ImageView mIvTopRight;
    private RecyclerView mRvMenu;
    private TextView mTvTimeUnit;
    private TextView mTvTimeStart;
    private TextView mTvTimeEnd;
    private LinearLayout mLlSelectTime;
    private FrameLayout mHealthRecordFl;
    private BaseQuickAdapter<MenuBean, BaseViewHolder> adapter;
    private HealthRecordTemperatureFragment temperatureFragment;
    private HealthRecordBloodpressureFragment bloodpressureFragment;
    private HealthRecordBloodsugarFragment bloodsugarFragment;
    private HealthRecordBloodoxygenFragment bloodoxygenFragment;
    private HealthRecordHeartrateFragment heartrateFragment;
    private HealthRecordCholesterolFragment cholesterolFragment;
    private HealthRecordBUAFragment buaFragment;
    private HealthRecordECGFragment ecgFragment;
    private HealthRecordWeightFragment weightFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_recoed_activity_health_record2);
        initFragments();
        initView();
        initMenu();
    }
    private void initFragments() {
        temperatureFragment = new HealthRecordTemperatureFragment();
        bloodpressureFragment = new HealthRecordBloodpressureFragment();
        bloodsugarFragment = new HealthRecordBloodsugarFragment();
        bloodsugarFragment.setRequestBloodsugarData(this);
        bloodoxygenFragment = new HealthRecordBloodoxygenFragment();
        heartrateFragment = new HealthRecordHeartrateFragment();
        cholesterolFragment = new HealthRecordCholesterolFragment();
        buaFragment = new HealthRecordBUAFragment();
        ecgFragment = new HealthRecordECGFragment();
        weightFragment = new HealthRecordWeightFragment();
    }
    private void initMenu() {
        List<MenuBean> menuBeans = new ArrayList<>();
        menuBeans.add(new MenuBean(0, "档案下载", false));
        menuBeans.add(new MenuBean(1, "体温", false));
        menuBeans.add(new MenuBean(2, "血压", false));
        menuBeans.add(new MenuBean(4, "血糖", false));
        menuBeans.add(new MenuBean(5, "血氧", false));
        menuBeans.add(new MenuBean(3, "心跳", false));
        menuBeans.add(new MenuBean(7, "胆固醇", false));
        menuBeans.add(new MenuBean(8, "血尿酸", false));
        menuBeans.add(new MenuBean(9, "心电图", false));
        menuBeans.add(new MenuBean(10, "体重", false));


        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        mRvMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<MenuBean, BaseViewHolder>(R.layout.health_record_item_menu, menuBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MenuBean item) {
                helper.setText(R.id.tv_menu, item.getName());
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((TextView) view).setSelected(menuBeans.get(position).getSelected());
                dealClickItem(menuBeans.get(position).getId());
            }
        });
    }

    private void dealClickItem(int id) {

    }

    private void initView() {
        mLlBack = (LinearLayout) findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);
        mTvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        mIvTopRight = (ImageView) findViewById(R.id.iv_top_right);
        mIvTopRight.setOnClickListener(this);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);
        mTvTimeUnit = (TextView) findViewById(R.id.tv_time_unit);
        mTvTimeUnit.setOnClickListener(this);
        mTvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        mTvTimeStart.setOnClickListener(this);
        mTvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        mTvTimeEnd.setOnClickListener(this);
        mLlSelectTime = (LinearLayout) findViewById(R.id.ll_select_time);
        mHealthRecordFl = (FrameLayout) findViewById(R.id.health_record_fl);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_time_unit) {

        } else if (i == R.id.tv_time_start) {

        } else if (i == R.id.tv_time_end) {

        } else if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.iv_top_right) {
            CCAppActions.jump2MainActivity();
        } else {
        }
    }

    @Override
    public void requestData() {

    }
}
