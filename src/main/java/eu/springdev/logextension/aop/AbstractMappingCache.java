package eu.springdev.logextension.aop;

import org.apache.commons.collections4.map.LRUMap;

import java.util.function.Function;

public abstract class AbstractMappingCache<RAW_KEY, KEY, VALUE> {

    private static final int DEFAULT_MAX_SIZE = 1000;
    private final LRUMap<KEY, VALUE> cache;

    AbstractMappingCache() {
        this.cache = new LRUMap<>(DEFAULT_MAX_SIZE);
    }

    protected AbstractMappingCache(int maxSize) {
        this.cache = new LRUMap<>(maxSize);
    }

    public VALUE get(RAW_KEY rawKey) {
        KEY key = computeKey(rawKey);
        return cache.computeIfAbsent(key, computeValueInternal(rawKey));
    }

    private Function<KEY, VALUE> computeValueInternal(RAW_KEY rawKey) {
        return computedKey -> {
            VALUE computedValue = computeValue(rawKey, computedKey);
            doIfComputed(rawKey, computedKey, computedValue);
            return computedValue;
        };
    }

    protected abstract KEY computeKey(RAW_KEY rawKey);

    protected abstract VALUE computeValue(RAW_KEY rawKey, KEY key);

    protected void doIfComputed(RAW_KEY rawKey, KEY computedKey, VALUE computedValue) {

    }
}
