package com.ml.brightness;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

public class BrightnessHelper {
    private ContentResolver resolver;
    private int maxBrightness = 255;

    public BrightnessHelper(Context context){
        resolver = context.getContentResolver();
    }

    private int adjustBrightnessNumber(int brightness){
        if (brightness < 0) {
            brightness = 0;
        } else if (brightness > 255) {
            brightness = 255;
        }
        return brightness;
    }

    public void offAutoBrightness(){
        try {
            int mode = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
            if(mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(resolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getSystemBrightness(){
        return Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS, 255);
    }

    public void setSystemBrightness(int newBrightness){
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS
                ,adjustBrightnessNumber(newBrightness));
    }

    public int getMaxBrightness() {
        return maxBrightness;
    }

    public void setAppBrightness(Activity activity, float brightnessPercent){
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightnessPercent;
        window.setAttributes(layoutParams);
    }
}
