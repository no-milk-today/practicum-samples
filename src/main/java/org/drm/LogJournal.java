package org.drm;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Журнал для упорядоченной записи объектов в журнал
 *
 * @param <T> тип записываемого объекта (при тестировании используйте {@link String},
 * представим что это журнал логов)
 */
public class LogJournal<T> {
    // Для хранения и получения информации подберите подходящий тип коллекции
    SequencedCollection<T> entries = new LinkedList<>();


    void logEntry(T entry) {
        entries.addFirst(entry);
    }

    /**
     * Получить последние актуальные записи в журнале
     *
     * @param limit число записей для получения
     * @return последние записи в журнале, ограниченные limit
     */

    List<T> getLastEntries(int limit) {
        return entries.stream()
                .limit(limit)
                .toList();
    }

    public static void main(String[] args) {
        var start = LocalDateTime.now();
        var journal = new LogJournal<String>();
        // Добавляем по очереди числа 1..99 а журнал

        IntStream.range(1, 100).boxed().map(Object::toString).forEach(journal::logEntry);
        // Последние десять записей должны быть - 90..99
        var expectedLastTenEntries = IntStream.range(90, 100).boxed().map(Object::toString).toList().reversed();

        // Получаем фактические 10 последних записей
        var actualLastTenEntries = journal.getLastEntries(10);

        // Проверяем соответствие последниъ 10 записей ожидаемым
        if (!actualLastTenEntries.equals(expectedLastTenEntries)) {
            throw new RuntimeException("Ой, записи не совпали. Ожидаемые - [90..99]. Фактические - " + journal.getLastEntries(10));
        } else System.out.println("Ура-ура-ура");
        System.out.println("Все задачи выполнены за " + start.until(LocalDateTime.now(), ChronoUnit.NANOS));
    }
}