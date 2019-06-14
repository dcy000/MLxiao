package com.gcml.module_health_profile.checklist.wrap;

import android.view.View;

import java.util.List;

public abstract class FlowAdapte<T> {
    List<T> data;

    public FlowAdapte(List<T> data) {
        this.data = data;
    }

    /**
     * @param position
     * @return
     */
    private T getItem(int position) {
        if (data != null && data.size() > position) {
            return data.get(position);
        } else {
            return null;
        }
    }

    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }


    public View getChildView(int position) {
        T item = getItem(position);
        View view = onBindViewHolder(item, position);
        return view;
    }

    public abstract View onBindViewHolder(T item, int position);
}
