package com.dou.home.report;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConvertToHashMap {

    public static final Map<String, Object> convert(Class clazz, Object instance) {
        Map<String, Object> resultMap = new HashMap<>();

        if (clazz == null || instance == null) {
            return null;
        }

        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                try {
                    if (annotation instanceof MapParse) {
                        MapParse mapParse = (MapParse) annotation;
                        String keyName = mapParse.value();
                        Object val = getVal(clazz, field, instance);
                        if (val != null) {
                            resultMap.put(keyName, val);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        return resultMap;
    }

    private static Object getVal(Class clazz, Field field, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    try {
                        return method.invoke(instance);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

}
