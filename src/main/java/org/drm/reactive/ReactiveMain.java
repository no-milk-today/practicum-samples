package org.drm.reactive;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class ReactiveMain {
    public static void main(String[] args) {
        executeExerciseExample();
    }

    private static void executeExerciseExample() {
        var iteratorPublisher = new IteratorPublisher<Integer>(List.of(1, 2, 3, 4, 5).iterator());
        iteratorPublisher.subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(3); // case 1
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext: " + item);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void executeFirstExample() {
        // Создаём источник с тремя элементами
        RandomIntegerPublisher publisher = new RandomIntegerPublisher(3);

        // Подписываемся на источник и описываем правила обработки сигналов от источника
        publisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                //s.request(3); case 1

                s.request(2); // case 2
                s.cancel();

                s.request(3); // Запросили всё, что есть
                s.request(1); // Просим ещё
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
