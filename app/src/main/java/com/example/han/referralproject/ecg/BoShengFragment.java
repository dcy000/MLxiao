package com.example.han.referralproject.ecg;

import android.text.TextUtils;

import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_ecg.ECG_Fragment;
import com.gcml.lib_ecg.base.UserInfoBean;
import com.gzq.lib_core.base.Box;

public class BoShengFragment extends ECG_Fragment {
    @Override
    public void onStart() {
        super.onStart();
        user = new UserInfoBean();
        com.example.han.referralproject.bean.UserInfoBean user = Box.getSessionManager().getUser();
        String sfz = user.sfz;
        if (!TextUtils.isEmpty(sfz) && sfz.length() == 18) {
            this.user.setBirth(sfz.substring(6, 14));
        } else {
            this.user.setBirth("20180101");
        }
        this.user.setName(user.bname);
        this.user.setPhone(user.tel);
        this.user.setSex(user.sex);
    }
}
