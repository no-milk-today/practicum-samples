package org.drm.parameterized;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class StringTests {

    @ParameterizedTest
    @ValueSource(strings = {"привет", "JUnit", "параметризованный"})
    void testStringIsNotEmpty(String input) {
        assertFalse(input.isEmpty(), "Строка не должна быть пустой");
    }
}
