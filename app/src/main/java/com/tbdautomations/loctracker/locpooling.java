package com.tbdautomations.loctracker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class locpooling extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public locpooling() {
        super("locpooling");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final Double Latitude = intent.getDoubleExtra("LAT",0);
            final Double Longitude = intent.getDoubleExtra("LONG",0);
            final String Phone = intent.getStringExtra("Phone");

            if(action=="SendLocation" && Latitude!=0 && Longitude != 0)
            {
                try {
                    URL url = new URL(String.format(Application.GatewayIP +"/api/LocTracking/SendLatLong?Lat=%1s&Long=%2s&Token=%3s",Latitude,Longitude,Application.PhoneNo));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    int Code = urlConnection.getResponseCode();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //urlConnection.disconnect();
                }

            }

            if(action=="CheckAccount" && Phone != null)
            {


            }
        }
    }
}
