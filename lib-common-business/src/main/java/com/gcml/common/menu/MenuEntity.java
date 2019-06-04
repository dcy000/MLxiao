package com.gcml.common.menu;

import android.support.annotation.NonNull;

public class MenuEntity implements Comparable<MenuEntity> {
    public int menuImage;
    public String menuLabel;
    public String routerPath;
    public int orderNum;

    public MenuEntity() {
    }

    public MenuEntity(int menuImage, String menuLabel) {
        this.menuImage = menuImage;
        this.menuLabel = menuLabel;
    }

    public MenuEntity(int menuImage, String menuLabel, String routerPath) {
        this.menuImage = menuImage;
        this.menuLabel = menuLabel;
        this.routerPath = routerPath;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public String getRouterPath() {
        return routerPath;
    }

    public void setRouterPath(String routerPath) {
        this.routerPath = routerPath;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public int compareTo(@NonNull MenuEntity o) {
        return this.getOrderNum() - o.getOrderNum();
    }
}
