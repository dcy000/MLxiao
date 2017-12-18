package com.example.han.referralproject.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.dialog.WifiInputDialog;
import com.example.han.referralproject.util.WiFiUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectRecyclerAdapter extends RecyclerView.Adapter<WifiConnectRecyclerAdapter.WifiHolder> {
    private LayoutInflater mInflater;
    private List<ScanResult> mDataList;
    private Context mContext;
//    private WifiManager mWifiManager;

    public WifiConnectRecyclerAdapter(Context context, List<ScanResult> dataList){
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        mContext = context;
//        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
//        WifiInfo mInfo = mWifiManager.getConnectionInfo();
//        if (mInfo != null && itemResult.BSSID.equals(mInfo.getBSSID())) {
//            holder.mStatusView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mStatusView.setVisibility(View.GONE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemResult.capabilities.contains("WPA2") || itemResult.capabilities.contains("WPA-PSK")
                        || itemResult.capabilities.contains("WPA")
                        || itemResult.capabilities.contains("WEP")) {
                    new WifiInputDialog(mContext, itemResult).show();
                } else {
                    /* WIFICIPHER_OPEN NOPASSWORD 开放无加密 */
                    WiFiUtil.getInstance(mContext).addWiFiNetwork(itemResult.SSID, "", WiFiUtil.Data.WIFI_CIPHER_NOPASS);
                }
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
        //public View mStatusView;

        public WifiHolder(View view){
            super(view);
            mNameTv = (TextView) view.findViewById(R.id.item_tv_wifi_name);
            mIpTv = (TextView) view.findViewById(R.id.item_tv_wifi_ip);
        }
    }
}
