package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Join extends AppCompatActivity {
    TextView pass;
    TextView questionDisplay;
    EditText join;
    EditText enrollment;
    CircularRevealCardView submit;
    String passKey;
    LinearLayout questionPaper;
    LinearLayout Id;
    RadioGroup radioGroup;
    RadioButton selected;
    RadioButton op1;
    RadioButton op2;
    RadioButton op3;
    RadioButton op4;
    boolean inCheck = false;
    boolean idFound=false;
    Button next;
    CircularRevealCardView search;
    String enrollment_number;
    int score=0;
    int count=1;
    int totalQuestions;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ArrayList<String> finalAnswer= new ArrayList<>();
    ArrayList<String> Answers= new ArrayList<>();
    String getID;
    String name;
    String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Hook();


        //SUBMIT BUTTON CLICK ACTION
        submit.setOnClickListener(view -> {
            passKey=join.getText().toString();
            if(passKey.length()==0 || enrollment.length()==0){
                if(enrollment.length()==0 && passKey.length()==0){
                    join.setError("Please Enter ID");
                    join.requestFocus();
                    enrollment.setError("Please Enter Name/Enrollment");
                    enrollment.requestFocus();
                }
                else if(enrollment.length()==0){
                    enrollment.setError("Please Enter Name/Enrollment");
                    enrollment.requestFocus();
                }
                else{
                    join.setError("Please Enter ID");
                    join.requestFocus();
                }
                return;
            }
            if(!idFound){
                join.setError("Id Not Found PLease Enter Valid ID");
                join.requestFocus();
            }
            else{


                StringBuilder x = new StringBuilder();
                for(int i=0;i<passKey.length();i++){
                    x.append((char) (passKey.charAt(i) - 1));
                }
                passKey = x.toString();

                enrollment_number=enrollment.getText().toString();
                String name = passKey.split(":#join my quiz#:")[0];
                String quizID = passKey.split(":#join my quiz#:")[1];
                databaseReference = FirebaseDatabase.getInstance().getReference().child(name).child(quizID);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Enrollment-NO").exists() && snapshot.child("Enrollment-NO").child(enrollment_number).exists() && (!inCheck)){
                            enrollment.setError("You have already attempted this quiz");
                            enrollment.requestFocus();
                        }
                        else{
                            databaseReference.child("Enrollment-NO").child(enrollment_number);
                            Id.setVisibility(View.GONE);
                            hook2();
                            questionPaper.setVisibility(View.VISIBLE);
                            setText();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Join.this, "Error "+error.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(Id.getVisibility()==View.VISIBLE){
            super.onBackPressed();
        }
    }
    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }


    public void Search(View view){
        passKey=join.getText().toString();
        ProgressBar PB = findViewById(R.id.PB);
        if(passKey.length()==0){
            join.setError("Please Enter Id");
            join.requestFocus();
            return;
        }
        PB.setVisibility(View.VISIBLE);
        StringBuilder x = new StringBuilder();
        for(int i=0;i<passKey.length();i++){
            x.append((char) (passKey.charAt(i) - 1));
        }
        passKey = x.toString();
        String code = ":#join my quiz#:";
        if(passKey.length()>code.length() && passKey.contains(":#join my quiz#:")) {
            name = passKey.split(":#join my quiz#:")[0];
            String ID = passKey.split(":#join my quiz#:")[1];
            databaseReference = FirebaseDatabase.getInstance().getReference().child(name);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot x:snapshot.getChildren()){
                            if(Objects.equals(x.getKey(), ID)){
                                getID = passKey;
                                idFound=true;
                                break;
                            }
                        }
                    }
                    if(idFound){
                        Toast.makeText(getApplicationContext(), "ID Found", Toast.LENGTH_SHORT).show();
                        view.setVisibility(View.GONE);
                        PB.setVisibility(View.GONE);
                        join.setVisibility(View.GONE);
                        pass.setVisibility(View.GONE);
                        enrollment.setCursorVisible(true);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"ID Not Found",Toast.LENGTH_SHORT).show();
                        PB.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Join.this, "Error "+error.getDetails(), Toast.LENGTH_SHORT).show();
                    PB.setVisibility(View.GONE);
                }
            });
        }
        else{
            join.setError("Please Enter Valid ID");
            join.requestFocus();
            PB.setVisibility(View.GONE);
        }
    }


    private void Hook() {
        pass=findViewById(R.id.Enter_join);
        join=findViewById(R.id.key);
        Id=findViewById(R.id.pass);
        submit=findViewById(R.id.submit);
        enrollment=findViewById(R.id.enrollment_name);
        search = findViewById(R.id.findID);
    }
    private void hook2(){
        questionPaper=findViewById(R.id.QuestionPaper);
        radioGroup=findViewById(R.id.options);
        next=findViewById(R.id.next);
        op1=findViewById(R.id.op1);
        op2=findViewById(R.id.op2);
        op3=findViewById(R.id.op3);
        op4=findViewById(R.id.op4);
        questionDisplay=findViewById(R.id.questionDisplay);
        progressBar = findViewById(R.id.progress);
    }


    public void setText(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String question;
                String opt1;
                String opt2;
                String opt3;
                String opt4;
                databaseReference = FirebaseDatabase.getInstance().getReference().child(passKey.split(":#join my quiz#:")[0]).child(passKey.split(":#join my quiz#:")[1]);
                String totalQ = snapshot.child("totalQuestions").getValue(String.class);
                subject = snapshot.child("subject").getValue(String.class);
                assert totalQ != null;
                totalQuestions = Integer.parseInt(totalQ);

                    question = snapshot.child("1").child("question").getValue(String.class);
                    opt1 = snapshot.child("1").child("op1").getValue(String.class);
                    opt2 = snapshot.child("1").child("op2").getValue(String.class);
                    opt3 = snapshot.child("1").child("op3").getValue(String.class);
                    opt4 = snapshot.child("1").child("op4").getValue(String.class);
                    Answers.add(snapshot.child("1").child("ans").getValue(String.class));
                questionDisplay.setText(question);
                op1.setText(opt1);
                op2.setText(opt2);
                op3.setText(opt3);
                op4.setText(opt4);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Join.this, "Error "+error.getDetails(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void setScore(){
        int id = radioGroup.getCheckedRadioButtonId();
        selected = findViewById(id);
        String x = selected.getText().toString();
        finalAnswer.add(x);
    }
    public void checkAnswer(){
        inCheck = true;
        Id.setVisibility(View.GONE);
        questionPaper.setVisibility(View.VISIBLE);
        if(Answers.size()==finalAnswer.size()){
            for(int i=0;i<Answers.size();i++){
                if(Answers.get(i).equals(finalAnswer.get(i))) score++;
            }
        }
        databaseReference.child("Enrollment-NO").child(enrollment_number).child("score").setValue(Integer.toString(score));
        Intent intent = new Intent(getApplicationContext(),FinalScore.class);
        intent.putExtra("score",score);
        intent.putExtra("subject",subject);
        intent.putExtra("totalQuestions",totalQuestions);
        intent.putExtra("id",passKey.split(":#join my quiz#:")[1]);
        intent.putExtra("name",passKey.split(":#join my quiz#:")[0]);
        Toast.makeText(this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
    public void setNext(View view){
        if(radioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Please Select a Option", Toast.LENGTH_SHORT).show();
            return;
        }
        setScore();
        if(count==totalQuestions-1) next.setText(getString(R.string.submit));
        if(count<totalQuestions){
            radioGroup.clearCheck();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    questionDisplay.setText(snapshot.child(Integer.toString(count)).child("question").getValue(String.class));
                    op1.setText(snapshot.child(Integer.toString(count)).child("op1").getValue(String.class));
                    op2.setText(snapshot.child(Integer.toString(count)).child("op2").getValue(String.class));
                    op3.setText(snapshot.child(Integer.toString(count)).child("op3").getValue(String.class));
                    op4.setText(snapshot.child(Integer.toString(count)).child("op4").getValue(String.class));
                    Answers.add(snapshot.child(Integer.toString(count)).child("ans").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Join.this, "Error "+error.getDetails(), Toast.LENGTH_SHORT).show();
                }
            });
            count++;
        }
        else{
            checkAnswer();
        }
    }
}