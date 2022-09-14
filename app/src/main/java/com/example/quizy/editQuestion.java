package com.example.quizy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class editQuestion extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    TextView questionNo;
    EditText question;
    EditText op1;
    EditText op2;
    EditText op3;
    EditText op4;
    EditText ans;
    Button submit;
    String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",1);
        String id = intent.getStringExtra("id");
        Name = intent.getStringExtra("name");
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Name).child(id);
        hook();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String x = Integer.toString(position);
                    String CurrQ = snapshot.child(x).child("question").getValue(String.class);
                    String option1 = snapshot.child(x).child("op1").getValue(String.class);
                    String option2 = snapshot.child(x).child("op2").getValue(String.class);
                    String option3 = snapshot.child(x).child("op3").getValue(String.class);
                    String option4 = snapshot.child(x).child("op4").getValue(String.class);
                    questionNo.setText(getString(R.string.editQuestion)+" "+x);
                    question.setText(CurrQ);
                    op1.setText(option1);
                    op2.setText(option2);
                    op3.setText(option3);
                    op4.setText(option4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        submit.setOnClickListener(view -> {
            String CurrQ = question.getText().toString();
            String answer = ans.getText().toString();
            String option1 = op1.getText().toString();
            String option2 = op2.getText().toString();
            String option3 = op3.getText().toString();
            String option4 = op4.getText().toString();
            if(CurrQ.length()==0){
                question.setError("Please Enter Question");
                question.requestFocus();
            }
            else if(option1.length()==0){
               op1.setError("please Enter option");
               op1.requestFocus();
            }
            else if(option2.length()==0){
                op2.setError("please Enter option");
                op2.requestFocus();
            }
            else if(option3.length()==0){
                op3.setError("please Enter option");
                op3.requestFocus();
            }
            else if(option4.length()==0){
                op4.setError("please Enter option");
                op4.requestFocus();
            }
            else if(answer.length()==0){
                ans.setError("please Enter answer number");
                ans.requestFocus();
            }
            else if(!(Integer.parseInt(answer)>=1 && Integer.parseInt(answer)<=4)){
                ans.setError("Please Enter Valid Option Number");
                ans.requestFocus();
            }
            else{
                String x = String.valueOf(position);
                databaseReference = FirebaseDatabase.getInstance().getReference().child(Name).child(id).child(x);
                Questions q = new Questions(CurrQ,option1,option2,option3,option4,answer);
                databaseReference.setValue(q);
                new Handler().postDelayed(()->{

                },1000);
                Intent intent1 = new Intent(getApplicationContext(),QuestionPaper.class);
                intent1.putExtra("generated_id",id);
                startActivity(intent1);
            }

        });
    }
    private void hook() {
        questionNo = findViewById(R.id.enter_question);
        question = findViewById(R.id.question);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        ans = findViewById(R.id.ans);
        submit = findViewById(R.id.submit);
    }
}