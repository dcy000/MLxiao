package com.gcml.auth.face3.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdAccessToken {

    /**
     * access_token : 24.15bdfc66903593317799a363622de59e.2592000.1543027976.282335-14408003
     * session_key : 9mzdCrE2XkKmM4dN3kIH9RslleezsLN8h6Qbu0jZCA3ZD1U7xsuJ1Mxm+IDR3dp4o06Q0Ij1tJ8wXLJ/qSzUkB0C1bjlbQ==
     * scope : public brain_all_scope vis-faceverify_faceverify_h5-face-liveness vis-faceverify_FACE_V3 vis-faceverify_FACE_Police wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey
     * refresh_token : 25.4f67bf1870b52e6492686ceb90964d8e.315360000.1855795976.282335-14408003
     * session_secret : a638468787ead12057587acbaf70e3e9
     * expires_in : 2592000
     */

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("session_key")
    private String sessionKey;
    @SerializedName("scope")
    private String scope;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("session_secret")
    private String sessionSecret;
    @SerializedName("expires_in")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
