package com.example.han.referralproject.tool.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.HistoryTodayActivity;
import com.example.han.referralproject.tool.wrapview.ExpandableTextView;
import com.example.han.referralproject.tool.wrapview.MixtureTextView;
import com.example.han.referralproject.tool.xfparsebean.HistoryTodayBean;
import com.example.han.referralproject.voice.SpeechSynthesizerHelper;
import com.hymane.expandtextview.ExpandTextView;
import com.hymane.expandtextview.OnReadMoreClickListener;

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
//        final ExpandableTextView close = holder.getView(R.id.expandableTextView);
//        final MixtureTextView open = holder.getView(R.id.mt_result);
//        open.setText(bean.description);
//        ImageView openImg = holder.getView(R.id.img_pic);
//        if (!bean.imgs.isEmpty()) {
//            Glide.with(openImg.getContext()).load(bean.imgs.get(0)).into(openImg);
//        }
//        close.isOpen = bean.flag;
//        if (bean.flag) {
//            close.setVisibility(View.GONE);
//            open.setVisibility(View.VISIBLE);
//        } else {
//            close.setVisibility(View.VISIBLE);
//            open.setVisibility(View.GONE);
//        }
//
//        close.setClickListner(new ExpandableTextView.ClickListner() {
//            @Override
//            public void onclick(boolean isOpen) {
//                getData().get(holder.getPosition()).flag = isOpen;
//                if (isOpen) {
//                    close.setVisibility(View.GONE);
//                    open.setVisibility(View.VISIBLE);
//                } else {
//                    close.setVisibility(View.VISIBLE);
//                    open.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });

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
                    SpeechSynthesizerHelper.stop();
                    SpeechSynthesizerHelper.startSynthesize(expandTextView.getContext(),bean.title+","+ bean.description);
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
                        SpeechSynthesizerHelper.stop();
                    }
                }
                getData().get(holder.getPosition()).flag = false;
            }
        });

    }
}
