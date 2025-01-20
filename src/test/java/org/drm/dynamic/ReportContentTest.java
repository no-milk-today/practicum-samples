package org.drm.dynamic;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportContentTest {

    @TestFactory
    Stream<DynamicTest> testGeneratedReportsContainSummary() throws Exception {
        // Указываем каталог, где хранятся динамически сгенерированные отчёты
        Path reportDirectory = Paths.get("src/test/resources/generatedReports");

        // Загружаем файлы из указанного каталога
        return Files.list(reportDirectory)
                .filter(Files::isRegularFile) // Обрабатываем только файлы
                .map(path -> DynamicTest.dynamicTest("Проверка содержимого отчёта: " + path.getFileName(),
                        () -> {
                            String content = Files.readString(path);
                            // Проверка, что файл содержит ключевую информацию
                            assertTrue(content.contains("Summary"), "Отчёт должен содержать 'Summary'");
                        }));
    }
}