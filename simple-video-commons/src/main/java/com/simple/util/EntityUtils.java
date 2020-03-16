package com.simple.util;

import cn.hutool.core.convert.Convert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: EntityUtils.java
 * @description: entity 工具类
 * @author: fan
 * @create: 2019-12-05 20:18
 **/
public class EntityUtils {

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = Convert.toStr(field.get(obj));
            map.put(fieldName, value);
        }
        return map;
    }
}
