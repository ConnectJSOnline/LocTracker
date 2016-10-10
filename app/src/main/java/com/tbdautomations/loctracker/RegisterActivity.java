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
import android.widget.Toast;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(!AlreadyRegistered()) {
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
                                if( Boolean.parseBoolean(message.obj.toString()))
                                {
                                    SharedPreferences sharedPref =  RegisterActivity.this.getPreferences(Context.MODE_PRIVATE);
                                    sharedPref.edit().clear();
                                    sharedPref.edit().putString("NickName",  txtNickName.getText().toString());
                                    sharedPref.edit().putString("Phone",  txtPhone.getText().toString());
                                    sharedPref.edit().commit();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                return  true;
                            }
                        };
                        chk.execute(txtPhone.getText().toString());
                    }
                }
            });
        }
        else {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public  boolean AlreadyRegistered(){

        SharedPreferences sharedPref =  RegisterActivity.this.getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.contains("NickName")
            && sharedPref.getString("NickName",null) !=  null
            && sharedPref.contains("Phone")
            && sharedPref.getString("Phone", null) != null)
            return  true;
        return  false;
    }

}
