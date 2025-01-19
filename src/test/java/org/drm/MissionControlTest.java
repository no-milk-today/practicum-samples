package org.drm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtendWith;

public class MissionControlTest {

    @Test
    @EnabledIf("isTestEnvironment")
    void testOnlyInTestEnv() {
        System.out.println("Тест выполняется только в тестовой среде.");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void notOnLinux() {
        System.out.println("Этот тест не выполняется на Windows.");
    }

    @Test
    @ExtendWith(MissionTypeCondition.class)
    void missionSpecificTest() {
        System.out.println("Тест выполняется только для конкретной миссии.");
    }

    private static boolean isTestEnvironment() {
        return "test".equals(System.getProperty("ENV"));
    }
}

class MissionTypeCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        boolean isMissionDay = java.time.LocalDate.now().getDayOfWeek().getValue() <= 5; // Запуск только в будние дни
        return isMissionDay
                ? ConditionEvaluationResult.enabled("Тест включён для будних дней")
                : ConditionEvaluationResult.disabled("Тест отключён на выходные");
    }
}