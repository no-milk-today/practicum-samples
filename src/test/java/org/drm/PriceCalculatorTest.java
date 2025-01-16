package org.drm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    @Test
    void testCalculateTotalPrice() {
        PriceCalculator calculator = new PriceCalculator();
        double price = 10.0;
        int quantity = 5;

        double totalPrice = calculator.calculateTotalPrice(price, quantity);

        assertEquals(50.0, totalPrice, "Общая цена должна быть равна 50.0");
        assertTrue(totalPrice > 0, "Общая цена должна быть положительной");
        assertNotNull(calculator, "Объект калькулятора не должен быть null");
    }

    @Test
    void testCalculateTotalPriceCombined() {
        PriceCalculator calculator = new PriceCalculator();
        double price = 10.0;
        int quantity = 5;

        double totalPrice = calculator.calculateTotalPrice(price, quantity);

        assertAll("Проверка общей цены",
                () -> assertEquals(50.0, totalPrice, "Общая цена должна быть равна 50.0"),
                () -> assertTrue(totalPrice > 0, "Общая цена должна быть положительной"),
                () -> assertNotNull(calculator, "Объект калькулятора не должен быть null")
        );
    }
}

class PriceCalculator {
    public double calculateTotalPrice(double price, int quantity) {
        return price * quantity;
    }
}
