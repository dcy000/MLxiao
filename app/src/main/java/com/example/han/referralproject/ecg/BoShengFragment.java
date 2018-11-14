package com.example.han.referralproject.ecg;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_ecg.ECG_Fragment;
import com.gcml.lib_ecg.base.UserInfoBean;

public class BoShengFragment extends ECG_Fragment {
    @Override
    public void onStart() {
        super.onStart();
        user = new UserInfoBean();
        user.setBirth(LocalShared.getInstance(mContext).getUserAge());
        user.setName(LocalShared.getInstance(mContext).getUserName());
        user.setPhone(LocalShared.getInstance(mContext).getPhoneNum());
        user.setSex(LocalShared.getInstance(mContext).getSex());
    }
}
