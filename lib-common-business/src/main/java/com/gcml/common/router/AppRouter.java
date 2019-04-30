package com.gcml.common.router;

import android.content.Intent;
import android.net.Uri;

import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.service.IFragmentProvider;
import com.gcml.common.service.IUserEntityProvider;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Flags;
import com.sjtu.yifei.annotation.Go;
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
    IFragmentProvider getECG_PDF_Fragment(@Extra("key_pdf_url") String key_pdf_url);

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

    @Flags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
    @Go("/app/welcome/activity")
    boolean skipWelcomeActivity();
}
