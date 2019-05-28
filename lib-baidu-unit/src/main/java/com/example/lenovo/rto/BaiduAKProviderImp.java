package com.example.lenovo.rto;

import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.common.service.IBaiduAKProvider;
import com.sjtu.yifei.annotation.Route;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
@Route(path = "/baidu/unit/ak/provider")
public class BaiduAKProviderImp implements IBaiduAKProvider {
    @Override
    public void initAK() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(new HttpListener<AccessToken>() {
            @Override
            public void onSuccess(AccessToken data) {
                EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
            }

            @Override
            public void onError() {
//                ToastUtils.showShort("初始化AK失败");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
