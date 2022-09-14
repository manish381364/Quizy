package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reset_password extends AppCompatActivity {
    EditText mail;
    FirebaseAuth auth;
    ProgressBar progressBar;
    CircularRevealCardView submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getIntent();
        auth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.mail);
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress);
        submit.setOnClickListener(view ->resetPassword());
    }

    private void resetPassword() {
        String Email = mail.getText().toString();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        if(TextUtils.isEmpty(Email)){
            mail.setError("Please Enter Email");
            mail.requestFocus();
        }
        else{
            Matcher matcher = pattern.matcher(Email);
            if(!matcher.matches()){
                mail.setError("Please Enter Valid Email");
                mail.requestFocus();
            }
            else{
                auth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(task -> {
                    if(Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty()){
                        Toast.makeText(reset_password.this, "Email Not Found!\n Create new account", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        auth.sendPasswordResetEmail(Email);
                        Toast.makeText(reset_password.this, "Password Reset Email Send to"+Email, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> startActivity(new Intent(getApplicationContext(),login.class)),1000);
                    }
                });

            }
        }
    }
}