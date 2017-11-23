package com.example.han.referralproject.bean;

import java.io.Serializable;
import java.util.List;

public class SymptomResultBean implements Serializable{

//    public String sid;
//    public String con;
//    public String deal;
//    public float probability;
    private List<zzs> zzs;
    private List<bqs> bqss;

    public List<SymptomResultBean.zzs> getZzs() {
        return zzs;
    }

    public void setZzs(List<SymptomResultBean.zzs> zzs) {
        this.zzs = zzs;
    }

    public List<bqs> getBqss() {
        return bqss;
    }

    public void setBqss(List<bqs> bqss) {
        this.bqss = bqss;
    }

    @Override
    public String toString() {
        return "SymptomResultBean{" +
                "zzs=" + zzs +
                ", bqss=" + bqss +
                '}';
    }

    public class zzs implements Serializable{
        private String id,name;

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

        @Override
        public String toString() {
            return "zzs{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    public class bqs implements Serializable{
        private String bname,gl,review,eat,sports,suggest;

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

        @Override
        public String toString() {
            return "bqs{" +
                    "bname='" + bname + '\'' +
                    ", gl='" + gl + '\'' +
                    ", review='" + review + '\'' +
                    ", eat='" + eat + '\'' +
                    ", sports='" + sports + '\'' +
                    ", suggest='" + suggest + '\'' +
                    '}';
        }
    }
}