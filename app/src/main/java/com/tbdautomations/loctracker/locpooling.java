package com.tbdautomations.loctracker;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


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

            if(action=="SendLocation" && Latitude!=0 && Longitude != 0)
            {
                try {
                    URL url = new URL(String.format(Application.GatewayIP +"/api/LocTracking/SendLatLong?Lat=%1s&Long=%2s&Token=%3s",Latitude,Longitude,Application.PhoneNo));
                    StringRequest request = new StringRequest(Request.Method.POST, url.toString(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    AppSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request,"com.tbdautomations.com.loctracker.pooling");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //urlConnection.disconnect();
                }
            }
        }
    }
}
