package com.example.lenovo.rto.unit;

import android.util.Log;


import com.example.lenovo.rto.http.API;
import com.example.lenovo.rto.http.HttpClient;
import com.example.lenovo.rto.http.HttpListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huyin on 2017/9/12.
 */

public class UnitModel {
     HttpListener<Unit> mUnitHttpListener;

     public void getUnit(String accessToken, int sceneId, String query, String sessionId,
                         HttpListener<Unit> unitHttpListener) {
          this.mUnitHttpListener = unitHttpListener;
          HttpClient.retrofit().create(API.UnitService.class)
                    .getUnit(accessToken, new UnitBody(sceneId, query, sessionId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Unit>() {
                         @Override
                         public void onSubscribe(Subscription s) {
                              s.request(Long.MAX_VALUE);
                         }

                         @Override
                         public void onNext(Unit unit) {
                              Log.e("TAG","---->---SUCCESS-----");
                              mUnitHttpListener.onSuccess(unit);
                         }

                         @Override
                         public void onError(Throwable t) {
                              Log.e("TAG","---->---ERROR-----"+t.getMessage());
                              mUnitHttpListener.onError();
                         }

                         @Override
                         public void onComplete() {

                         }
                    });

     }
}
