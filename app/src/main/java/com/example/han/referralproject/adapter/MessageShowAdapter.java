package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.MessageHolder> {
    private LayoutInflater mInflater;
    private String[] mDataList;

    public MessageShowAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mDataList = context.getResources().getStringArray(R.array.test_message_array);
    }

    @Override
    public MessageShowAdapter.MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(mInflater.inflate(R.layout.item_message, null));
    }

    @Override
    public void onBindViewHolder(MessageShowAdapter.MessageHolder holder, int position) {
        holder.messageTv.setText(mDataList[position]);
    }

    @Override
    public int getItemCount() {
        return mDataList.length;
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        public TextView messageTv;

        public MessageHolder(View itemView){
            super(itemView);
            messageTv = (TextView) itemView.findViewById(R.id.item_tv_message);
        }
    }
}
