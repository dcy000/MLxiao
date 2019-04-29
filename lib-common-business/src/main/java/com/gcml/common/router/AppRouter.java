package com.gcml.common.router;

import android.net.Uri;

import com.gcml.common.service.IUserEntityProvider;
import com.sjtu.yifei.annotation.Extra;
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
}
