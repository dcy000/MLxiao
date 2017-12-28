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
            JSONObject dataDataObj = dataObj.optJSONObject("data");
            if (dataDataObj == null) {
                return results;
            }
            String service = dataObj.optString("service");
            if (TextUtils.isEmpty(service)) {
                return results;
            }
            results.put("service", service);
            JSONArray resultArray = dataDataObj.optJSONArray("result");
            if (resultArray == null) {
                return results;
            }
            JSONObject resultObj = resultArray.getJSONObject(0);
            if (resultObj == null) {
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
                    results.put("text", results.get("text") + content);
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
            return results;
        } catch (JSONException e) {
            e.printStackTrace();
            return results;
        }
    }
}
