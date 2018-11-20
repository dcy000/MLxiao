package com.example.han.referralproject.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.ECGHistory;
import com.example.han.referralproject.util.Utils;
import com.gzq.lib_core.base.Box;

import java.util.List;

/**
 * Created by gzq on 2017/12/22.
 */

public class XindianAdapter extends BaseQuickAdapter<ECGHistory, BaseViewHolder> {
    private String[] strings;

    public XindianAdapter(int layoutResId, @Nullable List<ECGHistory> data) {
        super(layoutResId, data);
        strings = Box.getStrings(R.array.ecg_measureres);
    }

    @Override
    protected void convert(BaseViewHolder helper, ECGHistory item) {
        if (TextUtils.isEmpty(item.ecg)) {
            helper.setText(R.id.item_tv_message, "心电正常");
        } else {

            helper.setText(R.id.item_tv_message, strings[Integer.parseInt(item.ecg)]);
        }
        helper.setText(R.id.item_tv_time, Utils.stampToDate(Long.parseLong(item.time)));
    }
}
