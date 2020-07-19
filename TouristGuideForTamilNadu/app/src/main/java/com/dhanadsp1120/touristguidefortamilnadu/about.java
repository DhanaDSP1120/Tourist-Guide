package com.dhanadsp1120.touristguidefortamilnadu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class about extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void shareapk(View v)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"https://drive.google.com/file/d/1fMbRzNYcSDd_2d6ChHrnj0tNPZI4iqCT/view?usp=sharing");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Important");
        startActivity(Intent.createChooser(sharingIntent, "Share Apk To Friends"));

    }
}
