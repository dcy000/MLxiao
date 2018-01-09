package com.witspring.unitbody.model.entity;

import com.witspring.model.entity.Result;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 症状
 * Created by chenqiang on 16/3/8.
 */
public class Inquiry implements Serializable {
    private static final long serialVersionUID = -8725593123527852245L;

    //接口数据动态多变，直接返回原始数据
    public static Result<String> parseInquiry(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Result<String> result = new Result<>();
//            int status = jObj.optInt("status");

            result.setContent(json);
        return result;
        } catch (Exception e) {
            return new Result<>(Result.STATUS_DATA_ERROR);
        }
    }
}
