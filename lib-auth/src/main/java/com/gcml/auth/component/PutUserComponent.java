package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

public class PutUserComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.putUser";
    }

    @Override
    public boolean onCall(CC cc) {
        UserEntity user = cc.getParamItem("user");
        UserRepository userRepository = new UserRepository();
        Observable<Object> rxPutProfile = userRepository.putProfile(user);
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxPutProfile));
        return false;
    }
}
