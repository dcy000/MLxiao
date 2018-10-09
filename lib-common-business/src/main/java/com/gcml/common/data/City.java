package com.gcml.common.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by afirez on 2017/11/2.
 */

public class City {

    @SerializedName("name")
    private String name;

    @SerializedName("counties")
    private List<String> counties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCounties() {
        return counties;
    }

    public void setCounties(List<String> counties) {
        this.counties = counties;
    }
}
