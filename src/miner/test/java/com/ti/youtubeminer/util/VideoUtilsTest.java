package com.ti.youtubeminer.util;

import com.ti.youtubeminer.utils.VideoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VideoUtilsTest {

        @Test
        public void testConvertToSeconds() {
            String durationString = "PT10H28M56S";
            BigInteger expectedSeconds = BigInteger.valueOf(10 * 3600 + 28 * 60 + 56);
            assertEquals(expectedSeconds, VideoUtils.convertToSeconds(durationString));
        }

        @Test
        public void testConvertToSecondsWithMinutesAndSeconds() {
            String durationString = "PT45M30S";
            BigInteger expectedSeconds = BigInteger.valueOf(45 * 60 + 30);
            assertEquals(expectedSeconds, VideoUtils.convertToSeconds(durationString));
        }

        @Test
        public void testConvertToSecondsWithHoursOnly() {
            String durationString = "PT2H";
            BigInteger expectedSeconds = BigInteger.valueOf(2 * 3600);
            assertEquals(expectedSeconds, VideoUtils.convertToSeconds(durationString));
        }

        @Test
        public void testConvertToSecondsWithHoursAndSeconds() {
            String durationString = "PT3H15S";
            BigInteger expectedSeconds = BigInteger.valueOf(3 * 3600 + 15);
            assertEquals(expectedSeconds, VideoUtils.convertToSeconds(durationString));
        }

        @Test
        public void testConvertToSecondsWithSecondsOnly() {
            String durationString = "PT50S";
            BigInteger expectedSeconds = BigInteger.valueOf(50);
            assertEquals(expectedSeconds, VideoUtils.convertToSeconds(durationString));
        }

        @Test
        public void testExtractHours() {
            String durationString = "PT10H28M56S";
            assertEquals(10, VideoUtils.extractHours(durationString));
        }

        @Test
        public void testExtractHoursWithZero() {
            String durationString = "PT45M30S";
            assertEquals(0, VideoUtils.extractHours(durationString));
        }

        @Test
        public void testExtractMinutes() {
            String durationString = "PT10H28M56S";
            assertEquals(28, VideoUtils.extractMinutes(durationString));
        }

        @Test
        public void testExtractMinutesWithZero() {
            String durationString = "PT3H15S";
            assertEquals(0, VideoUtils.extractMinutes(durationString));
        }

        @Test
        public void testExtractSeconds() {
            String durationString = "PT10H28M56S";
            assertEquals(56, VideoUtils.extractSeconds(durationString));
        }

        @Test
        public void testExtractSecondsWithZero() {
            String durationString = "PT3H15M";
            assertEquals(0, VideoUtils.extractSeconds(durationString));
        }
}
