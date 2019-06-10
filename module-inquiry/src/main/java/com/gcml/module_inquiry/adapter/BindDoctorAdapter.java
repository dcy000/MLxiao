package com.gcml.module_inquiry.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.widget.toolbar.FilterClickListener;
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
        helper.setText(R.id.doctor_class, item.duty);

        ImageLoader.Options options = ImageLoader.newOptionsBuilder((ImageView) helper.getView(R.id.doctor_head_url),
                com.gcml.common.business.R.drawable.common_ic_robot)
                .build();
        ImageLoader.instance().load(options);

        FilterClickListener filterClickListener = new FilterClickListener();
        filterClickListener.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(item);
                }

            }
        });
        helper.getView(R.id.tv_qianyue).setOnClickListener(filterClickListener);

    }


    public interface OnClickQianyueListener {
        void onClick(Docter doctor);
    }

    OnClickQianyueListener listener;

    public void setListener(OnClickQianyueListener listener) {
        this.listener = listener;
    }
}
