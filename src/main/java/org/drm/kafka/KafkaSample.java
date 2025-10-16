package org.drm.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
Партиции в Apache Kafka — это не копии, а разные части (части) одного топика,
 и каждая партиция содержит свой уникальный набор сообщений.

* Топик — это как большая очередь сообщений.

* Чтобы ускорить обработку и масштабировать систему, очередь разбивается на несколько стопок — партиций.

* Каждая партиция — это своя собственная линейная последовательность сообщений, не пересекающаяся с другими партициями топика.

* Сообщения в топике распределяются между партициями по ключу или по алгоритму равномерного распределения, так что разные сообщения попадают в разные партиции.
 **/
public class KafkaSample {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<String, String>("visitors", "visitor1")); // простой send без разбиения на партиции

        for (int i=0; i<30; i++) { // Партиционирование сообщений
            producer.send(new ProducerRecord<String, String>("visitors", "key1", "visitor" + i + "_1"));
            producer.send(new ProducerRecord<String, String>("visitors", "key2", "visitor" + i + "_2"));
            producer.send(new ProducerRecord<String, String>("visitors", "key3", "visitor" + i + "_3"));
        }

        producer.close();
    }
}
