package com.linheimx.app.library.data;

/**
 * Created by lijian on 2016/11/13.
 */

public class Entry1 {

    protected double x;
    protected double y;

    public Entry1(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "日期：" + x + "    "+"脉搏：" + y;
    }
}
