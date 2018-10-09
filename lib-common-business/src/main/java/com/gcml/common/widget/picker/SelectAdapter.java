package com.gcml.common.widget.picker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.business.R;

import java.util.List;

/**
 * Created by lenovo on 2017/10/15.
 */

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.Holder> {

    private OnItemClickListener mOnItemClickListener;
    public List<String> mStrings;

    public void setStrings(List<String> strings) {
        mStrings = strings;
    }

    public List<String> getStrings() {
        return mStrings;
    }

    public SelectAdapter() {
    }

    public SelectAdapter(List<String> strings) {
        mStrings = strings;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.common_item_select, parent, false);
        return new Holder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String text = mStrings.get(position);
        holder.tvSelect.setText(text);
    }

    @Override
    public int getItemCount() {
        return mStrings == null ? 0 : mStrings.size();
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public OnItemClickListener mOnItemClickListener;
        public TextView tvSelect;

        public Holder(View itemView) {
            super(itemView);
        }

        public Holder(final View view, OnItemClickListener onItemClickListener) {
            this(view);
            mOnItemClickListener = onItemClickListener;
            view.setOnClickListener(this);
            tvSelect = view.findViewById(R.id.tv_select);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
