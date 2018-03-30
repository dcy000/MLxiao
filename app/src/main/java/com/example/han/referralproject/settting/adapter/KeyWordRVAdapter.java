package com.example.han.referralproject.settting.adapter;

import android.graphics.Typeface;
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
    ClickItemInterface listener;

    public interface ClickItemInterface {
        void onItemClick(int position);
    }

    public void setListener(ClickItemInterface listener) {
        this.listener = listener;
    }

    public KeyWordRVAdapter(List<KeyWordBean> data) {
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = View.inflate(viewGroup.getContext(), R.layout.key_word_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        KeyWordBean bean = data.get(position);

        holder.name.setText(bean.itemName);
        if (bean.title) {
            holder.name.setBackground(null);
            holder.name.setTextSize(32);
            holder.name.setEnabled(false);
            holder.name.setPadding(-40, 0, 0, 0);
            holder.name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.name.setTextSize(28);
            holder.name.setEnabled(true);
            holder.name.setPadding(0, 0, 0, 0);
            holder.name.setBackgroundResource(R.drawable.shape_item_key_word_set);
            holder.name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

//        if (bean.click) {
//            holder.name.setBackgroundResource(R.drawable.shape_item_key_word_click);
//        } else {
//            holder.name.setBackgroundResource(R.drawable.shape_item_key_word);
//        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).click = false;
                    }
                    data.get(position).click = true;
                }
                notifyDataSetChanged();
            }
        });
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
