package com.example.quizy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class launcher extends Activity {
    FirebaseAuth auth;
    FirebaseUser user;
    GoogleSignInAccount account;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new Handler().postDelayed(this::startMain,1000);
    }
    void startMain(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);
        if(user!=null || account!=null)
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        else
            startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }
}