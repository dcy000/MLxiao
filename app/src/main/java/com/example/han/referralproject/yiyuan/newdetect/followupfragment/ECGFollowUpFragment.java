package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.single_measure.bean.BoShengResultBean;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.bean.BoShengUserBean;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.utils.TimeUtils;
import com.google.gson.Gson;
import com.medlink.danbogh.utils.T;

import java.text.SimpleDateFormat;

/**
 * Created by lenovo on 2018/11/13.
 */

public class ECGFollowUpFragment extends ECG_Fragment {
    @Override
    public void onStart() {
        super.onStart();
        userBean = new BoShengUserBean();
        userBean.setBirthday(getBirth(LocalShared.getInstance(mContext).getIdCard()));
        userBean.setName(LocalShared.getInstance(mContext).getUserName());
        userBean.setPhone(LocalShared.getInstance(mContext).getPhoneNum());
        userBean.setSex(LocalShared.getInstance(mContext).getSex());


        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnChangeDevice.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
    }

    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
        if (results != null && results.length == 3) {

            BoShengResultBean resultBean = new Gson().fromJson(results[1], BoShengResultBean.class);
            DataInfoBean ecgInfo = new DataInfoBean();
            ecgInfo.ecg = resultBean.getStop_light();
            ecgInfo.heart_rate = resultBean.getAvgbeats().get(0).getHR();

            data.putString("ecg", ecgInfo.ecg + "");
            data.putString("heartRate", ecgInfo.heart_rate +"");
            NetworkApi.postData(ecgInfo, response -> {
                T.show("数据上传成功");
            }, message -> {
                T.show("数据上传失败");
            });
        }

    }

    Bundle data = new Bundle();

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, data);
        }
    }

    private String getBirth(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd"));
        }
        return idCard.substring(6, 14);
    }
}
