package com.gzq.lib_core.http.subscriber;


import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.BaseModel;
import com.gzq.lib_core.utils.ToastUtils;

/**
 * Created by wzm on 2017/6/11.
 */

public abstract class UploadSubscriber<T> extends BaseSubscriber{

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            _onProgress((Integer) o);
        }

        if (o instanceof BaseModel) {
            BaseModel baseModel = (BaseModel) o;
            if (!baseModel.isSuccess()) {
                _onError(baseModel.getCode(), baseModel.getMessage());
            } else {
                if (baseModel.getData() != null) {
                    _onNext((T) baseModel.getData());
                }
            }
        }
        mSubscription.request(1);
    }

    @Override
    protected void onError(ApiException ex) {
        _onError(ex.code,ex.message);
        ToastUtils.showShort(ex.message+":"+ex.code);
    }

    protected abstract void _onNext(T result);

    protected abstract void _onProgress(Integer percent);

    protected abstract void _onError(int errorCode, String msg);


}
