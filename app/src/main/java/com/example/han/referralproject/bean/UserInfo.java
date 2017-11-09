package com.example.han.referralproject.bean;

public class UserInfo {

    public String doctername;
    public String sfz;
    public String sex;
    public String eqid;
    public String bname;
    public String dz;
    public String tel;
    public String categoryname;
    public String mh;
    public String state;
    public String bid;
    public String age;
    public String categoryid;
    public String user_photo;
    public String amount;


    public UserInfo() {
        super();
    }


    public UserInfo(String doctername, String sfz, String sex, String eqid, String bname, String dz, String tel,
                    String categoryname, String mh, String state, String bid, String age, String categoryid, String user_photo, String amount) {
        super();
        this.doctername = doctername;
        this.sfz = sfz;
        this.sex = sex;
        this.eqid = eqid;
        this.bname = bname;
        this.dz = dz;
        this.tel = tel;
        this.categoryname = categoryname;
        this.mh = mh;
        this.state = state;
        this.bid = bid;
        this.age = age;
        this.categoryid = categoryid;
        this.user_photo = user_photo;
        this.amount = amount;

    }


    public String getDoctername() {
        return doctername;
    }


    public void setDoctername(String doctername) {
        this.doctername = doctername;
    }


    public String getSfz() {
        return sfz;
    }


    public void setSfz(String sfz) {
        this.sfz = sfz;
    }


    public String getSex() {
        return sex;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getEqid() {
        return eqid;
    }


    public void setEqid(String eqid) {
        this.eqid = eqid;
    }


    public String getBname() {
        return bname;
    }


    public void setBname(String bname) {
        this.bname = bname;
    }


    public String getDz() {
        return dz;
    }


    public void setDz(String dz) {
        this.dz = dz;
    }


    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getCategoryname() {
        return categoryname;
    }


    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }


    public String getMh() {
        return mh;
    }


    public void setMh(String mh) {
        this.mh = mh;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public String getBid() {
        return bid;
    }


    public void setBid(String bid) {
        this.bid = bid;
    }


    public String getAge() {
        return age;
    }


    public void setAge(String age) {
        this.age = age;
    }


    public String getCategoryid() {
        return categoryid;
    }


    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getuser_photo() {
        return user_photo;
    }


    public void setuser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "UserInfo [doctername=" + doctername + ", sfz=" + sfz + ", sex=" + sex + ", eqid=" + eqid + ", bname="
                + bname + ", dz=" + dz + ", tel=" + tel + ", categoryname=" + categoryname + ", mh=" + mh + ", state="
                + state + ", bid=" + bid + ", age=" + age + ", categoryid=" + categoryid + ", user_photo=" + user_photo + ", amount=" + amount + "]";
    }


}
