package com.example.han.referralproject.settting.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.bean.KeyWordBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/3/23.
 */

public class KeyWordRVAdapter extends RecyclerView.Adapter<KeyWordRVAdapter.VH> {
    List<KeyWordBean> data;

    public KeyWordRVAdapter(List<KeyWordBean> data) {
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = View.inflate(viewGroup.getContext(), R.layout.key_word_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        KeyWordBean bean = data.get(position);
        holder.name.setText(bean.itemName);
        if (bean.title) {
            holder.name.setBackground(null);
            holder.name.setTextSize(36);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        VH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
