package com.example.han.referralproject.recommend.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.List;

/**
 * Created by lenovo on 2018/9/19.
 */

public class RecommendAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public RecommendAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {
        holder.setText(R.id.tv_good_name, (String) (o));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong(holder.getPosition() + "");
            }
        });

    }

}
