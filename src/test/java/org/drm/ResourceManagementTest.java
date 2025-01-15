package org.drm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith({ResourceInitializerExtension.class, ResourceAvailabilityChecker.class, TestExecutionTimingExtension.class })
class ResourceManagementTest {

    @Test
    void shouldPerformLongRunningTest() throws InterruptedException {
        Thread.sleep(500);
        System.out.println("Выполняется долгий тест.");
        assertTrue(true);
    }

    @Test
    void shouldPerformShortRunningTest() throws InterruptedException {
        Thread.sleep(300);
        System.out.println("Выполняется короткий тест.");
        assertTrue(true);
    }
}

class ResourceInitializerExtension implements BeforeAllCallback, AfterAllCallback {

    private ExternalResource resource;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("Инициализация ресурсов перед всеми тестами.");
        resource = new ExternalResource();
        //Нужно открыть ресурс
        resource.open();
        System.out.println("Ресурс инициализирован.");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("Очистка ресурсов после всех тестов.");
        //Нужно закрыть ресурс
        resource.close();
        System.out.println("Ресурс освобожден.");
    }
}

class ResourceAvailabilityChecker implements BeforeTestExecutionCallback {

    private final ExternalResource resource = new ExternalResource();

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        if (!resource.isAvailable()) {
            throw new RuntimeException("Ресурс недоступен для теста: " + context.getDisplayName());
        }
        System.out.println("Ресурс доступен для теста: " + context.getDisplayName());
    }
}

class TestExecutionTimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final Map<String, Long> testDurations = new HashMap<>();

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        long startTime = testDurations.get(context.getDisplayName());
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Тест завершен: " + context.getDisplayName() + ". Продолжительность: " + duration + " мс.");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("Начало теста: " + context.getDisplayName());
        testDurations.put(context.getDisplayName(), System.currentTimeMillis());
    }
}


class ExternalResource {

    private boolean available = true;

    public void open() {
        available = true;
        System.out.println("Внешний ресурс открыт.");
    }

    public void close() {
        available = false;
        System.out.println("Внешний ресурс закрыт.");
    }

    public boolean isAvailable() {
        return available;
    }
}
