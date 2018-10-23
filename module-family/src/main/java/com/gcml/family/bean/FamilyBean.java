package com.gcml.family.bean;

import java.io.Serializable;

public class FamilyBean implements Serializable {

    public String name;
    public String tag;

    public FamilyBean(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }
}
