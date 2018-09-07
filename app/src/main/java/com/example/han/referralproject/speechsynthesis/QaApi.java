package com.example.han.referralproject.speechsynthesis;

import android.text.TextUtils;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by afirz on 2017/12/18.
 */

public class QaApi {
    public static OkHttpClient client;

    public static HashMap<String, String> getQaFromXf(String text) {
        if (client == null) {
            client = new OkHttpClient();
        }
        RequestBody body = new FormBody.Builder()
                .add("eqid", Utils.getDeviceId())
                .add("text", text)
                .build();
        Request request = new Request.Builder()
                .url(NetworkApi.BasicUrl + "/ZZB/xf/xfrq")
                .post(body)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                return parseXffunQaAudioResponse(responseBody.string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static HashMap<String, String> parseXffunQaAudioResponse(String text) {
        HashMap<String, String> results = new HashMap<>();
        results.put("text", "");
        results.put("audiopath", "");
        results.put("q", "");
        results.put("service", "");
        results.put("dreamUrl", "");
        results.put("resultJson", "");
        try {
            JSONObject apiResponseObj = new JSONObject(text);
            text = apiResponseObj.optString("data");
            JSONObject qaResponseObj = new JSONObject(text);
            String code = qaResponseObj.optString("code");
            if (text.equals("1")) {
                return results;
            }
            if (code == null || !code.equals("00000")) {
                return results;
            }
            JSONObject dataObj = qaResponseObj.optJSONObject("data");
            if (dataObj == null) {
                return results;
            }
            String q = dataObj.optString("text");
            if (!TextUtils.isEmpty(q)) {
                results.put("q", q);
            }
            JSONObject answerObj = dataObj.optJSONObject("answer");
            if (answerObj != null) {
                String answer = answerObj.optString("text");
                if (!TextUtils.isEmpty(answer)) {
                    results.put("text", answer);
                }
            }
            String service = dataObj.optString("service");
            if (TextUtils.isEmpty(service)) {
                return results;
            }
            results.put("service", service);
            JSONObject dataDataObj = dataObj.optJSONObject("data");
            if (dataDataObj == null) {
                return results;
            }
            JSONArray resultArray = dataDataObj.optJSONArray("result");
            if (resultArray == null) {
                return results;
            }
            String resultJson = resultArray.toString();
            results.put("resultJson", resultJson);

            JSONObject resultObj = resultArray.getJSONObject(0);
            if (resultObj == null) {
                return results;
            }

            if (service.equals("radio")) {
                String playUrl = resultObj.optString("url");
                if (!TextUtils.isEmpty(playUrl)) {
                    results.put("audiopath", playUrl);
                }
                return results;
            }

            if (service.equals("story")) {
                String playUrl = resultObj.optString("playUrl");
                if (!TextUtils.isEmpty(playUrl)) {
                    results.put("audiopath", playUrl);
                }
                return results;
            }

            if (service.equals("musicX")) {
                //音乐(MP3)
                String audiopath = resultObj.optString("audiopath");
                if (!TextUtils.isEmpty(audiopath)) {
                    results.put("audiopath", audiopath);
                }
                return results;
            }
            if (service.equals("joke")) {
                //笑话
                String content = resultObj.optString("content");
                if (!TextUtils.isEmpty(content)) {
                    results.put("text", results.get("text"));
                }
                return results;
            }
            if (service.equals("cookbook")) {
                //菜谱
                String steps = resultObj.optString("steps");
                if (!TextUtils.isEmpty(steps)) {
                    results.put("text", results.get("text") + steps);
                }
                return results;
            }
            if (service.equals("news")) {
                //新闻(MP3)
                String title = resultObj.optString("title");
                if (!TextUtils.isEmpty(title)) {
                    results.put("text", results.get("text") + "\n\n\n\n" + title);
                }
                String url = resultObj.optString("url");
                if (!TextUtils.isEmpty(url)) {
                    results.put("audiopath", url);
                }
                return results;
            }

            //评书,历史上的今天,搞笑段子,相声小品,公开课,名人演讲,戏曲
            if (service.equals("storyTelling")
                    || service.equals("history")
                    || service.equals("LEIQIAO.funnyPassage")
                    || service.equals("crossTalk")
                    || service.equals("LEIQIAO.openClass")
                    || service.equals("LEIQIAO.speech")
                    || service.equals("drama")
                    ) {
                String url = resultObj.getString("url");
                if (!TextUtils.isEmpty(url)) {
                    results.put("audiopath", url);
                }
                return results;
            }


            if (service.equals("weather")) {
                //天气
                JSONObject exp = resultObj.getJSONObject("exp");
                if (exp == null) {
                    return results;
                }
                JSONObject ct = exp.getJSONObject("ct");
                if (ct == null) {
                    return results;
                }
                results.put("text", results.get("text") + "," + ct.getString("prompt"));

                //明天天气
                JSONObject tomorrow = resultArray.getJSONObject(1);
                if (tomorrow == null) {
                    return results;
                }

                results.put("text", results.get("text") + ","
                        + "明天" + tomorrow.getString("weather") + ","
                        + tomorrow.getString("tempRange") + ","
                        + tomorrow.getString("wind")
                );
            }

            if (service.equals("dream")) {
                //解梦的网页url
                String url = resultObj.getString("url");
                results.put("dreamUrl", url);
            }


            return results;
        } catch (JSONException e) {
            e.printStackTrace();
            return results;
        }
    }
}
