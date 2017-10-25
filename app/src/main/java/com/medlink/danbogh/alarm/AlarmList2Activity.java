package com.medlink.danbogh.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/9/26.
 */

public class AlarmList2Activity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.icon_home)
    ImageView icon_home;
    @BindView(R.id.alarm_list_rv_alarms)
    RecyclerView rvAlarms;
    @BindView(R.id.alarm_list_tv_add_alarm)
    TextView tvAddAlarm;
    private AlarmsAdapter mAdapter;
    public Unbinder mUnbinder;

    public static Intent newLaunchIntent(Context context) {
        Intent intent = new Intent(context, AlarmList2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list2);
        mUnbinder = ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        rvAlarms.setLayoutManager(layoutManager);
        mAdapter = new AlarmsAdapter();
        rvAlarms.setAdapter(mAdapter);
        refresh();
    }

    private void refresh() {
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null && models.size() != 0) {
            mAdapter.replaceAll(models);
        }
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.icon_home)
    public void onHomeBackClicked() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.alarm_list_tv_add_alarm)
    public void onTvAddAlarmClicked() {
        openAlarmDetailActivity(-1);
    }

    public void openAlarmDetailActivity(long id) {
        Intent intent = AlarmDetail2Activity.newLaunchIntent(this, id);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
            if (models != null && models.size() != 0) {
                mAdapter.replaceAll(models);
            }
        }
    }

    public void onDeleteAlarm(long id) {
        final long alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("删除提醒")
                .setTitle("确定删除?")
                .setCancelable(true)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAlarm(alarmId);
                    }
                }).show();
    }

    public void cancelAlarm(long alarmId) {
        AlarmHelper.cancelAlarms(this);
        DataSupport.delete(AlarmModel.class, alarmId);
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        mAdapter.replaceAll(models);
        AlarmHelper.setupAlarms(this);
    }

    public void setAlarmEnabled(long id, boolean enabled) {
        AlarmHelper.cancelAlarms(this);
        AlarmModel model = DataSupport.find(AlarmModel.class, id);
        if (model != null) {
            model.setEnabled(enabled);
            model.update(id);
        }
        AlarmHelper.setupAlarms(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
