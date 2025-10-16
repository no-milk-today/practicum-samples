package org.drm.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * производитель не ждал подтверждения от брокера Kafka о доставке сообщений и сразу продолжал свою работу,
 * а в случае ошибки при отправке сообщения выводил в консоль строку “Не получилось отправить”.
 * Исполнение при этом не должно блокироваться.
 */
public class SimpleProducer {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // Производитель не должен ждать подтверждения доставки сообщения
        props.put("acks", 0);


        Producer<String, String> producer = new KafkaProducer<>(props);
        while (true) {
            Coordinates coordinates = getCoordinates();
            String messageText = coordinates.carId() + "/" + coordinates.x() + "/" + coordinates.y();
            producer.send(new ProducerRecord<String, String>("cars-placement", coordinates.carId(), messageText), (RecordMetadata record, Exception e) -> {
                if (e != null) {
                    System.out.println("Не получилось отправить");
                }
            });
        }
    }

    private static Coordinates getCoordinates() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}

record Coordinates (String carId, double x, double y) {}

