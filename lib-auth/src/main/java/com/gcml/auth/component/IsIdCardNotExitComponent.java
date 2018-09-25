package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;

import io.reactivex.Observable;

public class IsIdCardNotExitComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.isIdCardNotExit";
    }

    @Override
    public boolean onCall(CC cc) {
        String idCard = cc.getParamItem("idCard");
        UserRepository repository = new UserRepository();
        Observable<Object> rxIdCardRegistered = repository.isIdCardNotExit(idCard);
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxIdCardRegistered));
        return false;
    }
}
