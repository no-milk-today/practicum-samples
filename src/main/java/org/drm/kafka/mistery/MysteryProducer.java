package org.drm.kafka.mistery;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

// bin/kafka-topics.sh --create  --topic kafka-mystery --partitions 5 --bootstrap-server localhost:9092
public class MysteryProducer {

    public static void main(String[] args) {
        String baseProducerName = args[0];
        Integer numberOfProducers = Integer.parseInt(args[1]);

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // чтобы Producers максимально активно использовали буферизацию
/*        props.setProperty("batch.size", "131072"); // 128 kb
        props.setProperty("linger.ms", "2000"); // 2 sec*/
        props.setProperty("auto.commit.interval.ms", "20000");

        // Производитель потокобезопасный, поэтому создаём один экземпляр на все потоки
        Producer<String, String> producer = new KafkaProducer<>(props);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfProducers);
        for (int num = 1; num <= numberOfProducers; num++) {
            String producerName = baseProducerName + "-" + num;
            executor.submit(() -> {
                System.out.println("Создан производитель с именем " + producerName);

                while (true) {
                    try {
                        // Генерируем индекс для ключа, от 1 до 10
                        Integer keyIdx = ThreadLocalRandom.current().nextInt(0, 10) + 1;
                        // Создаём ключ
                        String messageKey = "key" + keyIdx;

                        // Генерируем магическое число от 0 до 100
                        Integer magicNumber = ThreadLocalRandom.current().nextInt(0, 100);
                        // Собираем сообщение
                        String messageValue = producerName + " " + magicNumber;

                        // Создаём сообщение. Передаём null в качестве номера партиции - она должна определяться по ключу
                        ProducerRecord<String, String> message =
                                new ProducerRecord<>("kafka-mystery", null, System.currentTimeMillis(), messageKey, messageValue);
                        producer.send(message);

                        // Определяем паузу перед отправкой следующего сообщения - от 0 до 2 секунд
                        Integer timeToWait = ThreadLocalRandom.current().nextInt(0, 2000);
                        Thread.sleep(timeToWait);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
