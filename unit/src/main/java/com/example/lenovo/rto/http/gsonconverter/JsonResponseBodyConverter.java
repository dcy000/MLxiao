package com.example.lenovo.rto.http.gsonconverter;



import com.example.lenovo.rto.http.code.ErrorCode;
import com.example.lenovo.rto.http.gson.GsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by huyin on 16/7/13.
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

     private final Type type;

     public JsonResponseBodyConverter(Type type) {
          this.type = type;
     }

     /**
      * 转换
      *
      * @param responseBody
      * @return
      * @throws IOException
      */
     @Override
     public T convert(ResponseBody responseBody) throws IOException {
          BufferedSource bufferedSource = Okio.buffer(responseBody.source());
          String tempStr = bufferedSource.readUtf8();
          bufferedSource.close();
          JSONObject jsonObject = null;
          T t = null;
          try {
               jsonObject = new JSONObject(tempStr);
               if (jsonObject.has("error_code")) {
                    ErrorCode.errorCode = Integer.valueOf(jsonObject.getString("error_code"));
                    ErrorCode.handleHeaderCode(ErrorCode.errorCode);
               } else if (jsonObject.has("result")) {
                    t = GsonHelper.getDeserializer().fromJson(jsonObject.getString("result"), type);
               } else {
                    t = GsonHelper.getDeserializer().fromJson(jsonObject.toString(), type);
               }
          } catch (JSONException e) {
               e.printStackTrace();
          }
          return t;

     }
}