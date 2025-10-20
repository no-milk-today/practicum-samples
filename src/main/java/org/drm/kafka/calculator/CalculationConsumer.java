package org.drm.kafka.calculator;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class CalculationConsumer {

    public static void main(String[] args) {
        // Настройка свойств для подключения к Kafka
        Properties props = new Properties();

        // Адрес Kafka-брокера
        props.setProperty("bootstrap.servers", "localhost:9092");

        // Идентификатор группы потребителей (consumer group)
        // Все consumer'ы с одинаковым group.id образуют группу и делят между собой партиции
        props.setProperty("group.id", "calc-consumer");

        // Десериализаторы для ключа и значения (преобразуют байты обратно в объекты)
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Уровень изоляции для чтения транзакционных сообщений
        // read_committed - читаем только зафиксированные (committed) сообщения
        // Это важно! Без этого consumer мог бы прочитать сообщения из незавершённых транзакций
        props.setProperty("isolation.level", "read_committed");

        // Создаём consumer с заданными настройками
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подписываемся на два топика одновременно
        // input-numbers - содержит все исходные числа
        // results - содержит результаты вычислений
        consumer.subscribe(Arrays.asList("input-numbers", "results"));

        // Бесконечный цикл для постоянного чтения сообщений
        while (true) {
            // Запрашиваем новые сообщения из Kafka
            // Duration.ofMillis(500) - максимальное время ожидания новых сообщений
            // Если за 500мс новых сообщений нет, возвращается пустая коллекция
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));

            // Обрабатываем каждое полученное сообщение
            for (ConsumerRecord<String, String> record : records) {
                // Выводим значение сообщения на экран
                // record содержит также: topic (имя топика), key (ключ), offset (смещение),
                // partition (номер партиции), timestamp (время создания)
                System.out.println(record.value());
            }

            // Примечание: commit offset'ов происходит автоматически
            // по умолчанию каждые 5 секунд (enable.auto.commit=true)
        }
    }
}