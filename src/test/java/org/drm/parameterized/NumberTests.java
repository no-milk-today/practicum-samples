package org.drm.parameterized;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberTests {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testNumberIsPositive(int number) {
        assertTrue(number > 0, "Число должно быть положительным");
    }
}

