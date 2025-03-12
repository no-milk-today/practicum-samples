package org.drm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

interface Counter {
    void increment();
    int getValue();
}

class SimpleCounter implements Counter {

    private int count = 0;

    public void increment() {
        count++;
    }

    public int getValue() {
        return count;
    }
}


class CounterTest {

    public static void main(String[] args) {
        Counter counter = new SimpleCounter();

        int expectedLikes = 100_000_000; //100 млн взято специально, чтобы долго выполнялся код и мы видели результаты
        int runnableThreads = 10;

        Runnable likeTask = () -> {
            for (int i = 0; i < expectedLikes; i++) {
                counter.increment();
            }
        };
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        // Запускаем 10 потоков
        IntStream.range(0, runnableThreads).forEach(i -> executor.submit(likeTask));
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        // Выводим итоговое количество лайков
        System.out.println("Время выполнения: " + (end - start) + " мс");
        System.out.println("Итоговое количество лайков: " + counter.getValue());
        System.out.println("Ожидаемое количество лайков: " + expectedLikes*runnableThreads);


    }
}