package com.medlink.danbogh.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.han.referralproject.R;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmDetail2Activity extends AppCompatActivity {

    public static Intent newLaunchIntent(Context context, long id) {
        Intent intent = new Intent(context, AlarmDetail2Activity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("id", id);
        return intent;
    }

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.alarm_detail_sp_repeat)
    Spinner spRepeat;
    @BindView(R.id.alarm_detail_et_content)
    EditText etContent;
    @BindView(R.id.alarm_detail_tv_cancel)
    TextView tvCancel;
    @BindView(R.id.alarm_detail_tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.alarm_detail_tp_time)
    TimePicker tpTime;

    private Unbinder mUnbinder;

    private AlarmModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail2);
        mUnbinder = ButterKnife.bind(this);
        initView();
        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            mModel = new AlarmModel();
        } else {
            mModel = DataSupport.find(AlarmModel.class, id);
            show(mModel);
        }
    }

    private void initView() {
        spRepeat.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, getResources().getStringArray(R.array.repeats)));
    }

    private void show(AlarmModel model) {
        tpTime.setCurrentMinute(model.getMinute());
        tpTime.setCurrentHour(model.getHourOfDay());
        etContent.setText(model.getContent());
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.alarm_detail_tv_cancel)
    public void onTvCancelClicked() {
        finish();
    }

    @OnClick(R.id.alarm_detail_tv_confirm)
    public void onTvConfirmClicked() {
        updateModel();
        AlarmHelper.cancelAlarms(this);
        if (mModel.getId() < 0) {
            mModel.save();
        } else {
            mModel.update(mModel.getId());
        }
        AlarmHelper.setupAlarms(this);
        setResult(RESULT_OK);
        finish();
    }

    private void updateModel() {
        mModel.setMinute(tpTime.getCurrentMinute());
        mModel.setHourOfDay(tpTime.getCurrentHour());
        mModel.setContent(etContent.getText().toString().trim());
        mModel.setInterval(AlarmModel.INTERVAL_DAY);
        mModel.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
