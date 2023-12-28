package com.example.trivaapp.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trivaapp.controller.AppController;
import com.example.trivaapp.model.Question;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    public interface QuestionCallback {
        void onSuccess(List<Question> questions);

        void onError(String errorMessage);
    }

    private static final String URL = "https://opentdb.com/api.php?amount=10&category=18&difficulty=medium&type=boolean";

    public void getQuestions(QuestionCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        // Extract the "results" array from the response
                        JSONArray resultsArray = response.getJSONArray("results");
                        List<Question> questions = parseQuestions(resultsArray);
                        callback.onSuccess(questions);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Failed to parse response");
                    }
                },
                error -> {
                    callback.onError("Failed with error: " + error.getMessage());
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private List<Question> parseQuestions(JSONArray jsonArray) throws JSONException {
        List<Question> questions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String category = jsonObject.getString("category");
            String questionText = jsonObject.getString("question");
            boolean correctAnswer = Boolean.parseBoolean(jsonObject.getString("correct_answer"));
            List<String> incorrectAnswers = parseIncorrectAnswers(jsonObject.getJSONArray("incorrect_answers"));

            Question question = new Question(category, questionText, correctAnswer, incorrectAnswers);
            questions.add(question);
        }

        return questions;
    }

    private List<String> parseIncorrectAnswers(JSONArray jsonArray) throws JSONException {
        List<String> incorrectAnswers = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            incorrectAnswers.add(jsonArray.getString(i));
        }
        return incorrectAnswers;
    }
}
