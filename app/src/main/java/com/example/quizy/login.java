package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class login extends AppCompatActivity {
    FirebaseAuth auth;
    AppCompatButton login;
    TextInputEditText mail;
    TextInputEditText password;
    TextInputLayout password_layout;
    TextView reset;
    TextView signIn;
    ProgressBar progressBar;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getIntent();
        hook();
        login.setOnClickListener(view -> loginUser());
        signIn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),sign_in.class)));
        reset.setOnClickListener(view ->ResetPassword());
    }


    private void ResetPassword() {
        startActivity(new Intent(getApplicationContext(),reset_password.class));
    }


    private void loginUser(){
        auth = FirebaseAuth.getInstance();
        String Email = Objects.requireNonNull(mail.getText()).toString();
        String Pass = Objects.requireNonNull(password.getText()).toString();
        if(TextUtils.isEmpty(Email) || !Email.contains("@")){
            mail.setError("Email can't be empty");
            mail.requestFocus();
        }
        else if(TextUtils.isEmpty(Pass)){
            password.setError("Password can't be empty");
            password.requestFocus();
        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference();
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String UserName = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()).replace(".","DOT").replace("@","AT");
                    databaseReference.child(UserName).child("username").setValue(auth.getCurrentUser().getDisplayName());
                    Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else{
                    Toast.makeText(login.this, "Login error "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE),1000);
        }
    }



    private void hook() {
        progressBar = findViewById(R.id.progress);
        login = findViewById(R.id.login_button);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        signIn = findViewById(R.id.sign_in);
        reset = findViewById(R.id.reset);
        password_layout = findViewById(R.id.password);
    }
}