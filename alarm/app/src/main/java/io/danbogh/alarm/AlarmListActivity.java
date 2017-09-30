package io.danbogh.alarm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class AlarmListActivity extends AppCompatActivity {

    @BindView(R.id.alarm_ll_toolbar)
    LinearLayout llToolbar;
    @BindView(R.id.alarm_rv_alarms)
    RecyclerView rvAlarms;
    @BindView(R.id.alarm_iv_add_alarm)
    ImageView ivAddAlarm;
    public AlarmsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        ButterKnife.bind(this);

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
        mAdapter.replaceAll(models);
    }


    @OnClick(R.id.alarm_iv_add_alarm)
    public void onIvAddAlarmClicked() {
        startAlarmDetailsActivity(-1);
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
        AlarmHelper.cancelAlarms(AlarmListActivity.this);
        DataSupport.delete(AlarmModel.class, alarmId);
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        mAdapter.replaceAll(models);
        AlarmHelper.setupAlarms(AlarmListActivity.this);
    }

    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(this, AlarmDetailActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
            mAdapter.replaceAll(models);
        }
    }

    public void setAlarmEnabled(long id, boolean enabled) {
        AlarmHelper.cancelAlarms(this);

        AlarmModel model = DataSupport.find(AlarmModel.class, id);
        model.setEnabled(enabled);
        model.update(id);

        AlarmHelper.setupAlarms(this);
    }

    public static class AlarmsAdapter extends RecyclerView.Adapter<AlarmHolder> {

        private LayoutInflater mInflater;

        private List<AlarmModel> mAlarmModels = new ArrayList<>();

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            View view = mInflater.inflate(R.layout.item_alarm, parent, false);
            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            AlarmModel model = mAlarmModels.get(position);
            holder.onBind(model);
        }

        @Override
        public int getItemCount() {
            return mAlarmModels.size();
        }

        public void replaceAll(Collection<AlarmModel> models) {
            this.mAlarmModels.clear();
            this.mAlarmModels.addAll(models);
            this.notifyDataSetChanged();
        }

        public void add(AlarmModel model) {
            mAlarmModels.add(0, model);
            notifyItemInserted(0);
        }
    }

    public static class AlarmHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.alarm_item_tv_time)
        TextView itemTvTime;
        @BindView(R.id.alarm_item_tb_enable)
        ToggleButton itemTbEnable;
        @BindView(R.id.alarm_item_tv_content)
        TextView itemTvContent;
        @BindView(R.id.alarm_item_tv_sunday)
        TextView itemTvSunday;
        @BindView(R.id.alarm_item_tv_monday)
        TextView itemTvMonday;
        @BindView(R.id.alarm_item_tv_tuesday)
        TextView itemTvTuesday;
        @BindView(R.id.alarm_item_tv_wednesday)
        TextView itemTvWednesday;
        @BindView(R.id.alarm_item_tv_thursday)
        TextView itemTvThursday;
        @BindView(R.id.alarm_item_tv_friday)
        TextView itemTvFriday;
        @BindView(R.id.alarm_item_tv_saturday)
        TextView itemTvSaturday;

        public AlarmHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AlarmListActivity) v.getContext()).startAlarmDetailsActivity((Long) v.getTag());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AlarmListActivity) v.getContext()).onDeleteAlarm(((Long) v.getTag()));
                    return true;
                }
            });
        }

        @OnCheckedChanged(R.id.alarm_item_tb_enable)
        public void  onTbEnabledCheckedChaged(CompoundButton buttonView, boolean isChecked){
            ((AlarmListActivity) itemView.getContext())
                    .setAlarmEnabled(((Long) buttonView.getTag()), isChecked);
        }

        public void onBind(AlarmModel model) {
            itemTbEnable.setChecked(model.isEnabled());
            itemTvContent.setText(model.getContent());
            String time = String.format(Locale.CHINA,
                    "%02d : %02d", model.getHourOfDay(), model.getMinute());
            itemTvTime.setText(time);
            updateTextColor(itemTvSunday, model.hasDayOfWeek(Calendar.SUNDAY));
            updateTextColor(itemTvMonday, model.hasDayOfWeek(Calendar.MONDAY));
            updateTextColor(itemTvTuesday, model.hasDayOfWeek(Calendar.TUESDAY));
            updateTextColor(itemTvWednesday, model.hasDayOfWeek(Calendar.WEDNESDAY));
            updateTextColor(itemTvThursday, model.hasDayOfWeek(Calendar.THURSDAY));
            updateTextColor(itemTvFriday, model.hasDayOfWeek(Calendar.FRIDAY));
            updateTextColor(itemTvSaturday, model.hasDayOfWeek(Calendar.SATURDAY));
            itemTbEnable.setChecked(model.isEnabled());
            itemTbEnable.setTag(model.getId());
            itemView.setTag(model.getId());
        }

        public void updateTextColor(TextView view, boolean isOn) {
            if (isOn) {
                view.setTextColor(Color.GREEN);
            } else {
                view.setTextColor(Color.BLACK);
            }
        }
    }
}