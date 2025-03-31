package org.drm.reactive;

import java.util.Iterator;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class IteratorPublisher<T> implements Publisher<T> {

    private final Iterator<T> source;

    public IteratorPublisher(Iterator<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new Subscription() {
            private boolean isCancelled = false;

            @Override
            public void request(long n) {
                if (n <= 0) {
                    subscriber.onError(new IllegalArgumentException("Запрос должен быть больше 0"));
                    return;
                }

                if (isCancelled) {
                    subscriber.onError(new IllegalStateException("Подписка отменена"));
                    return;
                }

                while (source.hasNext() && n > 0) {
                    subscriber.onNext(source.next());
                    n--;
                }

                if (!source.hasNext()) {
                    subscriber.onComplete();
                }
            }

            @Override
            public void cancel() {
                isCancelled = true;
            }
        });
    }
}