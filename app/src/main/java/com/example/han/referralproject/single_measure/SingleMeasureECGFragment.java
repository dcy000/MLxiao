package com.example.han.referralproject.single_measure;

import android.text.TextUtils;

import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.bean.BoShengUserBean;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.utils.TimeUtils;

import java.text.SimpleDateFormat;

/**
 * Created by lenovo on 2018/11/13.
 */

public class SingleMeasureECGFragment extends ECG_Fragment {
    @Override
    public void onStart() {
        super.onStart();
        userBean = new BoShengUserBean();
        userBean.setBirthday(getBirth(LocalShared.getInstance(mContext).getIdCard()));//25/19930115
        userBean.setName(LocalShared.getInstance(mContext).getUserName());
        userBean.setPhone(LocalShared.getInstance(mContext).getPhoneNum());
        userBean.setSex(LocalShared.getInstance(mContext).getSex());
    }

    private String getBirth(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return TimeUtils.getCurTimeString(new SimpleDateFormat("yyyyMMdd"));
        }
        return idCard.substring(6, 14);
    }

//    @Override
//    public void onResume() {
//        userBean= (BoShengUserBean) getArguments().getSerializable("user");
//        super.onResume();
//    }

    @Override
    protected void onMeasureFinished(String... results) {
        super.onMeasureFinished(results);
        //todo:上传数据
    }
}
