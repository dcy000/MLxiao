package com.sjtu.yifei.GZeAUxUSdp;

import com.gcml.module_doctor_advisory.ui.CheckContractActivity;
import com.gcml.module_doctor_advisory.ui.DiseaseDetailsActivity;
import com.gcml.module_doctor_advisory.ui.DoctorAskGuideActivity;
import com.gcml.module_doctor_advisory.ui.DoctorappoActivity2;
import com.gcml.module_doctor_advisory.ui.MessageActivity;
import com.gcml.module_doctor_advisory.ui.OfflineActivity;
import com.gcml.module_doctor_advisory.ui.OnlineDoctorListActivity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$GZeAUxUSdp$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/doctor/advisory/check/contract/activity", CheckContractActivity.class);
    routMap.put("/app/online/doctor/list", OnlineDoctorListActivity.class);
    routMap.put("/app/activity/offline/activity", OfflineActivity.class);
    routMap.put("/doctor/advisory/disease/details/activity", DiseaseDetailsActivity.class);
    routMap.put("/doctor/advisory/doctor/appo/activity2", DoctorappoActivity2.class);
    routMap.put("/doctor/advisory/doctor/ask/guide/activity", DoctorAskGuideActivity.class);
    routMap.put("/app/activity/message/activity", MessageActivity.class);
    return routMap;
  }
}
