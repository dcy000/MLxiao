package com.example.han.referralproject.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.InputDialog;
import com.gcml.lib_utils.network.WiFiUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.billy.cc.core.component.CC.getApplication;

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
        holder.mWifiName.setText(itemResult.SSID);
        RxUtils.rxWifiLevels(getApplication(), 4, itemResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter((LifecycleOwner) holder.itemView.getContext()))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer level) throws Exception {
                        holder.mWifiLevel.setImageLevel(level);
                    }
                });
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
                    new InputDialog(mContext)
                            .builder()
                            .setMsg(itemResult.SSID)
                            .setPositiveButton("连接", new InputDialog.OnInputChangeListener() {
                                @Override
                                public void onInput(String s) {
                                    if (itemResult.capabilities.contains("WPA2") || itemResult.capabilities.contains("WPA-PSK")) {
                                        WiFiUtil.getInstance(mContext).addWiFiNetwork(itemResult.SSID, s, WiFiUtil.Data.WIFI_CIPHER_WPA2);
                                    } else if (itemResult.capabilities.contains("WPA")) {
                                        WiFiUtil.getInstance(mContext).addWiFiNetwork(itemResult.SSID, s, WiFiUtil.Data.WIFI_CIPHER_WPA);
                                    } else if (itemResult.capabilities.contains("WEP")) {
                                        /* WIFICIPHER_WEP 加密 */
                                        WiFiUtil.getInstance(mContext).addWiFiNetwork(itemResult.SSID, s, WiFiUtil.Data.WIFI_CIPHER_WEP);
                                    } else {
                                        /* WIFICIPHER_OPEN NOPASSWORD 开放无加密 */
                                        WiFiUtil.getInstance(mContext).addWiFiNetwork(itemResult.SSID, "", WiFiUtil.Data.WIFI_CIPHER_NOPASS);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
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

        public TextView mWifiName;
        public ImageView mWifiLevel;

        public WifiHolder(View view){
            super(view);
            mWifiName = view.findViewById(R.id.item_tv_wifi_name);
            mWifiLevel = view.findViewById(R.id.item_iv_wifi_level);
        }
    }
}
