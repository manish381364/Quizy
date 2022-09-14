package com.example.quizy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class CreateQuestionPaper extends AppCompatActivity {



    EditText question;
    EditText op1;
    EditText op2;
    EditText op3;
    EditText op4;
    EditText ans;
    Button next;
    TextView textView;
    int count=1;
    DatabaseReference databaseReference;
    FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_paper);
        Intent intent = getIntent();
        int total = intent.getIntExtra("noOfQuestions",0);
        String subject = intent.getStringExtra("subject");
        hook();
        question.requestFocus();
        auth = FirebaseAuth.getInstance();
        String x = getString(R.string.question_no);
        x += (" "+count);
        textView.setText(x);
        ArrayList<Questions> q = new ArrayList<>();



        next.setOnClickListener(view -> {
            if(invalid()){
                Toast.makeText(CreateQuestionPaper.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
            }
            else if(count<=total){
                String EQuestion = question.getText().toString();
                String Eop1 = op1.getText().toString();
                String Eop2 = op2.getText().toString();
                String Eop3 = op3.getText().toString();
                String Eop4 = op4.getText().toString();
                String Eop5 = ans.getText().toString();
                if(Integer.parseInt(Eop5)<=0 || Integer.parseInt(Eop5)>4){
                    Toast.makeText(CreateQuestionPaper.this, "Please Valid Option Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    count++;
                    setClearFields();
                    question.requestFocus();
                    Questions temp = new Questions(EQuestion,Eop1,Eop2,Eop3,Eop4,Eop5);
                    q.add(temp);
                    if(count>total){
                        Intent intent1 = new Intent(getApplicationContext(),QuestionPaper.class);
                        String name = Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()).replace(".","DOT").replace("@","AT");
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        String ID = databaseReference.child(name).push().getKey();
                        assert ID != null;
                        databaseReference.child(name).child(ID).child("totalQuestions").setValue(Integer.toString(total));
                        databaseReference.child(name).child(ID).child("subject").setValue(subject);
                        for(int i=0;i<q.size();i++){
                            databaseReference.child(name).child(ID).child(Integer.toString(i+1)).setValue(q.get(i));
                        }
                        intent1.putExtra("generated_id",ID);
                        intent1.putExtra("name",name);
                        startActivity(intent1);
                    }
                }
            }
        });



    }
    @SuppressLint("SetTextI18n")
    private void setClearFields(){
        textView.setText(getString(R.string.question_no)+" "+(count));
        question.setText("");
        op1.setText("");
        op2.setText("");
        op3.setText("");
        op4.setText("");
        ans.setText("");
    }



    private boolean invalid() {
        String one = question.getText().toString();
        String two = op1.getText().toString();
        String three = op2.getText().toString();
        String four = op3.getText().toString();
        String five = op4.getText().toString();
        return (one.length() == 0 || two.length() == 0 || three.length() == 0 || four.length() == 0 || five.length() == 0 || ans.length()==0);
    }



    private void hook(){
        question=findViewById(R.id.question);
        op1=findViewById(R.id.op1);
        op2=findViewById(R.id.op2);
        op3=findViewById(R.id.op3);
        op4=findViewById(R.id.op4);
        next=findViewById(R.id.next);
        textView=findViewById(R.id.enter_question);
        ans=findViewById(R.id.ans);
    }
}