package com.example.han.referralproject.tool;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.tool.xfparsebean.BaiKeBean;
import com.example.han.referralproject.tool.xfparsebean.ChineseZodiacBean;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;
import com.example.han.referralproject.tool.xfparsebean.CrossTalkBean;
import com.example.han.referralproject.tool.xfparsebean.DreamBean;
import com.example.han.referralproject.tool.xfparsebean.HealthBean;
import com.example.han.referralproject.tool.xfparsebean.HistoryBean;
import com.example.han.referralproject.tool.xfparsebean.HistoryTodayBean;
import com.example.han.referralproject.tool.xfparsebean.HolidayBean;
import com.example.han.referralproject.tool.xfparsebean.IdiomBean;
import com.example.han.referralproject.tool.xfparsebean.OpenClassBean;
import com.example.han.referralproject.tool.xfparsebean.RiddleBean;
import com.example.han.referralproject.tool.xfparsebean.SpeechBean;
import com.example.han.referralproject.tool.xfparsebean.StoryTellingBean;
import com.example.han.referralproject.tool.xfparsebean.TranslationBean;
import com.example.han.referralproject.tool.xfparsebean.WeatherBean;
import com.example.han.referralproject.tool.xfparsebean.WebsearchBean;
import com.example.han.referralproject.tool.xfparsebean.WordFindingBean;
import com.example.han.referralproject.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
     * @return 直接anwser返回
     */
    public static void getSkillData(String contentText, final getDataListener listener) {

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
//                    Type type = new TypeToken<List<ChineseZodiacBean>>() {
//                    }.getType();
//                    List<ChineseZodiacBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    List<ChineseZodiacBean> chineseZodiacBeans = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        ChineseZodiacBean chineseZodiacBean = new ChineseZodiacBean();
                        JSONObject jsonObject = result.getJSONObject(i);
                        chineseZodiacBean.causes = jsonObject.getString("causes");
                        chineseZodiacBean.summary = jsonObject.getString("summary");
                        chineseZodiacBean.source = jsonObject.getString("source");
                        chineseZodiacBean.name = jsonObject.getString("name");
                        chineseZodiacBean.year = jsonObject.getString("year");
                        chineseZodiacBean.url = jsonObject.getString("url");

                        Type typeDetail = new TypeToken<List<ChineseZodiacBean.DetailBean>>() {
                        }.getType();
                        chineseZodiacBean.detail = gson.fromJson(jsonObject.getJSONArray("detail").toString(), typeDetail);

                        Type typeFortune = new TypeToken<List<ChineseZodiacBean.FortuneBean>>() {
                        }.getType();
                        chineseZodiacBean.fortune = gson.fromJson(jsonObject.getJSONArray("fortune").toString(), typeFortune);

                        Type typeNumerology= new TypeToken<List<ChineseZodiacBean.NumerologyBean>>() {
                        }.getType();
                        chineseZodiacBean.numerology = gson.fromJson(jsonObject.getJSONArray("numerology").toString(), typeNumerology);

                        chineseZodiacBeans.add(chineseZodiacBean);
                    }

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
                    Type type = new TypeToken<List<HistoryTodayBean>>() {
                    }.getType();
                    List<HistoryTodayBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }


            if ("history".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<HistoryBean>>() {
                    }.getType();
                    List<HistoryBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }

            //相声小品
            if ("crossTalk".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<CrossTalkBean>>() {
                    }.getType();
                    List<CrossTalkBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }

            //评书
            if ("storyTelling".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<StoryTellingBean>>() {
                    }.getType();
                    List<StoryTellingBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(chineseZodiacBeans);
                }
            }

            //名人演讲
            if ("LEIQIAO.speech".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<SpeechBean>>() {
                    }.getType();
                    List<SpeechBean> speechBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(speechBeans);
                }
            }

            //公开课
            if ("LEIQIAO.openClass".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<OpenClassBean>>() {
                    }.getType();
                    List<OpenClassBean> openClassBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(openClassBeans);
                }
            }


            //健康知识
            if ("health".equals(service)) {
                if (result != null) {
                    Type type = new TypeToken<List<HealthBean>>() {
                    }.getType();
                    List<HealthBean> healthBeans = gson.fromJson(result.toString(), type);
                    listener.onSuccess(healthBeans);
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
