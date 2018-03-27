package com.example.han.referralproject.settting;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.han.referralproject.settting.bean.KeyWordBean;
import com.google.gson.Gson;

/**
 * Created by lenovo on 2018/3/21.
 */

public class SharedPreferencesUtils {
    /**
     * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
     * 同样调用getParam就能获取到保存在手机里面的数据
     * @author xiaanming
     */
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_keyword";
    private static final String BEAN_NAME = "KeyWordBean";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {
        Gson gson = new Gson();
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        } else if (BEAN_NAME.equals(type)) {
            editor.putString(key, gson.toJson(object));
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (BEAN_NAME.equals(type)) {
            return new Gson().fromJson(sp.getString(key, getKeyWordBeanString()), KeyWordBean.class);
        }
        return null;
    }

    public static String getKeyWordBeanString() {
        KeyWordBean bean = new KeyWordBean();
        bean.yueya = "我要测血压";
        return new Gson().toJson(bean);
    }
}
