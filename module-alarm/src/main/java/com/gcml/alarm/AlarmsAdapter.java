package com.gcml.alarm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2017/9/27.
 */

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmHolder> {

    private LayoutInflater mInflater;

    private List<AlarmEntity> mAlarmEntities = new ArrayList<>();

    AlarmRepository alarmRepository;

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_alarm2, parent, false);
        return new AlarmHolder(view, alarmRepository);
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
        AlarmEntity model = mAlarmEntities.get(position);
        holder.onBind(model);
    }

    @Override
    public int getItemCount() {
        return mAlarmEntities.size();
    }

    public void replaceAll(Collection<AlarmEntity> models) {
        this.mAlarmEntities.clear();
        this.mAlarmEntities.addAll(models);
        this.notifyDataSetChanged();
    }

    public void add(AlarmEntity model) {
        mAlarmEntities.add(0, model);
        notifyItemInserted(0);
    }
}
