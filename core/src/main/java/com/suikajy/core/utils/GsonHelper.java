package com.suikajy.core.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suikajy on 2017/4/7.
 * Gson数据转化处理
 */

public class GsonHelper {

    private static Gson sGson = new Gson();
    private static JsonParser sJsonParser = new JsonParser();

    private GsonHelper() {
    }

    /**
     * 返回json转化的单个对象
     */
    public static <T> T convertEntity(String jsonData, Class<T> entityClass) {
        T entity = null;
        try {
            entity = sGson.fromJson(jsonData, entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 返回json转化的对象集合
     */
    public static <T> List<T> convertEntities(@NonNull String jsonData, Class<T> entityClass) {
        List<T> entites = new ArrayList<>();
        try {
            JsonArray jsonArray = sJsonParser.parse(jsonData).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                entites.add(sGson.fromJson(element, entityClass));
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return entites;
    }

    /**
     * 将object转化为json对象
     */
    public static String object2JsonStr(Object jsonObject) {
        return sGson.toJson(jsonObject);
    }
}
