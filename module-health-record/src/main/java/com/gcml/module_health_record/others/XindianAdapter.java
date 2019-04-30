package com.gcml.module_health_record.others;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.bean.ECGHistory;
import com.sjtu.yifei.route.Routerfit;

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
                Routerfit.register(AppRouter.class).skipShowPDFActivity(item.result_url);
            }
        });
    }
}