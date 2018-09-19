package com.gcml.old.common.push;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.medlink.danbogh.utils.JpushAliasUtils;

public class PushDeleteTagComponent implements IComponent{
    @Override
    public String getName() {
        return "com.gcml.zzb.common.push.deleteTag";
    }

    @Override
    public boolean onCall(CC cc) {
        new JpushAliasUtils(cc.getContext().getApplicationContext()).deleteAlias();
        return false;
    }
}
