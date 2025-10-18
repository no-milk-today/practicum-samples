package org.drm.kafka.mistery;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MysteryConsumer {

    public static void main(String[] args) {
        String baseConsumerName = args[0];
        Integer numberOfConsumers = Integer.parseInt(args[1]);

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", "mystery-consumers");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("auto.commit.interval.ms", "20000");

        ExecutorService executor = Executors.newFixedThreadPool(numberOfConsumers);
        for (int num = 1; num <= numberOfConsumers; num++) {
            String consumerName = baseConsumerName + "-" + num;
            executor.submit(() -> {
                // Потребитель непотокобезопасный, поэтому создаём каждого в своём потоке
                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Arrays.asList("kafka-mystery"));

                System.out.println("Создан потребитель с именем " + consumerName);

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        // Вычисляем разницу времени между отправкой и получением сообщения (в миллисекундах)
                        long timeToReceive = System.currentTimeMillis() - record.timestamp();
                        System.out.printf("Потребитель: %s, сообщение: %s, ключ: %s, номер партиции: %d, офсет: %d, время на доставку: %d%n",
                                consumerName, record.value(), record.key(), record.partition(), record.offset(), timeToReceive);
                    }
                }
            });
        }
    }
}
