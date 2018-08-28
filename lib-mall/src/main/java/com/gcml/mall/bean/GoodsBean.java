package com.gcml.mall.bean;

/**
 * Created by wecent on 2018/8/15.
 * 商品信息
 */

public class GoodsBean {

    public String name;
    public int img;
    public String price;

    public GoodsBean(String name, int img, String price) {
        this.name = name;
        this.img = img;
        this.price = price;
    }

}
