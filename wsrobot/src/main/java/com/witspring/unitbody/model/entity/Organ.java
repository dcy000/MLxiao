package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;
import com.witspring.util.CommUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 器官
 * @author Created by Goven on 2018/1/3 下午3:06
 * @email gxl3999@gmail.com
 */
public class Organ implements Serializable {

    private static final long serialVersionUID = 1597990713968118992L;
    private int id;
    private String name;
    private String iconUrl;
    private boolean leaf;// 是否子器官

    public static Result<Map<String, Object>> parseOrganAndSymptoms(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<Map<String, Object>> result = new Result<>(jObj);
            Map<String, Object> map = new HashMap<>();
            JSONObject dataObj = jObj.optJSONObject("data");
            JSONArray organsArr = dataObj.optJSONArray("organs");
            List<Organ> organList = new ArrayList<>();
            if (CommUtil.notEmpty(organsArr)) {
                for (int i = 0; i < organsArr.length(); i++) {
                    JSONObject itemObj = organsArr.optJSONObject(i);
                    Organ item = new Organ();
                    item.setId(itemObj.optInt("id"));
                    item.setName(itemObj.optString("name"));
                    item.setIconUrl(itemObj.optString("photo"));
                    int haveLeaf = itemObj.optInt("leaf");
                    item.setLeaf(haveLeaf == 0);
                    organList.add(item);
                }
            }
            map.put("organs", organList);

            JSONArray symptomsArr = dataObj.optJSONArray("slangs");
            List<String[]> symptoms = new ArrayList<>();
            if (CommUtil.notEmpty(symptomsArr)) {
                for (int i = 0; i < symptomsArr.length(); i++) {
                    JSONObject symptomObj = symptomsArr.optJSONObject(i);
                    String[] texts = new String[3];
                    texts[0] = symptomObj.optString("name");
                    texts[1] = symptomObj.optString("slang");
                    texts[2] = symptomObj.optString("desc");
                    symptoms.add(texts);
                }
            }
            map.put("symptoms", symptoms);
            result.setContent(map);
            return result;
        } catch (Exception e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }

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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
