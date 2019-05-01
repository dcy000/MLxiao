package com.gcml.common.recommend.bean.get;

import java.io.Serializable;

public class GoodsBean implements Serializable {

    public String goodsid;
    public String goodsname;
    public String goodsimage;
    public String goodsprice;
    public String goodstate;

    public GoodsBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public GoodsBean(String goodsid, String goodsname, String goodsimage, String goodsprice, String goodstate) {
        this.goodsid = goodsid;
        this.goodsname = goodsname;
        this.goodsimage = goodsimage;
        this.goodsprice = goodsprice;
        this.goodstate = goodstate;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "goodsid='" + goodsid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", goodsimage='" + goodsimage + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", goodstate='" + goodstate + '\'' +
                '}';
    }
}
