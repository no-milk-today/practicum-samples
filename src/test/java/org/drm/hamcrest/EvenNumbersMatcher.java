package org.drm.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class EvenNumbersMatcher extends TypeSafeMatcher<List<Integer>> {

    @Override
    protected boolean matchesSafely(List<Integer> list) {
        return list.stream().allMatch(num -> num % 2 == 0);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("список должен содержать только чётные числа");
    }

    @Override
    protected void describeMismatchSafely(List<Integer> list, Description mismatchDescription) {
        mismatchDescription.appendText("список содержит нечётные числа");
    }

    public static EvenNumbersMatcher containsOnlyEvenNumbers() {
        return new EvenNumbersMatcher();
    }
}
