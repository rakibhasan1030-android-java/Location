package com.rakib.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private TextView latTV, lonTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latTV = findViewById(R.id.latTV);
        lonTV = findViewById(R.id.lonTV);


        client = LocationServices.getFusedLocationProviderClient(this);

        //request is for your current location update, how will you want to get yours location updates. like how priority, interval and fastest interval will be.
        request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // accuracy is high so battery will consume more power, but it will give accurate value (about 100 feet +-)
                .setInterval(10000) // 10 sec interval loaction will update
                .setFastestInterval(5000);// how much processing time take to update locations

        //when location updates or changes callback will inform or give you the data(lat, lon)
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //the location will come with an array, so we've to find it
                for(Location location : locationResult.getLocations()){
                    latTV.setText(String.valueOf(location.getLatitude()));
                    lonTV.setText(String.valueOf(location.getLongitude()));
                }
            }
        };

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        client.requestLocationUpdates(request, callback, null);
    }
}
