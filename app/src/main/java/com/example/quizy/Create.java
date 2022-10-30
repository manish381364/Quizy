package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Create extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button submit;
    EditText subject;
    int ques;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        hook();
        submit.setOnClickListener(view -> Submit());
    }


    private void Submit() {
        String noOfQuestions = editText.getText().toString();
        String Subject = subject.getText().toString();
        if(noOfQuestions.length()==0){
            editText.setError("Please Enter No of Questions");
            editText.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(Subject)){
            subject.setError("Please Enter Subject");
            subject.requestFocus();
            return;
        }
        try {
            ques = Integer.parseInt(noOfQuestions);
            if(ques<=0){
                editText.setError("Please Enter Valid Number");
                editText.requestFocus();
                return;
            }
            Intent intent1 = new Intent(getApplicationContext(),CreateQuestionPaper.class);
            intent1.putExtra("noOfQuestions",ques);
            intent1.putExtra("subject",Subject);
            finish();
            startActivity(intent1);
        }
        catch (Exception e){
            editText.setError("Please Enter Valid Number");
            editText.requestFocus();
        }
    }


    private void hook(){
        submit = findViewById(R.id.submit);
        textView = findViewById(R.id.questionDisplay);
        editText = findViewById(R.id.numberOfQuestions);
        subject = findViewById(R.id.subject);
    }
}