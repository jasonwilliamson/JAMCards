package com.co.jammcards.jammcards;

import android.graphics.Matrix;
import android.media.ExifInterface;
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
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainQuiz extends AppCompatActivity {

    private int Max_Cards;
    private int max_score;
    private int current_card;
    private int score;
    private boolean answer;
    private boolean correct;
    private String[] answers;
    private ImageView cardImageView;
    private TextView currentCardTextView;
    private File cardImageFile;
    private Deck mDeck;
    private List<Card> mCards;
    private Card mCard;

    public static final String QUIZ_RESULT_CORRECT =
            "come.co.jammcards.jammcards.quiz_result_correct";
    public static final String QUIZ_RESULT_TOTAL =
            "come.co.jammcards.jammcards.quiz_result_total";

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, MainQuiz.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        UUID deckId = (UUID) getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        mDeck = DeckLab.get(this).getDeck(deckId);
        mCards = CardLab.get(this).getCards(deckId);

        cardImageView = (ImageView) findViewById(R.id.card_view);
        currentCardTextView = (TextView) findViewById(R.id.card_text);

        Max_Cards = mCards.size();
        max_score = Max_Cards;
        current_card = 0;
        score = 0;
        answer = false;
        correct = false;
        answers = new String[Max_Cards];
        for (int i = 0; i < answers.length; i++) {
            answers[i] = "Answer - " + Integer.toString(i+1);
        }

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
                nextCard();
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
                correct = true;
                mCard.setCorrectCount(mCard.getCorrectCount() + 1);
                mCard.setTotalCount(mCard.getTotalCount() + 1);
                CardLab.get(MainQuiz.this).updateCard(mCard);
                nextCard();
            }
        });

        Button m_wrong_button = (Button) findViewById(R.id.wrong_button);
        m_wrong_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = false;
                mCard.setTotalCount(mCard.getTotalCount() + 1);
                CardLab.get(MainQuiz.this).updateCard(mCard);
                nextCard();
            }
        });
    }

    private void updateCurrentCard() {
        mCard = mCards.get(current_card);
        if (mCard == null) return;
        if (!mCard.isShown()) {
            max_score --;
            nextCard();
            return;
        }
        if (answer) {
            cardImageFile = CardLab.get(this).getBackPhotoFile(mCard);
        } else {
            cardImageFile = CardLab.get(this).getPhotoFile(mCard);
        }
        updatePhotoView();
        if (answer) {
            currentCardTextView.setText(mCard.getBackText());
        } else {
            currentCardTextView.setText(mCard.getText());
        }
    }

    public Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void updatePhotoView() {
        Bitmap mBitmap;
        if (cardImageFile == null || !cardImageFile.exists()) {
            cardImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    cardImageFile.getPath(), this);

            Bitmap workingBitmap = null;
            try {
                workingBitmap = modifyOrientation(bitmap, cardImageFile.getAbsolutePath());
            }catch (IOException e){
                e.printStackTrace();
            }

            bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            cardImageView.setImageBitmap(bitmap);

        }


    }

    private void endQuiz() {
        UUID deckId = (UUID) getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        Intent intent = QuizResults.newIntent(this);
        intent.putExtra(MainQuiz.QUIZ_RESULT_CORRECT, score);
        intent.putExtra(MainQuiz.QUIZ_RESULT_TOTAL, max_score);
        intent.putExtra(CardListActivity.EXTRA_DECK_ID, deckId);
        startActivity(intent);
        finish();
    }

    private void nextCard() {
        current_card ++;
        answer = false;
        if (correct) {
            score ++;
        }
        correct = false;
        if (current_card >= Max_Cards) {
            endQuiz();
            return;
        }
        updateCurrentCard();
    }
}
