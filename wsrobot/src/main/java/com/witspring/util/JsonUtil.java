package com.witspring.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static Gson gson;
	private static JsonUtil instance;

	public static JsonUtil getInstance() {
		if (instance == null) {
			instance = new JsonUtil();
		}
		return instance;
	}

	private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT).create();
        }
        return gson;
    }

	/**
	 * json数据转换成Map对象
	 * @param json json数据
	 * @return Map对象
	 */
	public static Map<String, Map<String, String[]>> json2Map(String json) {
		if (StringUtil.isTrimBlank(json)) {
			return null;
		}
		Type type = new TypeToken<Map<String, Map<String, String[]>>>() {
		}.getType();
		return getGson().fromJson(json, type);
	}

	/**
	 * json数据转换成任何对象
	 * @param json json数据
	 * @return 任何对象
	 */
	public static <T> T json2Any(String json, Type type) {
		try {
			return getGson().fromJson(json, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 把JSON转化为对象
	 * @param json json对象
	 * @return 返回的对象
	 */
	public static <T> T json2Obj(String json, Class<T> clazz) {
		try {
			return getGson().fromJson(json, clazz);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 把对象转成JSON数据
	 * @param object 对象
	 * @return json字符串
	 */
	public static String toJson(Object object) {
		return getGson().toJson(object);
	}
	
	public static String getString(JSONObject obj, String name) {
		if (obj.isNull(name)) {
			return null;
		} else {
			return obj.optString(name);
		}
	}

	public static int getInt(JSONObject obj, String name) {
		if (obj.isNull(name)) {
			return 0;
		} else {
			return obj.optInt(name);
		}
	}
	
	public static double getDouble(JSONObject obj, String name) {
		if (obj.isNull(name)) {
			return 0.0;
		} else {
			return obj.optDouble(name);
		}
	}

	public static boolean getBoolean(JSONObject obj, String name) {
		if (obj.isNull(name)) {
			return false;
		} else {
			return obj.optBoolean(name);
		}
	}
	
}
