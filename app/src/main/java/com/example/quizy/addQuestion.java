package com.example.quizy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addQuestion extends AppCompatActivity {
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
    String total;
    String Name;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Name = intent.getStringExtra("name");
        total = intent.getStringExtra("total");
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Name).child(id);
        hook();
        submit.setOnClickListener(view -> SubmitQ());
    }


    private void SubmitQ() {
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

            databaseReference = FirebaseDatabase.getInstance().getReference().child(Name).child(id).child(String.valueOf(Integer.parseInt(total)+1));
            Questions q = new Questions(CurrQ,option1,option2,option3,option4,answer);
            databaseReference.setValue(q);
            new Handler().postDelayed(()->{

            },1000);
            databaseReference.getDatabase().getReference().child(Name).child(id).child("totalQuestions").setValue(String.valueOf(Integer.parseInt(total)+1));
            Intent intent1 = new Intent(getApplicationContext(),QuestionPaper.class);
            intent1.putExtra("generated_id",id);
            intent1.putExtra("name",Name);
            startActivity(intent1);
        }
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