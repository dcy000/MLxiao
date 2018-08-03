package com.example.lenovo.rto.http.gson;

import com.example.lenovo.rto.http.gson.exception.JsonException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class GsonHelper {
    private static final GsonBuilder DESERIALIZER_BUILDER = new GsonBuilder();
    private static final GsonBuilder SERIALIZER_BUILDER = new GsonBuilder();
    private static Gson deserializer;
    private static Gson serializer;

    public static <T> String arrayToJson(T[] paramArrayOfT) {
        return getSerializer().toJson(paramArrayOfT);
    }

    public static Gson getDeserializer() {
        if (deserializer == null) {
            deserializer = DESERIALIZER_BUILDER.create();
        }
        return deserializer;
    }

    public static Gson getSerializer() {
        if (serializer == null) {
            serializer = SERIALIZER_BUILDER.create();
        }
        return serializer;
    }

    public static <T> T jsonToArray(String paramString, Class<T> paramClass) {
        try {
            return getDeserializer().fromJson(paramString, paramClass);
        } catch (Exception localException) {
            //FCLog.e(JsonMapper.class, localException);
            return null;
        }
    }

    public static <T> List<T> jsonToList(String paramString,
                                         TypeToken<List<T>> paramTypeToken) {
        try {
            return (List) getDeserializer().fromJson(paramString,
                    paramTypeToken.getType());
        } catch (Exception localException) {
            //FCLog.e(JsonMapper.class, localException);
            return null;
        }
    }

    public static <K, V> Map<K, V> jsonToMap(String paramString,
                                             TypeToken<Map<K, V>> paramTypeToken) {
        return (Map<K, V>) getDeserializer().fromJson(paramString,
                paramTypeToken.getType());
    }

    public static <T> String listToJson(List<T> paramList) {
        return getSerializer().toJson(paramList);
    }

    public static <K, V> String mapToJson(Map<K, V> paramMap) {
        return getSerializer().toJson(paramMap);
    }

    public static <T extends IJsonable> T parseJsonObject(
            JsonElement paramJsonElement, Class<T> paramClass)
            throws JsonException {
        try {
            IJsonable localIJsonable = (IJsonable) getDeserializer().fromJson(
                    paramJsonElement, paramClass);
            return (T) localIJsonable;
        } catch (Throwable localThrowable) {
            throw new JsonException("json=" + paramJsonElement, localThrowable);
        }
    }

    public static <T extends IJsonable> T parseJsonObject(String paramString,
                                                          Class<T> paramClass) throws JsonException {
        try {
            IJsonable localIJsonable = (IJsonable) getDeserializer().fromJson(
                    paramString, paramClass);
            return (T) localIJsonable;
        } catch (Throwable localThrowable) {
            //FCLog.e("JsonException", localThrowable);
            throw new JsonException("json=" + paramString, localThrowable);
        }
    }

    public static <T extends IJsonable> T parseJsonObject(
            JSONObject paramJSONObject, Class<T> paramClass)
            throws JsonException {
        return parseJsonObject(paramJSONObject.toString(), paramClass);
    }

    public static <T> T readValue(String paramString, Class<T> paramClass) {
        return getDeserializer().fromJson(paramString, paramClass);
    }

    public static <T> void registerDeserializer(Class<T> paramClass,
                                                JsonDeserializer<T> paramJsonDeserializer) {

        DESERIALIZER_BUILDER.registerTypeAdapter(paramClass,
                paramJsonDeserializer);
        deserializer = DESERIALIZER_BUILDER.create();

    }

    public static <T extends IJsonable> String toJson(T paramT) {
        return getSerializer().toJson(paramT);
    }

    public static <T extends IJsonable> JSONObject toJsonObject(T paramT)
            throws JsonException {
        try {
            JSONObject localJSONObject = new JSONObject(toJson(paramT));
            return localJSONObject;
        } catch (Throwable localThrowable) {
            throw new JsonException(localThrowable);
        }
    }

    public static String writeValue(Object paramObject) {
        return getSerializer().toJson(paramObject);
    }
}