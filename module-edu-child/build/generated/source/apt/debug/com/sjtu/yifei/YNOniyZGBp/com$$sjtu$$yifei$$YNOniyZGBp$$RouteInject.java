package com.sjtu.yifei.YNOniyZGBp;

import com.gcml.module_edu_child.ui.ChildEduHomeActivity;
import com.gcml.module_edu_child.ui.ChildEduJokesActivity;
import com.gcml.module_edu_child.ui.ChildEduPoemListActivity;
import com.gcml.module_edu_child.ui.ChildEduSheetDetailsActivity;
import com.sjtu.yifei.annotation.Inject;
import com.sjtu.yifei.ioc.RouteInject;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

@Inject
public class com$$sjtu$$yifei$$YNOniyZGBp$$RouteInject implements RouteInject {
  @Override
  public Map<String, Class<?>> getRouteMap() {
    Map<String, Class<?>> routMap = new HashMap<>();
    routMap.put("/edu/child/edu/home/activity", ChildEduHomeActivity.class);
    routMap.put("/edu/child/sheet/details/activity", ChildEduSheetDetailsActivity.class);
    routMap.put("/edu/child/jokes/activity", ChildEduJokesActivity.class);
    routMap.put("/edu/child/poem/list/activity", ChildEduPoemListActivity.class);
    return routMap;
  }
}
