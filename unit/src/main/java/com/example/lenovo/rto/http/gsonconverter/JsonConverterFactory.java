package com.example.lenovo.rto.http.gsonconverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by huyin on 16/7/13.
 */
public class JsonConverterFactory extends Converter.Factory {

     public static JsonConverterFactory create(Gson gson) {
          if (gson == null) throw new NullPointerException("gson == null");
          return new JsonConverterFactory(gson);
     }

     private final Gson gson;

     private JsonConverterFactory(Gson gson) {
          this.gson = gson;
     }

     @Override
     public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                             Retrofit retrofit) {
          return new JsonResponseBodyConverter<>(type);//响应
     }

     @Override
     public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                           Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
          TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
          return new JsonRequestBodyConverter<>(gson, adapter);//请求
     }
}
