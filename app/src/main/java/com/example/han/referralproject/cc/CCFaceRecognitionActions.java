package com.example.han.referralproject.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.bean.NDialog;
import com.example.han.referralproject.bean.NDialog2;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.shopping.GoodDetailActivity;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.entity.UserInfoBean;
import com.gcml.old.auth.register.SignUp14DiseaseHistoryActivity;
import com.gcml.old.auth.register.simple.SignUp03PasswordActivity;
import com.gcml.old.auth.signin.ChooseLoginTypeActivity;
import com.gcml.old.auth.signin.SignInActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/15 15:26
 * created by:gzq
 * description:操作module-face_recognition模块的actions
 * 源模块维护CCActions，目标模块维护CCResultActions
 * 一个模块维护一个类，不同模块不能交叉使用
 */
public class CCFaceRecognitionActions {
    public static final String MODULE_NAME = "face_recognition";

    /**
     * 所有发送action的name
     */
    interface SendActionNames {
        /**
         * 跳转RegisterHead2XunfeiActivity
         */
        String TO_REGISTERHEAD2XUNFEIACTIVITY = "To_RegisterHead2XunfeiActivity";
        /**
         * 跳转到FaceRecognitionActivity
         */
        String TO_FACERECOGNITIONACTIVITY = "To_FaceRecognitionActivity";
    }

    /**
     * 所有接受action的name
     */
    interface ReceiveResultActionNames {
        /**
         * 点击了返回按钮
         */
        String PRESSED_BACK_BUTTON = "pressedBackButton";
        /**
         * 点击了跳过按钮
         */
        String PRESSED_JUMP_BUTTON = "pressedJumpButton";
        /**
         * 出错了
         */
        String ON_ERROR = "onError";
        /**
         * 注册人头像成功
         */
        String REGIST_HEAD_SUCCESS = "RegistHeadSuccess";
        /**
         * 用户拒绝了摄像头的权限
         */
        String USER_REFUSED_CAMERA_PERMISSION = "userRefusedCameraPermission";
        /**
         * 头像验证成功
         */
        String FACE_RECOGNITION_SUCCESS = "faceRecognitionSuccess";
    }

    /**
     * 所有发送参数的key
     */
    interface SendKeys {
        /**
         * 讯飞id
         */
        String KEY_EXTRA_XFID = "key_xfid";
        /**
         * 传递bundle对象的key
         */
        String KEY_EXTRA_BUNDLE = "key_bundle";
    }

    /**
     * 所有接受参数的Key
     */
    interface ReceiveResultKeys {
        /**
         * 接受的action的key
         */
        String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
        /**
         * 接收Userinforbean
         */
        String KEY_EXTRA_CC_USERINFOBEAN = "key_cc_userinfobean";
    }

