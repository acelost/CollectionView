package ru.acelost.collectionadapter.measurement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

public class Measurement {

    private static final String DEFAULT_TAG = "MEASUREMENT";
    private static final String DEFAULT_COUNTER_FORMAT = "{%s : %d}";

    private static Measurement sInstance;

    private final CounterManager mCounterManager = new CounterManager();

    @Nullable
    private Printer mPrinter;

    private Measurement() { }

    @NonNull
    public static Measurement getInstance() {
        if (sInstance == null) {
            synchronized (Measurement.class) {
                if (sInstance == null) {
                    sInstance = new Measurement();
                }
            }
        }
        return sInstance;
    }

    public static void drop() {
        synchronized (Measurement.class) {
            sInstance = null;
        }
    }

    @NonNull
    private Printer getPrinter() {
        if (mPrinter == null) {
            mPrinter = defaultPrinter();
        }
        return mPrinter;
    }

    @NonNull
    private Printer defaultPrinter() {
        return new Printer() {
            @Override
            public void print(@Nullable String text) {
                Log.i(DEFAULT_TAG, text);
            }
        };
    }

    public void setPrinter(@Nullable Printer printer) {
        mPrinter = printer;
    }

    // region Counter methods

    public int increment(Object key) {
        return increment(key, 1);
    }

    public int increment(Object key, int value) {
        return mCounterManager.change(key, value);
    }

    public int decrement(Object key) {
        return decrement(key, 1);
    }

    public int decrement(Object key, int value) {
        return mCounterManager.change(key, -value);
    }

    public void resetCounter(Object key) {
        mCounterManager.reset(key);
    }

    public void printCounters() {
        printCounters(DEFAULT_COUNTER_FORMAT);
    }

    public void printCounters(String format) {
        printCounters(getPrinter(), format);
    }

    public void printCounters(Printer printer) {
        printCounters(printer, DEFAULT_COUNTER_FORMAT);
    }

    public void printCounters(Printer printer, String format) {
        mCounterManager.print(printer, format);
    }

    // endregion

}
