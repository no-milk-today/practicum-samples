package org.drm.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class UserProfileTest {

    @ParameterizedTest
    @MethodSource("provideUsers")
    void testUserProfile(User user) {
        Assertions.assertTrue(user.hasValidName(), "Имя не должно быть пустым или иметь значение null");
        Assertions.assertTrue(user.isValidAge(), "Возвраст должен быть от 18 до 60");
    }

    private static Stream<User> provideUsers() {
        return Stream.of(
                new User("Alice", 30),
                new User("Bob", 25),
                new User("Charlie", 35)
        );
    }
}

record User(String name, int age) {

    public boolean isValidAge() {
        return age >= 18 && age <= 60;
    }

    public boolean hasValidName() {
        return name != null && !name.trim().isEmpty();
    }
}
