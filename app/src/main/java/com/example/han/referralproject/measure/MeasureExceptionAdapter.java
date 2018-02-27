package com.example.han.referralproject.measure;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;

import java.util.List;

/**
 * Created by gzq on 2018/2/26.
 */

public class MeasureExceptionAdapter extends BaseQuickAdapter<Integer,BaseViewHolder>{
    public MeasureExceptionAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        ((ImageView)helper.getView(R.id.title)).setImageResource(item);
    }
}
