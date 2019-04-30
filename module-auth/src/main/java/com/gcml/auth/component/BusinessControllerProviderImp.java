package com.gcml.auth.component;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.service.IBusinessControllerProvider;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/common/business/fetch/code")
public class BusinessControllerProviderImp implements IBusinessControllerProvider {
    @Override
    public Observable<String> fetchCode(String phone) {
        return new UserRepository().fetchCode(phone);
    }

    @Override
    public Observable<Object> isIdCardNotExit(String idCard) {
        return new UserRepository().isIdCardNotExit(idCard);
    }
}
