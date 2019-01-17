package com.gcml.module_inquiry.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.model.Docter;

import java.util.List;

/**
 * Created by lenovo on 2019/1/16.
 */

public class BindDoctorAdapter extends BaseQuickAdapter<Docter, BaseViewHolder> {
    public BindDoctorAdapter(int layoutResId, @Nullable List<Docter> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Docter item) {
        helper.setText(R.id.doctor_name, item.doctername);

    }
}
