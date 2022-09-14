package com.example.quizy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.circularreveal.cardview.CircularRevealCardView;

public class FinalScore extends AppCompatActivity {
    int score;
    int totalQuestions;
    TextView result;
    TextView finalScore;
    String subject;
    CircularRevealCardView share;
    CircularRevealCardView leaderboard;
    String id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        subject = intent.getStringExtra("subject");
        result = findViewById(R.id.result);
        share = findViewById(R.id.share);
        finalScore = findViewById(R.id.score);
        leaderboard = findViewById(R.id.leaderBoard);

        totalQuestions = intent.getIntExtra("totalQuestions",0);
        score = intent.getIntExtra("score",0);

        String FinalScore = "Correct "+ score +"/"+ totalQuestions;
        finalScore.setText(FinalScore);
        double tempScore = score;
        final double x = (tempScore/totalQuestions)*100;
        final String textScore = getString(R.string.score)+" \n       "+ x +" %";
        result.setText(textScore);



        //shareButton
        share.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("text/plain");
            String intentText = "My Score is \n"+ x +" %"+" in "+subject;
            intent1.putExtra(Intent.EXTRA_TEXT,intentText);
            Intent intent2 = Intent.createChooser(intent1,"Share Your Score By");
            startActivity(intent2);
        });


        //LeaderBoard
         leaderboard.setOnClickListener(view->{
             Intent jumpToLeaderBoard = new Intent(getApplicationContext(), leaderboard.class);
             jumpToLeaderBoard.putExtra("id",id);
             jumpToLeaderBoard.putExtra("name",name);
             String my_score = getString(R.string.myScore)+"\n"+x+" %";
             jumpToLeaderBoard.putExtra("score",(FinalScore+"\n"+my_score));
             startActivity(jumpToLeaderBoard);
         });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}