package com.witspring.unitbody.model;

import com.witspring.mlrobot.BuildConfig;
import com.witspring.model.ApiCallback;
import com.witspring.model.BaseApi;
import com.witspring.model.entity.Result;
import com.witspring.unitbody.model.entity.CommParse;
import com.witspring.unitbody.model.entity.Organ;

import java.util.Map;

/**
 * @author Created by Goven on 2018/1/2 下午5:20
 * @email gxl3999@gmail.com
 */
public class BodyApi extends BaseApi {

    public static final String URL_BODY_TOP_ORGANS = BuildConfig.URL_SERVICE + "service/infirmary/humanOrganSearch.do";
    public static final String URL_COMMON_SYMPTOMS = BuildConfig.URL_SERVICE + "service/infirmary/commonSymptom.do";
    public static final String URL_ORGANS_AND_SYMPTOMS = BuildConfig.URL_SERVICE + "service/infirmary/organAndSymptomRetrive.do";

    public void topLevelOrgans(int sex, ApiCallback callback) {
        Map<String, Object> params = obtainParams();
        params.put("action", "HUMANORGAN-SEARCH");
        params.put("gender", sex);
        postWithUrl(URL_BODY_TOP_ORGANS, params, callback);
    }

    public void organsAndSymptoms(int sex, int ageMonths, int partId, String partName, ApiCallback callback) {
        Map<String,Object> params = obtainParams();
        params.put("action", "ORGANANDSYMPTOM-RETRIVE");
        params.put("gender", sex);
        params.put("id", partId);
        params.put("age", ageMonths);
        params.put("organ_name", partName);
        postWithUrl(URL_ORGANS_AND_SYMPTOMS, params, callback);
    }

    public void commonSymptoms(int sex, int ageMonths, ApiCallback callback) {
        Map<String,Object> params = obtainParams();
        params.put("action","COMMON_SYMPTOM");
        params.put("gender", sex);
        params.put("age",ageMonths == 0 ? "0.0" : ageMonths);
        postWithUrl(URL_COMMON_SYMPTOMS, params, callback);
    }

    @Override
    protected Result parseJson(String url, String json) {
        if (url == URL_BODY_TOP_ORGANS) {
            return CommParse.bodyPartIds(json);
        } else if (url == URL_COMMON_SYMPTOMS) {
            return CommParse.commSymptoms(json);
        } else if (url == URL_ORGANS_AND_SYMPTOMS) {
            return Organ.parseOrganAndSymptoms(json);
        }
        return null;
    }

}
