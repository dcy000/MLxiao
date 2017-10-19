package com.example.han.referralproject.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.dialog.WifiInputDialog;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectRecyclerAdapter extends RecyclerView.Adapter<WifiConnectRecyclerAdapter.WifiHolder> {
    private LayoutInflater mInflater;
    private List<ScanResult> mDataList;
    private Context mContext;

    public WifiConnectRecyclerAdapter(Context context, List<ScanResult> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        mContext = context;
    }

    @Override
    public WifiConnectRecyclerAdapter.WifiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WifiHolder(mInflater.inflate(R.layout.item_wifi_info_layout, null));
    }

    @Override
    public void onBindViewHolder(WifiConnectRecyclerAdapter.WifiHolder holder, int position) {
        final ScanResult itemResult = mDataList.get(position);
        holder.mNameTv.setText(itemResult.SSID);
        holder.mIpTv.setText(itemResult.BSSID);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WifiInputDialog(mContext, itemResult).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class WifiHolder extends RecyclerView.ViewHolder {
        public TextView mNameTv;
        public TextView mIpTv;

        public WifiHolder(View view){
            super(view);
            mNameTv = (TextView) view.findViewById(R.id.item_tv_wifi_name);
            mIpTv = (TextView) view.findViewById(R.id.item_tv_wifi_ip);
        }
    }
}
