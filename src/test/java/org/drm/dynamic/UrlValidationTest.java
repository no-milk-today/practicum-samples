package org.drm.dynamic;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlValidationTest {

    @TestFactory
    Stream<DynamicTest> testUrlsFromFiles() throws Exception {
        var urlDirectory = Paths.get("src/test/resources/urls");

        return Files.list(urlDirectory)
                .filter(Files::isRegularFile)
                .map(path -> {
                    // Добавьте DynamicTest для проверки каждого файла
                    return DynamicTest.dynamicTest("Проверка содержимого файла: " + path.getFileName(),
                            () -> {
                                String content = Files.readString(path).trim();
                                assertTrue(content.startsWith("http"), "Контент должен начинаться с 'http'");
                            });
                });
    }
}
