package com.sjtu.yifei.IAQJyBLlgT;

import com.gcml.alarm.AlarmDetail2Activity;
import com.gcml.alarm.AlarmList2Activity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$IAQJyBLlgT$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/app/alarm/details2/activity", AlarmDetail2Activity.class);
    routMap.put("/app/alarm/list/activity", AlarmList2Activity.class);
    return routMap;
  }
}
