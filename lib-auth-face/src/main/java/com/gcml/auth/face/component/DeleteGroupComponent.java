package com.gcml.auth.face.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face.model.FaceRepository;

import io.reactivex.Observable;

public class DeleteGroupComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face.deleteGroup";
    }

    @Override
    public boolean onCall(CC cc) {
        FaceRepository faceRepository = new FaceRepository();
        Observable<Object> observable = faceRepository.deleteGroup();
        Object o = observable.blockingFirst();
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
