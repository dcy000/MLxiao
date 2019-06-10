package com.gcml.common.menu;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.Utils;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2018/9/21.
 */

public class MenuRepository {

    MenuService menuService = RetrofitHelper.service(MenuService.class);

    public Observable<AppMenuBean> getAllMenus(boolean localJson) {
        if (localJson) {
            String provincesArr = Utils.readTextFromAssetFile(UM.getApp().getApplicationContext(), "menu_nav.json");
            Gson gson = new Gson();
            return Observable
                    .just(gson.fromJson(provincesArr, AppMenuBean.class))
                    .subscribeOn(Schedulers.io());
        } else {
            return menuService.getAllMenus().compose(RxUtils.apiResultTransformer());
        }
    }

}
