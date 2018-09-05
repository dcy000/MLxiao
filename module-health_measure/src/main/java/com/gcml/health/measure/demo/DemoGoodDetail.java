package com.gcml.health.measure.demo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/4 17:13
 * created by:gzq
 * description:TODO
 */
public class DemoGoodDetail implements Parcelable{
    private int img;
    private String title;
    private String description;
    private String price;

    public DemoGoodDetail() {
    }

    public DemoGoodDetail(int img, String title, String description, String price) {
        this.img = img;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    protected DemoGoodDetail(Parcel in) {
        img = in.readInt();
        title = in.readString();
        description = in.readString();
        price = in.readString();
    }

    public static final Creator<DemoGoodDetail> CREATOR = new Creator<DemoGoodDetail>() {
        @Override
        public DemoGoodDetail createFromParcel(Parcel in) {
            return new DemoGoodDetail(in);
        }

        @Override
        public DemoGoodDetail[] newArray(int size) {
            return new DemoGoodDetail[size];
        }
    };

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(img);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
    }
}
