package com.zhang.hui.lib_recreation.tool.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.widget.ExpandTextView;
import com.gcml.common.widget.OnReadMoreClickListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HistoryTodayBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/7.
 */

public class HistoryTodayRVAdapter extends BaseQuickAdapter<HistoryTodayBean, BaseViewHolder> {

    public int position;

    public HistoryTodayRVAdapter(int layoutResId, @Nullable List<HistoryTodayBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final HistoryTodayBean bean) {
//        holder.setText(R.id.tv_content, bean.description);
        holder.setText(R.id.tv_title, bean.title);

        holder.setText(R.id.tv_content, bean.description);
        final ExpandTextView expandTextView = holder.getView(R.id.expandableTextView);
        expandTextView.flag = !bean.flag;
        expandTextView.expandWithNoAnimation();
        expandTextView.flag = bean.flag;

        expandTextView.setOnReadMoreListener(new OnReadMoreClickListener() {
            @Override
            public void onExpand() {
                //手动点击 由折叠到展开
                if (bean.flag == false) {
                    //记录手动点击展开的位置
                    position = holder.getPosition();
                    HistoryTodayBean bean = getData().get(position);
                    MLVoiceSynthetize.stop();
                    MLVoiceSynthetize.startSynthesize(expandTextView.getContext(),bean.title+","+ bean.description,false);
                }
                getData().get(holder.getPosition()).flag = true;
            }

            @Override
            public void onFold() {
                //手动点击 由展开到折叠
                if (bean.flag) {
                    int position = holder.getPosition();
                    //仅仅点击折叠当前条目 停止播报
                    if (position == HistoryTodayRVAdapter.this.position) {
                        MLVoiceSynthetize.stop();
                    }
                }
                getData().get(holder.getPosition()).flag = false;
            }
        });

    }
}
