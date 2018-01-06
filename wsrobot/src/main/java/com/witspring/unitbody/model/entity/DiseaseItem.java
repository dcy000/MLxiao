package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 疾病列表的vo
 * Created by chenqiang on 16/3/8.
 */
public class DiseaseItem implements Serializable {

    private static final long serialVersionUID = -4384570549724273119L;

    private String name;
    private int id;
    private float possible;

    public DiseaseItem(String name, int id, float possible) {
        this.name = name;
        this.id = id;
        this.possible = possible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPossible() {
        return possible;
    }

    public void setPossible(float possible) {
        this.possible = possible;
    }
}
