package org.drm.repeated;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ConditionalCheckWithRepetitionInfo {

    @RepeatedTest(5)
    void conditionalTest(RepetitionInfo repetitionInfo) {
        boolean result = runOperation();

        if (repetitionInfo.getCurrentRepetition() == repetitionInfo.getTotalRepetitions()) {
            // Строгая проверка на последнем повторении
            assertTrue(result, "На последнем повторении ожидается успех.");
        } else {
            // Более мягкая проверка на остальных повторениях
            assertFalse(result, "На этом повторении результат может быть произвольным.");
        }
    }

    boolean runOperation() {
        // Симулируем результат операции
        return Math.random() > 0.5;
    }
}

