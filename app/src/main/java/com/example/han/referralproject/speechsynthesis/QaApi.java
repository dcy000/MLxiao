package com.example.han.referralproject.speechsynthesis;

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
            if (response.isSuccessful()) {
                return parseXffunQaAudioResponse(response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static HashMap<String, String> parseXffunQaAudioResponse(String text) {
        HashMap<String, String> results = new HashMap<>();
        try {
            JSONObject apiResponseObj = new JSONObject(text);
            text = apiResponseObj.optString("data");
            JSONObject qaResponseObj = new JSONObject(text);
            String code = qaResponseObj.optString("code");
            if (text.equals("1")) {
                results.put("text", "");
                results.put("audiopath", "");
                return results;
            }
            if (code == null || !code.equals("00000")) {
                results.put("text", "");
                results.put("audiopath", "");
                return results;
            }
            JSONObject dataObj = qaResponseObj.optJSONObject("data");
            if (dataObj == null) {
                results.put("text", "");
                results.put("audiopath", "");
                return results;
            }
            JSONObject answerObj = dataObj.optJSONObject("answer");
            if (answerObj == null) {
                results.put("text", "");
            } else {
                String answer = answerObj.optString("text");
                results.put("text", answer == null ? "" : answer);
            }
            JSONObject dataDataObj = dataObj.optJSONObject("data");
            if (dataDataObj == null) {
                return results;
            }
            JSONArray resultArray = dataDataObj.optJSONArray("result");
            if (resultArray == null) {
                results.put("audiopath", "");
                return results;
            }
            JSONObject resultObj = resultArray.getJSONObject(0);
            if (resultObj == null) {
                results.put("audiopath", "");
                return results;
            }
            String audiopath = resultObj.optString("audiopath");
            if (audiopath == null || audiopath.isEmpty()) {
                results.put("audiopath", "");
                return results;
            }
            results.put("audiopath", audiopath);
            return results;
        } catch (JSONException e) {
            e.printStackTrace();
            results.put("text", "");
            results.put("audiopath", "");
            return results;
        }
    }
}
