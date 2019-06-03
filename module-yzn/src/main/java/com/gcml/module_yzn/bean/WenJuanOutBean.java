package com.gcml.module_yzn.bean;

import java.io.Serializable;
import java.util.List;

public class WenJuanOutBean implements Serializable {
    public List<ItemBean> itemBeans;

    public static class ItemBean implements Serializable{
        public String title;
        public String desp;
        public String link;
    }
}
