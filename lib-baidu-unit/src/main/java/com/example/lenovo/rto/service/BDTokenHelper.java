package com.example.lenovo.rto.service;


import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.common.bd.BDInterface;
import com.sjtu.yifei.annotation.Route;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

@Route(path = "bd/token/provider")
public class BDTokenHelper implements BDInterface {
    @Override
    public void getBdToken() {
        initAToken();
    }

    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(new HttpListener<AccessToken>() {
            @Override
            public void onSuccess(AccessToken data) {
                EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
