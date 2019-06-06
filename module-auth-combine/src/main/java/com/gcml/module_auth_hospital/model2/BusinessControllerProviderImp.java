package com.gcml.module_auth_hospital.model2;

import com.gcml.common.service.IBusinessControllerProvider;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.findPassWord.CodeRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/common/business/fetch/code")
public class BusinessControllerProviderImp implements IBusinessControllerProvider {
    @Override
    public Observable<String> fetchCode(String phone) {
        return new CodeRepository().fetchCode(phone);
    }

    @Override
    public Observable<Object> isIdCardNotExit(String idCard) {
//        return new UserRepository().isAccountExist(idCard);
        return Observable.error(new RuntimeException("no impl"));
    }
}
