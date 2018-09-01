package com.gcml.task.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.task.bean.get.TaskHealthBean;
import com.gcml.task.network.TaskRepository;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class IsTaskComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.task.isTask";
    }

    @Override
    public boolean onCall(CC cc) {
        TaskRepository repository = new TaskRepository();
        Observable<Object> rxUser = repository.isTaskHealthListFromApi((String) SPUtil.get("user_id",""));
        rxUser.subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object body) {
                        CC.sendCCResult(cc.getCallId(), CCResult.success());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable instanceof NullPointerException) {
                            CC.sendCCResult(cc.getCallId(), CCResult.success());
                        } else {
                            CC.sendCCResult(cc.getCallId(), CCResult.error(""));
                        }
                    }
                });
        return true;
    }
}
