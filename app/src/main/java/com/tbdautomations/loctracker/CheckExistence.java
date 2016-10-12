package com.tbdautomations.loctracker;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sunikumar on 10/10/2016.
 */

public class CheckExistence extends AsyncTask<String,Void,Boolean>
{
    public Handler.Callback callback ;

    @Override
    protected Boolean doInBackground(String... data) {
        try {

            /*
            URL url = new URL(String.format(Application.GatewayIP +"api/LocTracking/CheckAccount?Phone=%1s",data));
            StringRequest request = new StringRequest(Request.Method.POST, url.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String Response  = new String(response);
                    Boolean Stat = Boolean.parseBoolean(Response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppSingleton.getInstance(Application.context).addToRequestQueue(request,"com.tbdautomations.com.loctracker.pooling");
            */

            URL url = new URL(String.format(Application.GatewayIP +"api/LocTracking/CheckAccount?Phone=%1s",data));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            InputStream in = urlConnection.getInputStream();
            byte[] res = new byte[5];
            in.read(res);
            String Response  = new String(res);

            if(Response.length() > 4 && Response.charAt(4) == '\u0000')
            {
                Response = Response.replace(Response.charAt(4),' ').trim();
            }

            Boolean Stat = Boolean.parseBoolean(Response);
            return Stat;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {

        }
        return  false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(callback!=null) {
            Handler handler = new Handler(callback);
            Message msg = new Message();
            msg.obj = aBoolean;
            handler.sendMessage(msg);
        }
    }

}
