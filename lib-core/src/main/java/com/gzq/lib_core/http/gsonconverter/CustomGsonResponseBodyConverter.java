package com.gzq.lib_core.http.gsonconverter;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.gzq.lib_core.http.exception.ErrorType;
import com.gzq.lib_core.http.exception.ServerException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;


public final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final JsonParser jsonParser;

    public CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
        jsonParser = new JsonParser();
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        JsonElement jsonElement = jsonParser.parse(response);
        JsonElement code = jsonElement.getAsJsonObject().get("code");
        int parseCode = -1000;
        if (code != null) {
            parseCode = code.getAsInt();
        }
        if (parseCode != ErrorType.SUCCESS) {
            value.close();
            JsonElement data = jsonElement.getAsJsonObject().get("data");
            String msg = null;
            if (data != null) {
                msg = data.getAsString();
            }
            if (msg != null) {
                throw new ServerException(msg, parseCode);
            }
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
