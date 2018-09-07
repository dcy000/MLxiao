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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
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
 * 1:1 注册 识别 删除，
 * 1:N 人脸检索 组创建/添加成员/查询组成员/删除成员/删除组/
 * 有些操作暂未实现, 见官方demo
 *
 * @see this#obtainEngine(Context context), 获取引擎
 * @see this#signUp(Context context, byte[] faceData, String faceId), 1:1 人脸注册
 * @see this#signIn(Context context, byte[] faceData, String groupId), 1:N 人脸检索
 * @see this#createGroup(Context context, String faceId), 1:N 创建组
 * @see this#joinGroup(Context context, String groupId, String faceId), 1:N 加组
 */
public class FaceIdHelper {

    public Observable<IdentityVerifier> obtainEngine(Context context) {
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
                            if (!emitter.isDisposed()) {
                                emitter.onError(new FaceRepository.FaceError(ERROR_ON_ENGINE_INIT, "", null));
                            }
                        }
                    }
                };
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        if (IdentityVerifier.getVerifier() != null) {
                            if (IdentityVerifier.getVerifier().isWorking()) {
                                IdentityVerifier.getVerifier().cancel();
                            }
                            IdentityVerifier.getVerifier().destroy();
                            Timber.i("Face Engine destroy");
                        }
                    }
                });
                IdentityVerifier.createVerifier(context, initListener);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Timber.i("Face obtainEngine: ");
            }
        }).unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io());
    }


    /**
     * @param context  用于创建验证器
     * @param faceData faceData
     * @param faceId   faceId
     * @return faceId
     */
    public Observable<String> signUp(Context context, byte[] faceData, String faceId) {
        return obtainEngine(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> apply(IdentityVerifier verifier) throws Exception {
                        return signUpInternal(verifier, faceData, faceId);
                    }
                });
    }

    private Observable<String> signUpInternal(
            IdentityVerifier verifier,
            byte[] faceData,
            String faceId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置业务场景：人脸（ifr）或声纹（ivp）
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                // 设置业务类型：注册（enroll）
                verifier.setParameter(SpeechConstant.MFV_SST, "enroll");
                // 设置用户id
                verifier.setParameter(SpeechConstant.AUTH_ID, faceId);
                IdentityListener identityListener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult result, boolean b) {
                        Timber.i("Face sign up success, %s", result.getResultString());
                        emitter.onNext(faceId);
                    }

                    @Override
                    public void onError(SpeechError error) {
                        //没网（20001）
                        //网络信号差
                        //模型或记录已存在 (10121)
                        //模型数据不存在 (10116)
                        //空组 （10141）
                        //组不存在 （10143）
                        Timber.e(error, "Face sign up error， %s", error.getPlainDescription(true));
                        if (!emitter.isDisposed()) {
                            emitter.onError(new FaceRepository.FaceError(
                                    ERROR_ON_FACE_SIGN_UP,
                                    error.getPlainDescription(true),
                                    error)
                            );
                        }
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
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Timber.i("Face signUp: ");
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<String> joinGroup(
            Context context,
            String groupId,
            String faceId) {
        return obtainEngine(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(IdentityVerifier verifier) throws Exception {
                        return joinGroupInternal(verifier, groupId, faceId);
                    }
                });
    }

    public Observable<String> joinGroupInternal(
            IdentityVerifier verifier,
            String groupId,
            String faceId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置会话场景
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
                // 用户id
                verifier.setParameter(SpeechConstant.AUTH_ID, faceId);
                String format = "auth_id=%s,scope=person,group_id=%s";
                String param = String.format(format, faceId, groupId);
                IdentityListener listener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult result, boolean b) {
                        Timber.i("Face joinGroup Success, %s", result.getResultString());
                        emitter.onNext(groupId);
                    }

                    @Override
                    public void onError(SpeechError error) {
                        String errorMsg = error.getPlainDescription(true);
                        Timber.e(error, "Face joinGroup Error, %s", errorMsg);
                        if (!emitter.isDisposed()) {
                            if (error.getErrorCode() == 10143 || error.getErrorCode() == 10106) {
                                emitter.onError(new FaceRepository.FaceError(ERROR_ON_JOIN_GROUP_NOT_EXIST, errorMsg, error));
                            } else {
                                emitter.onError(new FaceRepository.FaceError(ERROR_ON_JOIN_GROUP_UNKNOWN, errorMsg, error));
                            }
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.execute("ipt", "add", param, listener);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Timber.i("Face joinGroup: groupId = %s", groupId);
            }
        }).subscribeOn(Schedulers.io());
    }


    public Observable<String> createGroup(
            Context context,
            String faceId) {
        return obtainEngine(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(IdentityVerifier verifier) throws Exception {
                        return createGroupInternal(verifier, faceId);
                    }
                });
    }

    private Observable<String> createGroupInternal(
            IdentityVerifier verifier,
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
                        String resultJson = result.getResultString();
                        Timber.i("Face createGroup Success，%s", resultJson);
                        try {
                            JSONObject resultObj = new JSONObject(resultJson);
                            String groupId = resultObj.optString("group_id");
                            emitter.onNext(groupId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(SpeechError error) {
                        String errorMsg = error.getPlainDescription(true);
                        Timber.e(error, "Face createGroup Error，%s", errorMsg);
                        if (!emitter.isDisposed()) {
                            emitter.onError(new FaceRepository.FaceError(ERROR_ON_CREATE_GROUP, errorMsg, error));
                        }
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                };
                verifier.execute("ipt", "add", param, listener);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Timber.i("Face createGroup: ");
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * @param context  用于初始化引擎
     * @param faceData 人脸数据
     * @param groupId  组 Id
     * @return faceId:score
     */
    public Observable<String> signIn(Context context, byte[] faceData, String groupId) {
        return obtainEngine(context)
                .flatMap(new Function<IdentityVerifier, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(IdentityVerifier verifier) throws Exception {
                        return signInInternal(verifier, faceData, groupId);
                    }
                });
    }

    private ObservableSource<String> signInInternal(
            IdentityVerifier verifier,
            byte[] faceData,
            String groupId) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                verifier.setParameter(SpeechConstant.PARAMS, null);
                // 设置业务场景
                verifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                // 设置业务类型
                verifier.setParameter(SpeechConstant.MFV_SST, "identify");
                // 设置监听器，开始会话
                IdentityListener listener = new IdentityListener() {
                    @Override
                    public void onResult(IdentityResult result, boolean b) {
                        String resultJson = result.getResultString();
                        try {
                            JSONObject resultObj = new JSONObject(resultJson);
                            int ret = resultObj.optInt("ret");
                            if (ErrorCode.SUCCESS != ret) {
                                String errorMsg = new SpeechError(ret).getPlainDescription(true);
                                Timber.e("Face sign in error, %s", errorMsg);
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new FaceRepository.FaceError(
                                            ERROR_ON_FACE_SIGN_IN,
                                            errorMsg,
                                            null));
                                }
                                return;
                            }
                            JSONArray scoreArray = resultObj
                                    .optJSONObject("ifv_result")
                                    .optJSONArray("candidates");
                            Timber.i("Face sign in success, %s", resultJson);
                            JSONObject firstScoreObj = scoreArray.optJSONObject(0);
                            String firstFaceId = firstScoreObj.optString("user");
                            double firstScore = firstScoreObj.optDouble("score");
                            Timber.i("FirstFace: {faceId: %s, score: %s}", firstFaceId, firstScore);
                            String faceIdWithScore = String.format("%s:%s", firstFaceId, firstScore);
                            emitter.onNext(faceIdWithScore);
                        } catch (Throwable e) {
                            Timber.e(e, "Face sign in error, %s", e.getMessage());
                            if (!emitter.isDisposed()) {
                                emitter.onError(new FaceRepository.FaceError(ERROR_ON_FACE_SIGN_IN, "", e));
                            }
                        }
                    }

                    @Override
                    public void onError(SpeechError error) {
                        String errorMsg = error.getPlainDescription(true);
                        Timber.e(error, "Face sign in error, %s", errorMsg);
                        if (!emitter.isDisposed()) {
                            emitter.onError(new FaceRepository.FaceError(
                                    ERROR_ON_FACE_SIGN_IN,
                                    errorMsg,
                                    error));
                        }
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
}
