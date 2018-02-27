package com.example.han.referralproject.speechsynthesis;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.speechsynthesis.xfparsebean.BaiKeBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.ChineseZodiacBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.CookbookBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.DreamBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.HistoryBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.HolidayBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.IdiomBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.RiddleBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.TranslationBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.WeatherBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.WebsearchBean;
import com.example.han.referralproject.speechsynthesis.xfparsebean.WordFindingBean;
import com.example.han.referralproject.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medlink.danbogh.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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
    private static JSONObject xfContentData;
    private static String service;

    /**
     * 成功的回调借口
     */
    public interface getDataListener {
        void onSuccess(Object anwser);
    }

    /**
     * 查询日期
     *
     * @param contentText 输入的内容
     * @param skillType   技能类型
     * @return 直接anwser返回
     */
    public static void getSkillData(String contentText, String skillType, final getDataListener listener) {

        JSONObject xfDataJSONObject = getXFDataJSONObject(contentText);
        if (xfDataJSONObject == null) {
            return;
        }
        try {
            service = xfDataJSONObject.getString("service");
            //直接回答anwser
            JSONObject answer = xfDataJSONObject.getJSONObject("answer");
            if (answer != null) {
                if (listener != null) {
                    if ("datetime".equals(service)
                            || "calc".equals(service)
                            || "AIUI.guessNumber".equals(service)
                            ) {
                        listener.onSuccess(answer.getString("text"));
                        return;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            JSONObject data = xfDataJSONObject.getJSONObject("data");
            Gson gson = new Gson();
            JSONArray result = null;
            if (data != null) {
                result = data.getJSONArray("result");
            }

            if ("weather".equals(service)) {
                //解析result类 XFdada中的data数据
                if (result != null) {
                    Type type = new TypeToken<List<WeatherBean>>() {
                    }.getType();
                    List<WeatherBean> weatherBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(weatherBeans);
                }
            }


            if ("cookbook".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<CookbookBean>>() {
                    }.getType();
                    List<CookbookBean> cookBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(cookBeans);
                }
            }

            if ("dream".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<DreamBean>>() {
                    }.getType();
                    List<DreamBean> dreamBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(dreamBeans);
                }
            }

            if ("baike".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<BaiKeBean>>() {
                    }.getType();
                    List<BaiKeBean> baikeBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(baikeBeans);
                }
            }

            if ("translation".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<TranslationBean>>() {
                    }.getType();
                    List<TranslationBean> translationBeanBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(translationBeanBeans);
                }
            }

            if ("riddle".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<RiddleBean>>() {
                    }.getType();
                    List<RiddleBean> riddleBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(riddleBeans);
                }
            }

            if ("wordFinding".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<WordFindingBean>>() {
                    }.getType();
                    List<WordFindingBean> findingBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(findingBeans);
                }
            }

            if ("idiom".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<IdiomBean>>() {
                    }.getType();
                    List<IdiomBean> idiomBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(idiomBeans);
                }
            }

            if ("holiday".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<HolidayBean>>() {
                    }.getType();
                    List<HolidayBean> holidayBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(holidayBeans);
                }
            }


            if ("chineseZodiac".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<ChineseZodiacBean>>() {
                    }.getType();
                    List<ChineseZodiacBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }


            if ("websearch".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<WebsearchBean>>() {
                    }.getType();
                    List<WebsearchBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }

            if ("LEIQIAO.historyToday".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<HistoryBean>>() {
                    }.getType();
                    List<HistoryBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static JSONObject getXFDataJSONObject(String contentText) {
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
                        JSONObject XFDataObj = new JSONObject(data);
                        String code = XFDataObj.getString("code");
                        if (code.equals("00000")) {
                            xfContentData = XFDataObj.getJSONObject("data");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return xfContentData;
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
