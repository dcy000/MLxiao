package com.gcml.old.common.push;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.medlink.danbogh.utils.JpushAliasUtils;

public class PushSetTagComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.zzb.common.push.setTag";
    }

    @Override
    public boolean onCall(CC cc) {
        new JpushAliasUtils(cc.getContext().getApplicationContext())
                .setAlias("user_" + cc.getParamItem("userId"));
        CC.sendCCResult(cc.getCallId(),CCResult.success());
        return false;
    }
}
