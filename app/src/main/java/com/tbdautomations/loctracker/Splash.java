package com.tbdautomations.loctracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        if(AlreadyRegistered())
        {
            CheckExistence chk = new CheckExistence();
            chk.callback = new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return  true;
                }
            };

            chk.execute(Application.PhoneNo);
        }
        else
        {
            Intent intent = new Intent(Splash.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public  boolean AlreadyRegistered(){

        SharedPreferences sharedPref = getSharedPreferences("LoctrackerSettings", Context.MODE_PRIVATE);
        if(sharedPref.contains("NickName")
                && sharedPref.getString("NickName",null) !=  null
                && sharedPref.contains("Phone")
                && sharedPref.getString("Phone", null) != null)
        {
            Application.NickName = sharedPref.getString("NickName",null);
            Application.PhoneNo = sharedPref.getString("Phone",null);
            return  true;
        }

        return  false;

    }
}
