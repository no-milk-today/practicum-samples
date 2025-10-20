package org.drm.kafka.calculator;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

public class CalculatorProducer {

    public static void main(String[] args) {
        System.out.println("Вас приветствует самый умный Kafka-калькулятор");

        Properties props = new Properties();

        // Адрес Kafka-брокера
        props.put("bootstrap.servers", "localhost:9092");

        // Уникальный идентификатор для транзакционного producer
        // Необходим для обеспечения exactly-once семантики
        props.put("transactional.id", "calculator-producer-id");

        // Таймаут транзакции - максимальное время на выполнение транзакции
        // Если транзакция не будет зафиксирована в этот срок, она будет автоматически откатана
        props.put("transaction.timeout.ms", "300000");

        // Сериализаторы для ключа и значения (преобразуют объекты в байты)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Отключаем автоматическое обновление метаданных топиков
        // -1 означает, что обновление происходит только при необходимости
        props.put("topic.metadata.refresh.interval.ms", "-1");

        // Создаём producer с заданными настройками
        Producer<String, String> producer = new KafkaProducer<>(props);

        // Инициализируем транзакции - обязательно для транзакционного producer
        producer.initTransactions();

        Scanner scanner = new Scanner(System.in);

        // Бесконечный цикл для обработки пользовательского ввода
        while (true) {
            try {
                // Начинаем новую транзакцию
                // Все операции внутри транзакции будут атомарными:
                // либо все сообщения отправятся, либо ни одно
                producer.beginTransaction();

                // Запрашиваем у пользователя число
                System.out.println("Введите исходное значение:");
                Integer inputNumber = scanner.nextInt();
                scanner.nextLine(); // Очищаем буфер после nextInt()

                // Вычисляем ключ партиции: последняя цифра числа (0-9)
                // Это обеспечивает распределение сообщений по партициям
                String key = String.valueOf(inputNumber % 10);

                // Отправляем исходное число в топик input-numbers
                // Это сохраняет историю всех входных данных
                producer.send(new ProducerRecord<>("input-numbers", key, String.valueOf(inputNumber)));

                // Запрашиваем операцию для выполнения
                System.out.println("Введите операцию и нажмите enter:");
                String operation = scanner.nextLine();

                // Выполняем запрошенную операцию
                Integer result = switch (operation) {
                    case "sq" -> inputNumber * inputNumber;      // Возведение в квадрат
                    case "abs" -> Math.abs(inputNumber);         // Абсолютное значение
                    default -> throw new IllegalArgumentException("Невалидная операция");
                };

                // Формируем результирующее сообщение в формате: "число операция результат"
                String resultMessage = inputNumber + " " + operation + " " + result;

                // Отправляем результат в топик results
                // Используем тот же ключ для сохранения порядка сообщений в партиции
                producer.send(new ProducerRecord<>("results", key, resultMessage));

                // Фиксируем транзакцию - все сообщения будут записаны
                // Важно: если commitTransaction() не вызван, сообщения не появятся в топиках
                producer.commitTransaction();

            } catch (Exception e) {
                // При любой ошибке откатываем транзакцию
                // Это гарантирует, что если операция не выполнилась,
                // входное число также не сохранится в input-numbers
                System.out.println("Ошибка: " + e.getMessage());
                producer.abortTransaction();
            }
        }
    }
}