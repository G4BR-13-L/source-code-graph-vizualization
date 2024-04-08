package com.ti.youtubeminer.utils;

import java.util.UUID;

public class HashUtils {

    public static String generateHash(){

        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
