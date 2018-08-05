package ru.acelost.collectionadapter.measurement;

import java.util.HashMap;
import java.util.Map;

class CounterManager {

    private Map<Object, Integer> mCounters = new HashMap<>();

    public int change(Object key, int value) {
        Integer current = mCounters.get(key);
        if (current == null) {
            current = 0;
        }
        int actual = current + value;
        mCounters.put(key, actual);
        return actual;
    }

    public void reset(Object key) {
        mCounters.remove(key);
    }

    public void print(Printer printer, String format) {
        StringBuilder builder = new StringBuilder();
        builder.append("COUNTERS (").append(mCounters.size()).append(")\n");
        for (Map.Entry<Object, Integer> entry : mCounters.entrySet()) {
            builder.append(String.format(format, entry.getKey(), entry.getValue())).append("\n");
        }
        printer.print(builder.toString());
    }

}
