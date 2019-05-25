package com.gcml.common.router;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;

import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.bean.get.GoodsBean;
import com.gcml.common.recommend.bean.get.Music;
import com.gcml.common.recommend.bean.post.TaskSchemaResultBean;
import com.gcml.common.service.IAppUpdateProvider;
import com.gcml.common.service.IBaiduAKProvider;
import com.gcml.common.service.IBusinessControllerProvider;
import com.gcml.common.service.ICallProvider;
import com.gcml.common.service.IECG_PDF_FragmentProvider;
import com.gcml.common.service.IFaceProvider;
import com.gcml.common.service.IHealthRecordBloodpressureFragmentProvider;
import com.gcml.common.service.IHuiQuanBodyTestProvider;
import com.gcml.common.service.IMusicPlayProvider;
import com.gcml.common.service.ISystemSettingProvider;
import com.gcml.common.service.ITaskProvider;
import com.gcml.common.service.IUserEntityProvider;
import com.gcml.common.service.IVertifyFaceProvider;
import com.gcml.common.service.IVideoListFragmentProvider;
import com.gcml.common.service.IVolumeControlProvider;
import com.gcml.common.service.IWakeUpControlProvider;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Flags;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;

public interface AppRouter {

    @Go("/common/business/user/provider")
    IUserEntityProvider getUserProvider();

    @Go("/health/measure/bloodpressure/manager")
    boolean skipBloodpressureManagerActivity(@Extra("fromActivity") String fromActivity);

    @Go("/health/measure/bloodpressure/manager")
    boolean skipBloodpressureManagerActivity(@Extra("fromActivity") String fromActivity, @Extra("toActivity") String toActivity);

    @Go("/health/measure/bloodpressure/manager")
    boolean skipBloodpressureManagerActivity(@Extra ActivityCallback callback);

    @Go("/health/measure/weight/manager")
    boolean skipWeightManagerActivity(@Extra("fromActivity") String fromActivity, @Extra("toActivity") String toActivity);

    @Go("/app/health/manager/treatment")
    boolean skipTreatmentPlanActivity();

    @Go("/health/measure/bloodsugar/manager")
    boolean skipBloodsugarManagerActivity(@Extra("fromActivity") String fromActivity, @Extra("toActivity") String toActivity);

    @Go("/app/activity/wifi/connect")
    boolean skipWifiConnectActivity(@Extra("is_first_wifi") boolean isFirstWifi);

    @Go("/app/activity/wifi/disconnected")
    boolean skipWifiDisconnectedActivity();

    @Go("/health/measure/choose/device")
    boolean skipMeasureChooseDeviceActivity(
            @Extra("isFaceSkip") boolean isFaceSkip
    );

    @Go("/health/measure/choose/device")
    boolean skipMeasureChooseDeviceActivity(
            @Extra("isFaceSkip") boolean isFaceSkip,
            @Extra("ServicePackage") String servicePackage,
            @Extra("ServicePackageUUID") String servicePackageUUID
    );

    @Go("/health/measure/all/measure")
    boolean skipAllMeasureActivity(@Extra("measure_type") int measureType);

    @Go("/health/measure/all/measure")
    boolean skipAllMeasureActivity(
            @Extra("measure_type") int measureType,
            @Extra("is_measure_task") boolean isMeasureTask);

    @Go("/health/measure/all/measure")
    boolean skipAllMeasureActivity(
            @Extra("measure_type") int measureType,
            @Extra("isFaceSkip") boolean isFaceSkip,
            @Extra("ServicePackage") String servicePackage,
            @Extra("ServicePackageUUID") String servicePackageUUID
    );

    @Go("/app/online/doctor/list")
    boolean skipOnlineDoctorListActivity(@Extra("flag") String flag);

    @Go("/app/online/doctor/list")
    boolean skipOnlineDoctorListActivity(@Extra("flag") String flag, @Extra("title") String title, @Extra("fromWhere") String from);

    @Go("/app/hypertension/slow/disease/management")
    boolean skipSlowDiseaseManagementActivity();

