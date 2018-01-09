package com.witspring.model.entity;

/**
 * @author Created by wu_zf on 1/4/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseImg {
    private int orderIndex;
    private String photo;
    private String thumbPhoto;

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbPhoto() {
        return thumbPhoto;
    }

    public void setThumbPhoto(String thumbPhoto) {
        this.thumbPhoto = thumbPhoto;
    }
}
