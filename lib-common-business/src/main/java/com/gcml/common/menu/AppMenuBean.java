package com.gcml.common.menu;

import java.util.List;

public class AppMenuBean {

    private List<MenuListBean> menuList;
    private List<String> permissions;

    public List<MenuListBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuListBean> menuList) {
        this.menuList = menuList;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public static class MenuListBean {
        private int menuId;
        private int parentId;
        private Object parentName;
        private String name;
        private String url;
        private String menuAgent;
        private String perms;
        private int type;
        private String icon;
        private int orderNum;
        private Object open;
        private List<MenuListBean> list;

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public Object getParentName() {
            return parentName;
        }

        public void setParentName(Object parentName) {
            this.parentName = parentName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMenuAgent() {
            return menuAgent;
        }

        public void setMenuAgent(String menuAgent) {
            this.menuAgent = menuAgent;
        }

        public String getPerms() {
            return perms;
        }

        public void setPerms(String perms) {
            this.perms = perms;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public Object getOpen() {
            return open;
        }

        public void setOpen(Object open) {
            this.open = open;
        }

        public List<MenuListBean> getList() {
            return list;
        }

        public void setList(List<MenuListBean> list) {
            this.list = list;
        }
    }
}
