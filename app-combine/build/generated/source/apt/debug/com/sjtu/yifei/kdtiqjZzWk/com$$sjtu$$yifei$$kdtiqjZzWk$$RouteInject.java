package com.sjtu.yifei.kdtiqjZzWk;

import com.example.han.referralproject.WelcomeActivity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$kdtiqjZzWk$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/app/welcome/activity", WelcomeActivity.class);
    return routMap;
  }
}
