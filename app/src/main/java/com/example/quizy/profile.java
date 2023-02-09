package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

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
        if(user != null){
            String userName = Objects.requireNonNull(user.getEmail()).split("@")[0];
            String eMail = Objects.requireNonNull(user.getEmail());
            if(user.getDisplayName()!=null){
                name.setText(user.getDisplayName());
            }
            else name.setText(userName);
            mail.setText(eMail);
        }
        else{
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            assert account != null;
            Picasso.get().load( Objects.requireNonNull(account.getPhotoUrl()).toString()).into(profileImage);
            name.setText(account.getDisplayName());
            mail.setText(account.getEmail());
        }
        signOut.setOnClickListener(view -> logOut());
        myQuestionPapers.setOnClickListener(view -> getQuestionPapers());
    }


    public void logOut(){
        if(auth!=null){
            auth.signOut();
        }
        else{
            GoogleSignInClient gsc = GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
            gsc.signOut();
        }
        finish();
        Intent i = new Intent(getApplicationContext(),login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void getQuestionPapers(){
        startActivity(new Intent(getApplicationContext(),MyQuizzes.class));
    }
}