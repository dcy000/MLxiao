package com.example.han.referralproject;

public class HealthLifeBean {
    private String mainTitle;
    private String subTitle;
    private int menuImage;

    public HealthLifeBean(String mainTitle, String subTitle, int menuImage) {
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.menuImage = menuImage;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(int menuImage) {
        this.menuImage = menuImage;
    }
}
