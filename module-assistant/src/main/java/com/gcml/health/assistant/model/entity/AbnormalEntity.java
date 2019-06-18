package com.gcml.health.assistant.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AbnormalEntity implements Parcelable {

    private String label;
    private String type;
    private String value;
    private String unit;

    protected AbnormalEntity(Parcel in) {
        label = in.readString();
        type = in.readString();
        value = in.readString();
        unit = in.readString();
    }

    public static final Creator<AbnormalEntity> CREATOR = new Creator<AbnormalEntity>() {
        @Override
        public AbnormalEntity createFromParcel(Parcel in) {
            return new AbnormalEntity(in);
        }

        @Override
        public AbnormalEntity[] newArray(int size) {
            return new AbnormalEntity[size];
        }
    };

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbnormalEntity that = (AbnormalEntity) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(type, that.type) &&
                Objects.equals(value, that.value) &&
                Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, type, value, unit);
    }

    @Override
    public String toString() {
        return "AbnormalEntity{" +
                "label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(type);
        dest.writeString(value);
        dest.writeString(unit);
    }
}
