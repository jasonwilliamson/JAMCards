package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class DeckQuizStatsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private Deck mDeck;
    private List<Card> mCards;
    private static Context context;

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, DeckQuizStatsActivity.class);
        context = packageContent;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_quiz_stats);

        // retrieve deck ID
        UUID deckId = (UUID) getIntent()
                .getSerializableExtra(CardListActivity.EXTRA_DECK_ID);
        mDeck = DeckLab.get(this).getDeck(deckId);
        mCards = CardLab.get(this).getCards(deckId);

        // remove this
        // mDeck.setCategoryA(mDeck.getCategoryA() + 1);

        createPieChart();
        setCardStats();
        //createCardStatList();
    }

    private void setCardStats()
    {
        // get list of values to display from cards
        List<String> cardStats = new ArrayList<String>();

        float maxCorrectRatio = 0;
        String maxCorrectTitle = "";

        float minCorrectRatio = 100;
        String minCorrectTitle = "";

        for (int i = 0; i < mCards.size(); ++i) {
            if (mCards.get(i).getTotalCount() > 0) {
                float correctRatio =  ((float) mCards.get(i).getCorrectCount() / (float) mCards.get(i).getTotalCount()) * 100;

                if (correctRatio >= maxCorrectRatio && mCards.get(i).isShown()) {
                    maxCorrectRatio = correctRatio;
                    maxCorrectTitle = mCards.get(i).getTitle();
                }

                if (correctRatio <= minCorrectRatio && mCards.get(i).isShown()) {
                    minCorrectRatio = correctRatio;
                    minCorrectTitle = mCards.get(i).getTitle();
                }
            }
        }

        if (maxCorrectTitle != "")
        {
            TextView maxCorrectView = (TextView) findViewById(R.id.best_card_text);
            maxCorrectView.setText("Most correctly answered card: " + maxCorrectTitle + " - " + maxCorrectRatio + "%");
        }
        if (minCorrectTitle != "")
        {
            TextView minCorrectView = (TextView) findViewById(R.id.worst_card_text);
            minCorrectView.setText("Least correctly answered card: " + minCorrectTitle + " - " + minCorrectRatio + "%");
        }
    }

    private void createPieChart()
    {
        float totalQuizRuns = mDeck.getCategoryA() + mDeck.getCategoryB() + mDeck.getCategoryC() + mDeck.getCategoryD() + mDeck.getCategoryF();

        // only initialize chart if quiz data has been recorded
        if (totalQuizRuns > 0) {
            // styling
            pieChart = findViewById(R.id.quizPieChart);
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(0f);
            pieChart.setTransparentCircleRadius(0f);
            pieChart.setDrawEntryLabels(true);
            pieChart.setEntryLabelTextSize(15);
            pieChart.setEntryLabelColor(Color.BLACK);

            // retrieve chart data from deck
            ArrayList<PieEntry> pieSlices = new ArrayList<>();
            ArrayList<String> pieLabels = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();


            // A range
            if (mDeck.getCategoryA() > 0) {
                float aRuns = ((float)mDeck.getCategoryA()/totalQuizRuns) * 100;
                pieSlices.add(new PieEntry(aRuns, "80-100% Correct", mDeck.getCategoryA()));
                pieLabels.add("80-100% Correct");
                colors.add(Color.GREEN);
            }

            // B range
            if (mDeck.getCategoryB() > 0) {
                float bRuns = ((float)mDeck.getCategoryB()/totalQuizRuns) * 100;
                pieSlices.add(new PieEntry(bRuns, "70-79% Correct", mDeck.getCategoryB()));
                pieLabels.add("70-79% Correct");
                colors.add(Color.CYAN);
            }

            // C range
            if (mDeck.getCategoryC() > 0) {
                float cRuns = ((float)mDeck.getCategoryC()/totalQuizRuns) * 100;
                pieSlices.add(new PieEntry(cRuns, "60-69% Correct", mDeck.getCategoryC()));
                pieLabels.add("60-69% Correct");
                colors.add(Color.YELLOW);
            }

            // D range
            if (mDeck.getCategoryD() > 0) {
                float dRuns = ((float)mDeck.getCategoryD()/totalQuizRuns) * 100;
                pieSlices.add(new PieEntry(dRuns, "50-59% Correct", mDeck.getCategoryD()));
                pieLabels.add("50-59% Correct");
                colors.add(Color.MAGENTA);
            }

            // F range
            if (mDeck.getCategoryF() > 0) {
                float fRuns = ((float)mDeck.getCategoryF()/totalQuizRuns) * 100;
                pieSlices.add(new PieEntry(fRuns, "<50% Correct", mDeck.getCategoryF()));
                pieLabels.add("<50% Correct");
                colors.add(Color.RED);
            }

            PieDataSet quizDataSet = new PieDataSet(pieSlices, "Quiz Grades");
            quizDataSet.setColors(colors);

            PieData quizData = new PieData(quizDataSet);
            pieChart.setData(quizData);
            pieChart.getLegend().setEnabled(false);
            pieChart.invalidate();
        }
    }
}
