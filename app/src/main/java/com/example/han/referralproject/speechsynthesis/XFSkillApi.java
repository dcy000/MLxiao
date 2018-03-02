package com.example.han.referralproject.speechsynthesis;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/2/27.
 */

public class XFSkillApi {
    public static OkHttpClient client;

    /**
     * 成功的回调借口
     */
    private interface getDataListener {
        void onSuccess(String anwser);
    }

    /**
     * 查询日期
     *
     * @param contentText 输入的内容
     * @param skillType   技能类型
     * @return 直接anwser返回
     */
    public static void getStringData(String contentText, String skillType, final getDataListener listener) {
        String anwser = "";
        Call call = initPostCall(contentText);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    try {
                        JSONObject object = new JSONObject(string);
                        String data = object.getString("data");
                        JSONObject XFDataObj = new JSONObject(string);
                        //讯飞成功返回
                        if (XFDataObj.get("code").equals("00000")) {
                            JSONObject XFContentData = XFDataObj.getJSONObject("data");
                            JSONObject answer = XFContentData.getJSONObject("answer");
                            if (answer != null) {
                                if (listener != null) {
                                    listener.onSuccess(answer.getString("text"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static Call initPostCall(String contentText) {
        getClient();
        RequestBody body = new FormBody.Builder()
                .add("eqid", Utils.getDeviceId())
                .add("text", contentText)
                .build();
        Request request = new Request.Builder()
                .url(NetworkApi.BasicUrl + "/ZZB/xf/xfrq")
                .post(body)
                .build();
        return client.newCall(request);
    }

    private static void getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
    }


}
