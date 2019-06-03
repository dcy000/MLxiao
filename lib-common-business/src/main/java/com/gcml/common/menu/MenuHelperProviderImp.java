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
    public void menu(EMenu menu, MenuResult callback) {
        new MenuRepository()
                .getAllMenus()
                .compose(RxUtils.io2Main())
                .subscribe(new DefaultObserver<AppMenuBean>() {
                    @Override
                    public void onNext(AppMenuBean appMenuBean) {
                        AppMenuBean.MenuListBean menuListBean = appMenuBean.getMenuList().get(0);//主界面
                        if (menuListBean == null) {
                            callback.onError("获取菜单失败，请联系管理员");
                            return;
                        }
                        List<AppMenuBean.MenuListBean> list = menuListBean.getList();
                        if (list == null) {
                            callback.onError("获取菜单失败，请联系管理员");
                            return;
                        }
                        switch (menu) {
                            case MAIN:
                                dealMainMenu(list, callback);
                                break;
                            case DETECTION:
                                break;
                            case PERSON_DETAIL:
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

    private void dealMainMenu(List<AppMenuBean.MenuListBean> list, MenuResult callback) {
        for (AppMenuBean.MenuListBean menu : list) {
            MenuEntity entity = new MenuEntity();
            String name = menu.getName();
            entity.setMenuLabel(name);
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
