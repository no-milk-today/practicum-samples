package org.drm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

class EnvironmentVariableConditionalTests {

    @Test
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "CI")
    void onlyOnCIEnvironment() {
        System.out.println("Этот тест запускается только в окружении CI");
    }
}

