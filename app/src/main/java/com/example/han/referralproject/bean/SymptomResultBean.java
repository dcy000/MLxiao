package com.example.han.referralproject.bean;

import java.io.Serializable;
import java.util.List;

public class SymptomResultBean implements Serializable{

    private List<zzs> zzs;
    private List<bqs> bqs;

    public List<SymptomResultBean.zzs> getZzs() {
        return zzs;
    }


    public void setZzs(List<SymptomResultBean.zzs> zzs) {
        this.zzs = zzs;
    }

    public List<SymptomResultBean.bqs> getBqs() {
        return bqs;
    }

    public void setBqs(List<SymptomResultBean.bqs> bqs) {
        this.bqs = bqs;
    }

    public static class zzs implements Serializable{
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public static class bqs implements Serializable{
        private String bname;
        private String gl;
        private String review;
        private String eat;
        private String sports;
        private String suggest;

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getGl() {
            return gl;
        }

        public void setGl(String gl) {
            this.gl = gl;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getEat() {
            return eat;
        }

        public void setEat(String eat) {
            this.eat = eat;
        }

        public String getSports() {
            return sports;
        }

        public void setSports(String sports) {
            this.sports = sports;
        }

        public String getSuggest() {
            return suggest;
        }

        public void setSuggest(String suggest) {
            this.suggest = suggest;
        }
    }
}