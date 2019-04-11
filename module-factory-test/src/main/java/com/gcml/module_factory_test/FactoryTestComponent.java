package com.gcml.module_factory_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_factory_test.ui.FactoryTestActivity;

public class FactoryTestComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.system.factoryTest";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
//        Intent intent = new Intent(context, FactoryTestActivity.class);
        Intent intent = new Intent(context, FactoryTestActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        return false;
    }
}
