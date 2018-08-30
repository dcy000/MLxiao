package com.gcml.auth.face.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_CREATE_GROUP;
import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_ENGINE_INIT;
import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_FACE_SIGN_IN;
import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_FACE_SIGN_UP;
import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_JOIN_GROUP_NOT_EXIST;
import static com.gcml.auth.face.model.FaceRepository.ERROR_ON_JOIN_GROUP_UNKNOWN;


/**
 * 讯飞人脸业务工具
 */
public class FaceIdHelper {

    public Observable<IdentityVerifier> obtainVerifier(Context context) {
        return Observable.create(new ObservableOnSubscribe<IdentityVerifier>() {
            @Override
            public void subscribe(ObservableEmitter<IdentityVerifier> emitter) throws Exception {
                IdentityVerifier verifier = IdentityVerifier.getVerifier();
                if (verifier != null) {
                    Timber.i("Face Engine ready!");
                    emitter.onNext(verifier);
                    return;
                }
                InitListener initListener = new InitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == ErrorCode.SUCCESS && IdentityVerifier.getVerifier() != null) {
                            Timber.i("Face Engine init success and ready!");
                            emitter.onNext(IdentityVerifier.getVerifier());
                        } else {
                            Timber.e("Face Engine init error");
                            emitter.onError(new FaceRepository.FaceError(ERROR_ON_ENGINE_INIT, "", null));
                        }
                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (IdentityVerifier.getVerifier() != null) {
                            IdentityVerifier.getVerifier().cancel();
                            IdentityVerifier.getVerifier().destroy();
                        }
                    }
                });
                IdentityVerifier.createVerifier(context, initListener);
            }
        }).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> signUp(Context context, byte[] faceData, String faceId) {
        return obtainVerifier(context)
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
                        Timber.i("Face sign up success");
                        emitter.onNext(true);
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        Timber.e("Face sign up error");
                        emitter.onError(new FaceRepository.FaceError(
                                ERROR_ON_FACE_SIGN_UP,
                                speechError.getMessage(),
                                speechError)
                        );
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
                verifier.startWorking(identityListener);
                //上传头像该信息
                verifier.writeData("ifr", "", faceData, 0, faceData.length);
                // 写入完成后，需要调用stopWrite停止写入，在停止的时候同样需要指定子业务类型。只有
                // stopWrite之后，才会触发 listener 中的回调接口，返回结果或者错误信息。
                verifier.stopWrite("ifr");
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> joinOrCreateGroup(
            Context context,
            String userId,
            String groupId,
            String faceId) {
        return joinGroup(context, groupId, faceId)
                .retryWhen(new Function<Observable<? extends Throwable>, ObservableSource<?>>() {
                    private AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public ObservableSource<?> apply(Observable<? extends Throwable> rxError) throws Exception {
                        return rxError.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable error) throws Exception {
                                if (error instanceof FaceRepository.FaceError
                                        && ((FaceRepository.FaceError) error).getCode() == ERROR_ON_JOIN_GROUP_NOT_EXIST
                                        && count.compareAndSet(0, 1)) {
                                    return createGroup(context, userId, faceId);
                                }
                                return Observable.error(error);
                            }
                        });
                    }
                });
    }

    public Observable<Boolean> joinGroup(
            Context context,
            String groupId,
            String faceId) {
        return obtainVerifier(context)
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
                IdentityListener listener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult identityResult, boolean b) {
                        emitter.onNext(true);
                    }

                    @Override
                    public void onError(SpeechError error) {
                        Timber.e(error);
                        if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {
                            emitter.onError(new FaceRepository.FaceError(ERROR_ON_JOIN_GROUP_NOT_EXIST, error.getErrorDescription(), error));
                        } else {
                            emitter.onError(new FaceRepository.FaceError(ERROR_ON_JOIN_GROUP_UNKNOWN, error.getErrorDescription(), error));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.execute("ipt", "add", param, listener);
            }
        }).subscribeOn(Schedulers.io());
    }


    public Observable<String> createGroup(
            Context context,
            String userId,
            String faceId) {
        return obtainVerifier(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(IdentityVerifier verifier) throws Exception {
                        return createGroupInternal(verifier, userId, faceId);
                    }
                });
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
                        emitter.onError(new FaceRepository.FaceError(ERROR_ON_CREATE_GROUP, error.getErrorDescription(), error));
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

    public Observable<Float> signIn(Context context, byte[] faceData, String groupId) {
        return obtainVerifier(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<Float>>() {
                    @Override
                    public ObservableSource<Float> apply(IdentityVerifier verifier) throws Exception {
                        return signInInternal(verifier, faceData, groupId);
                    }
                });
    }

    private ObservableSource<Float> signInInternal(
            IdentityVerifier verifier,
            byte[] faceData,
            String groupId) {
        return Observable.create(new ObservableOnSubscribe<Float>() {
            @Override
            public void subscribe(ObservableEmitter<Float> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置业务场景
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                // 设置业务类型
                verifier.setParameter(SpeechConstant.MFV_SST, "identify");
                // 设置监听器，开始会话
                IdentityListener listener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult result, boolean b) {
                        String json = result.getResultString();
                        try {
                            JSONObject resultObj = new JSONObject(json);
                            int ret = resultObj.optInt("ret");
                            if (ErrorCode.SUCCESS != ret) {
                                Timber.e("Face sign in error");
                                emitter.onError(new FaceRepository.FaceError(ERROR_ON_FACE_SIGN_IN, "", null));
                                return;
                            }
                            JSONArray scoreArray = resultObj
                                    .optJSONObject("ifv_result")
                                    .optJSONArray("candidates");
                            JSONObject firstScoreObj = scoreArray.optJSONObject(0);
                            String firstFaceId = firstScoreObj.optString("user");
                            double firstScore = firstScoreObj.optDouble("score");
                            Timber.i("FirstFace: {faceId: %s, score: %s}", firstFaceId, firstScore);
                            emitter.onNext((float) firstScore);
                        } catch (Throwable e) {
                            Timber.e(e, "Face sign in error");
                            emitter.onError(new FaceRepository.FaceError(ERROR_ON_FACE_SIGN_IN, "", e));
                        }
                    }

                    @Override
                    public void onError(SpeechError error) {
                        Timber.e(error, "Face sign in error");
                        emitter.onError(new FaceRepository.FaceError(
                                ERROR_ON_FACE_SIGN_IN,
                                error.getPlainDescription(true),
                                error));
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.startWorking(listener);
                String format = "group_id=%s,topc=3";
                String param = String.format(format, groupId);
                verifier.writeData("ifr", param, faceData, 0, faceData.length);
                // 写入完毕
                verifier.stopWrite("ifr");
            }
        });
    }



    /**
     * 老版存储本机登录账户信息的key
     */
    private static final String KEY_USER_ACCOUNT = "user_accounts";
    /**
     * 新版存储本机登录账户信息的key
     */
    private static final String KEY_USER_ACCOUNT_NEW = "user_accounts_new";
    /**
     * 用户id的key
     */
    private static final String KEY_USER_ID = "user_id";
    /**
     * 讯飞id的key
     */
    private static final String KEY_XUNFEI_ID = "Xunfei_Id";

    /**
     * 人脸识别组id
     */
    private static final String KEY_GROUP_ID = "group_id";
    /**
     * 创建人脸识别组的时候传入的第一个讯飞id
     */
    private static final String KEY_CREATE_GROUP_FIRST_XFID = "group_first_xfid";

    private volatile SharedPreferences mPreferences;

    public SharedPreferences getPreferences(Context context) {
        if (mPreferences == null) {
            synchronized (this) {
                if (mPreferences == null) {
                    mPreferences = context.getSharedPreferences("ScopeMediaPrefsFile", Context.MODE_PRIVATE);
                }
            }
        }
        return mPreferences;
    }
}
