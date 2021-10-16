package com.apple.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Program: telecom-CustomerService
 * @ClassName: LRUCache
 * @Description: TODO
 * @Author Mr.Apple
 * @Create: 2021-10-08 22:11
 * @Version 1.1.0
 **/
public class LRUCache extends LinkedHashMap<String, Integer> {

    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxElements = maxSize;
    }

    /**
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
        return (size() > this.maxElements);
    }
}
