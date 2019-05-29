package com.gcml.module_yzn.bean;

import com.gcml.common.data.UserSpHelper;

import java.io.Serializable;

public class NewsInputBean implements Serializable {
    public String userId = UserSpHelper.getUserId();
    public String input;
}
