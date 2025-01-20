package org.drm.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CsvSourceTests {

    @ParameterizedTest
    @CsvSource({
            "hello, HELLO",
            "java, JAVA",
            "JUnit, JUNIT"
    })
    void testToUpperCase(String input, String expected) {
        Assertions.assertEquals(expected, input.toUpperCase());
    }

    @ParameterizedTest
    @CsvSource({
            "10, 20, 30",
            "0, 0, 0",
            "-10, 5, -5"
    })
    void testSum(int a, int b, int expected) {
        Assertions.assertEquals(expected, a + b);
    }

    @ParameterizedTest
    @CsvSource({
            "apple, 1",
            "banana, 2",
            "cherry, 3"
    })
    void testWithCsvSource(String fruit, int rank) {
        System.out.println("Fruit: " + fruit + ", Rank: " + rank);
    }
}
