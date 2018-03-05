package com.example.han.referralproject.tool.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/5.
 */

public class CookBookRVAdapter extends BaseQuickAdapter<CookbookBean, BaseViewHolder> {
    public CookBookRVAdapter(int layoutResId, @Nullable List<CookbookBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, CookbookBean cookbookBean) {
        baseViewHolder.setText(R.id.textView7, cookbookBean.title);
        baseViewHolder.setText(R.id.textView9, cookbookBean.steps);
        baseViewHolder.getView(R.id.textView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = baseViewHolder.getView(R.id.textView9);
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        });

    }
}
