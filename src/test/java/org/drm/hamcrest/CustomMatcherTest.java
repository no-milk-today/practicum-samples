package org.drm.hamcrest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.drm.hamcrest.EvenNumbersMatcher.containsOnlyEvenNumbers;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomMatcherTest {
    @Test
    void testEvenNumbersInList() {
        List<Integer> numbers = Arrays.asList(2, 4, 6, 8);
        assertThat(numbers, containsOnlyEvenNumbers());
    }
}