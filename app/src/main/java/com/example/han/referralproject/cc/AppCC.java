package com.example.han.referralproject.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/14 13:47
 * created by:gzq
 * description:TODO
 */
public class AppCC implements IComponent {
    @Override
    public String getName() {
        return "app";
    }

    @Override
    public boolean onCall(CC cc) {
        return false;
    }
}
