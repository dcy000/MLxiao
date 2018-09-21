package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;

import java.util.List;

import io.reactivex.Observable;

public class GetUsersComponent implements IComponent{

    @Override
    public String getName() {
        return "com.gcml.auth.getUsers";
    }

    @Override
    public boolean onCall(CC cc) {
        UserRepository repository = new UserRepository();
        Observable<List<UserEntity>> rxUsers = repository.getUsers();
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxUsers));
        return false;
    }
}
