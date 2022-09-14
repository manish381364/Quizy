package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_in extends AppCompatActivity {
    FirebaseAuth auth;
    AppCompatButton sign_in;
    TextInputEditText mail;
    TextInputEditText password;
    TextInputEditText confirm_password;
    TextInputEditText name;
    TextView Login;
    ProgressBar progressBar;
    TextInputLayout confirm_layout,password_layout;
    CardView email;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getIntent();
        auth = FirebaseAuth.getInstance();
        hook();
        String userName = Objects.requireNonNull(name.getText()).toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(userName);

        //password check
        Objects.requireNonNull(password_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()<1){
                    password_layout.setError("Please Re-Enter Password");
                }
                if(charSequence.length()>0){
                    password_layout.setError(null);
                    password_layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //confirm password check
        Objects.requireNonNull(confirm_layout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()<1 || charSequence.length()!= Objects.requireNonNull(password.getText()).toString().length()){
                    confirm_layout.setError("Please Re-Enter Password");
                }
                if(charSequence.toString().equals(Objects.requireNonNull(password.getText()).toString())){
                    confirm_layout.setError(null);
                    confirm_layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        sign_in.setOnClickListener(view -> {
                String Email = Objects.requireNonNull(mail.getText()).toString();
                String Password = Objects.requireNonNull(password.getText()).toString();
                String ConfirmPassword = Objects.requireNonNull(confirm_password.getText()).toString();
                String Name = Objects.requireNonNull(name.getText()).toString();
                String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                Pattern pattern = Pattern.compile(regex);
                if(TextUtils.isEmpty(Name)){
                    name.setError("Please Enter Name");
                    name.requestFocus();
                }
                else if(TextUtils.isEmpty(Email)){
                    mail.setError("Email cannot be empty");
                    mail.requestFocus();
                }
                else if(Password.length()==0){
                    password_layout.setError("Please enter password");
                    password_layout.requestFocus();
                    if(confirm_layout.isErrorEnabled()) confirm_layout.setErrorEnabled(false);
                }
                else if(ConfirmPassword.length()==0){
                    confirm_layout.setError("Please re-Enter password");
                    confirm_layout.requestFocus();
                    if(password_layout.isErrorEnabled()) password_layout.setErrorEnabled(false);
                }
                else if(Password.equals(ConfirmPassword)){
                    Matcher matcher = pattern.matcher(Email);
                    if(!matcher.matches()){
                        mail.setError("Please Enter Valid Email");
                        mail.requestFocus();
                    }
                    else{
                        password_layout.setErrorEnabled(false);
                        confirm_layout.setErrorEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        auth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(task -> {
                            if(Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty()){
                                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(Name).build();
                                        assert user != null;
                                        user.updateProfile(profileUpdates);
                                        user.sendEmailVerification();
                                        Toast.makeText(sign_in.this, "Verification Email send to"+Email, Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(() -> {
                                            Toast.makeText(sign_in.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), login.class));
                                        },1000);
                                    }
                                    else{
                                        Toast.makeText(sign_in.this, "Error"+ Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                }
                else{
                    confirm_layout.setError("Password didn't match");
                    confirm_layout.requestFocus();
                }
        });
        Login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.example.quizy.login.class)));
    }
    private void hook() {
        progressBar = findViewById(R.id.progress);
        sign_in = findViewById(R.id.sign_in);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirm_password = findViewById(R.id.confirm_password);
        Login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        confirm_layout = findViewById(R.id.conform_layout);
        password_layout = findViewById(R.id.password_layout);
        email = findViewById(R.id.mail);
    }
}