package org.drm.template;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiRequestTest {

    //Создайте шаблонный тест и добавьте расширение
    @TestTemplate
    @ExtendWith(ApiInvocationContextProvider.class)
    void testApiRequest(String apiUrl, String httpMethod) {
        // Проверка, что URL начинается с "https://"
        assertTrue(apiUrl.startsWith("https://"), "URL должен начинаться с 'https://'");
        // Проверка, что метод поддерживается (GET или POST)
        assertTrue(httpMethod.equals("GET") || httpMethod.equals("POST"), "Метод должен быть GET или POST");
    }
}

// Поставщик контекста для различных конфигураций API
class ApiInvocationContextProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(
                new ApiTestTemplateInvocationContext("https://api.example.com/resource1", "GET"),
                new ApiTestTemplateInvocationContext("https://api.example.com/resource2", "POST"),
                new ApiTestTemplateInvocationContext("https://api.example.com/resource3", "GET")
        );
    }


    private static final class ApiTestTemplateInvocationContext implements TestTemplateInvocationContext, ParameterResolver {
        private final String apiUrl;
        private final String httpMethod;

        private ApiTestTemplateInvocationContext(String apiUrl, String httpMethod) {
            this.apiUrl = apiUrl;
            this.httpMethod = httpMethod;
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return "Тест API: " + apiUrl + " [" + httpMethod + "]";
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return (parameterContext.getParameter().getType().equals(String.class));
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            return parameterContext.getIndex() == 0 ? apiUrl : httpMethod;
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return List.of(this); // Возвращаем контекст как ParameterResolver
        }
    }
}
