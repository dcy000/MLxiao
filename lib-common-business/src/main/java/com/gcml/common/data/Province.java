package com.gcml.common.data;

import com.contrarywind.interfaces.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovo on 2017/11/2.
 */

public class Province  implements IPickerViewData {

    @SerializedName("name")
    private String name;

    @SerializedName("cities")
    private List<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
