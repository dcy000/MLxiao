package com.example.lib_alarm_clock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzq.lib_core.bean.AlarmModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2017/9/27.
 */

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmHolder> {

    private LayoutInflater mInflater;

    private List<AlarmModel> mAlarmModels = new ArrayList<>();

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_alarm2, parent, false);
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
