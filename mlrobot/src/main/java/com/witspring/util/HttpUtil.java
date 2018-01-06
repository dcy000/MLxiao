package com.witspring.util;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.witspring.mlrobot.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Http请求
 * @author Created by Goven on 16/1/5 下午2:14
 * @email gxl3999@gmail.com
 */
public class HttpUtil {

    public static int requestId;

    public static void init(Application app) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 打印日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("Goven");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        // 全局的读取、写入、连接超时时间
        builder.readTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);

        // 配置Cookie
        //使用sp保持cookie，如果cookie不过期，则一直有效
        // builder.cookieJar(new CookieJarImpl(new SPCookieStore(app)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        // builder.cookieJar(new CookieJarImpl(new DBCookieStore(app)));
        //使用内存保持cookie，app退出后，cookie消失
        // builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        // Https配置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();

        // 初始化
        OkGo.getInstance().init(app)                            //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    /**
     * Post请求，默认10秒超时
     * @param url 请求地址
     * @param params 请求参数
     * @param callback 回调
     */
    public static int doPost(String url, Map<String, Object> params, Callback callback) {
        return doPost(url, params, 0, callback);
    }

    public static int doPost(String url, Map<String, Object> params, int timeout, Callback callback) {
        return doPost(++requestId, url, params, timeout, callback);
    }

    /**
     * Post请求
     * @param requestId 请求ID
     * @param url 请求地址
     * @param params 请求参数
     * @param callback 回调
     */
    private static <T> int doPost(int requestId, String url, Map<String, Object> params, int timeout, Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        createSign(params);
        PostRequest<T> request = OkGo.post(url);
        if (timeout > 0) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.retryOnConnectionFailure(false);
            request.client(builder.build());
        }
        request.tag(requestId);
        request.retryCount(0);
        if (CommUtil.notEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                request.params(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        request.execute(callback);
        return  requestId;
    }

    public static int doGet(String urlPre, Map<String, Object> params, Callback callback) {
        return doGet(++requestId, urlPre, params, callback);
    }

    private static <T> int doGet(int requestId, String url, Map<String, Object> params, Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        GetRequest<T> request = OkGo.get(url);
        request.tag(requestId);
        request.retryCount(0);
        if (CommUtil.notEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                request.params(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        request.execute(callback);

        return  requestId;
    }

    private final static OkHttpClient client = new OkHttpClient();
    public static void get(String url, okhttp3.Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static void createSign(Map<String, Object> params) {
        params.put("app_key", "customer_107");
        params.put("app_secret", "340b3a2b-fd1f-443e-8b45-8aba4f1bf7bf");
//        params.put("app_key", "483OedYnY945yTfdUd5Rxruf");
//        params.put("app_secret", "1rogPFfwMpa3U5cgrjsns99wy2QSx909");
        params.put("timestamp", System.currentTimeMillis());
        params.put("uuid", "mailian_robot");
        params.put("version", BuildConfig.VERSION_NAME);
        params.put("platform", 2);

        // 获取请求参数的MD5值
        List<String> sortedKeys = new ArrayList<>(params.keySet());
        Collections.sort(sortedKeys);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            Object value = params.get(key);
            String valueStr = String.valueOf(value);
            if (i > 0) {
                builder.append("&");
            }
            builder.append(key).append("=").append(valueStr);
        }
        String md5Value = StringUtil.encodeByMD5(builder.toString());
        params.remove("app_secret");
        params.put("sign", md5Value);
    }


}
