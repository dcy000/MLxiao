package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;

import io.reactivex.Observable;

public class FetchUserComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.fetchUser";
    }

    @Override
    public boolean onCall(CC cc) {
        UserRepository repository = new UserRepository();
        Observable<UserEntity> rxUser = repository.fetchUser(UserSpHelper.getUserId());
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxUser));
        return false;
    }
}
