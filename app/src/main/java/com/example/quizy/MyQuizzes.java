package com.example.quizy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyQuizzes extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    TextView result;
    Button create;
    ProgressBar progressBar;
    CircularRevealCardView delete,viewPaper,delete_view,confirmation,delete2,cancel;
    ListView listView;
    String name;
    ArrayList<String> myQuestionList = new ArrayList<>();
    ArrayList<Pair<String,String>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quizzes);
        getIntent();
        auth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.QList);
        progressBar = findViewById(R.id.progress);
        create = findViewById(R.id.create);
        result = findViewById(R.id.result);
        delete = findViewById(R.id.delete);
        delete2 = findViewById(R.id.delete2);
        cancel = findViewById(R.id.cancel);
        viewPaper = findViewById(R.id.view);
        delete_view = findViewById(R.id.delete_view);
        confirmation = findViewById(R.id.confirmation);
        confirmation.setVisibility(View.GONE);
        name = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()).replace(".","DOT").replace("@","AT");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(name);





        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.child("subject").exists()){
                            myQuestionList.add(dataSnapshot.child("subject").getValue(String.class));
                            Pair<String,String> p = new Pair<>(dataSnapshot.getKey(), dataSnapshot.child("subject").getValue(String.class));
                            list.add(p);
                        }
                }
                if(list.size()==0){
                    create.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);
                }
                else{
                    ArrayAdapter<Pair<String,String>> adapter = new ArrayAdapter<Pair<String,String>>(getApplicationContext(), R.layout.list2, list){
                        @SuppressLint("SetTextI18n")
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            LayoutInflater layoutInflater = MyQuizzes.this.getLayoutInflater();
                            @SuppressLint({"ViewHolder", "InflateParams"}) View itemView = layoutInflater.inflate(R.layout.list2,null,true);
                            TextView qList = itemView.findViewById(R.id.text_view);

                            if(position>=0 && position<list.size()){
                                Pair<String,String> current = list.get(position);
                                qList.setText("Subject: "+current.second);
                            }
                            return itemView;
                        }
                    };
                    listView.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i>=0 && i<list.size()){
                viewQuestionPaper(i);
            }
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            listView.setClickable(false);
            new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialog)).setTitle("Delete").setMessage("Do you want to delete?").setPositiveButton("Delete", (dialogInterface, I) -> deletePaper(i)).setNegativeButton("Cancel",null).show();
            return true;
        });
        delete.setOnClickListener(view -> confirmDelete());
        cancel.setOnClickListener(view -> cancel());
        create.setOnClickListener(view ->createPaper());

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),profile.class));
    }

    private void viewQuestionPaper(int i) {
        Intent intent = new Intent(getApplicationContext(),QuestionPaper.class);
        intent.putExtra("generated_id",list.get(i).first);
        intent.putExtra("name", name);
        startActivity(intent);
    }
    private void cancel(){
        confirmation.setVisibility(View.GONE);
        delete_view.setVisibility(View.VISIBLE);
    }
    private void confirmDelete(){
        delete_view.setVisibility(View.GONE);
        new Handler().postDelayed(() -> confirmation.setVisibility(View.VISIBLE),200);
    }

    public void deletePaper(int i) {
        String to_del = list.get(i).first;
        ArrayAdapter<Pair<String,String>> adapter = new ArrayAdapter<Pair<String,String>>(getApplicationContext(), R.layout.list2, list){
            @SuppressLint("SetTextI18n")
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                LayoutInflater layoutInflater = MyQuizzes.this.getLayoutInflater();
                @SuppressLint({"ViewHolder", "InflateParams"}) View itemView = layoutInflater.inflate(R.layout.list2,null,true);
                TextView qList = itemView.findViewById(R.id.text_view);

                if(position>=0 && position<list.size()){
                    Pair<String,String> current = list.get(position);
                    qList.setText("Subject: "+current.second);
                }
                return itemView;
            }
        };

        if(list.size()-1>=i){
            adapter.remove(list.get(i));
        }
        listView.setAdapter(adapter);
        listView.setClickable(true);
        adapter.notifyDataSetChanged();
        listView.invalidateViews();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(name).child(to_del);
        databaseReference.removeValue();
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
        this.recreate();
    }

    private void createPaper() {
        startActivity(new Intent(getApplicationContext(),Create.class));
    }

}