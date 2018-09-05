package com.gcml.auth.face.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face.model.FaceRepository;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;

import io.reactivex.schedulers.Schedulers;

public class JoinGroupComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face.joingroup";
    }

    @Override
    public boolean onCall(CC cc) {
        FaceRepository faceRepository = new FaceRepository();
        faceRepository.tryJoinGroup(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String groupId) {
                        CC.sendCCResult(cc.getCallId(), CCResult.success("groupId", groupId));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CC.sendCCResult(cc.getCallId(), CCResult.error("当前设备未注册过人脸, 请登录后注册人脸"));
                    }
                });
        return true;
    }
}
