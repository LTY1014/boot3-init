package com.lty.util;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

/**
 * json字符串处理
 * @author lty
 */
public class GsonUtil {

    private static Gson gson = null;

    // 判断gson对象是否存在了,不存在则创建对象
    static {
        if (gson == null) {
            // gson = new Gson();
            // 当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
        }
    }

    private GsonUtil() {
    }

    /**
     * bean转成json字符串
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String beanToJson(T bean) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(bean);
        }
        return gsonString;
    }

    /**
     * json字符串转成对象
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            // 传入json对象和对象类型,将json转成对象
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * json字符串转成list<Class>
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String gsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        if (gson != null) {
            JsonParser parser = new JsonParser();
            JsonArray jsonarray = parser.parse(gsonString).getAsJsonArray();
            for (JsonElement element : jsonarray) {
                list.add(gson.fromJson(element, cls));
            }
        }
        return list;
    }

    /**
     * list<Class>转为json字符串
     *  @return jsons
     */
    public static <T> String listToJson(List<T> ts) {
        String jsons = gson.toJson(ts);
        return jsons;
    }

}
