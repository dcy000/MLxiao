package com.witspring.model;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.witspring.model.entity.Result;
import com.witspring.util.CommUtil;
import com.witspring.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * API请求基类
 * @author Created by Goven on 2017/8/7 下午2:07
 * @email gxl3999@gmail.com
 */
public abstract class BaseApi {

    private Map<String, Object> params;

    protected abstract Result parseJson(String url, String json);

    protected Result parseJson(String url, String tag, String json) {
        return null;
    }

    protected Map<String, Object> obtainParams() {
        if (params == null) {
            params = new HashMap<>();
        } else {
            params.clear();
        }
        return params;
    }

    protected int getWithUrl(String url,String tag, Map<String, Object> params, ApiCallback listener) {
        return getWithUrl(url, tag,params, 0, listener);
    }

    protected int getWithUrl(String url,String tag ,Map<String, Object> params, int timeout, ApiCallback listener) {
        if (CommUtil.isNetworkOk()) {
            HttpUtil.get(url, new OKCallback(url,tag,listener));
            return 0;
        } else {
            listener.onResult(new Result(Result.STATUS_NETWORK_ERROR));
            return -1;
        }
    }

    protected int postWithUrl(String url, Map<String, Object> params, ApiCallback listener) {
        return postWithUrl(url, params, 0, listener);
    }

    protected int postWithUrl(String url, Map<String, Object> params, int timeout, ApiCallback listener) {
        if (CommUtil.isNetworkOk()) {
            return HttpUtil.doPost(url, params, timeout, new MyStringCallback(url, listener));
        } else {
            listener.onResult(new Result(Result.STATUS_NETWORK_ERROR));
            return -1;
        }
    }

    private class MyStringCallback extends StringCallback {

        private String url;
        private ApiCallback listener;

        public MyStringCallback(String url, ApiCallback listener) {
            this.url = url;
            this.listener = listener;
        }

        @Override
        public void onSuccess(Response<String> response) {
            if (listener != null) {
                Result result = parseJson(url, response.body());
                if (result != null) {
                    listener.onResult(result);
                }
            }
        }

        @Override
        public void onError(Response<String> response) {
            if (listener != null) {
                listener.onResult(new Result(Result.STATUS_FAIL));
            }
        }

    }

    private class CQStringCallback extends StringCallback {

        private String url;
        private String tag;
        private ApiCallback listener;

        public CQStringCallback(String url,String tag, ApiCallback listener) {
            this.url = url;
            this.tag = tag;
            this.listener = listener;
        }

        @Override
        public void onSuccess(Response<String> response) {
            if (listener != null) {
                Result result = parseJson(url,tag, response.body());
                if (result != null) {
                    listener.onResult(result);
                }
            }
        }

        @Override
        public void onError(Response<String> response) {
            if (listener != null) {
                listener.onResult(new Result(Result.STATUS_FAIL));
            }
        }

    }

    private class OKCallback implements okhttp3.Callback {

        private String url;
        private String tag;
        private ApiCallback listener;

        public OKCallback(String url,String tag, ApiCallback listener) {
            this.url = url;
            this.tag = tag;
            this.listener = listener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (listener != null) {
                listener.onResult(new Result(Result.STATUS_FAIL));
            }
        }

        @Override
        public void onResponse(Call call, okhttp3.Response response) throws IOException {
            if (listener != null) {
                Result result = parseJson(url,tag, response.body().string());
                if (result != null) {
                    listener.onResult(result);
                }
            }
        }
    }

}
