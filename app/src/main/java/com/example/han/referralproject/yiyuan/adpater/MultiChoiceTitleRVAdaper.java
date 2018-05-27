package com.example.han.referralproject.yiyuan.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.adapter.KeyWordRVAdapter;
import com.example.han.referralproject.yiyuan.bean.MultipleChoiceBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/5/26.
 */

public class MultiChoiceTitleRVAdaper extends RecyclerView.Adapter<MultiChoiceTitleRVAdaper.VH> {
    private final Context context;
    List<MultipleChoiceBean> data;
    KeyWordRVAdapter.ClickItemInterface listener;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.item_name)
    TextView itemName;


    @OnClick(R.id.icon)
    public void onViewClicked() {
    }

    public interface ClickItemInterface {
        void onItemClick(int position);
    }

    public void setListener(KeyWordRVAdapter.ClickItemInterface listener) {
        this.listener = listener;
    }

    public MultiChoiceTitleRVAdaper(List<MultipleChoiceBean> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.multiple_choice_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int positon) {
        MultipleChoiceBean bean = data.get(positon);
        if (bean.isTitle()) {
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
            if (bean.isSelected) {
                Glide.with(context).load(R.drawable.icon_huaiyun).into(holder.icon);
            } else {
                Glide.with(context).load(R.drawable.icon_huaiyun_not).into(holder.icon);
            }
        }
        holder.name.setText(bean.getItemName());

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(positon);
                    if (!data.get(positon).isMultipleChoice) {

                        if (data.get(positon).getItemName().equals("否")) {
                            for (int i = 0; i < data.size(); i++) {
                                if (data.get(i).isMultipleChoice) {
                                    data.get(i).isSelected = false;
                                }
                            }
                        }
                        //其他单选 false  多选的不断
                        for (int i = 0; i < data.size(); i++) {
                            if (!data.get(i).isMultipleChoice) {
                                data.get(i).isSelected = false;
                            }
                        }
                        data.get(positon).isSelected = true;

                    } else {

                        data.get(positon).isSelected = true;
                    }
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView name;

        @BindView(R.id.icon)
        ImageView icon;

        VH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
