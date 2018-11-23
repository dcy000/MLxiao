package com.gzq.lib_core.constant;

/**
 * created on 2018/10/16 11:28
 * created by: gzq
 * description:
 */
public interface Constants {
    /**
     * 存放session user信息的key
     * {@link com.gzq.lib_core.session.PreferencesSessionManager}
     * {@link com.gzq.lib_core.session.MmkvSessionManager}
     */
    String KEY_SESSION_USER="session_user";
    /**
     * 存放session token信息的key
     *  {@link com.gzq.lib_core.session.PreferencesSessionManager}
     * {@link com.gzq.lib_core.session.MmkvSessionManager}
     */
    String KEY_SESSION_TOKEN="session_token";
    /**
     * room数据库默认的名字
     * {@link com.gzq.lib_core.base.Box#getRoomDataBase(Class)}
     */
    String NAME_ROOM_DATABASE="EasyDB";
    /**
     * 存本机器的组id
     */
    String KEY_GROUP_ID="group_id";

    /**
     * 创建人脸识别组的时候传入的第一个讯飞id
     */
    String KEY_CREATE_GROUP_FIRST_XFID = "group_first_xfid";
}
