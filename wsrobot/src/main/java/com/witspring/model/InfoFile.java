package com.witspring.model;

import android.app.Application;

import com.witspring.util.SPUtil;

/**
 * @author Created by Goven on 2018/1/1 下午9:38
 * @email gxl3999@gmail.com
 */
public class InfoFile {

    private SPUtil util;

    public InfoFile(Application app) {
        util = SPUtil.getInstance(app);
    }

    public void setUsername(String name) {
        util.put("username", name);
    }

    public String getUsername() {
        return util.optString("username");
    }

    public void setDeviceId(String id) {
        util.put("deviceId", id);
    }

    public String getDeviceId() {
        return util.optString("deviceId");
    }

}
