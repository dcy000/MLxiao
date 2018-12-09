package com.example.han.referralproject.person.bean;

/**
 * Created by gzq on 2018/1/5.
 */

public class DiseaseUser {
    private String name;
    private int sex;
    private int age;
    private String photo;

    public DiseaseUser() {
    }

    public DiseaseUser(String name, int sex, int age, String photo) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
