package com.gzq.test_all_devices.formatte;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.lib_utils.data.TimeUtils;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.health_record_bean.ECGHistory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by gzq on 2017/12/22.
 */

public class XindianAdapter extends BaseQuickAdapter<ECGHistory, BaseViewHolder> {

    public XindianAdapter(int layoutResId, @Nullable List<ECGHistory> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ECGHistory item) {
        if (TextUtils.isEmpty(item.ecg)) {
            helper.setText(R.id.item_tv_message, "心电正常");
        } else
            helper.setText(R.id.item_tv_message, item.ecg);
        helper.setText(R.id.item_tv_time, TimeUtils.milliseconds2String(Long.parseLong(item.time),
                new SimpleDateFormat("yyyy-MM-dd HH:mm")));
    }
}
