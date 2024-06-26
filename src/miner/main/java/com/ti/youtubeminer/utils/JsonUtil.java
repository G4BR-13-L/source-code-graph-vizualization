package com.ti.youtubeminer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ti.youtubeminer.config.BootstrapOnReady;
import lombok.experimental.UtilityClass;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class JsonUtil {


    public <T> List<T> objectListFromJson(String dataGroup, String pathResource, Type listType) {
        List<T> list = null;
        try {
            String json = readResourceJson(dataGroup, pathResource);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            list = gson.fromJson(json, listType);
        } catch (JSONException exception) {
            throw new RuntimeException(exception);
        }
        return list;
    }

    public <T> T objectFromJson(String dataGroup, String pathResource, Type type) {
        String json = readResourceJson(dataGroup, pathResource);
        Gson gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return gson.fromJson(json, type);
    }


    public String readResourceJson(String dataGroup, String pathResource) {
        JSONObject jsonDataObject = null;
        try {
            InputStream stream = BootstrapOnReady.class.getResourceAsStream(pathResource);
            jsonDataObject = new JSONObject(StreamUtils.copyToString(stream, StandardCharsets.UTF_8));
            return jsonDataObject.get(dataGroup).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}