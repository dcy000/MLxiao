package com.example.han.referralproject.settting.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/26.
 */

public class KeyWordDifineRVAdapter extends BaseQuickAdapter<KeyWordDefinevBean, BaseViewHolder> {
    DeleteClickListener listener;

    public void setListener(DeleteClickListener listener) {
        this.listener = listener;
    }

    public interface DeleteClickListener {
        void onDeleteClick(int position);
    }


    public KeyWordDifineRVAdapter(int layoutResId, @Nullable List<KeyWordDefinevBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, KeyWordDefinevBean bean) {
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(bean.name);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        if (bean.show) {
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(View.GONE);
        }
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(holder.getPosition());
                }
            }
        });

    }
}
