package com.zhang.hui.lib_recreation.tool.other;


import com.gcml.common.BuildConfig;
import com.gcml.common.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhang.hui.lib_recreation.app.RecreationApp;
import com.zhang.hui.lib_recreation.tool.xfparsebean.BaiKeBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.ChineseZodiacBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.CookbookBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.CrossTalkBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.DreamBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HealthBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HistoryBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HistoryTodayBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.HolidayBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.IdiomBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.OpenClassBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.RiddleBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.SpeechBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.StoryTellingBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.TranslationBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.WeatherBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.WebsearchBean;
import com.zhang.hui.lib_recreation.tool.xfparsebean.WordFindingBean;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static JSONObject answer;
    private static String question;
    private static String anwserText;

    /**
     * 成功的回调借口
     */
    public interface getDataListener {
        //有result结果

        /**
         * @param data     result data
         * @param anwser   anwser text   可能没有
         * @param service  service      可能没有
         * @param question 问题
         */
        void onSuccess(Object data, String anwser, String service, String question);
    }

    /**
     * 查询日期
     *
     * @param contentText 输入的内容
     * @return 直接anwser返回
     */
    public static void getSkillData(String contentText, final getDataListener listener) {
        getXFDataJSONObject(contentText, listener);
    }

    public static void getXFDataJSONObject(final String contentText, final getDataListener listener) {
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
                            question = xfContentData.getString("text");
                            if (xfContentData == null) {
                                return;
                            }
                            try {
                                service = xfContentData.getString("service");
                                //直接回答anwser
                                answer = xfContentData.getJSONObject("answer");
                                anwserText = answer.getString("text");
                                if (answer != null) {
                                    if (listener != null) {
                                        if ("datetime".equals(service)
                                                || "calc".equals(service)
                                                || "AIUI.guessNumber".equals(service)
                                                ) {
                                            listener.onSuccess(null, anwserText, service, question);
                                            return;
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                //没有service  anwser的情况
                                listener.onSuccess(null, "", "", question);
                                return;
                            }


                            try {
                                JSONObject dataContent = xfContentData.getJSONObject("data");
                                Gson gson = new Gson();
                                JSONArray result = null;
                                if (data != null) {
                                    result = dataContent.getJSONArray("result");
                                }

                                if ("weather".equals(service)) {
                                    //解析result类 XFdada中的data数据
                                    if (result != null) {
                                        Type type = new TypeToken<List<WeatherBean>>() {
                                        }.getType();
                                        List<WeatherBean> weatherBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(weatherBeans, anwserText, service, question);
                                    }
                                }


                                if ("cookbook".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<CookbookBean>>() {
                                        }.getType();
                                        List<CookbookBean> cookBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(cookBeans, anwserText, service, question);
                                    }
                                }

                                if ("dream".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<DreamBean>>() {
                                        }.getType();
                                        List<DreamBean> dreamBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(dreamBeans, anwserText, service, question);
                                    }
                                }

                                if ("baike".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<BaiKeBean>>() {
                                        }.getType();
                                        List<BaiKeBean> baikeBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(baikeBeans, anwserText, service, question);
                                    }
                                }

                                if ("translation".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<TranslationBean>>() {
                                        }.getType();
                                        List<TranslationBean> translationBeanBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(translationBeanBeans, anwserText, service, question);
                                    }
                                }

                                if ("riddle".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<RiddleBean>>() {
                                        }.getType();
                                        List<RiddleBean> riddleBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(riddleBeans, anwserText, service, question);
                                    }
                                }

                                if ("wordFinding".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<WordFindingBean>>() {
                                        }.getType();
                                        List<WordFindingBean> findingBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(findingBeans, anwserText, service, question);
                                    }
                                }

                                if ("idiom".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<IdiomBean>>() {
                                        }.getType();
                                        List<IdiomBean> idiomBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(idiomBeans, anwserText, service, question);
                                    }
                                }

                                if ("holiday".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<HolidayBean>>() {
                                        }.getType();
                                        List<HolidayBean> holidayBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(holidayBeans, anwserText, service, question);
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

                                            Type typeNumerology = new TypeToken<List<ChineseZodiacBean.NumerologyBean>>() {
                                            }.getType();
                                            chineseZodiacBean.numerology = gson.fromJson(jsonObject.getJSONArray("numerology").toString(), typeNumerology);

                                            chineseZodiacBeans.add(chineseZodiacBean);
                                        }

                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }


                                if ("websearch".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<WebsearchBean>>() {
                                        }.getType();
                                        List<WebsearchBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }

                                if ("LEIQIAO.historyToday".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<HistoryTodayBean>>() {
                                        }.getType();
                                        List<HistoryTodayBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }


                                if ("history".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<HistoryBean>>() {
                                        }.getType();
                                        List<HistoryBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }

                                //相声小品
                                if ("crossTalk".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<CrossTalkBean>>() {
                                        }.getType();
                                        List<CrossTalkBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }

                                //评书
                                if ("storyTelling".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<StoryTellingBean>>() {
                                        }.getType();
                                        List<StoryTellingBean> chineseZodiacBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(chineseZodiacBeans, anwserText, service, question);
                                    }
                                }

                                //名人演讲
                                if ("LEIQIAO.speech".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<SpeechBean>>() {
                                        }.getType();
                                        List<SpeechBean> speechBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(speechBeans, anwserText, service, question);
                                    }
                                }

                                //公开课
                                if ("LEIQIAO.openClass".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<OpenClassBean>>() {
                                        }.getType();
                                        List<OpenClassBean> openClassBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(openClassBeans, anwserText, service, question);
                                    }
                                }


                                //健康知识
                                if ("health".equals(service)) {
                                    if (result != null) {
                                        Type type = new TypeToken<List<HealthBean>>() {
                                        }.getType();
                                        List<HealthBean> healthBeans = gson.fromJson(result.toString(), type);
                                        listener.onSuccess(healthBeans, anwserText, service, question);
                                    }
                                }


                            } catch (JSONException e) {
                                listener.onSuccess(null, "", "", question);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onSuccess(null, "", "", contentText);
                }
            }
        });

    }

    private static Call initPostCall(String contentText) {
        getClient();
        RequestBody body = new FormBody.Builder()
                .add("eqid", Utils.getDeviceId(RecreationApp.application.getContentResolver()))
                .add("text", contentText)
                .build();
        Request request = new Request.Builder()
                .url(BuildConfig.BASE_URL_DEBUG + "/ZZB/xf/xfrq")
                .post(body)
                .build();
        return client.newCall(request);
    }

    public final static int CONNECT_TIMEOUT = 15;
    public final static int READ_TIMEOUT = 15;
    public final static int WRITE_TIMEOUT = 15;

    private static void getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .build();
        }
    }


}
