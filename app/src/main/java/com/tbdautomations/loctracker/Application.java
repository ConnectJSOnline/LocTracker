package com.tbdautomations.loctracker;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sunikumar on 10/10/2016.
 */

public class Application extends android.app.Application {

    public static String    NickName = "";
    public static String    PhoneNo = "";
    public  static String GatewayIP = "";

    @Override
    public void onCreate() {
        super.onCreate();

        GatewayIP = getResources().getString(R.string.api_address);



        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Josefin/JosefinSans-SemiBold.ttf")
                        .build());
    }
}
