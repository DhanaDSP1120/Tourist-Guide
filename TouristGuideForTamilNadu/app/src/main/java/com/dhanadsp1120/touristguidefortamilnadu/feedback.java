package com.dhanadsp1120.touristguidefortamilnadu;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class feedback extends AppCompatActivity {
    EditText name, email, feed;
    TextInputLayout n, e, f;
    private DatabaseReference db;
    ProgressDialog dialog;

    static String names, emails, feedbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        feed = findViewById(R.id.feedback);
        n = findViewById(R.id.n);
        e = findViewById(R.id.e);
        f = findViewById(R.id.f);
    }


    public void submit(View v) {
        if (isConnected()) {
            names = name.getText().toString();
            emails = email.getText().toString();
            feedbacks = feed.getText().toString();
            if (names.isEmpty()) {
                name.setError("Name is Requried");
            } else if (emails.isEmpty()) {
                email.setError("Email is Requried");
            } else if (feedbacks.isEmpty()) {
                feed.setError("FeedBack is Requried");
            } else if (name.length() < 4) {
                n.setError("Minimum 5 characters");
            } else if (emails.contains("@") == false) {
                e.setError("Invalid email");
            } else {
                dialog = new ProgressDialog(feedback.this, R.style.AppCompatAlertDialogStyle);

                dialog.show();


                dialog.setCancelable(false);
                dialog.setContentView(R.layout.feedback);
                dialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        db = FirebaseDatabase.getInstance().getReference().child("feedback").child(names);

                        HashMap<String, String> b = new HashMap<String, String>();
                        b.put("name", names);
                        b.put("email", emails);
                        b.put("feedback", feedbacks);
                        db.setValue(b);
                        dialog.dismiss();

                        startActivity(new Intent(feedback.this, mainpage.class));
                        finish();
                    }
                }, 3900);


            }
        }
        else {
            AlertDialog.Builder on = new AlertDialog.Builder(feedback.this);
            on.setTitle("Warning")
                    .setMessage("Please enable the Internet to Feedback submit")
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
}
