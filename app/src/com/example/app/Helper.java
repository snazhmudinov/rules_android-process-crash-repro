package com.example.app;

import android.content.Context;

/**
 * Simple helper class. R8 full mode may inline, finalize, or repackage this.
 */
public class Helper {
    private static boolean initialized = false;

    public static void init(Context context) {
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
