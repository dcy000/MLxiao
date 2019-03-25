package com.gcml.module_health_profile.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_health_profile.OutputResultActivity;
import com.gcml.module_health_profile.webview.WenZenOutputActivity;

public class WenZenOutputComponent implements IComponent {
    @Override
    public String getName() {
        return "health.profile.wenzen.output";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        String highPrssure = cc.getParamItem("highPrssure");
        String lowPressure = cc.getParamItem("lowPressure");
        Intent intent = new Intent(context, WenZenOutputActivity.class);
        intent.putExtra("highPrssure", highPrssure);
        intent.putExtra("lowPressure", lowPressure);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
