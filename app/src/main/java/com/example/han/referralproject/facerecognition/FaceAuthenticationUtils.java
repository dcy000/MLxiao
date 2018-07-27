package com.example.han.referralproject.facerecognition;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.gcml.lib_utils.data.TimeUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.utils.Handlers;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by gzq on 2018/2/28.
 */

public class FaceAuthenticationUtils {
    private static volatile FaceAuthenticationUtils singleton = null;
    private IdentityVerifier mIdVerifier;
    private static String TAG = FaceAuthenticationUtils.class.getSimpleName();
    private HashMap<String, String> xfid_userid;
    private String[] xfids;
    private Context context;

    private FaceAuthenticationUtils(){}
    private FaceAuthenticationUtils(Context context) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        this.context = contextWeakReference.get();
        if (context != null) {
            mIdVerifier = IdentityVerifier.createVerifier(contextWeakReference.get(), new InitListener() {
                @Override
                public void onInit(int i) {
                    if (i == ErrorCode.SUCCESS) {
                        Log.d(TAG, "初始化引擎成功");
                    } else {
                        Log.d(TAG, "初始化引擎失败");
                    }
                }
            });
        }
    }

    public static FaceAuthenticationUtils getInstance(Context context) {

        if (singleton == null) {
            synchronized (FaceAuthenticationUtils.class) {
                if (singleton == null) {
                    singleton = new FaceAuthenticationUtils(context);
                }
            }
        }
        return singleton;
    }

    public IdentityVerifier getmIdVerifier() {
        return mIdVerifier;
    }

    private void getLocalAllUsers() {
        xfid_userid = new HashMap<>();
        //获取本地所有账号
        if (context != null) {
            String[] accounts = LocalShared.getInstance(context).getAccounts();
            if (accounts != null) {
                xfids = new String[accounts.length];
                for (int i = 0; i < accounts.length; i++) {
                    xfids[i] = accounts[i].split(",")[1];
                    xfid_userid.put(accounts[i].split(",")[1], accounts[i].split(",")[0]);
                }
            }
        }
    }

    public void updateGroupInformation(final String groupId, final String xfid) {
        //将组的相关信息存到服务器上
        Handlers.bg().post(new Runnable() {
            @Override
            public void run() {
                NetworkApi.recordGroup(groupId, xfid, new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
//                        NetworkApi.getXfGroupInfo(groupId, xfids[0], new NetworkManager.SuccessCallback<ArrayList<XfGroupInfo>>() {
//                            @Override
//                            public void onSuccess(ArrayList<XfGroupInfo> response) {
//                                for (XfGroupInfo xfGroupInfo : response) {
//                                    if (xfGroupInfo.gid.equals(groupId)) {
//                                        deleteGroupId = xfGroupInfo.grid;
//                                        break;
//                                    }
//                                }
//                                Handlers.bg().removeCallbacksAndMessages(null);
//                            }
//                        });
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        Handlers.bg().removeCallbacksAndMessages(null);
                    }
                });
            }
        });
    }

    /**
     * 获取本地所有已经登录账号的xfid
     *
     * @return
     */
    public String[] getAllLocalXfids() {
        getLocalAllUsers();
        return xfids;
    }

    /**
     * 获取本地所又已经登录账号的讯飞id和userid的键值对
     *
     * @return
     */
    public HashMap<String, String> getAllLocalXfid_Userid() {
        getLocalAllUsers();
        return xfid_userid;
    }

    /**
     * 注册人脸
     */
    public void registerFace(byte[] mImageData,String xfid){
        if (mImageData!=null){
            //清空参数
            mIdVerifier.setParameter(SpeechConstant.PARAMS,null);
            // 设置业务场景：人脸（ifr）或声纹（ivp）
            mIdVerifier.setParameter( SpeechConstant.MFV_SCENES, "ifr" );

            // 设置业务类型：注册（enroll）
            mIdVerifier.setParameter( SpeechConstant.MFV_SST, "enroll" );

            // 设置用户id
            mIdVerifier.setParameter( SpeechConstant.AUTH_ID, xfid );

            // 设置监听器，开始会话
            mIdVerifier.startWorking(new IdentityListener() {
                @Override
                public void onResult(IdentityResult identityResult, boolean b) {
                    if (registerFaceListener!=null){
                        registerFaceListener.onResult(identityResult,b);
                    }
                }

                @Override
                public void onError(SpeechError speechError) {
                    if (registerFaceListener!=null){
                        registerFaceListener.onError(speechError);
                    }
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                    if (registerFaceListener!=null){
                        registerFaceListener.onEvent(i,i1,i2,bundle);
                    }
                }
            });
            //上传头像该信息
            mIdVerifier.writeData( "ifr", "", mImageData, 0, mImageData.length );
            // 写入完成后，需要调用stopWrite停止写入，在停止的时候同样需要指定子业务类型。只有
            // stopWrite之后，才会触发 listener 中的回调接口，返回结果或者错误信息。
            mIdVerifier.stopWrite( "ifr" );
        }
    }

    /**
     * 单个头像验证
     * @param mImageData
     * @param xfid
     */
    public void verificationSingleFace(byte[] mImageData,String xfid){
        if (mImageData!=null) {
            //清空参数
            mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
            // 设置会话场景
            mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
            // 设置会话类型
            mIdVerifier.setParameter(SpeechConstant.MFV_SST, "verify");
            // 设置验证模式，单一验证模式：sin
            mIdVerifier.setParameter(SpeechConstant.MFV_VCM, "sin");
            // 用户id
            mIdVerifier.setParameter(SpeechConstant.AUTH_ID, xfid);
            // 设置监听器，开始会话
            mIdVerifier.startWorking(new IdentityListener() {
                @Override
                public void onResult(IdentityResult identityResult, boolean b) {
                    if (singleFaceVertifyListener!=null){
                        singleFaceVertifyListener.onResult(identityResult,b);
                    }
                }

                @Override
                public void onError(SpeechError speechError) {
                    if (singleFaceVertifyListener!=null){
                        singleFaceVertifyListener.onError(speechError);
                    }
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                    if (singleFaceVertifyListener!=null){
                        singleFaceVertifyListener.onEvent(i,i1,i1,bundle);
                    }
                }
            });
            mIdVerifier.writeData("ifr", "", mImageData, 0, mImageData.length);
            mIdVerifier.stopWrite("ifr");
        }
    }
    /**
     * 验证
     */
    public void verificationFace(byte[] mImageData, String mGroupId) {
        if (mImageData != null) {
            // 清空参数
            mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
            // 设置业务场景
            mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
            // 设置业务类型
            mIdVerifier.setParameter(SpeechConstant.MFV_SST, "identify");
            // 设置监听器，开始会话
            mIdVerifier.startWorking(new IdentityListener() {
                @Override
                public void onResult(IdentityResult identityResult, boolean b) {
                    if (vertifyFaceListener != null)
                        vertifyFaceListener.onResult(identityResult, b);
                }

                @Override
                public void onError(SpeechError speechError) {
                    if (vertifyFaceListener != null)
                        vertifyFaceListener.onError(speechError);
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {
                    if (vertifyFaceListener != null)
                        vertifyFaceListener.onEvent(i, i1, i2, bundle);
                }
            });

            // 子业务执行参数，若无可以传空字符传
            StringBuffer params = new StringBuffer();
            params.append(",group_id=" + mGroupId + ",topc=3");
            // 向子业务写入数据，人脸数据可以一次写入
            mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
            // 写入完毕
            mIdVerifier.stopWrite("ifr");
        }
    }

    /**
     * 创建组
     *
     * @param xfids
     */
    public void createGroup(String[] xfids) {
        //默认将当前日期作为组名
        String groupName = "gcml" + TimeUtils.milliseconds2String(System.currentTimeMillis(),
                new SimpleDateFormat("MM.dd"));
        // sst=add，scope=group，group_name=famil;
        // 设置人脸模型操作参数
        // 清空参数
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, xfids[0]);

        // 设置模型参数，若无可以传空字符传
        StringBuffer params = new StringBuffer();
        params.append("auth_id=" + xfids[0]);
        params.append(",scope=group");
        params.append(",group_name=" + groupName);
        // 执行模型操作
        mIdVerifier.execute("ipt", "add", params.toString(), new IdentityListener() {
            @Override
            public void onResult(IdentityResult identityResult, boolean b) {
                if (createListener != null)
                    createListener.onResult(identityResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                if (createListener != null)
                    createListener.onError(speechError);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                if (createListener != null)
                    createListener.onEvent(i, i1, i2, bundle);
            }
        });
    }

    /**
     * 创建组
     * @param xfid 注册成功的一个讯飞id
     */
    public void createGroup(String xfid) {
        //默认将当前日期作为组名
        String groupName = "gcml" + TimeUtils.milliseconds2String(System.currentTimeMillis(),
                new SimpleDateFormat("MM.dd"));
        // sst=add，scope=group，group_name=famil;
        // 设置人脸模型操作参数
        // 清空参数
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, xfid);

        // 设置模型参数，若无可以传空字符传
        StringBuffer params = new StringBuffer();
        params.append("auth_id=" + xfid);
        params.append(",scope=group");
        params.append(",group_name=" + groupName);
        // 执行模型操作
        mIdVerifier.execute("ipt", "add", params.toString(), new IdentityListener() {
            @Override
            public void onResult(IdentityResult identityResult, boolean b) {
                if (createListener != null)
                    createListener.onResult(identityResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                if (createListener != null)
                    createListener.onError(speechError);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                if (createListener != null)
                    createListener.onEvent(i, i1, i2, bundle);
            }
        });
    }

    /**
     * 加入组
     *
     * @param groupId 组id
     * @param authId 需要加入组的讯飞id
     */
    public void joinGroup(String groupId, String authId) {
        // sst=add，auth_id=eqhe，group_id=123456，scope=person
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, authId);
        // 设置模型参数，若无可以传空字符传
        StringBuffer params2 = new StringBuffer();
        params2.append("auth_id=" + authId);
        params2.append(",scope=person");
        params2.append(",group_id=" + groupId);
        // 执行模型操作
        mIdVerifier.execute("ipt", "add", params2.toString(), new IdentityListener() {
            @Override
            public void onResult(IdentityResult identityResult, boolean b) {
                if (joinGroupListener != null)
                    joinGroupListener.onResult(identityResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                if (joinGroupListener != null)
                    joinGroupListener.onError(speechError);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                if (joinGroupListener != null)
                    joinGroupListener.onEvent(i, i1, i2, bundle);
            }
        });
    }

    /**
     * 删除组
     * @param groupId 组id
     * @param xfid 需要删除的讯飞id
     */
    public void deleteGroup(String groupId, String xfid) {

        // sst=add，auth_id=eqhe，group_id=123456，scope=person
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, xfid);

        // 设置模型参数，若无可以传空字符传
        StringBuffer params2 = new StringBuffer();
        params2.append("scope=group");

        params2.append(",group_id=" + groupId);
        // 执行模型操作
        mIdVerifier.execute("ipt", "delete", params2.toString(), new IdentityListener() {
            @Override
            public void onResult(IdentityResult identityResult, boolean b) {
                if (deleteGroupListener != null)
                    deleteGroupListener.onResult(identityResult, b);
                if (context != null) {
                    LocalShared.getInstance(context).setGroupId("");
                    LocalShared.getInstance(context).setGroupFirstXfid("");
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                if (deleteGroupListener != null)
                    deleteGroupListener.onError(speechError);
                if (context != null) {
                    LocalShared.getInstance(context).setGroupId("");
                    LocalShared.getInstance(context).setGroupFirstXfid("");
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                if (deleteGroupListener != null)
                    deleteGroupListener.onEvent(i, i1, i2, bundle);
                if (context != null) {
                    LocalShared.getInstance(context).setGroupId("");
                    LocalShared.getInstance(context).setGroupFirstXfid("");
                }
            }
        });
    }

    /**
     * 查询指定组中的所有成员
     * @param groupId 组id
     */
    public void queryGroup(String groupId) {

        // sst=add，auth_id=eqhe，group_id=123456，scope=person
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ipt");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, xfids[0]);
        // 设置模型参数，若无可以传空字符传
        StringBuffer params2 = new StringBuffer();
        params2.append("scope=group");
        params2.append(",group_id=" + groupId);
        // 执行模型操作
        mIdVerifier.execute("ipt", "query", params2.toString(), new IdentityListener() {
            @Override
            public void onResult(IdentityResult identityResult, boolean b) {
                Log.e("查询组成员", "onResult: " + identityResult.getResultString());
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.e("查询出错", "onError: " + speechError.toString());
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public void cancelIdentityVerifier() {
        if (mIdVerifier != null) {
            mIdVerifier.cancel();
            mIdVerifier.destroy();
            createListener=null;
            joinGroupListener=null;
            deleteGroupListener=null;
            vertifyFaceListener=null;
            registerFaceListener=null;
            singleFaceVertifyListener=null;
            context=null;
            singleton=null;
        }
    }

    private ICreateGroupListener createListener;
    private IJoinGroupListener joinGroupListener;
    private IDeleteGroupListener deleteGroupListener;
    private IVertifyFaceListener vertifyFaceListener;
    private IRegisterFaceListener registerFaceListener;
    private ISingleFaceVertifyListener singleFaceVertifyListener;
    public void setOnSingleFaceVertifyListener(ISingleFaceVertifyListener singleFaceVertifyListener){
        this.singleFaceVertifyListener=singleFaceVertifyListener;
    }
    public void setOnRegisterListener(IRegisterFaceListener registerFaceListener){
        this.registerFaceListener=registerFaceListener;
    }
    public void setOnCreateGroupListener(ICreateGroupListener createListener) {
        this.createListener = createListener;
    }

    public void setOnJoinGroupListener(IJoinGroupListener joinGroupListener) {
        this.joinGroupListener = joinGroupListener;
    }

    public void setOnDeleteGroupListener(IDeleteGroupListener deleteGroupListener) {
        this.deleteGroupListener = deleteGroupListener;
    }

    public void setOnVertifyFaceListener(IVertifyFaceListener vertifyFaceListener) {
        this.vertifyFaceListener = vertifyFaceListener;
    }

}
