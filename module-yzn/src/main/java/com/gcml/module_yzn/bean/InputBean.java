package com.gcml.module_yzn.bean;

import com.gcml.common.data.UserSpHelper;

import java.io.Serializable;

/**
 * Created by lenovo on 2019/5/6.
 */

public class InputBean implements Serializable {
    public String userId = UserSpHelper.getUserId();
    public String input;
}