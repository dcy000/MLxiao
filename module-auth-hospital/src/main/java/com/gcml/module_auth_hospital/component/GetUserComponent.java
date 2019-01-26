package com.gcml.module_auth_hospital.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.common.data.UserEntity;
import com.gcml.module_auth_hospital.model.UserRepository;

import io.reactivex.Observable;

import static com.gcml.common.IConstant.KEY_GET_USER_INFO;

/**
 * Created by lenovo on 2019/1/22.
 */

public class GetUserComponent implements IComponent {
    @Override
    public String getName() {
        return KEY_GET_USER_INFO;
    }

    @Override
    public boolean onCall(CC cc) {
        //暂时是本地的 User
        UserRepository repository = new UserRepository();
        Observable<UserEntity> rxUser = repository.getUserSignIn();
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxUser));
        return false;
    }
}