    @Go("/app/tcm/symptom/check")
    boolean skipSymptomCheckActivity();

    @Go("/video/normal/video/play")
    boolean skipNormalVideoPlayActivity(
            @Extra("key_uri") Uri uri,
            @Extra("key_url") String url,
            @Extra("key_title") String title,
            @Extra ActivityCallback callback);

    @Go("/video/measure/video/play")
    boolean skipMeasureVideoPlayActivity(
            @Extra("key_uri") Uri uri,
            @Extra("key_url") String url,
            @Extra("key_title") String title,
            @Extra ActivityCallback callback);

    @Go("/health/record/health/record/activity")
    boolean skipHealthRecordActivity(@Extra("position") int position);

    @Go("/app/homepage/main/activity")
    boolean skipMainActivity();

    @Go("/health/measure/first/diagnosis")
    boolean skipFirstDiagnosisActivity();

    @Go("/health/measure/health/inquiry")
    boolean skipHealthInquiryActivity();

    @Go("/bluetooth/ecg/pdf/fragment")
    IECG_PDF_FragmentProvider getECG_PDF_Fragment(@Extra("key_pdf_url") String key_pdf_url);

    @Go("/health/record/show/pdf")
    boolean skipShowPDFActivity(@Extra("url") String url);

    @Go("/app/slow/disease/management/tip")
    boolean skipSlowDiseaseManagementTipActivity();

    @Go("/app/hypertension/basic/information")
    boolean skipBasicInformationActivity(@Extra("fromWhere") String fromWhere);

    @Go("/app/hypertension/tip/activity")
    boolean skipHypertensionTipActivity();

    @Go("/app/hypertension/is/empty/stomach/or/not")
    boolean skipIsEmptyStomachOrNotActivity();

    @Go("/app/hypertension/management/normal/hight")
    boolean skipNormalHightActivity(@Extra("fromWhere") String fromWhere);

    @Go("/app/activity/market/activity")
    boolean skipMarketActivity();

    @Go("/app/heypertension/detecte/tip")
    boolean skipDetecteTipActivity(@Extra("fromWhere") String fromWhere);

    @Go("/app/activity/message/activity")
    boolean skipMessageActivity();

    @Go("/app/activity/offline/activity")
    boolean skipOfflineActivity();

    @Go("/edu/the/old/home/activity")
    boolean skipTheOldHomeActivity();

    @Go("/edu/child/edu/home/activity")
    boolean skipChildEduHomeActivity();

    @Go("/app/shopping/goods/detail")
    boolean skipGoodDetailActivity(@Extra("goods") GoodBean goodBean);

    @Go("/mall/order/list/activity")
    boolean skipOrderListActivity();

    @Go("/mall/mall/activity")
    boolean skipMallActivity();

    @Go("/recreation/entrance/activity")
    boolean skipRecreationEntranceActivity();

    @Go("/recreation/tools/activity")
    boolean skipToolsActivity();

    @Go("/recreation/jiemeng/activity")
    boolean skipJieMengActivity();

    @Go("/recreation/history/today/activity")
    boolean skipHistoryTodayActivity();

    @Go("/recreation/date/inquire/activity")
    boolean skipDateInquireActivity();

    @Go("/recreation/cook/book/activity")
    boolean skipCookBookActivity();

    @Go("/recreation/baike/activity")
    boolean skipBaikeActivity();

    @Go("/recreation/calculation/activity")
    boolean skipCalculationActivity();

    @Go("/factory/test/activity")
    boolean skipFactoryTestActivity();

    @Go("/app/speech/synthesis/activity")
    boolean skipSpeechSynthesisActivity();

    @Flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
    @Go("/app/welcome/activity")
    boolean skipWelcomeActivity();

    @Go("/common/business/fetch/code")
    IBusinessControllerProvider getBusinessControllerProvider();

    @Go("/auth/find/password/activity")
    boolean skipFindPasswordActivity(@Extra("phone") String phone);

    @Go("/auth/user/protocol/activity")
    boolean skipUserProtocolActivity();

    @Go("/auth/signup/activity")
    boolean skipSignUpActivity();

