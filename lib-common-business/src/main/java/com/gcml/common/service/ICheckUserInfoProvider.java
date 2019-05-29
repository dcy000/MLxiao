package com.gcml.common.service;

import com.gcml.common.constant.EUserInfo;

public interface ICheckUserInfoProvider {
    void check(CheckUserInfoProviderImp.CheckUserInfo listener, EUserInfo... condition);
}
