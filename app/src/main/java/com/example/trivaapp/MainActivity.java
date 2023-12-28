package com.example.trivaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trivaapp.data.Repository;
import com.example.trivaapp.model.Question;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private TextView QuesNum;
    private Button TrueBtn;
    private Button FalseBtn;
    private Button ScoreBTN;
    int score;
    int QuesNo = 1;
    private List<Question> questions;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.data);
        Button nextButton = findViewById(R.id.Nextbutton);
        TrueBtn = findViewById(R.id.TrueBtn);
        FalseBtn = findViewById(R.id.FalseBtn);
        ScoreBTN = findViewById(R.id.ScoreBtn);
        QuesNum = findViewById(R.id.Question_Number);

        Repository repository = new Repository();

        repository.getQuestions(new Repository.QuestionCallback() {
            @Override
            public void onSuccess(List<Question> fetchedQuestions) {

                questions = fetchedQuestions;

                showQuestion(currentQuestionIndex);// Display the first question initially

                TrueBtn.setOnClickListener(view -> {
                    userResponse(true);
                });

                FalseBtn.setOnClickListener(view -> {
                    userResponse(false);
                });

                ScoreBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, Scoreboard.class);
                        intent.putExtra("Key",score);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("MainActivity", "Error: " + errorMessage);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuesNum.setText(String.format("Question: %d/%d", QuesNo, questions.size()));
                showNextQuestion();
            }

        });

    }
    private void userResponse(boolean userResp) {
        Question currentQuestion = questions.get(currentQuestionIndex);

        if (userResp != currentQuestion.getCorrectAnswer()) {
            tv.setTextColor(getResources().getColor(R.color.red));
            score--;
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
            shakeAnimation();
        } else {
            tv.setTextColor(getResources().getColor(R.color.green));
            score++;
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            shakeAnimation();
        }

    }

    private void showQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            Question question = questions.get(index);
            String questionString = createQuestionString(question);
            tv.setText(questionString);
        } else {
            tv.setText("No more questions");
        }
    }

    private void showNextQuestion() {
        tv.setTextColor(getResources().getColor(R.color.quesColour));
        QuesNo++;
        currentQuestionIndex++;
        showQuestion(currentQuestionIndex);
    }

    private String createQuestionString(Question question) {
        StringBuilder stringBuilder1 = new StringBuilder();

//        stringBuilder1.append("Category: ").append(question.getCategory()).append("\n");
        stringBuilder1.append("Question: ").append(question.getQuestion()).append("\n");

//        stringBuilder1.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n");
//        stringBuilder1.append("Incorrect Answers: ").append(question.getIncorrectAnswers()).append("\n");

        return stringBuilder1.toString();
    }
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        tv.startAnimation(shake);
    }

}
