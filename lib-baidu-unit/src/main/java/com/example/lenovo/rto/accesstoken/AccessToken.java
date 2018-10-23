package com.example.lenovo.rto.accesstoken;

import com.google.gson.annotations.SerializedName;

/**
 * Created by huyin on 2017/9/10.
 */

public class AccessToken {

     @SerializedName("access_token")
     private String accessToken;
     @SerializedName("session_key")
     private String sessionKey;
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
