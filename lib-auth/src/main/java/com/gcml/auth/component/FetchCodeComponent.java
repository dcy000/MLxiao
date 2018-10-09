package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;

import io.reactivex.Observable;

public class FetchCodeComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.fetchCode";
    }

    @Override
    public boolean onCall(CC cc) {
        UserRepository repository = new UserRepository();
        String phone = cc.getParamItem("phone");
        Observable<String> rxCode = repository.fetchCode(phone);
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxCode));
        return false;
    }
}
