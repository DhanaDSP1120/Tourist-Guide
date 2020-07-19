package com.dhanadsp1120.touristguidefortamilnadu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

public class place extends AppCompatActivity {
    GridView gv;
    public static String placename="",content="",placeimg="";
    public static List<String> links=new ArrayList<>();
     public static String latitude,longitude;
    public ACProgressFlower dialog;
    private DatabaseReference db,bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        gv=findViewById(R.id.gridview1);
       CustomAdapter Custom=new CustomAdapter();
        gv.setAdapter(Custom);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            placename=mainpage.place.get(position);
            placeimg=mainpage.placeimg.get(position);
                dialog = new ACProgressFlower.Builder(place.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)
                        .fadeColor(Color.DKGRAY).build();
                dialog.show();
                bd= FirebaseDatabase.getInstance().getReference().child("place").child(mainpage.district).child(placename);

                bd.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialog.dismiss();
                        links.clear();
                        if(snapshot.exists()) {

                            content =snapshot.child("content").getValue().toString();
                            String k=snapshot.child("link").getValue().toString();
                            String l[]=k.split("@@");
                            for(int i=0;i<l.length-1;i++)
                            {
                                links.add(l[i]);
                            }
                            startActivity(new Intent(place.this,placefulldetials.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return mainpage.place.size();
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
            latitude=mainpage.placelat.get(i);
            longitude=mainpage.placelon.get(i);
            if(latitude.contains("N")||latitude.contains("E")||latitude.contains("W")||latitude.contains("S"))
            {
                int l=latitude.length();

                if(latitude.charAt(l-1)=='N'||latitude.charAt(l-1)=='E') {
                    latitude = latitude.substring(0, l - 3);

                }
                else
                {
                    latitude = latitude.substring(0, l - 4);

                }

            }
            if(longitude.contains("N")||longitude.contains("E")||longitude.contains("W")||longitude.contains("S"))
            {
                int l=longitude.length();
                longitude=longitude.substring(0,l-3);

            }
            double latt1 =Double.parseDouble(entry.lat);
            Log.d("name", mainpage.place.get(i));
            Log.d("lat", latitude);
            int l=latitude.length();
            if(latitude.charAt(l-1)==' ') {
                latitude = latitude.substring(0, l-1);

            }
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
            name.setText(mainpage.place.get(i));
            Picasso.get().load(mainpage.placeimg.get(i)).into(image);

            return view1;
        }
    }
}