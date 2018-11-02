package com.gcml.family.bean;

import java.io.Serializable;

public class NewsBean implements Serializable {

    public String msg;
    public int type;
    public int time;

    public NewsBean(String msg, int type, int time) {
        this.msg = msg;
        this.type = type;
        this.time = time;
    }
}
