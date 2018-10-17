package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;

public class DeleteUsersComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.deleteUsers";
    }

    @Override
    public boolean onCall(CC cc) {
        UserRepository userRepository = new UserRepository();
        userRepository.deleteUsers();
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
