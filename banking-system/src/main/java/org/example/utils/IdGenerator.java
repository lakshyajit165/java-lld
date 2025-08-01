package org.example.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(1);

    public static int generate() {
        return counter.getAndIncrement();
    }
}
