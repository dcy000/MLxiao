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

    private int targetLength;

    private int sourceLength;

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

    public int getTargetLength() {
        return targetLength;
    }

    public void setTargetLength(int targetLength) {
        this.targetLength = targetLength;
    }

    public int getSourceLength() {
        return sourceLength;
    }

    public void setSourceLength(int sourceLength) {
        this.sourceLength = sourceLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TargetModel that = (TargetModel) o;

        if (targetLength != that.targetLength) return false;
        if (sourceLength != that.sourceLength) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        return source != null ? source.equals(that.source) : that.source == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + targetLength;
        result = 31 * result + sourceLength;
        return result;
    }

    @Override
    public String toString() {
        return "TargetModel{" +
                "title='" + title + '\'' +
                ", target='" + target + '\'' +
                ", source='" + source + '\'' +
                ", targetLength=" + targetLength +
                ", sourceLength=" + sourceLength +
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
        dest.writeInt(this.targetLength);
        dest.writeInt(this.sourceLength);
    }

    protected TargetModel(Parcel in) {
        this.title = in.readString();
        this.target = in.readString();
        this.source = in.readString();
        this.targetLength = in.readInt();
        this.sourceLength = in.readInt();
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
