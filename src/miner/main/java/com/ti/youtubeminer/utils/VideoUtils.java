package com.ti.youtubeminer.utils;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@UtilityClass
public class VideoUtils {

    public static BigInteger convertToSeconds(String durationString) {
        int hours = extractHours(durationString);
        int minutes = extractMinutes(durationString);
        int seconds = extractSeconds(durationString);

        BigInteger totalSeconds = BigInteger.valueOf(hours * 3600 + minutes * 60 + seconds);

        return totalSeconds;
    }

    public static int extractHours(String durationString) {
        return extractTimeUnit(durationString, 'H');
    }

    public static int extractMinutes(String durationString) {
        return extractTimeUnit(durationString, 'M');
    }

    public static int extractSeconds(String durationString) {
        return extractTimeUnit(durationString, 'S');
    }

    private static int extractTimeUnit(String durationString, char unit) {
        Pattern pattern = Pattern.compile("(\\d+)" + unit);
        Matcher matcher = pattern.matcher(durationString);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 0;
        }
    }
}