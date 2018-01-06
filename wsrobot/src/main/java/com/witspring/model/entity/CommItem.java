package com.witspring.model.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用实体类
 * @author Created by Goven on 14-10-20 下午7:26
 * @email gxl3999@gmail.com
 */
public class CommItem implements Serializable {

    private static final long serialVersionUID = -6007646868684978445L;

    private int id;// 主键
    private String code;// 编码
    private String name;// 名称
    private int count;// 数量
    private int type;// 类型
    private boolean opened;// 是否开启
    private String iconUrl;// 图标地址
    private String time;// 时间
    private String memo;// 备注
    private String content;// 内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static List<CommItem> buildOrganAndSymptoms(String json, List<String[]> symptoms) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray organsArr = jsonObj.optJSONArray("organs");
            List<CommItem> organList = null;
            if (organsArr != null && organsArr.length() > 0) {
                organList = new ArrayList<>();
                for (int i = 0; i < organsArr.length(); i++) {
                    JSONObject itemObj = organsArr.optJSONObject(i);
                    CommItem item = new CommItem();
                    item.setId(itemObj.optInt("id"));
                    item.setName(itemObj.optString("name"));
                    item.setIconUrl(itemObj.optString("photo"));
                    int haveLeaf = itemObj.optInt("leaf");
                    item.setOpened(haveLeaf == 0);
                    organList.add(item);
                }
            }
            JSONArray symptomsArr = jsonObj.optJSONArray("slangs");
            if (symptomsArr != null && symptomsArr.length() > 0) {
                for (int i = 0; i < symptomsArr.length(); i++) {
                    JSONObject symptomObj = symptomsArr.optJSONObject(i);
                    String[] texts = new String[3];
                    texts[0] = symptomObj.optString("name");
                    texts[1] = symptomObj.optString("slang");
                    texts[2] = symptomObj.optString("desc");
                    symptoms.add(texts);
                }
            }
            return organList;
        } catch (Exception e) {
            return null;
        }
    }

}
