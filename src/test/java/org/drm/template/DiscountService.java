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

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountService {

    public double calculateDiscount(String customerStatus, String region, String orderType) {
        if ("VIP".equals(customerStatus) && "US".equals(region) && "BULK".equals(orderType)) {
            return 0.2; // 20% скидка
        } else if ("REGULAR".equals(customerStatus) && "EU".equals(region) && "BULK".equals(orderType)) {
            return 0.1; // 10% скидка
        } else {
            return 0.0; // Без скидки для всех остальных случаев
        }
    }
}

class DiscountServiceTest {

    @TestTemplate
    @ExtendWith(DiscountInvocationContextProvider.class)
    void testCalculateDiscount(String customerStatus, String region, String orderType, double expectedDiscount) {
        DiscountService discountService = new DiscountService();
        double actualDiscount = discountService.calculateDiscount(customerStatus, region, orderType);
        assertEquals(expectedDiscount, actualDiscount, "Скидка должна быть равна " + expectedDiscount);
    }
}

// Поставщик контекста для шаблона теста
class DiscountInvocationContextProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        return Stream.of(
                new DiscountTestContext("VIP", "US", "BULK", 0.2),
                new DiscountTestContext("REGULAR", "EU", "BULK", 0.1),
                new DiscountTestContext("OTHER", "OTHER", "OTHER", 0.0)
        );
    }

    // Внутренний класс для контекста выполнения теста с параметрами
    private static final class DiscountTestContext implements TestTemplateInvocationContext, ParameterResolver {

        private final String customerStatus;
        private final String region;
        private final String orderType;
        private final double expectedDiscount;

        private DiscountTestContext(String customerStatus, String region, String orderType,
                                    double expectedDiscount) {
            this.customerStatus = customerStatus;
            this.region = region;
            this.orderType = orderType;
            this.expectedDiscount = expectedDiscount;
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return String.format("Проверка скидки: статус %s, регион %s, тип заказа %s", customerStatus, region, orderType);
        }

        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            Class<?> type = parameterContext.getParameter().getType();
            return (type == String.class && parameterContext.getIndex() < 3) || (type == double.class && parameterContext.getIndex() == 3);
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
            int index = parameterContext.getIndex();
            if (index == 0) return customerStatus;
            if (index == 1) return region;
            if (index == 2) return orderType;
            if (index == 3) return expectedDiscount;
            throw new IllegalArgumentException("Неподдерживаемый параметр");
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return List.of(this); // Возвращаем себя как ParameterResolver
        }
    }
}