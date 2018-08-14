package debug;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.gcml.common.app.lifecycle.AppLifecycleCallbacks;
import com.gcml.lib_utils.UtilsManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 10:20
 * created by:gzq
 * description:TODO
 */
public class InitializationLifecycleCallbacks implements AppLifecycleCallbacks {
    private static final String TAG = "InitializationLifecycle";
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        Log.e(TAG, "onCreate: " );
        //初始化工具类
        UtilsManager.init(app);
        //语音模块初始化
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(app, builder.toString());
    }

    @Override
    public void onTerminate(Application app) {

    }
}
