package org.drm;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


class BasicStringCache {

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    // Добавить запись в кеш
    public void put(String key, String value) {
        cache.put(key, value);
    }

    // Получить запись из кеша
    public String get(String key) {
        return cache.get(key);
    }

    // Если запись отсутствует в кеше, то получить её из БД, вызвав переданный метод
    public String get(String key, Function<String, String> valueFromDb) {
        return cache.computeIfAbsent(key, valueFromDb);
    }

    // Удалить запись из кеша
    public void remove(String key) {
        cache.remove(key);
    }

    // Очистить весь кеш
    public void clear() {
        cache.clear();
    }

    // Получить количество записей в кеше
    public int size() {
        return cache.size();
    }
}

class TestCache {
    public static void main(String[] args) {
        var cache = new BasicStringCache();

        // Добавление записей в кеш
        cache.put("1", "One");
        cache.put("2", "Two");
        cache.put("3", "Three");

        // Проверка получения записей
        assert "One".equals(cache.get("1")) : "Key 1 should return 'One'";
        assert "Two".equals(cache.get("2")) : "Key 2 should return 'Two'";
        assert "Three".equals(cache.get("3")) : "Key 3 should return 'Three'";

        // Проверка получения с использованием функции (например, получение из "БД")
        String valueFromDb = cache.get("4", k -> "Four from DB");
        assert "Four from DB".equals(valueFromDb) : "Key 4 should return 'Four from DB'";

        // Проверка удаления записи
        cache.remove("2");
        assert cache.get("2") == null : "Key 2 should be null after removal";

        // Проверка текущего содержимого кеша
        assert cache.size() == 3 : "Cache should contain 3 items after removal";
        assert cache.get("1") != null : "Cache should contain key '1'";
        assert cache.get("3") != null : "Cache should contain key '3'";
        assert cache.get("4") != null : "Cache should contain key '4'";

        // Очистка кеша
        cache.clear();
        assert cache.size() == 0 : "Cache should be empty after clearing";

        System.out.println("All assertions passed!");
    }
}

