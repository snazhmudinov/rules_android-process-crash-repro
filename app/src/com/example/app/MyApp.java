package com.example.app;

import android.app.Application;
import android.content.Context;

/**
 * Application class with an overridden method that R8 full mode will finalize.
 */
public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // Custom initialization
        Helper.init(base);
    }
}
