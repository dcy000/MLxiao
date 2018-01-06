package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;
import com.witspring.util.CommUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 通用解析类
 * @author Created by Goven on 2018/1/2 下午5:33
 * @email gxl3999@gmail.com
 */
public class CommParse {

    public static Result<Integer[]> bodyPartIds(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<Integer[]> result = new Result<>(jObj);
            JSONObject dataObj = jObj.optJSONObject("data");
            if (CommUtil.notEmpty(dataObj)) {
                JSONArray jsonArray = dataObj.optJSONArray("list");
                if (CommUtil.notEmpty(jsonArray)) {
                    Integer[] ids = new Integer[14];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        int position = jsonObject.optInt("position");
                        int id = jsonObject.optInt("id");
                        if (position == 4) {
                            ids[6] = ids[12] = id;
                        } else if (position == 5) {
                            ids[9] = id;
                        } else if (position == 6) {
                            ids[3] = id;
                        } else if (position == 7) {
                            ids[4] = id;
                        } else if (position == 8) {
                            ids[10] = id;
                        } else if (position == 9) {
                            ids[5] = ids[11] = id;
                        } else if (position == 10) {
                            ids[13] = id;
                        } else if (position == 11) {
                            ids[7] = id;
                        } else if (position == 12) {
                            ids[8] = id;
                        } else if (position >= 1 && position <= 12) {
                            ids[position - 1] = id;
                        }
                    }
                    result.setContent(ids);
                }
            }
            return result;
        } catch (Exception e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }

    public static Result<String[]> commSymptoms(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<String[]> result = new Result<>(jObj);
            JSONArray jsonArr = jObj.optJSONObject("data").optJSONArray("list");
            if (CommUtil.notEmpty(jsonArr)) {
                String[] symptoms = new String[jsonArr.length()];
                for (int i = 0; i < jsonArr.length(); i++) {
                    symptoms[i] = jsonArr.optString(i);
                }
                result.setContent(symptoms);
            }
            return result;
        } catch (Exception e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }

}
