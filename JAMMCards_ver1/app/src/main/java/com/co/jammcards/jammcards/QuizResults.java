package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

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

        Button m_stats_button = (Button) findViewById(R.id.view_deck_stats);
        m_stats_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewStats();
            }
        });

        resultsTextView = (TextView) findViewById(R.id.quiz_results_text);
        int score = getIntent().getIntExtra(MainQuiz.QUIZ_RESULT_CORRECT, 0);
        int max_score = getIntent().getIntExtra(MainQuiz.QUIZ_RESULT_TOTAL, score);
        resultsTextView.setText((String)(Integer.toString(score) + " / " + Integer.toString(max_score)));

        UUID deckId = (UUID) getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        Deck deck = DeckLab.get(this).getDeck(deckId);
        float percent = ((float) score) / ((float) max_score);
        if (percent >= 0.80) {
            deck.setCategoryA(deck.getCategoryA());
        } else if (percent >= 0.70) {
            deck.setCategoryB(deck.getCategoryB());
        } else if (percent >= 0.60) {
            deck.setCategoryC(deck.getCategoryC());
        } else if (percent >= 0.50) {
            deck.setCategoryD(deck.getCategoryD());
        } else {
            deck.setCategoryF(deck.getCategoryF());
        }
    }

    void viewStats() {
        Intent intent = DeckQuizStatsActivity.newIntent(this);
        startActivity(intent);
    }

}
