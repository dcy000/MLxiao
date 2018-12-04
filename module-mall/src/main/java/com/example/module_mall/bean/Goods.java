package com.example.module_mall.bean;

import java.io.Serializable;

public class Goods implements Serializable {

    public String goodsid;
    public String goodsname;
    public String goodsimage;
    public String goodsprice;
    public String goodstate;


    public Goods() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Goods(String goodsid, String goodsname, String goodsimage, String goodsprice, String goodstate) {
        this.goodsid = goodsid;
        this.goodsname = goodsname;
        this.goodsimage = goodsimage;
        this.goodsprice = goodsprice;
        this.goodstate = goodstate;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsimage() {
        return goodsimage;
    }

    public void setGoodsimage(String goodsimage) {
        this.goodsimage = goodsimage;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public String getGoodstate() {
        return goodstate;
    }

    public void setGoodstate(String goodstate) {
        this.goodstate = goodstate;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsid='" + goodsid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsimage='" + goodsimage + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", goodstate='" + goodstate + '\'' +
                '}';
    }
}
