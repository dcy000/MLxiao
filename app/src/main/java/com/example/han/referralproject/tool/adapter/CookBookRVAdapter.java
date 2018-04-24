package com.example.han.referralproject.tool.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.List;

/**
 * Created by lenovo on 2018/3/5.
 */

public class CookBookRVAdapter extends BaseQuickAdapter<CookbookBean, BaseViewHolder> {
    public CookBookRVAdapter(int layoutResId, @Nullable List<CookbookBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CookbookBean cookbookBean) {
        baseViewHolder.setText(R.id.tv_title, (baseViewHolder.getPosition() + 1) + "." + cookbookBean.title);
        baseViewHolder.setText(R.id.tv_content, cookbookBean.steps);
        final View view = baseViewHolder.getView(R.id.tv_content);
        if (cookbookBean.flag) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }


        baseViewHolder.getView(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookbookBean.flag) {
                    getData().get(baseViewHolder.getPosition()).flag = false;
                    view.setVisibility(View.GONE);
                    MLVoiceSynthetize.stop();
                } else {
                    getData().get(baseViewHolder.getPosition()).flag = true;
                    view.setVisibility(View.VISIBLE);
                    MLVoiceSynthetize.stop();
                    CookbookBean bean = getData().get(baseViewHolder.getPosition());
                    MLVoiceSynthetize.startSynthesize(v.getContext(), bean.title + "," + bean.steps,false);
                }
            }
        });

    }
}
