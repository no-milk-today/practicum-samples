package org.drm.template;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiService {
    private final String apiUrl;
    private final String authToken;

    public ApiService(String apiUrl, String authToken) {
        this.apiUrl = apiUrl;
        this.authToken = authToken;
    }

    public boolean connect() {
        // Логика подключения к API
        System.out.println("Подключение к " + apiUrl + " с токеном " + authToken);
        return apiUrl.startsWith("https://"); // Для примера, успешное подключение только для https
    }
}

class EnvironmentBasedTests {

    @TestTemplate
    @ExtendWith(EnvironmentInvocationContextProvider.class)
    void testApiConnection(ApiService apiService) {
        // Проверка успешного подключения
        boolean isConnected = apiService.connect();
        assertTrue(isConnected, "Подключение должно быть успешным");
    }
}

// Поставщик контекста для тестов с разными окружениями
class EnvironmentInvocationContextProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(
                new EnvironmentTestContext("dev"),
                new EnvironmentTestContext("test"),
                new EnvironmentTestContext("prod")
        );
    }

    // Внутренний класс для контекста теста, создающего ApiService на основе окружения
    private static class EnvironmentTestContext implements TestTemplateInvocationContext, ParameterResolver {
        private final String environment;

        EnvironmentTestContext(String environment) {
            this.environment = environment;
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return "Тест окружения: " + environment;
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return parameterContext.getParameter().getType() == ApiService.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            // Получаем параметры из переменных окружения или системных свойств
            String apiUrl = System.getenv(environment.toUpperCase() + "_API_URL");
            String authToken = System.getenv(environment.toUpperCase() + "_AUTH_TOKEN");

            // Если переменные окружения не заданы, пробуем использовать системные свойства
            if (apiUrl == null) {
                apiUrl = System.getProperty(environment.toLowerCase() + ".api.url", "https://default.url.com");
            }
            if (authToken == null) {
                authToken = System.getProperty(environment.toLowerCase() + ".auth.token", "default_token");
            }

            // Создаем и возвращаем ApiService с полученными параметрами
            return new ApiService(apiUrl, authToken);
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return List.of(this);
        }
    }
}
