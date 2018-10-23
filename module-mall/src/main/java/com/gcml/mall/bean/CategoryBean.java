package com.gcml.mall.bean;

public class CategoryBean {

    /**
     * mallProductTypeId : 1
     * name : 试纸类
     * showStatus : 0
     */

    public CategoryBean(int typeId, String name, int showStatus) {
        this.mallProductTypeId = typeId;
        this.name = name;
        this.showStatus = showStatus;
    }

    public int mallProductTypeId;
    public String name;
    public int showStatus;

}
