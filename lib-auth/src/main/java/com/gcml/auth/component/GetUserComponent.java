package com.gcml.auth.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.common.data.UserEntity;
import com.gcml.auth.model.UserRepository;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GetUserComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.user.auth.getUser";
    }

    @Override
    public boolean onCall(CC cc) {
        UserRepository repository = new UserRepository();
        Single<UserEntity> rxUser = repository.getUserSignIn();
        rxUser.subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<UserEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(UserEntity user) {
                        CC.sendCCResult(cc.getCallId(), CCResult.success("user", user));
                    }

                    @Override
                    public void onError(Throwable e) {
                        CC.sendCCResult(cc.getCallId(), CCResult.error("请重新登录！"));
                    }
                });
        return true;
    }
}
