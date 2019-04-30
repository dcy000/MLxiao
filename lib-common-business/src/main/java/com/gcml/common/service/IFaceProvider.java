package com.gcml.common.service;

import io.reactivex.Observable;

public interface IFaceProvider {
    Observable<String> getFaceId(String userId);
}
