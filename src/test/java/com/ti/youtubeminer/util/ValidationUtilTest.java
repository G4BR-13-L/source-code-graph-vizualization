package com.ti.youtubeminer.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.ti.youtubeminer.utils.ValidationUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ValidationUtilTest {

    @Test
    void testIsNotBlank() {
        assertTrue(isNotBlank("Hello"));
        assertTrue(isNotBlank("   Spaces   "));
        assertFalse(isNotBlank(""));
        assertFalse(isNotBlank("  "));
        assertFalse(isNotBlank(null));
    }

    @Test
    void testIsNotNull() {
        assertTrue(isNotNull("Hello"));
        assertTrue(isNotNull(42));
        assertTrue(isNotNull(0));
        assertFalse(isNotNull(null));
    }

    @Test
    void testIsEmpty() {
        List<String> emptyList = new ArrayList<>();
        List<String> nonEmptyList = List.of("item1", "item2");

        assertTrue(isEmpty(emptyList));
        assertFalse(isEmpty(nonEmptyList));
    }

    @Test
    void testIsNotEmpty() {
        List<String> emptyList = new ArrayList<>();
        List<String> nonEmptyList = List.of("item1", "item2");

        assertFalse(isNotEmpty(emptyList));
        assertTrue(isNotEmpty(nonEmptyList));
    }

    @Test
    void testIsBlank() {
        assertTrue(isBlank(""));
        assertTrue(isBlank("   "));
        assertFalse(isBlank("Hello"));
        assertFalse(isBlank("  Spaces  "));
        assertTrue(isBlank(null));
    }

    @Test
    void testIsNull() {
        assertTrue(isNull(null));
        assertFalse(isNull("Hello"));
        assertFalse(isNull(42));
        assertFalse(isNull(new Object()));
    }
}
