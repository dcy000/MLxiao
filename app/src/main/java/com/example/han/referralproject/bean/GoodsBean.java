package com.example.han.referralproject.bean;

/**
 * Created by gzq on 2018/1/5.
 */

public class GoodsBean {
    private String name;
    private int img;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public GoodsBean(String name, int img, String price) {
        this.name = name;
        this.img = img;
        this.price = price;
    }

    public GoodsBean() {
    }
}
