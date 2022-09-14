package com.example.quizy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class launcher extends Activity {
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        new Handler().postDelayed(()-> startActivity(new Intent(getApplicationContext(),MainActivity.class)),1000);
    }
}