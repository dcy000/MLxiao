package com.gcml.common.mvp;

import android.arch.lifecycle.ViewModel;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class HolderViewModel extends ViewModel {
    private WeakHashMap<String, Object> values = new WeakHashMap<>();

    public void put(String key, Object value) {
        this.values.put(key, value);
    }

    public Object get(String key) {
        return values.get(key);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Set<Map.Entry<String, Object>> entries = values.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object value = entry.getValue();
            if (value instanceof IPresenter) {
                ((IPresenter) value).onCleared();
            }
        }
    }
}
