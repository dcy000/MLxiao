package com.gcml.common.service;

import io.reactivex.Observable;

public interface IBusinessControllerProvider {
    Observable<String> fetchCode(String phone);
    Observable<Object> isIdCardNotExit(String idCard);

}
