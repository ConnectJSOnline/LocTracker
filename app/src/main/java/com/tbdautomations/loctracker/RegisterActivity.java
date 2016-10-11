package com.tbdautomations.loctracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    ProgressBar loader;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loader = (ProgressBar) findViewById(R.id.progLoader);

            Button btnRegister = (Button) findViewById(R.id.btnRegister);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final EditText txtNickName = (EditText) findViewById(R.id.txtNickName);
                    final EditText txtPhone = (EditText) findViewById(R.id.txtPhone);

                    if(txtNickName.getText().toString().length()<2 || txtPhone.getText().toString().length() < 10)
                    {
                        Toast.makeText(RegisterActivity.this,"Please Enter your Details to Register",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        CheckExistence chk = new CheckExistence();
                        chk.callback = new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message message) {
                                if(Boolean.parseBoolean(message.obj.toString()))
                                {
                                    Application.NickName = txtNickName.getText().toString();
                                    SharedPreferences.Editor sharedPref =  getSharedPreferences("LoctrackerSettings", Context.MODE_PRIVATE).edit();
                                    sharedPref.clear();
                                    sharedPref.putString("NickName",  txtNickName.getText().toString());
                                    sharedPref.putString("Phone",  txtPhone.getText().toString());
                                    sharedPref.commit();

                                    Application.NickName = txtNickName.getText().toString();
                                    Application.PhoneNo =  txtPhone.getText().toString();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this,"Sorry! This Phone no is already in Use",Toast.LENGTH_LONG).show();
                                }

                                loader.setVisibility(View.INVISIBLE);
                                return  true;
                            }
                        };
                        loader.setVisibility(View.VISIBLE);
                        chk.execute(txtPhone.getText().toString());
                    }
                }
            });
    }




}
