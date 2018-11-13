package com.example.han.referralproject.yiyuan.newdetect.followupfragment;

import android.view.View;

import com.example.han.referralproject.util.LocalShared;
import com.gcml.module_blutooth_devices.bean.BoShengUserBean;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;

/**
 * Created by lenovo on 2018/11/13.
 */

public class ECGFollowUpFragment  extends ECG_Fragment{
    @Override
    public void onStart() {
        super.onStart();
        userBean = new BoShengUserBean();
        userBean.setBirthday(LocalShared.getInstance(mContext).getUserAge());
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

    }

    @Override
    protected void clickHealthHistory(View view) {
        super.clickHealthHistory(view);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, null);
        }
    }
}
