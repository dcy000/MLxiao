package com.gcml.module_health_profile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2019/3/3.
 */

public class DataBean implements Serializable {
    public String constitutionName;
    public int score;
    public Object oppositeList;
    public String result;
    public List<Integer> questionList;
}
