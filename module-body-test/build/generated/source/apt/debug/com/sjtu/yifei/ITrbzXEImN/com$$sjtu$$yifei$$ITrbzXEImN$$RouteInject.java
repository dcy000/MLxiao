package com.sjtu.yifei.ITrbzXEImN;

import com.gcml.module_body_test.HuiQuanBodyTestProviderImp;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$ITrbzXEImN$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/huiquan/body/test/provider", HuiQuanBodyTestProviderImp.class);
    return routMap;
  }
}
