package org.drm;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;
import java.util.List;

public class ReactiveStreamsExample {

    public static void main(String[] args) {
        // Создаём Publisher, который будет испускать числа от 1 до 5
        MyPublisher publisher = new MyPublisher(List.of(1, 2, 3, 4, 5));
        // Создаём Subscriber, который будет получать данные
        MySubscriber subscriber = new MySubscriber();
        // Подписываемся на Publisher
        publisher.subscribe(subscriber);
    }

    // Publisher, который эмитирует данные из списка
    static class MyPublisher implements Publisher<Integer> {
        private final List<Integer> data;

        public MyPublisher(List<Integer> data) {
            this.data = data;
        }

        @Override
        public void subscribe(Subscriber<? super Integer> subscriber) {
            // Создаём Subscription и сообщаем подписчику о подписке
            MySubscription subscription = new MySubscription(subscriber, data);
            subscriber.onSubscribe(subscription);
        }
    }

    // Subscription, отвечающий за передачу данных и управление запросами
    static class MySubscription implements Subscription {
        private final Subscriber<? super Integer> subscriber;
        private final Iterator<Integer> iterator;
        private boolean cancelled = false;

        public MySubscription(Subscriber<? super Integer> subscriber, List<Integer> data) {
            this.subscriber = subscriber;
            this.iterator = data.iterator();
        }

        @Override
        public void request(long n) {
            int count = 0;
            try {
                // Передаём до n элементов
                while (count < n && iterator.hasNext() && !cancelled) {
                    Integer item = iterator.next();
                    subscriber.onNext(item);
                    count++;
                }
                // Если данные закончились, сообщаем об этом подписчику
                if (!iterator.hasNext() && !cancelled) {
                    subscriber.onComplete();
                }
            } catch (Throwable t) {
                subscriber.onError(t);
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
        }
    }

    // Subscriber, реализующий методы onSubscribe, onNext, onError и onComplete
    static class MySubscriber implements Subscriber<Integer> {
        private Subscription subscription;

        @Override
        public void onSubscribe(Subscription s) {
            this.subscription = s;
            System.out.println("Подписка установлена.");
            // Запрашиваем первый элемент
            s.request(1);
        }

        @Override
        public void onNext(Integer item) {
            System.out.println("Получено значение: " + item);
            // Запрашиваем следующий элемент после обработки текущего
            subscription.request(1);
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Ошибка: " + t.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Данные закончились.");
        }
    }
}

