package com.gzq.lib_core.session;

/**
 * 会话状态改变监听器
 */
public interface SessionStateChangedListener {

    /**
     * 用户信息改变
     */
    void onUserInfoChanged(SessionManager sessionManager);

    /**
     * 访问凭证发生改变
     */
    void onTokenInfoChanged(SessionManager sessionManager);

}
