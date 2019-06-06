package com.gcml.common.menu;

import com.gcml.common.service.IMenuHelperProvider;
import com.gcml.common.utils.RxUtils;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DefaultObserver;

@Route(path = "/common/business/menu/helper/provider")
public class MenuHelperProviderImp implements IMenuHelperProvider {
    List<MenuEntity> menuEntities = new ArrayList<>();

    @Override
    public void menu(boolean is, EMenu menu, MenuResult callback) {
        new MenuRepository()
                .getAllMenus(is)
                .compose(RxUtils.io2Main())
                .subscribe(new DefaultObserver<AppMenuBean>() {
                    @Override
                    public void onNext(AppMenuBean appMenuBean) {
                        switch (menu) {
                            case MAIN:
                                dealMenu(appMenuBean.getMenuList().get(0), callback);
                                break;
                            case DETECTION:
                                dealMenu(appMenuBean.getMenuList().get(2), callback);
                                break;
                            case LOGIN:
                                dealMenu(appMenuBean.getMenuList().get(1), callback);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError("获取菜单失败，请联系管理员");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dealMenu(AppMenuBean.MenuListBean menuListBean, MenuResult callback) {
        if (menuListBean == null) {
            callback.onError("获取菜单失败，请联系管理员");
            return;
        }
        List<AppMenuBean.MenuListBean> list = menuListBean.getList();
        if (list == null) {
            callback.onError("获取菜单失败，请联系管理员");
            return;
        }
        for (AppMenuBean.MenuListBean menu : list) {
            MenuEntity entity = new MenuEntity();
            entity.setMenuLabel(menu.getName());
            entity.setOrderNum(menu.getOrderNum());
            entity.setRouterPath(menu.getUrl());
            menuEntities.add(entity);
        }
        callback.onSuccess(menuEntities);
    }


    public interface MenuResult {
        void onSuccess(List<MenuEntity> menus);

        void onError(String msg);
    }

}
