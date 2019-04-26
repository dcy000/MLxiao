package com.gcml.common.router;

import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;
import com.sjtu.yifei.annotation.RequestCode;
import com.sjtu.yifei.route.ActivityCallback;

public interface AppRouter {
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
}
