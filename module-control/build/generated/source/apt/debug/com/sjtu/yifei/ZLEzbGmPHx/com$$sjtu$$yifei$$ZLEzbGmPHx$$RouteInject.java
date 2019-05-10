package com.sjtu.yifei.ZLEzbGmPHx;

import com.example.module_control_volume.robot.SpeechSynthesisActivity;
import com.example.module_control_volume.ui.SettingActivity;
import com.example.module_control_volume.update.AppUpdateProviderImp;
import com.example.module_control_volume.volume.VolumeControlProviderImp;
import com.example.module_control_volume.wifi.WifiConnectActivity;
import com.example.module_control_volume.wifi.WifiDetailActivity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$ZLEzbGmPHx$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/module/control/wifi/detail/activity", WifiDetailActivity.class);
    routMap.put("/app/speech/synthesis/activity", SpeechSynthesisActivity.class);
    routMap.put("/app/activity/wifi/connect", WifiConnectActivity.class);
    routMap.put("/module/control/setting/activity", SettingActivity.class);
    routMap.put("/module/control/volume/control/provider", VolumeControlProviderImp.class);
    routMap.put("/module/control/app/update/provider", AppUpdateProviderImp.class);
    return routMap;
  }
}
