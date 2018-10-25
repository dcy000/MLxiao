package com.gcml.auth.component;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.Utils;

import io.reactivex.Observable;

public class RefreshTokenComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.refreshToken";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        String deviceId = Utils.getDeviceId(context.getContentResolver());
        String userId = cc.getParamItem("userId", "");
        UserRepository repository = new UserRepository();
        Observable<UserEntity> rxUser = repository.refreshToken(deviceId, userId);
        CC.sendCCResult(cc.getCallId(), CCResult.success("data", rxUser));
        return false;
    }
}
