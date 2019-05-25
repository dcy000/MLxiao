package com.gcml.module_health_profile.bracelet.bean;

import com.google.common.util.concurrent.ListeningExecutorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by lenovo on 2019/2/20.
 */

public class FalseCorrelationDataBean implements Serializable {
    public String name;
    public List<itemBean> items=new ArrayList<>();

    public static class itemBean implements Serializable {
        public String itemName;
        public String itemPhone;
    }

}
