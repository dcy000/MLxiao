package com.witspring.unitdisease.model;


import com.witspring.model.entity.Disease;
import com.witspring.model.entity.Medicine;
import com.witspring.model.entity.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseParse {

    public static Result<Disease> parseDisease(String json) {
        Disease disease = new Disease();
        try {
            JSONObject jObj = new JSONObject(json);
            Result<Disease> result = new Result<>(jObj);
            JSONObject jsonObj = jObj.optJSONObject("data");
            disease.setId(jsonObj.optInt("diseaseId"));
            disease.setName(jsonObj.optString("diseaseName"));
            disease.setTherapy(jsonObj.optString("therapy"));
            disease.setXgzd(jsonObj.optString("xgzd"));
            disease.setInfo(jsonObj.optString("introduction"));
            disease.setDiseasePhotos(Disease.parseDiseaseImgs(jsonObj.optString("diseasePhotos")));
            result.setStatus(Result.STATUS_SUCCESS);
            result.setContent(disease);
            return result;
        } catch (JSONException e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }

    public static Result<ArrayList<Medicine>> parseMedicines(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<ArrayList<Medicine>> result = new Result<>(jObj);
            JSONArray array = jObj.optJSONObject("data").optJSONArray("drugs");
            result.setContent(Medicine.buildMedicines(array));
            return result;
        } catch (JSONException e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }

    public static Result parseMedicineDetail(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<Medicine> result = new Result<>(jObj);
            result.setContent(Medicine.buildMedicine(jObj.optJSONObject("data")));
            return result;
        } catch (JSONException e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }
}