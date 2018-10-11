package com.gcml.mall.bean;

import java.io.Serializable;

public class GoodsDetailBean implements Serializable {

    public String goodsid;
    public String goodsname;
    public String goodsimage;
    public String goodsprice;
    public String goodstate;

    public GoodsDetailBean(String goodsid, String goodsname, String goodsimage, String goodsprice, String goodstate) {
        this.goodsid = goodsid;
        this.goodsname = goodsname;
        this.goodsimage = goodsimage;
        this.goodsprice = goodsprice;
        this.goodstate = goodstate;
    }

    @Override
    public String toString() {
        return "GoodsDetailBean{" +
                "goodsid='" + goodsid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsimage='" + goodsimage + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", goodstate='" + goodstate + '\'' +
                '}';
    }
}
