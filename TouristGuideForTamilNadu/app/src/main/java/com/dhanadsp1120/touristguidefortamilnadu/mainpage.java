package com.dhanadsp1120.touristguidefortamilnadu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class mainpage extends AppCompatActivity {
    DrawerLayout dr;
    SharedPreferences sp;
    NavigationView nv;
    GridView gv;
public static String latitude,longitude,district;
    public ACProgressFlower dialog;
    private DatabaseReference db,bd;

    public static String dn="",dlat="",dlon="",dim="";
    static List<String> place=new ArrayList<>();
    static List<String> placelat=new ArrayList<>();
    static List<String> placelon=new ArrayList<>();
    static List<String> placeimg=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        dr=findViewById(R.id.drawerlayout);
        gv=findViewById(R.id.gridview1);

        sp=getSharedPreferences("check",0);

        CustomAdapter Custom=new CustomAdapter();
        gv.setAdapter(Custom);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                district=entry.district.get(position);
                place.clear();
                placeimg.clear();
                placelat.clear();
                placelon.clear();
                dialog = new ACProgressFlower.Builder(mainpage.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .fadeColor(Color.DKGRAY).build();
                dialog.show();

                bd= FirebaseDatabase.getInstance().getReference().child("place").child(district);

                bd.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialog.dismiss();

                        if (snapshot.exists()) {

                            dn = snapshot.child("placename").getValue().toString();
                            dlat = snapshot.child("placelat").getValue().toString();
                            dlon= snapshot.child("placelon").getValue().toString();
                            dim = snapshot.child("placeimage").getValue().toString();
                            String[] h = dn.split("@@");
                            String[] hlat=dlat.split("@@");
                            String[] hlon=dlon.split("@@");
                            String [] him=dim.split("@@");
                            for (int i = 0; i < h.length; i++) {
                                place.add(h[i]);
                                placelat.add(hlat[i]);
                                placelon.add(hlon[i]);
                                placeimg.add(him[i]);

                            }

                            startActivity(new Intent(mainpage.this, place.class));

                        } else {
                            AlertDialog.Builder on = new AlertDialog.Builder(mainpage.this);
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
        });
        nv=findViewById(R.id.ngv);

        nv.setItemIconTintList(null);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.logout:
                        logout();
                        break;
                    case R.id.about:
                        about1();
                        break;
                    case R.id.send:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT,"https://drive.google.com/file/d/1fMbRzNYcSDd_2d6ChHrnj0tNPZI4iqCT/view?usp=sharing");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "App");
                        startActivity(Intent.createChooser(sharingIntent, "Share Apk To Friends"));

                        break;
                    case R.id.feedback:
                        startActivity(new Intent(mainpage.this,feedback.class));
                        break;
                }
                dr.closeDrawer(GravityCompat.START);
                return false;

            }
        });
    }
    public void opendrawer(View v)
    {
        dr.openDrawer(GravityCompat.START);

    }
    public void about(View v)
    {  about1();
         }
    public void about1()
    {
        startActivity(new Intent(this,about.class));
    }
    public void logout()
    {
        AlertDialog.Builder di= new AlertDialog.Builder(mainpage.this);
        di.setMessage("Are you sure want to LogOut");
        di.setCancelable(true);
        di.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("Phone","");
                ed.commit();

                finish();

                Intent intent = new Intent(mainpage.this, login.class);
                startActivity(intent);

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ad=di.create();
        ad.show();

    }
    private class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return entry.district.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.ima, null);

            TextView name = view1.findViewById(R.id.placename);
            ImageView image = view1.findViewById(R.id.place);


          TextView t;
            t = view1.findViewById(R.id.distanceshow1);
            int Radius = 6371;// radius of earth in Km
            latitude=entry.districtlat.get(i);
            longitude=entry.districtlon.get(i);
            Log.d("name",entry.district.get(i));
            Log.d("l",entry.districtlat.get(i));

            if(latitude.contains("N")||latitude.contains("E")||latitude.contains("W")||latitude.contains("S"))
            {
                int l=latitude.length();

                if(latitude.charAt(l-1)=='N'||latitude.charAt(l-1)=='E') {
                    latitude = latitude.substring(0, l - 3);
                    Log.d("ll", latitude);
                }
                else
                {
                    latitude = latitude.substring(0, l - 4);
                    Log.d("ll", latitude);
                }
            }
            Log.d("la",entry.districtlon.get(i));
            if(longitude.contains("N")||longitude.contains("E")||longitude.contains("W")||longitude.contains("S"))
            {
                int l=longitude.length();
                longitude=longitude.substring(0,l-3);
                Log.d("lla",longitude);

            }

            double latt1 =Double.parseDouble(entry.lat);
            double latt2 =Double.parseDouble(latitude);
            double long1 =Double.parseDouble(entry.lon);
            double long2 =Double.parseDouble(longitude);
            double dLat = Math.toRadians(latt2 - latt1);
            double dLon = Math.toRadians(long2 - long1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(latt1))
                    * Math.cos(Math.toRadians(latt2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;
            double km = valueResult / 1;
            DecimalFormat newFormat = new DecimalFormat("####");
            int kmInDec = Integer.valueOf(newFormat.format(km));
            String x=Integer.toString(kmInDec);
            String xx=x+" KM";
            t.setText(xx);


            name.setText(entry.district.get(i));
            Picasso.get().load(entry.districtimg.get(i)).into(image);

            return view1;
        }
    }
    public void onBackPressed() {
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure want to Exit");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ad=dialog.create();
        ad.show();


    }
}