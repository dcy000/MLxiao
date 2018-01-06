package com.witspring.unitbody.model;

import com.witspring.model.ApiCallback;
import com.witspring.model.BaseApi;
import com.witspring.model.entity.Result;
import com.witspring.unitbody.model.entity.Inquiry;
import com.witspring.util.JsonUtil;

import java.util.Map;

/**
 * @author Created by Goven on 2018/1/2 下午5:20
 * @email gxl3999@gmail.com
 */
public class InquiryApi extends BaseApi {

    private String talkServerUrl = "http://app.witspring.net:55008/question_answer/";//王震的机器人接口地址

    public static final String Tag_chat_Init = "Tag_chat_Init";//获取初始化session信息
    public static final String Tag_chat_Next_Step = "Tag_chat_Next_Step";//获取下一步回话
    public static final String Tag_chat_Result_Back_Now = "Tag_chat_Result_Back_Now";//提前结束问诊，获取疾病结果

    /**
     * 获取对话式交互的初始化数据
     * @param status
     * @param sex
     * @param ageMonth
     * @param phrase
     * @return
     */
    public void chatInit(String status, int sex, int ageMonth, String phrase, ApiCallback callback) {
        Map<String, Object> params = obtainParams();
        params.put("status", status);
        params.put("sex", sex + "");
        params.put("age", ageMonth + "");
        params.put("phrase", phrase);
        String jsonData = JsonUtil.toJson(params);
        System.out.println(jsonData);
        String url = talkServerUrl+jsonData;
        getWithUrl(url,Tag_chat_Init, params, callback);
    }

    /**
     * 对话初始化后的下一步业务性的对话请求
     * @param status
     * @param sessionId
     * @param location
     * @param answer 是有;隔开的字符串形式的数组如：咳嗽;发烧,但是要做一个/的特殊字符替换
     * @return
     */
    public void chatNextStep(String status,String sessionId,String location,String answer, ApiCallback callback) {
        Map<String, Object> params = obtainParams();
        params.put("status", status);
        params.put("session_id", sessionId);
        params.put("location", location);
        params.put("answer", answer.replaceAll("/","**"));
        String jsonData = JsonUtil.toJson(params);
        System.out.println(jsonData);
        String url = talkServerUrl+jsonData;
        getWithUrl(url,Tag_chat_Next_Step, params, callback);
    }

    /**
     * 提前结束问诊获取诊断结果
     * @param status
     * @param sessionId
     * @return
     */
    public void chatResultBackNow(String status,String sessionId, ApiCallback callback) {
        Map<String, Object> params = obtainParams();
        params.put("status", status);
        params.put("session_id", sessionId);
        String jsonData = JsonUtil.toJson(params);
        System.out.println(jsonData);
        String url = talkServerUrl+jsonData;
        getWithUrl(url,Tag_chat_Result_Back_Now, params, callback);
    }

    @Override
    protected Result parseJson(String url, String json) {
        return null;
    }

    @Override
    protected Result parseJson(String url, String tag, String json) {
        if (tag == Tag_chat_Init) {
            return Inquiry.parseInquiry(json);
        } else if (tag == Tag_chat_Next_Step) {
            return Inquiry.parseInquiry(json);
        } else if (tag == Tag_chat_Result_Back_Now) {
            return Inquiry.parseInquiry(json);
        }
        return null;
    }

}
