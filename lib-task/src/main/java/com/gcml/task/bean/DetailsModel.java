package com.gcml.task.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by afirez on 2018/3/22.
 */

public class DetailsModel implements Parcelable {

    private int what;
    private String action;
    private String title;
    private int unitPosition;
    private String[] units;
    private String[] unitSum;
    private float[] selectedValues;
    private float[] minValues;
    private float[] maxValues;
    private float[] perValues;

    public DetailsModel() {

    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnitPosition() {
        return unitPosition;
    }

    public void setUnitPosition(int unitPosition) {
        this.unitPosition = unitPosition;
    }

    public String[] getUnits() {
        return units;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }

    public String[] getUnitSum() {
        return unitSum;
    }

    public void setUnitSum(String[] unitSum) {
        this.unitSum = unitSum;
    }

    public float[] getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(float[] selectedValues) {
        this.selectedValues = selectedValues;
    }

    public float[] getMinValues() {
        return minValues;
    }

    public void setMinValues(float[] minValues) {
        this.minValues = minValues;
    }

    public float[] getMaxValues() {
        return maxValues;
    }

    public void setMaxValues(float[] maxValues) {
        this.maxValues = maxValues;
    }

    public float[] getPerValues() {
        return perValues;
    }

    public void setPerValues(float[] perValues) {
        this.perValues = perValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailsModel that = (DetailsModel) o;

        if (what != that.what) return false;
        if (unitPosition != that.unitPosition) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(units, that.units)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(unitSum, that.unitSum)) return false;
        if (!Arrays.equals(selectedValues, that.selectedValues)) return false;
        if (!Arrays.equals(minValues, that.minValues)) return false;
        if (!Arrays.equals(maxValues, that.maxValues)) return false;
        return Arrays.equals(perValues, that.perValues);
    }

    @Override
    public int hashCode() {
        int result = what;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + unitPosition;
        result = 31 * result + Arrays.hashCode(units);
        result = 31 * result + Arrays.hashCode(unitSum);
        result = 31 * result + Arrays.hashCode(selectedValues);
        result = 31 * result + Arrays.hashCode(minValues);
        result = 31 * result + Arrays.hashCode(maxValues);
        result = 31 * result + Arrays.hashCode(perValues);
        return result;
    }

    @Override
    public String toString() {
        return "DetailsModel{" +
                "what=" + what +
                ", action='" + action + '\'' +
                ", title='" + title + '\'' +
                ", unitPosition=" + unitPosition +
                ", units=" + Arrays.toString(units) +
                ", unitSum=" + Arrays.toString(unitSum) +
                ", selectedValues=" + Arrays.toString(selectedValues) +
                ", minValues=" + Arrays.toString(minValues) +
                ", maxValues=" + Arrays.toString(maxValues) +
                ", perValues=" + Arrays.toString(perValues) +
                '}';


    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.what);
        dest.writeString(this.action);
        dest.writeString(this.title);
        dest.writeInt(this.unitPosition);
        dest.writeStringArray(this.units);
        dest.writeStringArray(this.unitSum);
        dest.writeFloatArray(this.selectedValues);
        dest.writeFloatArray(this.minValues);
        dest.writeFloatArray(this.maxValues);
        dest.writeFloatArray(this.perValues);
    }

    protected DetailsModel(Parcel in) {
        this.what = in.readInt();
        this.action = in.readString();
        this.title = in.readString();
        this.unitPosition = in.readInt();
        this.units = in.createStringArray();
        this.unitSum = in.createStringArray();
        this.selectedValues = in.createFloatArray();
        this.minValues = in.createFloatArray();
        this.maxValues = in.createFloatArray();
        this.perValues = in.createFloatArray();
    }

    public static final Creator<DetailsModel> CREATOR = new Creator<DetailsModel>() {
        @Override
        public DetailsModel createFromParcel(Parcel source) {
            return new DetailsModel(source);
        }

        @Override
        public DetailsModel[] newArray(int size) {
            return new DetailsModel[size];
        }
    };
}