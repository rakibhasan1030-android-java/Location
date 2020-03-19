package com.rakib.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private TextView latTV, lonTV, addressTV;
    private Geocoder geocoder; // geocoder will give you physical address
    private List<Address> addresses = new ArrayList<>();
    private String _address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latTV = findViewById(R.id.latTV);
        lonTV = findViewById(R.id.lonTV);
        addressTV = findViewById(R.id.addressTV);

        geocoder = new Geocoder(this, Locale.getDefault());

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

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    latTV.setText(String.valueOf(latitude));
                    lonTV.setText(String.valueOf(longitude));

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 5);
                        _address = addresses.get(0).getAddressLine(0) + "\n"+
                                addresses.get(0).getLocality()+"\n"+
                                addresses.get(0).getCountryName()+"("+
                                addresses.get(0).getCountryCode()+")";

                        Toast.makeText(getApplicationContext(), "_address", Toast.LENGTH_LONG).show();

                        addressTV.setText("_address");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MYAPP", "exception: " + e.getMessage());

                    }

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
