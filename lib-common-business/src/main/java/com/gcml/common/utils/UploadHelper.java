package com.gcml.common.utils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 七牛云文件上传工具
 *
 * @see this#upload(byte[], String, String), 文件上传
 */
public class UploadHelper {

    public static final String BASE_URL_QINIU = "http://oyptcv2pb.bkt.clouddn.com/";

    private UploadManager mUploader = new UploadManager();

    /**
     * @param data  要上传文件的字节数据
     * @param key   要上传文件的外链后缀
     * @param token 七牛 token
     * @return 外链
     */
    public Observable<String> upload(
            byte[] data,
            String key,
            String token) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                UpCompletionHandler completionHandler = new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (!info.isOK() && !emitter.isDisposed()) {
                            emitter.onError(new RuntimeException(info.error));
                            return;
                        }
                        emitter.onNext(BASE_URL_QINIU + key);
                    }
                };
                mUploader.put(data, key, token, completionHandler, null);
            }
        });
    }
}
