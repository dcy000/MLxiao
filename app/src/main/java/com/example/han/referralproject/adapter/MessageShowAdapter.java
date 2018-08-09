package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.YzInfoBean;

import java.util.ArrayList;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.MessageHolder> {
    private LayoutInflater mInflater;
    private ArrayList<YzInfoBean> mDataList;

    public MessageShowAdapter(Context context, ArrayList<YzInfoBean> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    @Override
    public MessageShowAdapter.MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(mInflater.inflate(R.layout.item_message, null));
    }

    @Override
    public void onBindViewHolder(MessageShowAdapter.MessageHolder holder, int position) {
        YzInfoBean itemBean = mDataList.get(position);
        holder.messageTv.setText(itemBean.yz);
        holder.timeTv.setText(itemBean.time);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        public TextView messageTv;
        public TextView timeTv;

        public MessageHolder(View itemView){
            super(itemView);
            messageTv = itemView.findViewById(R.id.item_tv_message);
            timeTv = itemView.findViewById(R.id.item_tv_time);
        }
    }
}
