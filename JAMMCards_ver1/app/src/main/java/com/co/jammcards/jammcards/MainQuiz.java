package com.co.jammcards.jammcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class MainQuiz extends AppCompatActivity {

    static int Max_Cards = 10;

    private int current_card;
    private boolean answer;
    private boolean[] correct;
    private String[] questions;
    private String[] answers;
    private ImageView cardImageView;
    private TextView currectCardTextView;
    private File cardImageFile;
    private Deck mDeck;
    private List<Card> mCards;
    private Card mCard;

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, MainQuiz.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        current_card = 0;
        answer = false;
        correct = new boolean[Max_Cards];
        for (int i = 0; i < correct.length; i++) {
            correct[i] = false;
        }
        questions = new String[Max_Cards];
        for (int i = 0; i < questions.length; i++) {
            questions[i] = "Question - " + Integer.toString(i+1);
        }
        answers = new String[Max_Cards];
        for (int i = 0; i < answers.length; i++) {
            answers[i] = "Answer - " + Integer.toString(i+1);
        }

        UUID deckId = (UUID) getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        mDeck = DeckLab.get(this).getDeck(deckId);
        mCards = CardLab.get(this).getCards(deckId);

        cardImageView = (ImageView) findViewById(R.id.card_view);
        currectCardTextView = (TextView) findViewById(R.id.card_text);

        cardImageView.setOnTouchListener(new SwipeListener(MainQuiz.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                answer = (!answer);
                updateCurrentCard();
            }
            public void onSwipeLeft() {
                answer = (!answer);
                updateCurrentCard();
            }
            public void onSwipeBottom() {
            }

        });

        updateCurrentCard();

        Button m_next_button = (Button) findViewById(R.id.next_button);
        m_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_card ++;
                if (current_card >= Max_Cards) {
                    current_card = 0;
                }
                updateCurrentCard();
            }
        });

        Button m_flip_button = (Button) findViewById(R.id.flip_button);
        m_flip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = (!answer);
                updateCurrentCard();
            }
        });

        Button m_correct_button = (Button) findViewById(R.id.correct_button);
        m_correct_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct[current_card] = true;
            }
        });

        Button m_wrong_button = (Button) findViewById(R.id.wrong_button);
        m_wrong_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct[current_card] = false;
            }
        });
    }

    private void updateCurrentCard() {
        mCard = mCards.get(current_card);
        if (mCard == null) return;
        cardImageFile = CardLab.get(this).getPhotoFile(mCard);
        updatePhotoView();
        if (answer) {
            currectCardTextView.setText(answers[current_card]);
        } else {
            currectCardTextView.setText(questions[current_card]);
        }
    }
    private void updatePhotoView() {
        if (cardImageFile == null || !cardImageFile.exists()) {
            System.out.println("-|-NULL-|-");
            cardImageView.setImageDrawable(null);
        } else {
            System.out.println("|-|" + cardImageFile.getPath() + "|-|");
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    cardImageFile.getPath(), this);
            cardImageView.setImageBitmap(bitmap);
        }
    }
}
