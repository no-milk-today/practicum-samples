package org.drm.repeated;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConcurrentCounterTest {

    private int counter;

    @RepeatedTest(10)
    void testConcurrentIncrement() throws InterruptedException {
        counter = 0;

        Thread thread1 = new Thread(this::incrementCounter);
        Thread thread2 = new Thread(this::incrementCounter);

        thread1.start();
        thread2.start();

        // thread1.join() и thread2.join() заставляют main поток ждать,
        // пока thread1 и thread2 не завершат выполнение своих задач.
        // Это гарантирует, что оба потока завершат инкрементирование счетчика перед тем,
        // как будет выполнена проверка
        // assertEquals(2000, counter, "Ожидаемое значение counter: 2000, но получили другое.");.
        //
        // Проще говоря, join() позволяет синхронизировать выполнение потоков,
        // чтобы убедиться, что все потоки завершили свою работу перед продолжением выполнения программы.
        thread1.join();
        thread2.join();

        assertEquals(2000, counter, "Ожидаемое значение counter: 2000, но получили другое.");
    }

    void incrementCounter() {
        for (int i = 0; i < 1000; i++) {
            counter++;
        }
    }
}

