package com.ti.youtubeminer.utils;


import java.util.Collection;


public class ValidationUtil {

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean isNotNull(Object value) {
        return !isNull(value);
    }

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNull(Object value) {
        return value == null;
    }
}