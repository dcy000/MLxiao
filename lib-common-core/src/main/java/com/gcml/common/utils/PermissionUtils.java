package com.gcml.common.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.O;

public class PermissionUtils {

    public static final String PERMISSION_NOTIFICATION = "permissionx.NOTIFICATION";
    public static final String PERMISSION_SYSTEM_ALERT = "permissionx.SYSTEM_ALERT";
    public static final String PERMISSION_UNKNOWN_APP_SOURCES = "permissionx.UNKNOWN_APP_SOURCES";

    public static Observable<Permission> requestEach(final Activity activity, final String... permissions) {
        if (!isActivityAvailable(activity)) {
            return Observable.error(new IllegalStateException());
        }
        return Observable.fromArray(permissions)
                .flatMap(new Function<String, ObservableSource<Permission>>() {
                    @Override
                    public ObservableSource<Permission> apply(String s) throws Exception {
                        if (PERMISSION_SYSTEM_ALERT.equals(s)) {
                            if (checkSystemAlert(activity)) {
                                return Observable.just(new Permission(s, true, true));
                            } else {

                                try {
                                    Intent permissionXIntent;
                                    permissionXIntent = getPermissionXIntent(activity, s);
                                    activity.startActivity(permissionXIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return Observable.error(new SecurityException("去设置中打开悬浮窗权限"));
                            }
                        } else if (PERMISSION_NOTIFICATION.equals(s)) {
                            if (checkNotification(activity)) {
                                return Observable.just(new Permission(s, true, true));
                            } else {

                                try {
                                    Intent permissionXIntent;
                                    permissionXIntent = getPermissionXIntent(activity, s);
                                    activity.startActivity(permissionXIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return Observable.error(new SecurityException("去设置中打开设置通知权限"));
                            }
                        } else if (PERMISSION_UNKNOWN_APP_SOURCES.equals(s)) {
                            if (checkUnknownSource(activity)) {
                                return Observable.just(new Permission(s, true, true));
                            } else {
                                try {
                                    Intent permissionXIntent;
                                    permissionXIntent = getPermissionXIntent(activity, s);
                                    activity.startActivity(permissionXIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return Observable.error(new SecurityException("去设置中打开设置并允许安装未知来源应用权限"));
                            }
                        } else {
                            if (isOldPermissionSystem(activity)) {
                                if (check(activity, s)) {
                                    return Observable.just(new Permission(s, true, true));
                                } else {
                                    navToAppDetail(activity, -1);
                                    return Observable.error(new SecurityException("去设置中打开设置相关权限"));
                                }
                            }

                            return new RxPermissions(activity).requestEach(s)
                                    .flatMap(new Function<Permission, ObservableSource<Permission>>() {
                                        @Override
                                        public ObservableSource<Permission> apply(Permission permission) throws Exception {
                                            if (permission.granted) {
                                                return Observable.just(permission);
                                            }

                                            if (permission.shouldShowRequestPermissionRationale) {
                                                return Observable.error(new SecurityException("请同意相关权限"));
                                            }

                                            navToAppDetail(activity, -1);
                                            return Observable.error(new SecurityException("去设置中打开设置相关权限"));
                                        }
                                    });
                        }
                    }
                });
    }

    private static boolean isOldPermissionSystem(Context context) {
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        return Build.VERSION.SDK_INT < M || targetSdkVersion < M;
    }

    private static void navToAppDetail(Activity activity, int requestCode) {
        if (!isActivityAvailable(activity)) {
            return;
        }
        Intent appDetailIntent = getAppManageIntent(activity);
        if (null == appDetailIntent) {

        }
        activity.startActivityForResult(appDetailIntent, requestCode);
    }

    private static boolean isActivityAvailable(Activity activity) {
        if (null == activity) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    private static boolean checkNotification(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    private static boolean checkSystemAlert(Context context) {


        if (Build.VERSION.SDK_INT >= M) {
            return Settings.canDrawOverlays(context);
        }
        return check(context, PermissionUtils.PERMISSION_SYSTEM_ALERT);
    }

    private static boolean checkUnknownSource(Context context) {
        if (Build.VERSION.SDK_INT >= O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }


    private static Intent getPermissionXIntent(Context context, String permissionX) {
        Intent intent;
        switch (permissionX) {
            case PermissionUtils.PERMISSION_SYSTEM_ALERT:
                intent = getDrawOverPermissionIntent(context);
                break;
            case PermissionUtils.PERMISSION_UNKNOWN_APP_SOURCES:
                intent = getInstallPermissionIntent(context);
                break;
            case PermissionUtils.PERMISSION_NOTIFICATION:
            default:
                intent = getAppManageIntent(context);
                break;
        }
        return intent;
    }

    private static Intent getDrawOverPermissionIntent(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        //system support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageURI);
        }
        //  check by appOps, so go to Application settings
        return getAppManageIntent(context);
    }

    private static Intent getInstallPermissionIntent(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        }
        return null;
    }

    private static Intent getAppManageIntent(Context context) {
        Intent intent;
        try {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
        } catch (Exception e) {
            intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
        }
        return intent;
    }

    private static boolean check(Context context, String permission) {
        if (null == permission) {
            return true;
        }
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                return checkOp(context, 4);
            case Manifest.permission.WRITE_CONTACTS:
                return checkOp(context, 5);
            case Manifest.permission.CALL_PHONE:
                return checkOp(context, 13);
            case Manifest.permission.READ_PHONE_STATE:
                return checkOp(context, 51);
            case Manifest.permission.CAMERA:
                return checkOp(context, 26);
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return checkOp(context, 59);
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return checkOp(context, 60);
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return checkOp(context, 1);
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return checkOp(context, 0);
            case Manifest.permission.RECORD_AUDIO:
                return checkOp(context, 27);
            case Manifest.permission.BODY_SENSORS:
                return checkOp(context, 56);
            case Manifest.permission.READ_CALENDAR:
                return checkOp(context, 8);
            case Manifest.permission.WRITE_CALENDAR:
                return checkOp(context, 9);
            case Manifest.permission.SEND_SMS:
                return checkOp(context, 20);
            case Manifest.permission.READ_SMS:
                return checkOp(context, 14);
            case Manifest.permission.RECEIVE_SMS:
                return checkOp(context, 16);
            case PERMISSION_SYSTEM_ALERT:
                return checkOp(context, 24);
            default:
                break;
        }
        return true;
    }

    /**
     * check by reflect
     */
    private static boolean checkOp(Context context, int op) {
        if (Build.VERSION.SDK_INT < KITKAT) {
            return true;
        }
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            return 0 == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
