package org.drm;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class OrderProducer {
    private int orderNumber = 1;
    private final BlockingQueue<String> queue;

    public OrderProducer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void produceOrders(int orders) {
        try {
            for (int i = 0; i < orders; i++) {
                Thread.sleep(1_000);
                String order = "Order #" + orderNumber++;
                System.out.println("Клиент добавил: " + order);
                queue.put(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class OrderConsumer {
    private final BlockingQueue<String> queue;

    public OrderConsumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    public void processOrders(int orders) {
        try {
            for (int i = 0; i < orders; i++) {
                String order = queue.take();
                Thread.sleep(5_000);
                System.out.println("Бариста завершил приготовление: " + order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class CoffeeShopSlow {

    public static void main(String[] args) {
        int orders = 10;
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        var producer = new OrderProducer(queue);
        var consumer = new OrderConsumer(queue);

        Thread producerThread = new Thread(() -> producer.produceOrders(orders));
        Thread consumerThread = new Thread(() -> consumer.processOrders(orders));

        long start = System.currentTimeMillis();

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        System.out.println("Время выполнения: " + (end - start) / 1000 + " с");
    }
}