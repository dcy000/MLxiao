package com.gcml.module_health_record.others;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.bean.ECGHistory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by gzq on 2017/12/22.
 */

public class XindianAdapter extends BaseQuickAdapter<ECGHistory, BaseViewHolder> {

    private String[] ecgMeasureResult;

    public XindianAdapter(int layoutResId, @Nullable List<ECGHistory> data,String[] ecgMeasureResult) {
        super(layoutResId, data);
        this.ecgMeasureResult=ecgMeasureResult;
    }

    @Override
    protected void convert(BaseViewHolder helper, ECGHistory item) {
        if (TextUtils.isEmpty(item.ecg)) {
            helper.setText(R.id.item_tv_message, "分析结果出现错误");
        } else {
            int position = Integer.parseInt(item.ecg);
            if (position>=0&&position<ecgMeasureResult.length) {
                helper.setText(R.id.item_tv_message, ecgMeasureResult[position]);
            }else{
                helper.setText(R.id.item_tv_message, "分析结果出现错误");
            }
        }
        helper.setText(R.id.item_tv_time, TimeUtils.milliseconds2String(item.time,
                new SimpleDateFormat("yyyy-MM-dd HH:mm")));
    }
}