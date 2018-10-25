//package com.example.lenovo.rto.accesstoken;
//
//
//import com.example.lenovo.rto.http.HttpListener;
//
///**
// * Created by huyin on 2017/9/10.
// */
//
//public class AccessTokenPresenter implements HttpListener<AccessToken> {
//
//     AccessTokenView accessTokenView;
//     AccessTokenModel accessToken;
//
//     public AccessTokenPresenter(AccessTokenView accessTokenView) {
//          this.accessTokenView = accessTokenView;
//          accessToken = new AccessTokenModel();
//     }
//
//     public void getAccessToken() {
//          accessToken.getAccessToken(this);
//     }
//
//     @Override
//     public void onSuccess(AccessToken data) {
//          if (accessTokenView != null) {
//               if (data != null) {
//                    accessTokenView.setAccessToken(data);
//               }
//          }
//     }
//
//     @Override
//     public void onError() {
//          if (accessTokenView != null) {
//               accessTokenView.showOther();
//          }
//     }
//}
