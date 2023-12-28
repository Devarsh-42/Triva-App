package com.example.trivaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        TextView Your_Score = findViewById(R.id.Your_score);
        Intent intent = getIntent();
        if(intent != null){
            int score = intent.getIntExtra("Key",0);
            Your_Score.setText(String.valueOf(score));
        }
    }
}