package com.hyundai.softeer.backend.global.time;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TimeMeasurement {
    private static final ThreadLocal<ConcurrentHashMap<String, Long>> startTimes =
            ThreadLocal.withInitial(ConcurrentHashMap::new);
    private static final ThreadLocal<ConcurrentHashMap<String, Long>> endTimes =
            ThreadLocal.withInitial(ConcurrentHashMap::new);

    public static void start(String section) {
        startTimes.get().put(section, System.nanoTime());
    }

    public static void end(String section) {
        endTimes.get().put(section, System.nanoTime());
    }

    public static long getDurationMillis(String section) {
        Long start = startTimes.get().get(section);
        Long end = endTimes.get().get(section);
        if (start == null || end == null) {
            throw new IllegalArgumentException("Section not measured: " + section);
        }
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    public static void printDuration(String section) {
        System.out.printf("Thread %s - Section '%s' took %d ms%n",
                Thread.currentThread().getName(), section, getDurationMillis(section));
    }

    public static void reset() {
        startTimes.get().clear();
        endTimes.get().clear();
    }

    public static void remove() {
        startTimes.remove();
        endTimes.remove();
    }
}
