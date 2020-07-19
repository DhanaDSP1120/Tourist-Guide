package com.dhanadsp1120.touristguidefortamilnadu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class placefulldetials extends AppCompatActivity {
    ImageSlider is;
    ImageView iv;
    TextView pn;
   TextView pc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placefulldetials);
        iv=findViewById(R.id.image);
        pn=findViewById(R.id.placename);
        pc=findViewById(R.id.placecontent);
        is = findViewById(R.id.imageslider);
        Picasso.get().load(place.placeimg).into(iv);
        Log.d("content",place.content);
        pn.setText(place.placename);
        pc.setText(place.content);
        List<SlideModel> sm = new ArrayList<>();

        sm.clear();
for(int i=0;i<place.links.size();i++)
{
    sm.add(new SlideModel(place.links.get(i), ScaleTypes.FIT));
}

        is.setImageList(sm,ScaleTypes.FIT);

    }
    public void direction(View v)
    {//http://maps.google.com/maps?saddr=0,0"+entry.lat+","+entry.lon+"&daddr="+place.latitude+","+place.longitude
        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr="+entry.lat+","+entry.lon+"&daddr="+place.latitude+","+place.longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}