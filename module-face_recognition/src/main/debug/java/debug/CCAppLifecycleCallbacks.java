package debug;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;

public class CCAppLifecycleCallbacks implements AppLifecycleCallbacks {
    private static final String TAG = "CCAppLifecycleCallbacks";
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        Log.e(TAG, "onCreate: ");
        boolean debug = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        CC.enableVerboseLog(debug);
        CC.enableDebug(debug);
        CC.init(app);
//        CC.enableRemoteCC(true);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
