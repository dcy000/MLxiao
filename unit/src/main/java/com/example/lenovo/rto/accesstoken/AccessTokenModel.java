package com.example.lenovo.rto.accesstoken;

import com.example.lenovo.rto.Constans;
import com.example.lenovo.rto.http.API;
import com.example.lenovo.rto.http.HttpClient;
import com.example.lenovo.rto.http.HttpListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



/**
 * Created by huyin on 2017/9/10.
 */

public class AccessTokenModel {

     HttpListener<AccessToken> mAccessTokenHttpListener;


     public void getAccessToken(HttpListener<AccessToken> accessTokenHttpListener) {
          this.mAccessTokenHttpListener = accessTokenHttpListener;
          HttpClient.retrofit().create(API.AccessTokenService.class)
                    .getAccessToken("client_credentials", Constans.API_KEY, Constans.SECRET_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AccessToken>() {
                         @Override
                         public void onSubscribe(Subscription s) {
                              s.request(Long.MAX_VALUE);
                         }

                         @Override
                         public void onNext(AccessToken accessToken) {
                              mAccessTokenHttpListener.onSuccess(accessToken);
                         }

                         @Override
                         public void onError(Throwable t) {
                              mAccessTokenHttpListener.onError();
                         }

                         @Override
                         public void onComplete() {
                         }
                    });
     }

}
