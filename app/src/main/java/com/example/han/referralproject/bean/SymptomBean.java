package com.example.han.referralproject.bean;

import java.io.Serializable;

public class SymptomBean implements Serializable{
    public  boolean isSelected;
    public String id;
    public String name;

    public SymptomBean(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
