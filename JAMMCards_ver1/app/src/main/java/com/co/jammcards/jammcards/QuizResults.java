package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizResults extends AppCompatActivity {
    private TextView resultsTextView;

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, QuizResults.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        Button m_next_button = (Button) findViewById(R.id.end_quiz);
        m_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resultsTextView = (TextView) findViewById(R.id.quiz_results_text);
        resultsTextView.setText((String) getIntent().getSerializableExtra(MainQuiz.QUIZ_RESULT_STRING));
    }

}
