package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    CircularRevealCardView join;
    CircularRevealCardView create;
    TextView title;
    TextView name;
    GoogleSignInAccount account;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hook();
        join.setOnClickListener(view -> join());
        create.setOnClickListener(view -> createQuiz());
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);
        if(user==null && account==null){
            finish();
            startActivity(new Intent(getApplicationContext(),login.class));
        }
        else{
            if(user!=null){
                auth = FirebaseAuth.getInstance();
                String user1 = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
                String Name = Objects.requireNonNull(user.getEmail()).split("@")[0];
                assert user1 != null;
                if(user1.length()>0){
                    name.setText(user1);
                }
                else name.setText(Name);
            }
            else{
                String user1 = Objects.requireNonNull(account).getDisplayName();
                String Name = Objects.requireNonNull(account.getEmail()).split("@")[0];
                assert user1 != null;
                if(user1.length()>0){
                    name.setText(user1);
                }
                else name.setText(Name);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String selected = item.getTitle().toString();
        if(selected.equals("My Profile")){
            startActivity(new Intent(getApplicationContext(),profile.class));
        }
        else{
            new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialog)).setTitle("Logout").setMessage("Are you sure you want to sign out?").setPositiveButton("Logout", (dialogInterface, i) -> {
                if(auth.getCurrentUser() != null) auth.signOut();
                else gsc.signOut();
                finish();
                Intent act = new Intent(getApplicationContext(),login.class);
                act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(act);
            }).setNegativeButton("Cancel",null).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void createQuiz(){
        Intent intent = new Intent(this,Create.class);
        startActivity(intent);
    }


    public void join(){
        Intent intent = new Intent(this, Join.class);
        startActivity(intent);
    }


    private void hook() {
        join = findViewById(R.id.join);
        create = findViewById(R.id.create);
        title = findViewById(R.id.textView);
        name = findViewById(R.id.name);
    }
}