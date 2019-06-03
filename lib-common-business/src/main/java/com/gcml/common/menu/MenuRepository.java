package com.gcml.common.menu;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.recommend.network.RecommendService;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2018/9/21.
 */

public class MenuRepository {

    MenuService menuService = RetrofitHelper.service(MenuService.class);

    public Observable<AppMenuBean> getAllMenus() {
        return menuService.getAllMenus().compose(RxUtils.apiResultTransformer());
    }

}
