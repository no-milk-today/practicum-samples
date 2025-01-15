package org.drm;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Калькулятор")
class CalculatorTest {

    private Calculator calculator;

    @BeforeAll
    static void setupAll() {
        System.out.println("Подготовка перед запуском всех тестов");
    }

    @BeforeEach
    void setup() {
        calculator = new Calculator();
        System.out.println("Подготовка перед каждым тестом");
    }

    @Test
    @DisplayName("Тест сложения двух чисел")
    void testAddition() {
        int result = calculator.add(2, 3);
        assertEquals(5, result, "Результат сложения 2 и 3 должен быть 5");
    }

    @Test
    @DisplayName("Тест вычитания двух чисел")
    void testSubtraction() {
        int result = calculator.subtract(5, 3);
        assertEquals(2, result, "Результат вычитания 3 из 5 должен быть 2");
    }

    @Test
    void testEvenNumber() {
        assertTrue(() -> 4 % 2 == 0, "4 должно быть чётным числом");
    }

    @Tag("critical")
    @TestFactory
    Stream<DynamicTest> dynamicEvenNumberTests() {
        return Stream.of(2, 4, 6, 8, 10)
                .map(number -> DynamicTest.dynamicTest(
                        "Проверка: " + number + " является чётным",
                        () -> assertEquals(0, number % 2)
                ));
    }

    @ParameterizedTest
    @ValueSource(strings = { "шалаш", "казак", "довод", "потоп" })
    void testPalindrome(String candidate) {
        assertTrue(Calculator.isPalindrome(candidate));
    }

    @Test
    void testDivisionByZero() {
        // Проверяем, что выбрасывается ArithmeticException при делении на ноль
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            int result = 10 / 0;
        });

        // Проверяем, что сообщение об ошибке соответствует ожидаемому
        assertEquals("/ by zero", exception.getMessage());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Очистка после каждого теста");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Очистка после выполнения всех тестов");
    }
}

class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Checks if a given string is a palindrome.
     * A palindrome is a word, phrase, number, or other sequence of characters
     * which reads the same backward as forward.
     *
     * @param candidate the string to check
     * @return true if the string is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String candidate) {
        int left = 0;
        int right = candidate.length() - 1;

        while (left < right) {
            if (candidate.charAt(left) != candidate.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }


}