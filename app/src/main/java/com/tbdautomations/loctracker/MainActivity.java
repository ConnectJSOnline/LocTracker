package com.tbdautomations.loctracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    ArrayList<String> data = new ArrayList<String>();
    boolean StartLogging = false;
    Intent loc_pooling_service;
    int RequestsCounts = 0;
    CharSequence ApplicationOriginalTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create()
                .setInterval(10000)         // 10 seconds
                .setFastestInterval(16)    // 16ms = 60fps
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        loc_pooling_service = new Intent(MainActivity.this,locpooling.class);
        ApplicationOriginalTitle = this.getTitle();
        this.setTitle(ApplicationOriginalTitle + "-Disconnected");

        ToggleButton btnStartStop = (ToggleButton) findViewById(R.id.toggleButtonInitiate);
        btnStartStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mGoogleApiClient.reconnect();
                    StartLogging = true;
                    LogLocation(null);
                    MainActivity.this.setTitle(ApplicationOriginalTitle + " - Connected");
                }
                else
                {
                    mGoogleApiClient.disconnect();
                    StartLogging = false;
                    LogLocation(null);
                    MainActivity.this.setTitle(ApplicationOriginalTitle + " - Disconnected");
                }
            }
        });


        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API


        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*


        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        */
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                ) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET
                    },1);

        }
        else{
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            if (location == null) {


            } else {
                //If everything went fine lets get latitude and longitude
                LogLocation(location);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                int count = 0;
                for (int perm:grantResults) {
                    if(perm == PackageManager.PERMISSION_GRANTED)
                        count++;
                    else {
                        Toast.makeText(MainActivity.this, "Permission denied for " + permissions[count], Toast.LENGTH_SHORT).show();
                        mGoogleApiClient.disconnect();
                        break;
                    }
                }
            }
        }
    }

    public void LogLocation(Location location)
    {
        boolean isReallyChanged = false;
        double _currentLatitude, _currentLongitude;


        if(location==null)
        {
            data.clear();
            RequestsCounts = 0;
            ((TextView)findViewById(R.id.txtStatus)).setText(Integer.toString(RequestsCounts));
        }
        else
        {
            _currentLatitude = location.getLatitude();
            _currentLongitude = location.getLongitude();

            if(_currentLatitude!=currentLatitude || _currentLongitude != currentLongitude)
            {
                currentLatitude = _currentLatitude;
                currentLongitude = _currentLongitude;
                isReallyChanged = true;
                data.add("Location (" + currentLatitude + " , " + currentLongitude + ")");
            }
        }

        ArrayAdapter<String> a = new ArrayAdapter<String>(this,R.layout.listdata,data);


        if(!StartLogging || !isReallyChanged) return;

        try {
            loc_pooling_service.setAction("SendLocation");
            loc_pooling_service.putExtra("LAT",currentLatitude);
            loc_pooling_service.putExtra("LONG",currentLongitude);
            startService(loc_pooling_service);
            RequestsCounts++;
            ((TextView)findViewById(R.id.txtStatus)).setText(Integer.toString(RequestsCounts));

        }catch(Exception e)
        {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                    String ss = "Hello";
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (Exception e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LogLocation(location);
    }
}
