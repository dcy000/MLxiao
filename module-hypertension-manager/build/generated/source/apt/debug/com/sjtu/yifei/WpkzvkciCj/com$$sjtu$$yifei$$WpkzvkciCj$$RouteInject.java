package com.sjtu.yifei.WpkzvkciCj;

import com.gcml.module_hypertension_manager.ui.BasicInformationActivity;
import com.gcml.module_hypertension_manager.ui.DetecteTipActivity;
import com.gcml.module_hypertension_manager.ui.HypertensionTipActivity;
import com.gcml.module_hypertension_manager.ui.IsEmptyStomachOrNotActivity;
import com.gcml.module_hypertension_manager.ui.NormalHightActivity;
import com.gcml.module_hypertension_manager.ui.SlowDiseaseManagementActivity;
import com.gcml.module_hypertension_manager.ui.SlowDiseaseManagementTipActivity;
import com.gcml.module_hypertension_manager.ui.plan.TreatmentPlanActivity;
import com.gcml.module_hypertension_manager.zhongyi.OlderHealthManagementSerciveActivity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$WpkzvkciCj$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/app/hypertension/management/normal/hight", NormalHightActivity.class);
    routMap.put("/app/slow/disease/management/tip", SlowDiseaseManagementTipActivity.class);
    routMap.put("/app/hypertension/basic/information", BasicInformationActivity.class);
    routMap.put("/app/heypertension/detecte/tip", DetecteTipActivity.class);
    routMap.put("/app/hypertension/is/empty/stomach/or/not", IsEmptyStomachOrNotActivity.class);
    routMap.put("/app/health/manager/treatment", TreatmentPlanActivity.class);
    routMap.put("/app/hypertension/slow/disease/management", SlowDiseaseManagementActivity.class);
    routMap.put("hypertension/manager/older/health/management/sercive", OlderHealthManagementSerciveActivity.class);
    routMap.put("/app/hypertension/tip/activity", HypertensionTipActivity.class);
    return routMap;
  }
}
