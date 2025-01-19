package org.drm.custom.annotation;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.LocalTime;

public class WorkHoursCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        // Извлекаем аннотацию
        EnabledDuringWorkHours workHours = context.getElement()
                .map(e -> e.getAnnotation(EnabledDuringWorkHours.class))
                .orElse(null);

        if (workHours == null) {
            return ConditionEvaluationResult.enabled("Аннотация @EnabledDuringWorkHours отсутствует");
        }

        // Получаем текущее время и проверяем, входит ли оно в указанный диапазон
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(workHours.startHour(), 0); // LocalTime a = LocalTime.of(12, 30); // 12:30
        LocalTime endTime = LocalTime.of(workHours.endHour(), 0);

        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
            return ConditionEvaluationResult.enabled("Текущий час входит в рабочее время");
        } else {
            return ConditionEvaluationResult.disabled("Текущий час не входит в рабочее время");
        }
    }
}
