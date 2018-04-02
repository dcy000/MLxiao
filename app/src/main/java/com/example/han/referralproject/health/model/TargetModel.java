package com.example.han.referralproject.health.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2018/4/2.
 */

public class TargetModel implements Parcelable {
    private String title;

    private String target;

    private String source;

    public TargetModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetModel that = (TargetModel) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        return source != null ? source.equals(that.source) : that.source == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TargetModel{" +
                "title='" + title + '\'' +
                ", target='" + target + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.target);
        dest.writeString(this.source);
    }

    protected TargetModel(Parcel in) {
        this.title = in.readString();
        this.target = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<TargetModel> CREATOR = new Parcelable.Creator<TargetModel>() {
        @Override
        public TargetModel createFromParcel(Parcel source) {
            return new TargetModel(source);
        }

        @Override
        public TargetModel[] newArray(int size) {
            return new TargetModel[size];
        }
    };
}
