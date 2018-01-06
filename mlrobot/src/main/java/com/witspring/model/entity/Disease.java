package com.witspring.model.entity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public class Disease implements Serializable {

    private static final long serialVersionUID = -3203180616376321539L;
    private int id;// 疾病ID
    private String name;// 疾病名称
    private String info;
    private String therapy;//疾病治疗
    private String xgzd;//相关诊断
    private List<Medicine> relateMedicines;// 相关药物
    //新加的疾病展示内容跟疾病常识有关
    private List<DiseaseImg> diseasePhotos;//疾病图片

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTherapy() {
        return therapy;
    }

    public void setTherapy(String therapy) {
        this.therapy = therapy;
    }

    public String getXgzd() {
        return xgzd;
    }

    public void setXgzd(String xgzd) {
        this.xgzd = xgzd;
    }

    public List<Medicine> getRelateMedicines() {
        return relateMedicines;
    }

    public void setRelateMedicines(List<Medicine> relateMedicines) {
        this.relateMedicines = relateMedicines;
    }

    public List<DiseaseImg> getDiseasePhotos() {
        return diseasePhotos;
    }

    public void setDiseasePhotos(List<DiseaseImg> diseasePhotos) {
        this.diseasePhotos = diseasePhotos;
    }

    public static List<DiseaseImg> parseDiseaseImgs(String json){
        List<DiseaseImg> diseaseImgs = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                DiseaseImg img = new DiseaseImg();
                img.setOrderIndex(jsonArray.getJSONObject(i).optInt("order_index"));
                img.setPhoto(jsonArray.getJSONObject(i).optString("photo"));
                img.setThumbPhoto(jsonArray.getJSONObject(i).optString("thumb_photo"));
                diseaseImgs.add(img);
            }
            return diseaseImgs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

