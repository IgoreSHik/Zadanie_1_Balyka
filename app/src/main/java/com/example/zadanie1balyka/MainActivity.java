package com.example.zadanie1balyka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lombok.Getter;
import lombok.Setter;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;
    private Button hintButton;
    private static final int REQUEST_CODE_PROMPT = 0;

    public static boolean answerWasShown = false;

    private static final String QUIZ_TAG = "MainActivity";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG, "Jestem onSaveInstanceState. Wywolany.");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        hintButton = findViewById(R.id.hint_button);

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));
        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));

        hintButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Hint.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex+1)%questions.length;
                answerWasShown = false;
                setNextQuestion();
            }
        });
        setNextQuestion();
        Log.d(QUIZ_TAG, "Jestem onCreate. Wywolany.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(QUIZ_TAG, "Jestem onStart. Wywolany.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(QUIZ_TAG, "Jestem onResume. Wywolany.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(QUIZ_TAG, "Jestem onPause. Wywolany.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(QUIZ_TAG, "Jestem onStop. Wywolany.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(QUIZ_TAG, "Jestem onDestroy. Wywolany.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {return;}
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) {return;}
            answerWasShown = data.getBooleanExtra(Hint.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    @Setter
    @Getter
    public class Question {
        private int questionId;
        private boolean trueAnswer;

        public Question(int questionId, boolean trueAnswer){
            this.questionId = questionId;
            this.trueAnswer = trueAnswer;
        }
    }

    private Question[] questions = new Question[] {
            new Question(R.string.q_platypus, true),
            new Question(R.string.q_ctOS, true),
            new Question(R.string.q_fortnite, false),
            new Question(R.string.q_patronus, true),
            new Question(R.string.q_totoro, false)
    };

    private int currentIndex = 0;

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown){
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = R.string.correct_answer;
            } else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }
}