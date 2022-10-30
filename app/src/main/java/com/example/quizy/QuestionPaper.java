package com.example.quizy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class QuestionPaper extends AppCompatActivity {
    String generated_id;
    String Name;
    ListView listView;
    TextView textView;
    CircularRevealCardView Results;
    ProgressBar progressBar;
    MaterialButton button;
    ImageButton add;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> questions = new ArrayList<>();
    String totalQuestions;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_paper);
        Intent intent = getIntent();
        generated_id = intent.getStringExtra("generated_id");
        Name = intent.getStringExtra("name");
        hook();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference = databaseReference.child(Name).child(generated_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.child("totalQuestions").exists()){
                    int total = Integer.parseInt(Objects.requireNonNull(snapshot.child("totalQuestions").getValue(String.class)));
                    totalQuestions = String.valueOf(total);
                    textView.setText(getString(R.string.total_questions)+" "+ total);
                    for(int i=1;i<=total;i++){
                        String x = Integer.toString(i);
                        String CurrQ = getString(R.string.ques)+" "+x+": "+snapshot.child(x).child("question").getValue(String.class);
                        String answer = getString(R.string.answer)+" "+snapshot.child(x).child("ans").getValue(String.class);
                        String op1 = "Option 1: "+snapshot.child(x).child("op1").getValue(String.class);
                        String op2 = "Option 2: "+snapshot.child(x).child("op2").getValue(String.class);
                        String op3 = "Option 3: "+snapshot.child(x).child("op3").getValue(String.class);
                        String op4 = "Option 4: "+snapshot.child(x).child("op4").getValue(String.class);
                        questions.add(CurrQ+"\n\n"+op1+"\n\n"+op2+"\n\n"+op3+"\n\n"+op4+"\n\n"+answer);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view, questions){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            LayoutInflater layoutInflater = QuestionPaper.this.getLayoutInflater();
                            @SuppressLint({"ViewHolder", "InflateParams"}) View itemView = layoutInflater.inflate(R.layout.list_view,null,true);
                            TextView qList = itemView.findViewById(R.id.text_view);
                            String current;
                            if(position>=0 && position<questions.size()){
                                current = questions.get(position);
                                qList.setText(current);
                            }
                            return itemView;
                        }
                    };
                    listView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent1 = new Intent(getApplicationContext(),editQuestion.class);
            intent1.putExtra("position",i+1);
            intent1.putExtra("id",generated_id);
            intent1.putExtra("name",Name);
            startActivity(intent1);
        });

        add.setOnClickListener(view -> addQuestion());
        Results.setOnClickListener(view ->showResults());

    }

    private void showResults() {
        Intent intent = new Intent(getApplicationContext(),Results.class);
        intent.putExtra("id",generated_id);
        intent.putExtra("name",Name);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    private void addQuestion() {
        startActivity(new Intent(getApplicationContext(),addQuestion.class).putExtra("name",Name).putExtra("id",generated_id).putExtra("total",totalQuestions));
    }

    private void hook() {
        listView = findViewById(R.id.question_list);
        textView = findViewById(R.id.total);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.share);
        add = findViewById(R.id.add);
        Results = findViewById(R.id.view_results);
    }

    public void share(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String joinID = Name+":#join my quiz#:"+generated_id;
        StringBuilder x = new StringBuilder();
        for(int i=0;i<joinID.length();i++){
            x.append((char) (joinID.charAt(i) + 1));
        }
        joinID = x.toString();
        intent.putExtra(Intent.EXTRA_TEXT,joinID);
        Intent createChooser = Intent.createChooser(intent,"Send ID Via");
        startActivity(createChooser);
    }
}