package com.dhanadsp1120.touristguidefortamilnadu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class login extends AppCompatActivity {
    public ACProgressFlower dialog;
    public EditText un,pas;
    SharedPreferences sp;
    private DatabaseReference db,bd;
    public static String dn="",dlat="",dlon="",dim="";
    public  String phone,password,pp,ph,n,e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        un=findViewById(R.id.phone);
        pas=findViewById(R.id.loginpassword);
        sp=getSharedPreferences("check",0);
    }
    public void signup(View V)
    {
        Dialog m=new Dialog(this);
        m.setContentView(R.layout.dialogcustom);
        WebView w=m.findViewById(R.id.web);
        WebSettings webSettings=w.getSettings();
        webSettings.setJavaScriptEnabled(true);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient());
        w.loadUrl("http://dhanadsp.freesite.vip/guide/web/emailverificationforapp.html");
        m.show();
        m.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        m.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        m.setCanceledOnTouchOutside(false);
        m.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public void forgot(View V)
    {
        Dialog m=new Dialog(this);
        m.setContentView(R.layout.dialogcustom);
        WebView w=m.findViewById(R.id.web);
        WebSettings webSettings=w.getSettings();
        webSettings.setJavaScriptEnabled(true);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient());
        w.loadUrl("http://dhanadsp.freesite.vip/guide/web/forgot.html");
        m.show();
        m.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        m.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        m.setCanceledOnTouchOutside(false);
        m.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public void lok(View v) {
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();

        phone = un.getText().toString();
        password = pas.getText().toString();
        db= FirebaseDatabase.getInstance().getReference().child("apkregistered").child("profile").child(phone);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                     pp=dataSnapshot.child("Password").getValue().toString();
                     ph=dataSnapshot.child("Phone").getValue().toString();
                    e=dataSnapshot.child("Email").getValue().toString();
                     n=dataSnapshot.child("Name").getValue().toString();
                    if (pp.equals(password))
                    {
                        dialog.dismiss();
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("Phone",ph);
                        ed.putString("Email",e);
                        ed.putString("Name",n);
                        ed.commit();
                        load();

                    }
                    else
                    {
                        dialog.dismiss();
                        pas.setError("Incorrect password");

                    }

                }
                else
                {dialog.dismiss();
                    un.setError("InValid User");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(login.this,"Network Problem",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void load()
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
                entry.district.clear();
                entry.districtlat.clear();
                entry.districtlon.clear();
                entry.districtimg.clear();
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
                        entry.district.add(h[i]);
                        entry.districtlat.add(hlat[i]);
                        entry.districtlon.add(hlon[i]);
                        entry.districtimg.add(him[i]);

                    }

                    startActivity(new Intent(login.this, mainpage.class));

                } else {
                    AlertDialog.Builder on = new AlertDialog.Builder(login.this);
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
}