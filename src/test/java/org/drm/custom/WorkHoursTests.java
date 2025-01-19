package org.drm.custom;

import org.drm.custom.annotation.EnabledDuringWorkHours;
import org.junit.jupiter.api.Test;

class WorkHoursTests {

    @Test
    @EnabledDuringWorkHours(startHour = 9, endHour = 17)
    void testOnlyDuringWorkHours() {
        System.out.println("Этот тест выполняется только в рабочее время (с 9:00 до 17:00).");
    }
}