package com.example.quizy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class profile extends AppCompatActivity {
    ImageView profileImage;
    TextView name;
    TextView mail;
    CircularRevealCardView myQuestionPapers;
    CircularRevealCardView signOut;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profile_img);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        myQuestionPapers = findViewById(R.id.my_question_papers);
        signOut = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        assert user != null;
        String userName = Objects.requireNonNull(user.getEmail()).split("@")[0];
        String eMail = Objects.requireNonNull(user.getEmail());
        if(user.getDisplayName()!=null){
            name.setText(user.getDisplayName());
        }
        else name.setText(userName);
        mail.setText(eMail);
        signOut.setOnClickListener(view -> logOut());
        myQuestionPapers.setOnClickListener(view -> getQuestionPapers());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    public void logOut(){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
    }
    public void getQuestionPapers(){
        startActivity(new Intent(getApplicationContext(),MyQuizzes.class));
    }
}