package com.gcml.module_yzn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 资讯分类列表中 条目bean
 */
public class FenLeiInfoOutBean implements Serializable {
    public List<ItemBean> datas;
    public String group;


    public static class ItemBean implements Serializable {
        public String name;
        public String pic;
        public String link;
    }

}
