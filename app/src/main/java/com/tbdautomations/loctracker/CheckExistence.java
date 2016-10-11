package com.tbdautomations.loctracker;

import android.app.*;
import android.content.res.Resources;
import android.icu.text.Replaceable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodSession;

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
            Thread.sleep(3000);
            return Stat;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

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
