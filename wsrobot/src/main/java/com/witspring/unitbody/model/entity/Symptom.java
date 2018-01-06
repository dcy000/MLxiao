package com.witspring.unitbody.model.entity;

import com.witspring.util.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 症状
 * Created by chenqiang on 16/3/8.
 */
public class Symptom implements Serializable {

    private static final long serialVersionUID = 5644801426591487606L;
    private String name;//症状名称
    private String organName;// 器官名称
    private String desc;//症状描述
    private String slang;//大白话
    private boolean hasFeature;

    public Symptom() {}

    public Symptom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSlang() {
        return slang;
    }

    public void setSlang(String slang) {
        this.slang = slang;
    }

    public String getDisplay() {
        return StringUtil.isNotTrimBlank(slang) ? slang : name;
    }

    public boolean hasFeature() {
        return hasFeature;
    }

    public void setHasFeature(boolean hasFeature) {
        this.hasFeature = hasFeature;
    }

    public static class Feature implements Serializable {

        private static final long serialVersionUID = -2816647356534629423L;
        private int id;
        private String name;
        private int probality;
        private int status;
        private String type;
        private List<FeatureItem> items;
        private boolean multiple;
        private String question;
        public static final int LABEL_SAME = 0, LABEL_GLOBAL = 1, LABEL_NONE = 2;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProbality() {
            return probality;
        }

        public void setProbality(int probality) {
            this.probality = probality;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<FeatureItem> getItems() {
            return items;
        }

        public void setItems(List<FeatureItem> items) {
            this.items = items;
        }

        public boolean isMultiple() {
            return multiple;
        }

        public void setMultiple(boolean multiple) {
            this.multiple = multiple;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

    }

    public static class FeatureItem implements Serializable {

        private static final long serialVersionUID = 9025493087612619013L;
        private String desc;
        private boolean isDK;
        private String name;
        private int type = TYPE_DEFAULT;
        public static final int TYPE_DEFAULT = 0, TYPE_NONE = 1, TYPE_UNSURE = 2;
        public boolean checked;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public boolean isDK() {
            return isDK;
        }

        public void setDK(boolean DK) {
            isDK = DK;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

}
