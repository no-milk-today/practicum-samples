package org.drm.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class MethodSourceExamples {

    @Nested
    class SimpleMethodSourceExample {
        @ParameterizedTest
        @MethodSource("provideIntegers")
        void testWithIntegers(int number) {
            System.out.println("Number: " + number);
        }

        private static Stream<Integer> provideIntegers() {
            return Stream.of(1, 2, 3, 4, 5);
        }
    }

    @Nested
    class MultiParameterExample {
        @ParameterizedTest
        @MethodSource("provideStringsAndIntegers")
        void testWithMultipleParameters(String string, int num) {
            System.out.println("String: " + string + ", Number: " + num);
            Assertions.assertTrue(num > 0);
        }

        private static Stream<Arguments> provideStringsAndIntegers() {
            return Stream.of(
                    Arguments.of("apple", 1),
                    Arguments.of("banana", 2),
                    Arguments.of("cherry", 3)
            );
        }
    }

    @Nested
    class ObjectMethodSourceExample {
        record Person(String name, Integer age) {
        }

        @ParameterizedTest
        @MethodSource("providePeople")
        void testWithObjects(Person person) {
            System.out.println("Name: " + person.name + ", Age: " + person.age);
        }

        static Stream<Person> providePeople() {
            return Stream.of(
                    new Person("Alice", 30),
                    new Person("Bob", 25),
                    new Person("Charlie", 35)
            );
        }
    }

    @Nested
    class MultipleSourcesExample {
        @ParameterizedTest
        @MethodSource({"provideStrings", "provideMoreStrings"})
        void testWithMultipleSources(String input) {
            System.out.println("Input: " + input);
        }

        private static Stream<String> provideStrings() {
            return Stream.of("first", "second", "third");
        }

        private static Stream<String> provideMoreStrings() {
            return Stream.of("fourth", "fifth");
        }
    }
}

