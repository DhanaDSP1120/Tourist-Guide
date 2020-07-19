package com.dhanadsp1120.touristguidefortamilnadu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class entry extends AppCompatActivity {
    LocationRequest request;
    public static String lat,lon,rw;
    int PERMISSION_ID = 44;
   public static List li = new ArrayList<String>();
    SharedPreferences sp;
    public ACProgressFlower dialog;
    private DatabaseReference db,bd;
    static List<String> district=new ArrayList<>();
    static List<String> districtlat=new ArrayList<>();
    static List<String> districtlon=new ArrayList<>();
    static List<String> districtimg=new ArrayList<>();


    FusedLocationProviderClient mFusedLocationClient;
    private LocationSettingsRequest.Builder builder;
    public static String dn="",dlat="",dlon="",dim="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        sp=getSharedPreferences("check",0);

        rw=sp.getString("Phone","");
        requestPermissions();
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void getLastLocation() {

        if (checkPermissions()) {

            //Toast.makeText(MainActivity.this,"last", Toast.LENGTH_LONG).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {

                                String la = Double.toString(location.getLatitude());
                                String lo = Double.toString(location.getLongitude());

                                lat = la;
                                lon = lo;
                                //Toast.makeText(MainActivity.this,count, Toast.LENGTH_LONG).show();

                            }
                        }
                    }
            );

        } else {


            requestPermissions();
        }
    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            lat= Double.toString(mLastLocation.getLatitude());
            lon=Double.toString(mLastLocation.getLongitude());
            //Toast.makeText(MainActivity.this,lat, Toast.LENGTH_LONG).show();

        }
    };
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET},
                PERMISSION_ID
        );
    }


    public void go(View v)
    {
        if(isConnected()) {

            if (isLocationEnabled()) {


                mFusedLocationClient =LocationServices.getFusedLocationProviderClient(entry.this);
                getLastLocation();
                if(rw.length()>0)
                {
                    dialog = new ACProgressFlower.Builder(this)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)
                            .fadeColor(Color.DKGRAY).build();
                    dialog.show();
                    bd= FirebaseDatabase.getInstance().getReference().child("place").child("root");

                    bd.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dialog.dismiss();
                            district.clear();
                            districtlat.clear();
                            districtlon.clear();
                            districtimg.clear();
                            if (snapshot.exists()) {

                                dn = snapshot.child("districtname").getValue().toString();
                                dlat = snapshot.child("districtlat").getValue().toString();
                                dlon= snapshot.child("districtlon").getValue().toString();
                                dim = snapshot.child("districtimage").getValue().toString();
                                String[] h = dn.split("@@");
                                String[] hlat=dlat.split("@@");
                                String[] hlon=dlon.split("@@");
                                String [] him=dim.split("@@");
                                for (int i = 0; i < h.length; i++) {
                                    district.add(h[i]);
                                    districtlat.add(hlat[i]);
                                    districtlon.add(hlon[i]);
                                    districtimg.add(him[i]);

                                }

                                startActivity(new Intent(entry.this, mainpage.class));

                            } else {
                                AlertDialog.Builder on = new AlertDialog.Builder(entry.this);
                                on.setTitle("No Data")
                                        .setMessage("there is no data")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                            }
                                        });
                                AlertDialog m = on.create();
                                m.setCanceledOnTouchOutside(false);
                                m.show();

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else
                {

                    startActivity(new Intent(this,login.class ));

                }
            } else {
                request = new LocationRequest()
                        .setFastestInterval(1500)
                        .setInterval(3000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(entry.this).checkLocationSettings(builder.build());
                result.addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {

                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(entry.this, 8989);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
        else
        {
            AlertDialog.Builder on = new AlertDialog.Builder(entry.this);
            on.setTitle("Warning")
                    .setMessage("Please enable the Internet to use Tourist Guide for TamilNadu` ")
                    .setPositiveButton("Turn ON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent om = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                            startActivity(om);
                        }
                    })
                    .setIcon(R.drawable.network);
            AlertDialog m = on.create();
            m.show();
        }
    }
}