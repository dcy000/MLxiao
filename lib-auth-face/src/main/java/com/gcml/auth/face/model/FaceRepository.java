package com.gcml.auth.face.model;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FaceRepository {

    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public Observable<IdentityVerifier> createVerifier(Context context) {
        return Observable.create(new ObservableOnSubscribe<IdentityVerifier>() {
            @Override
            public void subscribe(ObservableEmitter<IdentityVerifier> emitter) throws Exception {
                IdentityVerifier verifier = IdentityVerifier.getVerifier();
                if (verifier != null) {
                    emitter.onNext(verifier);
                    return;
                }
                InitListener initListener = new InitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == ErrorCode.SUCCESS) {
                            emitter.onNext(IdentityVerifier.getVerifier());
                        } else {
                            emitter.onError(new Error(ERROR_ON_ENGINE_INIT, "", null));
                        }
                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {

                    }
                });
                IdentityVerifier.createVerifier(context, initListener);
            }
        });
    }

    public Observable<Boolean> signUp(byte[] faceData, String faceId) {
        return createVerifier(mContext)
                .flatMap(new Function<IdentityVerifier, ObservableSource<? extends Boolean>>() {
                    @Override
                    public ObservableSource<? extends Boolean> apply(IdentityVerifier verifier) throws Exception {
                        return signUpInternal(verifier, faceData, faceId);
                    }
                });
    }

    private Observable<Boolean> signUpInternal(
            IdentityVerifier verifier,
            byte[] faceData,
            String faceId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置业务场景：人脸（ifr）或声纹（ivp）
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                // 设置业务类型：注册（enroll）
                verifier.setParameter(SpeechConstant.MFV_SST, "enroll");
                // 设置用户id
                verifier.setParameter(SpeechConstant.AUTH_ID, faceId);
                IdentityListener identityListener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult identityResult, boolean b) {
                        emitter.onNext(true);
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        emitter.onError(new Error(
                                ERROR_ON_FACE_SIGN_UP,
                                speechError.getMessage(),
                                speechError)
                        );
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.startWorking(identityListener);
                //上传头像该信息
                verifier.writeData("ifr", "", faceData, 0, faceData.length);
                // 写入完成后，需要调用stopWrite停止写入，在停止的时候同样需要指定子业务类型。只有
                // stopWrite之后，才会触发 listener 中的回调接口，返回结果或者错误信息。
                verifier.stopWrite("ifr");
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {

                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }

//    public Observable<String> joinOrCreateGroup(
//            String userId,
//            String groupId,
//            String faceId) {
//        return joinGroup(groupId, faceId)
//                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
//                    private AtomicInteger count = new AtomicInteger(0);
//                    @Override
//                    public ObservableSource<?> apply(Observable<Throwable> rxError) throws Exception {
//                        return rxError.flatMap(new Function<Throwable, ObservableSource<?>>() {
//                            @Override
//                            public ObservableSource<String> apply(Throwable throwable) throws Exception {
//                                if (throwable instanceof Error) {
//                                    if (((Error) throwable).getCode() == ERROR_ON_JOIN_GROUP_NOT_EXIST) {
//                                        if (count.compareAndSet(0, 1)) {
//                                            return Observable.timer(100, TimeUnit.MILLISECONDS);
//                                        }
//                                    }
//                                }
//                                return Observable.error(throwable);
//                            }
//                        });
//                    }
//                }).;
//    }

    public Observable<Boolean> joinGroup(
            String groupId,
            String faceId) {
        return createVerifier(mContext)
                .flatMap(new Function<IdentityVerifier, ObservableSource<? extends Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(IdentityVerifier verifier) throws Exception {
                        return joinGroupInternal(verifier, groupId, faceId);
                    }
                });
    }

    public Observable<Boolean> joinGroupInternal(
            IdentityVerifier verifier,
            String groupId,
            String faceId) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置会话场景
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
                // 用户id
                verifier.setParameter(SpeechConstant.AUTH_ID, faceId);
                String format = "auth_id=%s,scope=person,group_id=%s";
                String param = String.format(format, faceId, groupId);
                IdentityListener identityListener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult identityResult, boolean b) {
                        emitter.onNext(true);
                    }

                    @Override
                    public void onError(SpeechError error) {
                        Timber.e(error);
                        if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {
                            emitter.onError(new Error(ERROR_ON_JOIN_GROUP_NOT_EXIST, error.getErrorDescription(), error));
                        } else {
                            emitter.onError(new Error(ERROR_ON_JOIN_GROUP_UNKNOWN, error.getErrorDescription(), error));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.execute("ipt", "add", param, identityListener);
            }
        }).subscribeOn(Schedulers.io());
    }

    private Observable<String> createGroupInternal(
            IdentityVerifier verifier,
            String userId,
            String faceId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String groupName = "gcml" + new SimpleDateFormat(
                        "MM.dd", Locale.getDefault()).format(new Date());
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置会话场景
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
                // 用户id
                verifier.setParameter(SpeechConstant.AUTH_ID, faceId);
                String format = "auth_id=%s,scope=group,group_name=%s";
                String param = String.format(format, faceId, groupName);
                IdentityListener listener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult result, boolean b) {
                        try {
                            JSONObject resultObj = new JSONObject(result.getResultString());
                            String groupId = resultObj.optString("group_id");
//                            FaceRecognitionSPManifest.setGroupId(groupId);
//                            FaceRecognitionSPManifest.setGroupFirstXfid(xfid);
//                            uploadHeadToSelf(userid, xfid);
//                            FaceAuthenticationUtils.getInstance(mContext).updateGroupInformation(groupId, xfid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(SpeechError error) {
                        Timber.e(error, "创建组失败");
                        emitter.onError(new Error(ERROR_ON_CREATE_GROUP, error.getErrorDescription(), error));
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {

                    }
                });
                verifier.execute("ipt", "add", param, listener);
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String groupId) throws Exception {

            }
        }).subscribeOn(Schedulers.io());
    }


    public static final int ERROR_ON_ENGINE_INIT = -1;
    public static final int ERROR_ON_FACE_SIGN_UP = -2;
    public static final int ERROR_ON_JOIN_GROUP_NOT_EXIST = -3;
    public static final int ERROR_ON_JOIN_GROUP_UNKNOWN = -4;
    public static final int ERROR_ON_CREATE_GROUP = -5;

    public static class Error extends RuntimeException {
        private int code;

        public Error(int code, String message, Throwable cause) {
            super(message, cause);
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
