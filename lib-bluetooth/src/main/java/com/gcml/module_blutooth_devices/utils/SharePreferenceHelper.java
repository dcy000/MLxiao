package com.gcml.module_blutooth_devices.utils;


import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 利用反射获取主项目中的SharePreference
 */
public class SharePreferenceHelper {
    private static volatile SharePreferenceHelper instance;

    private SharePreferenceHelper() {
    }

    private static volatile Object localShared;

    public static SharePreferenceHelper getInstance() {
        if (instance == null) {
            synchronized (SharePreferenceHelper.class) {
                if (instance == null) {
                    instance = new SharePreferenceHelper();
                }
            }
        }
        return instance;
    }

    public SharePreferenceHelper getLocalShared(Context context) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (localShared == null) {
            synchronized (SharePreferenceHelper.class) {
                if (localShared == null) {
                    Class<?> aClass = Class.forName("com.example.han.referralproject.util.LocalShared");
                    Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(Context.class);
                    declaredConstructor.setAccessible(true);
                    localShared = declaredConstructor.newInstance(context);
                }
            }
        }
        return instance;
    }

    public void setXueyangMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setXueyangMac = localShared.getClass().getMethod("setXueyangMac", String.class);
        setXueyangMac.invoke(localShared, string);
    }

    public String getXueyangMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getXueyangMac = localShared.getClass().getMethod("getXueyangMac", new Class[0]);
        Object invoke = getXueyangMac.invoke(localShared);
        return (String) invoke;
    }


    public void setXueyaMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setXueyaMac = localShared.getClass().getMethod("setXueyaMac", String.class);
        setXueyaMac.invoke(localShared, string);
    }

    public String getXueyaMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getXueyaMac = localShared.getClass().getMethod("getXueyaMac", new Class[0]);
        Object invoke = getXueyaMac.invoke(localShared);
        return (String) invoke;

    }


    public void setXuetangMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setXuetangMac = localShared.getClass().getMethod("setXuetangMac", String.class);
        setXuetangMac.invoke(localShared, string);
    }

    public String getXuetangMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getXuetangMac = localShared.getClass().getMethod("getXuetangMac", new Class[0]);
        Object invoke = getXuetangMac.invoke(localShared);
        return (String) invoke;
    }


    public void setWenduMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setWenduMac = localShared.getClass().getMethod("setWenduMac", String.class);
        setWenduMac.invoke(localShared, string);
    }

    public String getWenduMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getWenduMac = localShared.getClass().getMethod("getWenduMac", new Class[0]);
        Object invoke = getWenduMac.invoke(localShared);
        return (String) invoke;
    }

    public void setTizhongMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setTizhongMac = localShared.getClass().getMethod("setTizhongMac", String.class);
        setTizhongMac.invoke(localShared, string);
    }

    public String getTizhongMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getTizhongMac = localShared.getClass().getMethod("getTizhongMac", new Class[0]);
        Object invoke = getTizhongMac.invoke(localShared);
        return (String) invoke;
    }
    public String getXindianMac() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getXinDianMac = localShared.getClass().getMethod("getXinDianMac", new Class[0]);
        Object invoke = getXinDianMac.invoke(localShared);
        return ((String) invoke);
    }
    public void setXindianMac(String string) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setXinDianMac = localShared.getClass().getMethod("setXinDianMac", String.class);
        setXinDianMac.invoke(localShared,string);
    }
    public String getUserHeight() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getUserHeight = localShared.getClass().getMethod("getUserHeight", new Class[0]);
        Object invoke = getUserHeight.invoke(localShared);
        return (String) invoke;
    }
}
