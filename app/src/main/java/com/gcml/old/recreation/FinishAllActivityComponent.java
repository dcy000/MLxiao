package com.gcml.old.recreation;


import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.common.data.AppManager;

/**
 * Created by lenovo on 2018/8/27.
 */

public class FinishAllActivityComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.finishAll";
    }

    @Override
    public boolean onCall(CC cc) {
        AppManager.getAppManager().finishAllActivity();
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
