package com.gcml.module_health_record.others;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.ShowPDFActivity;
import com.gcml.module_health_record.bean.ECGHistory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by gzq on 2017/12/22.
 */

public class XindianAdapter extends BaseQuickAdapter<ECGHistory, BaseViewHolder> {

    private String[] ecgMeasureResult;

    public XindianAdapter(int layoutResId, @Nullable List<ECGHistory> data, String[] ecgMeasureResult) {
        super(layoutResId, data);
        this.ecgMeasureResult = ecgMeasureResult;
    }

    @Override
    protected void convert(BaseViewHolder helper, ECGHistory item) {
        if (TextUtils.isEmpty(item.result)) {
            if (TextUtils.equals("0", item.ecg)) {
                helper.setText(R.id.item_tv_message, "波形未见异常");
            } else {
                helper.setText(R.id.item_tv_message, "分析结果出现错误");
            }
        } else {
//            if (item.result.contains("重新测试")) {
//                return;
//            }
            helper.setText(R.id.item_tv_message, item.result);
        }

        helper.setText(R.id.item_tv_time, TimeUtils.milliseconds2String(item.time,
                new SimpleDateFormat("yyyy-MM-dd HH:mm")));
        if (TextUtils.isEmpty(item.result_url)) {
            helper.getView(R.id.btn_health_record_detail).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.btn_health_record_detail).setVisibility(View.VISIBLE);
        }
        ((TextView) helper.getView(R.id.btn_health_record_detail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPDFActivity.startActivity(mContext, item.result_url);
            }
        });
    }
}

interface ECGResult {
    String STATE_OK_1 = "波形未见异常";
    String STATE_HEARTBEAT_FAST_2 = "波形疑似心跳稍快,请注意休息";
    String STATE_HEARTBEAT_SO_FAST_3 = "波形疑似心跳过快,请注意休息";
    String STATE_HEARTBEAT_SO_FAST_TO_ASK_DOCTOR_4 = "波形疑似阵发性心跳过快,请咨询医生";
    String STATE_HEARTBEAT_SLOW_5 = "波形疑似心跳稍缓,请注意休息";
    String STATE_HEARTBEAT_SO_SLOW_6 = "波形疑似心跳过缓,请注意休息";
    String STATE_HEARTBEAT_SO_SLOW_TO_ASK_DOCTOR_7 = "波形疑似偶发心跳间期缩短,请咨询医生";
    String STATE_HEARTBEAT_IRREGULAR_8 = "波形疑似心跳间期不规则,请咨询医生";
    String STATE_HEARTBEAT_FAST_SHORTEN_9 = "波形疑似心跳稍快伴有偶发心跳间期缩短,请咨询医生";
    String STATE_HEARTBEAT_SLOW_SHORTEN_10 = "波形疑似心跳稍缓伴有偶发心跳间期缩短,请咨询医生";
    String STATE_HEARTBEAT_SLOW_SHORTEN_TO_ASK_DOCTOR_11 = "波形疑似心跳稍缓伴有心跳间期不规则,请咨询医生";
    String STATE_HEARTBEAT_DRIFT_12 = "波形有漂移,请重新测量";
    String STATE_HEARTBEAT_FAST_DRIFT_13 = "波形疑似心跳过快伴有波形漂移,请咨询医生";
    String STATE_HEARTBEAT_SLOW_DRIFT_14 = "波形疑似心跳过缓伴有波形漂移,请咨询医生";
    String STATE_HEARTBEAT_DRIFT_SHORTEN_15 = "波形疑似偶发心跳间期缩短伴有波形漂移,请咨询医生";
    String STATE_HEARTBEAT_IRREGULAR_DRIFT_16 = "波形疑似心跳间期不规则伴有波形漂移,请咨询医生";
    String STATE_HEARTBEAT_SIGNAL_INSTABILITY_17 = "信号较差请重新测量";
    String STATE_HEARTBEAT_ON_ERROR_18 = "分析结果出现错误";
}