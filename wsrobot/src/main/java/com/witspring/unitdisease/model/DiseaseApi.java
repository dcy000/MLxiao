package com.witspring.unitdisease.model;

import com.witspring.mlrobot.BuildConfig;
import com.witspring.model.ApiCallback;
import com.witspring.model.BaseApi;
import com.witspring.model.entity.Disease;
import com.witspring.model.entity.Medicine;
import com.witspring.model.entity.Result;
import com.witspring.model.entity.UserInfo;
import com.witspring.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by wu_zf on 1/2/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseApi extends BaseApi {

    public static final String URL_DISEASE_INFO_BY_NAME = BuildConfig.URL_SERVICE + "service/infirmary/diseaseWikiByNameRetrieve.do";
    public static final String PATH_DISEASE_DRUGS = BuildConfig.URL_SERVICE + "service/infirmary/diseaseDrugRetrieve.do";
    public static final String PATH_DISEASE_DRUG_DETAIL = BuildConfig.URL_SERVICE + "service/infirmary/drugDetailByIdRetrive.do";

    public void getDiseaseInfoByName(String diseaseName, String word, int sex, int ageMonth, ApiCallback<Disease> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", "DISEASE-WIKI-BY-NAME-RETRIEVE");
        params.put("disease_name", diseaseName);
//        if (StringUtil.isNotTrimBlank(word)) {
//            params.put("symptom_word", word);
//        }
//        params.put("gender", sex);
//        if(ageMonth == -1){
//            params.put("age", UserInfo.AGE_NULL);
//        }else if(ageMonth == 0){
//            params.put("age", "0.0");
//        }else{
//            params.put("age", ageMonth);
//        }
        postWithUrl(URL_DISEASE_INFO_BY_NAME, params, callback);
    }

    public void getMedicines(int diseaseId, String keyword, int sex, int ageMonth, ApiCallback<ArrayList<Medicine>> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", "DISEASE-DRUG-RETRIEVE");
        params.put("disease_id", diseaseId);
        if (StringUtil.isNotBlank(keyword)) {
            params.put("symptom_word", keyword);
        }
        params.put("gender", sex);
        if (ageMonth == -1) {
            params.put("age", UserInfo.AGE_NULL);
        } else if (ageMonth == 0) {
            params.put("age", "0.0");
        } else {
            params.put("age", ageMonth);
        }
        postWithUrl(PATH_DISEASE_DRUGS, params, callback);
    }

    public void getDrugDetilById(long id, int sex, int ageMonth, ApiCallback<Medicine> callback) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("action", "DRUG-DETAIL-RETRIVE-ID");
        params.put("drug_id", id);
        postWithUrl(PATH_DISEASE_DRUG_DETAIL, params, callback);
    }

    @Override
    protected Result parseJson(String url, String json) {
        if (url.equals(URL_DISEASE_INFO_BY_NAME)) {
            return DiseaseParse.parseDisease(json);
        }else if (url.equals(PATH_DISEASE_DRUGS)) {
            return DiseaseParse.parseMedicines(json);
        } else if (url.equals(PATH_DISEASE_DRUG_DETAIL)) {
            return DiseaseParse.parseMedicineDetail(json);
        }
        return null;
    }

}
