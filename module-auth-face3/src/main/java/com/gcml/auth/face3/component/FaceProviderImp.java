package com.gcml.auth.face3.component;

import com.gcml.auth.face3.model.FaceBdRepository;
import com.gcml.common.service.IFaceProvider;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;
@Route(path = "/auth/face3/face/provider")
public class FaceProviderImp implements IFaceProvider {
    @Override
    public Observable<String> getFaceId(String userId) {
        return  new FaceBdRepository().getFaceId(userId);
    }
}
