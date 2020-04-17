package com.example.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String s;
    LocationManager location;
    LocationListener listener;
    List<Address> addresses = null;
    double lon;
    List<String> fullData = new ArrayList<String>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            }
        }
    }

    /*
    method used to update the location of the user
     */
    public void updateLocation(Location location) {
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.ENGLISH);// geo coder is used to get the address of the location
        try {
            addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);//stores the location address in the list of type address

            fullData.add(String.valueOf(addresses.get(0).getThoroughfare() + "," + addresses.get(0).getAddressLine(0)) + "\n" + addresses.get(0).getCountryName());

            TextView loni = (TextView) findViewById(R.id.longititude);
            TextView lati = (TextView) findViewById(R.id.latitide);
            TextView address = (TextView) findViewById(R.id.Address);

            String addr = null;

            if (addresses.get(0).getThoroughfare() != null) {
                addr += addresses.get(0).getThoroughfare();
            }
            if (addresses.get(0).getAddressLine(0) != null) {
                addr += addresses.get(0).getAddressLine(0);

            }
            if (addresses.get(0).getCountryName() != null) {
                addr += addresses.get(0).getCountryName();
            }

            loni.setText("Longititude is:\n" + location.getLongitude());
            lati.setText("latitide is:\n" + location.getLatitude());
            address.setText("Address is\n" + addr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);

            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //to ckeck the access is given for accessing the location in the device
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);//if the accesss is not given the screens pops to ask the permission

        } else {


            location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);//else requests the location of the user
            Location lastKnownLocation = location.getLastKnownLocation(LocationManager.GPS_PROVIDER);//to get the last known location
            updateLocation(lastKnownLocation);//calls the function

        }
    }

}