    /**
     * 跳转RegisterHead2XunfeiActivity
     *
     * @param activity
     */
    public static void jump2RegisterHead2XunfeiActivity(Activity activity) {
        //跳转过来的activity有PerInfoActivity，
        CC.obtainBuilder(MODULE_NAME).setActionName(SendActionNames.TO_REGISTERHEAD2XUNFEIACTIVITY)
                .addParam(SendKeys.KEY_EXTRA_XFID, produceXfid())
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                Timber.e(result.toString());
                String dataItem = result.getDataItem(ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (dataItem) {
                    case ReceiveResultActionNames.PRESSED_BACK_BUTTON:
                        //点击了返回按钮
                        break;
                    case ReceiveResultActionNames.PRESSED_JUMP_BUTTON:
                        //点击了跳过按钮
                        break;
                    case ReceiveResultActionNames.ON_ERROR:
                        //注册出错了
                        break;
                    case ReceiveResultActionNames.REGIST_HEAD_SUCCESS:
                        //注册头像成功
                        if (activity instanceof SignUp14DiseaseHistoryActivity) {
                            //正常注册流程
                            activity.startActivity(new Intent(activity, RecoDocActivity.class));
                        } else if (activity instanceof SignUp03PasswordActivity) {
                            //快速注册
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        }
                        break;
                    case ReceiveResultActionNames.USER_REFUSED_CAMERA_PERMISSION:
                        //用户拒绝了摄像头权限
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 跳转FaceRecognitionActivity
     *
     * @param activity
     * @param bundle
     */
    public static void jump2FaceRecognitionActivity(Context activity, Bundle bundle) {
        CC.obtainBuilder(MODULE_NAME).setActionName(SendActionNames.TO_FACERECOGNITIONACTIVITY)
                .addParam(SendKeys.KEY_EXTRA_BUNDLE, bundle)
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String dataItem = result.getDataItem(ReceiveResultKeys.KEY_EXTRA_CC_CALLBACK);
                switch (dataItem) {
                    case ReceiveResultActionNames.PRESSED_BACK_BUTTON:
                        //点击了返回按钮
                        break;
                    case ReceiveResultActionNames.PRESSED_JUMP_BUTTON:
                        //点击了跳过按钮
                        if (activity instanceof ChooseLoginTypeActivity) {
                            //选择人脸登录
                            activity.startActivity(new Intent(activity, SignInActivity.class));
                        } else if (activity instanceof GoodDetailActivity) {

                        } else if (activity instanceof MainActivity) {
                            JumpPage(bundle);
                        } else {
                            JumpPage(bundle);
                        }
                        break;
                    case ReceiveResultActionNames.ON_ERROR:
                        if (activity instanceof ChooseLoginTypeActivity) {
                            ToastUtils.showLong("该机器人没有此账号的人脸认证信息，请手动登录");
                        } else if (activity instanceof MainActivity) {
                            ToastUtils.showShort("验证未通过");
                        } else if (activity instanceof GoodDetailActivity) {
//                            ToastUtils.showShort("验证未通过");
                        }
                        break;
                    case ReceiveResultActionNames.FACE_RECOGNITION_SUCCESS:
                        //验证头像成功
                        if (activity instanceof ChooseLoginTypeActivity) {
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        } else if (activity instanceof MainActivity) {
                            new JpushAliasUtils(activity).setAlias("user_"
                                    + LocalShared.getInstance(activity).getUserId());
                            Map<String, Object> dataMap = result.getDataMap();
                            if (dataMap != null && dataMap.size() > 0) {
                                UserInfoBean userinfo = (UserInfoBean) dataMap.get(ReceiveResultKeys.KEY_EXTRA_CC_USERINFOBEAN);
                                LocalShared.getInstance(activity).setUserInfo(userinfo);
                                LocalShared.getInstance(activity).setSex(userinfo.sex);
                                LocalShared.getInstance(activity).setUserPhoto(userinfo.userPhoto);
                                LocalShared.getInstance(activity).setUserAge(userinfo.age);
                                LocalShared.getInstance(activity).setUserHeight(userinfo.height);
                            }
                            if (bundle != null) {
                                String fromType = bundle.getString("fromType");
                                switch (fromType) {
                                    case "xindian":
//                                        activity.startActivity(new Intent(activity, XinDianDetectActivity.class));
                                        CCHealthMeasureActions.jump2XinDianDetectActivity();
                                        break;
                                    default:
//                                        activity.startActivity(new Intent(activity, Test_mainActivity.class));
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                        break;
                                }
                            }
                        } else if (activity instanceof GoodDetailActivity) {
                            showPaySuccessDialog(activity);
                        }
                        break;
                    case ReceiveResultActionNames.USER_REFUSED_CAMERA_PERMISSION:
                        //用户拒绝了摄像头权限
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private static void JumpPage(Bundle bundle) {
        if (bundle != null) {
            String fromType = bundle.getString("fromType");
            if (TextUtils.isEmpty(fromType)) {
//                                    activity.startActivity(new Intent(activity, Test_mainActivity.class));
                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                return;
            }
            switch (fromType) {
                case "xindian":
//                                        activity.startActivity(new Intent(activity, XinDianDetectActivity.class));
                    CCHealthMeasureActions.jump2XinDianDetectActivity();
                    break;
                default:
//                                        activity.startActivity(new Intent(activity, Test_mainActivity.class));
                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                    break;
            }
        }
    }

    public static void showPaySuccessDialog(Context activity) {
        MLVoiceSynthetize.startSynthesize(activity, "主人，恭喜您支付成功", false);
        NDialog2 dialog2 = new NDialog2(activity);
        dialog2.setMessageCenter(true)
                .setMessage("支付成功")
                .setMessageSize(50)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(Color.parseColor("#3F86FC"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog2.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            Intent intent = new Intent(activity, OrderListActivity.class);
                            activity.startActivity(intent);
                        }

                    }
                }).create(NDialog.CONFIRM).show();
    }

    /**
     * TODO:本操作应该放在后台进行
     * 生产xfid
     *
     * @return
     */
    private static String produceXfid() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return simple.format(date) + str;
    }
}
