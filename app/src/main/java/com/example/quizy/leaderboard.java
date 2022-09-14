package com.example.quizy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class leaderboard extends AppCompatActivity {
    ListView leaderList;
    DatabaseReference databaseReference;
    AppCompatButton share;
    String score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        leaderList = findViewById(R.id.leaderList);
        share = findViewById(R.id.share);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(name).child(id);

        ArrayList<String> list = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    databaseReference = databaseReference.child("Enrollment-NO");
                    for(DataSnapshot snaps: snapshot.getChildren()){
                        String key = snaps.getKey();
                        assert key != null;
                        if(key.equals("Enrollment-NO")){
                            for(DataSnapshot x : snaps.getChildren()){
                                String enrollment = x.getKey();
                                String score = x.child("score").getValue(String.class);
                                list.add("Enrollment: "+enrollment+"\n"+" score: "+score);
                            }
                        }
                    }
                    //  SORTING LIST ACCORDING TO SCORE
                    list.sort((s, t1) -> {
                        String[] x = s.split(":");
                        String[] y= t1.split(":");
                        String X = x[x.length-1].trim();
                        String Y = y[y.length-1].trim();
                        if(Integer.parseInt(X)>Integer.parseInt(Y)){
                            return -1;
                        }
                        else if(Integer.parseInt(X)==Integer.parseInt(Y)){
                            return 0;
                        }
                        return 1;
                    });
                    //  SETTING ADAPTER
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_view,list){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            LayoutInflater layoutInflater = leaderboard.this.getLayoutInflater();
                            @SuppressLint("InflateParams") View itemView = layoutInflater.inflate(R.layout.list_view,null,true);
                            TextView qList = itemView.findViewById(R.id.text_view);
                            CircularRevealCardView card = itemView.findViewById(R.id.cardList);
                            String current;
                            if(position>=0 && position<list.size()){
                                current = list.get(position);
                                qList.setText(current);
                                if(position==0){
                                    card.setCardBackgroundColor(getColor(R.color.green));
                                }
                                else if(position==1){
                                    card.setCardBackgroundColor(getColor(R.color.blue));
                                }
                                else if(position==2){
                                    card.setCardBackgroundColor(getColor(R.color.yellow));
                                }
                                else{
                                    qList.setBackgroundColor(getColor(R.color.grey2));
                                    card.setCardBackgroundColor(getColor(R.color.grey2));
                                }
                            }
                            return itemView;
                        }
                    };
                    leaderList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Share Score

        share.setOnClickListener(view ->{
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            intent1.putExtra(Intent.EXTRA_TEXT,score);
            startActivity(intent1);
        });
    }
}