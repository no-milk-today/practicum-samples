package org.drm.reactive;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import java.util.Random;

public class RandomIntegerPublisher implements Publisher<Integer> {

    private final Random random;
    private int count;

    public RandomIntegerPublisher(int count) {
        this.random = new Random();
        this.count = count;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> s) {
        s.onSubscribe(new Subscription() {
            private boolean isCancelled = false;

            @Override
            public void request(long n) {
                if (n <= 0) {
                    s.onError(new IllegalArgumentException("Запрашиваемое число должно быть положительным"));
                    return;
                }

                if (isCancelled) {
                    s.onError(new IllegalStateException("Подписка была отменена"));
                    return;
                }

                if (count <= 0) {
                    s.onError(new IllegalStateException("Источник завершён"));
                    return;
                }

                for (int i = 0; i < n && count > 0; i++) {
                    int randomInteger = random.nextInt(0, 100);
                    s.onNext(randomInteger);

                    count--;
                }

                if (count <= 0) {
                    s.onComplete();
                }
            }

            @Override
            public void cancel() {
                isCancelled = true;
            }
        });
    }
}
