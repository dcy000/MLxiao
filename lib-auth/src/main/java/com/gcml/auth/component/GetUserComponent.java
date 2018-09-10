package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

public class GetUserComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.getUser";
    }

    @Override
    public boolean onCall(CC cc) {
        //暂时是本地的 User
        UserRepository repository = new UserRepository();
        Observable<UserEntity> rxUser = repository.getUserSignIn();
        CC.sendCCResult(cc.getCallId(), CCResult.success("user", rxUser));
        return false;
    }
}
