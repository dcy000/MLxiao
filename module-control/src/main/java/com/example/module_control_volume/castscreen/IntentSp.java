package com.example.module_control_volume.castscreen;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class IntentSp {
    public static void restartActivity(Activity activity, boolean isSaveActivityToHistory) {
        if (activity == null) return;
        Intent intent = new Intent();
        String packageName = activity.getPackageName();
        String className = activity.getLocalClassName();
        String componentClassName = packageName + "." + className;
        if (className != null && className.split(".").length > 0) {
            componentClassName = className;
        }
        ComponentName componentName = new ComponentName(packageName, componentClassName);
        intent.setComponent(componentName);
        if (!isSaveActivityToHistory) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//添加该标志后，让activity不保存
        }
        activity.startActivity(intent);
        activity.finish();
    }

    public static void startActivity(Context context, String action, boolean isSaveActivityToHistory) {
        if (context == null || action == null) {
            return;
        }
        Intent intent=new Intent(action);
        if(!isSaveActivityToHistory){
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        context.startActivity(intent);
    }
}