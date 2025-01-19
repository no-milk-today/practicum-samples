package org.drm.custom.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(WorkHoursCondition.class)  // Подключаем условие
public @interface EnabledDuringWorkHours {
    int startHour() default 9;    // Начало рабочего времени
    int endHour() default 17;     // Конец рабочего времени
}

