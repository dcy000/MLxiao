package com.medlink.danbogh.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/9/26.
 */

public class AlarmList2Activity extends BaseActivity {

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
        NetworkApi.clueNotify(new NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>>() {
            @Override
            public void onSuccess(ArrayList<ClueInfoBean> response) {
                if (response == null || response.size() == 0) {
                    return;
                }
                StringBuilder mBuilder = new StringBuilder();

                for (ClueInfoBean itemBean : response) {
                    mBuilder.append(response.get(0).doctername).append("提醒您").append(itemBean.cluetime).append("吃").append(itemBean.medicine);
                }
                speak(mBuilder.toString());
            }
        });
    }

    private void refresh() {
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null) {
            Iterator<AlarmModel> iterator = models.iterator();
            while (iterator.hasNext()) {
                AlarmModel model = iterator.next();
                String tag = model.getTag();
                if (tag != null && !tag.isEmpty()) {
                    iterator.remove();
                }
            }
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
            if (models != null) {
                Iterator<AlarmModel> iterator = models.iterator();
                while (iterator.hasNext()) {
                    AlarmModel model = iterator.next();
                    String tag = model.getTag();
                    if (tag != null && !tag.isEmpty()) {
                        iterator.remove();
                    }
                }
                mAdapter.replaceAll(models);
            }
        }
    }

    public void onDeleteAlarm(long id) {
        final long alarmId = id;
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("您确定要删除吗?")
                .setMessageSize(35)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#FFA200"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            cancelAlarm(alarmId);
                        }
                    }
                }).create(NDialog.CONFIRM).show();
    }

    public void cancelAlarm(long alarmId) {
        AlarmHelper.cancelAlarms(this);
        DataSupport.delete(AlarmModel.class, alarmId);
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null) {
            Iterator<AlarmModel> iterator = models.iterator();
            while (iterator.hasNext()) {
                AlarmModel model = iterator.next();
                String tag = model.getTag();
                if (tag != null && !tag.isEmpty()) {
                    iterator.remove();
                }
            }
            mAdapter.replaceAll(models);
        }
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
