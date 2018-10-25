package com.gcml.auth.face.debug.model;

import com.gcml.common.repository.RepositoryApp;

import io.reactivex.Observable;

public class FaceBdRepository {

    private FaceBdService mFaceBdService =
            RepositoryApp.INSTANCE
                    .repositoryComponent()
                    .repositoryHelper()
                    .retrofitService(FaceBdService.class);


    public Observable<String> addFace(byte[] faceData, String faceId) {
        return null;
    }

}