    @Go("/auth/signin/activity")
    boolean skipSignInActivity();

    @Go("/auth/profile/info/activity")
    boolean skipProfileInfoActivity();

    @Go("/auth/set/password/activity")
    boolean skipSetPasswordActivity(@Extra("phone") String phone);

    /**
     * 1. 人脸识别登录 （verify = false）
     * 2. 人脸认证登录 （verify = true）
     */
    @Go("/auth/face/bd/signin/activity")
    boolean skipFaceBdSignInActivity(
            @Extra("skip") boolean isShowSkipButton,
            @Extra("verify") boolean verify,
            @Extra("faceId") String faceId,
            @Extra("hidden") boolean hidden,
            @Extra ActivityCallback callback);

    @Go("/auth/face2/face/provider")
    IFaceProvider getFaceProvider();

    /**
     * 1. 注册人脸
     * 2. 更新人脸
     */
    @Go("/auth/face2/face/bd/signup/activity")
    boolean skipFaceBdSignUpActivity(
            @Extra("userId") String userId,
            @Extra ActivityCallback callback
    );

    @Go("/auth/auth/activity")
//    @Go("/auth/hospital/user/logins2/activity")
    boolean skipAuthActivity();

    @Go("/auth/simple/profile/activity")
    boolean skipSimpleProfileActivity(
            @Extra("signUpType") String signUpType,
            @Extra("signUpIdCard") String signUpIdCard,
            @Extra ActivityCallback callback);

    @Go("/auth/simple/profile/activity")
    boolean skipSimpleProfileActivity(
            @Extra("signUpType") String signUpType,
            @Extra("signUpIdCard") String signUpIdCard,
            @Extra("fromWhere") String from);

    @Go("/auth/profile2/activity")
    boolean skipProfile2Activity(@Extra ActivityCallback callback);

    @Go("/task/task/provider")
    ITaskProvider getTaskProvider();

    @Go("/task/task/comply/choice/activity")
    boolean skipTaskComplyChoiceActivity(@Extra("isFirst") boolean isFirst);

    @Go("/task/task/comply/activity")
    boolean skipTaskComplyActivity();

    @Go("/task/task/comply/result/activity")
    boolean skipTaskComplyResultActivity(@Extra("resultBean") TaskSchemaResultBean resultBean);

    @Go("/task/task/activity")
    boolean skipTaskActivity(@Extra("startType") String startType);

    @Go("/task/task/dialy/activity")
    boolean skipTaskDialyActivity(@Extra("what") int what);

    @Go("/task/task/dialy/contact/activity")
    boolean skipTaskDialyContactActivity();

    @Go("/mall/goods/detail/activity")
    boolean skipGoodsDetailActivity(@Extra("goods") GoodsBean goods);

    @Go("/mall/recharge/activity")
    boolean skipRechargeActivity();

    @Go("/mall/recharge/define/activity")
    boolean skipRechargeDefineActivity();

    @Go("/mall/search/goods/activity")
    boolean skipSearchGoodsActivity();

    @Go("/app/pay/money/activity")
    boolean skipPayActivity();

    @Go("/mall/old/order/list/activity")
    boolean skipOldOrderListActivity();

    @Go("/health/record/blood/pressure/fragment/provider")
    IHealthRecordBloodpressureFragmentProvider getFragmentProvider();

    @Go("/app/alarm/details2/activity")
    boolean skipAlarmDetail2Activity(@Extra("id") long id);

    @Go("/music/player/activity")
    boolean skipMusicPlayActivity(@Extra("music") Music music);

    @Go("/music/player/provider")
    IMusicPlayProvider getMusicPlayProvider();

    @Go("/app/video/list/fragment/provider")
    IVideoListFragmentProvider getVideoListFragmentProvider();

    @Go("/edu/child/poem/list/activity")
    boolean skipChildEduPoemListActivity();

    @Go("/edu/child/jokes/activity")
    boolean skipChildEduJokesActivity();

    @Go("/edu/child/sheet/details/activity")
    boolean skipChildEduSheetDetailsActivity(@Extra("sheetCategory") String sheetCategory);

