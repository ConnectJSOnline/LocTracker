package com.tbdautomations.loctracker;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sunikumar on 10/10/2016.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Josefin/JosefinSans-SemiBold.ttf")
                        .build());
    }
}