    @Go("/module/control/setting/activity")
    boolean skipSettingActivity();

    @Go("/module/control/volume/control/provider")
    IVolumeControlProvider getVolumeControlProvider();

    @Go("/module/control/wake/control/provider")
    IWakeUpControlProvider getWakeControlProvider();

    @Go("/module/control/app/update/provider")
    IAppUpdateProvider getAppUpdateProvider();

    @Go("/edu/the/old/music/activity")
    boolean skipTheOldMusicActivity();

    @Go("/video/list/activity")
    boolean skipVideoListActivity(@Extra("position") int position);

    @Go("/call/call/provider")
    ICallProvider getCallProvider();

    @Go("/doctor/advisory/doctor/appo/activity2")
    boolean skipDoctorappoActivity2();

    @Go("/doctor/advisory/doctor/ask/guide/activity")
    boolean skipDoctorAskGuideActivity();

    @Go("/doctor/advisory/disease/details/activity")
    boolean skipDiseaseDetailsActivity(@Extra("type") String type);

    @Go("/doctor/advisory/check/contract/activity")
    boolean skipCheckContractActivity();

    @Go("/app/person/detail/activity")
    boolean skipPersonDetailActivity();

    @Go("/module/mall/service/package/activity")
    boolean skipServicePackageActivity(@Extra("isSkip") boolean isKip);

    @Go("hypertension/manager/older/health/management/sercive")
    boolean skipOlderHealthManagementSerciveActivity();

    @Go("/module/edu/radio/activity")
    boolean skipRadioActivity();

    @Go("/huiquan/body/test/provider")
    IHuiQuanBodyTestProvider getBodyTestProvider();

    @Go("/app/alarm/list/activity")
    boolean skipAlarmList2Activity();

    @Go("/baidu/unit/ak/provider")
    IBaiduAKProvider getBaiduAKProvider();

    @Go("/module/control/wifi/detail/activity")
    boolean skipWifiDetailActivity(@Extra("wifi") ScanResult wifi);

    @Go("/module/yzn/zenduan/activity")
    boolean skipZenDuanActivity();

    @Go("/module/detection/connect/activity")
    boolean skipConnectActivity(@Extra("detectionType") int type);

    @Go("/module/detection/connect/activity")
    boolean skipConnectActivity(@Extra("detectionType") int type, @Extra ActivityCallback callback);

    @Go("/module/detection/choose/dection/type")
    boolean skipChooseDetectionTypeActivity();

    @Go("/module/control/system/setting/provider")
    ISystemSettingProvider getSystemSettingProvider();

    @Go("/auth/hospital/user/logins2/activity")
    boolean skipUserLogins2Activity();

    @Go("module/control/voice/setting/activity")
    boolean skipVoiceSettingActivity();

    @Go("/module/control/about/activity")
    boolean skipAboutActivity();

    @Go("/common/business/checkUserEntityAndVertifyFace/face/provider")
    IVertifyFaceProvider getVertifyFaceProvider();

    @Go("/health/profile/add/followup")
    boolean skipAddFollowupActivity(
            @Extra("healthRecordId") String healthRecordId,
            @Extra("rdRecordId") String rdRecordId,
            @Extra("typeString") String typeString);

    @Go("/health/profile/wenzen/output")
    boolean skipWenZenOutputActivity(@Extra("highPrssure") String highPrssure, @Extra("lowPressure") String lowPressure);

    @Go("/health/profile/bracelet")
    boolean skipBraceletActivtity();

    @Go("/health/profile/health/profile/activity")
    boolean skipHealthProfileActivity();

    /**
     * @param RdCordId 默认：22d594369d8246ad9542f462d6f0f4ce
     * @return
     */
    @Go("/health/profile/add/health/profile")
    boolean skipAddHealthProfileActivity(@Extra("RdCordId") String RdCordId);

    @Go("/health/profile/output/result")
    boolean skipOutputResultActivity(
            @Extra("rdRecordId") String rdRecordId,
            @Extra("userRecordId") String userRecordId,
            @Extra("typeString") String typeString
    );
}
