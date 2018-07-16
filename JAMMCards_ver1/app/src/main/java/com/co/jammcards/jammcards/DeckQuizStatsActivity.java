package com.co.jammcards.jammcards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, DeckQuizStatsActivity.class);
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

        // remove this later
        //initDummyChartData();

        createPieChart();
    }

    private void initDummyChartData()
    {
        mDeck.setCategoryA(6);
        mDeck.setCategoryB(9);
        mDeck.setCategoryC(3);
        mDeck.setCategoryD(0);
        mDeck.setCategoryF(0);
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
            pieChart.setDrawEntryLabels(true);
            pieChart.setEntryLabelTextSize(15);
            pieChart.setEntryLabelColor(Color.BLACK);

            // retrieve chart data from deck
            ArrayList<PieEntry> pieSlices = new ArrayList<>();
            ArrayList<String> pieLabels = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();


            // A range
            float aRuns = ((float)mDeck.getCategoryA()/totalQuizRuns) * 100;
            pieSlices.add(new PieEntry(aRuns, "80-100% Correct", mDeck.getCategoryA()));
            if (aRuns > 0) {
                pieLabels.add("80-100% Correct");
            }

            // B range
            float bRuns = ((float)mDeck.getCategoryB()/totalQuizRuns) * 100;
            pieSlices.add(new PieEntry(bRuns, "70-79% Correct", mDeck.getCategoryB()));
            if (bRuns > 0) {
                pieLabels.add("70-79% Correct");
            }

            // C range
            float cRuns = ((float)mDeck.getCategoryC()/totalQuizRuns) * 100;
            pieSlices.add(new PieEntry(cRuns, "60-69% Correct", mDeck.getCategoryC()));
            if (cRuns > 0) {
                pieLabels.add("60-69% Correct");
            }

            // D range
            float dRuns = ((float)mDeck.getCategoryD()/totalQuizRuns) * 100;
            pieSlices.add(new PieEntry(dRuns, "50-59% Correct", mDeck.getCategoryD()));
            if (dRuns > 0) {
                pieLabels.add("50-59% Correct");
            }

            // F range
            float fRuns = ((float)mDeck.getCategoryF()/totalQuizRuns) * 100;
            pieSlices.add(new PieEntry(fRuns, "<50% Correct", mDeck.getCategoryF()));
            if (fRuns > 0) {
                pieLabels.add("<50% Correct");
            }

            colors.add(Color.GREEN);
            colors.add(Color.BLUE);
            colors.add(Color.YELLOW);
            colors.add(Color.MAGENTA);
            colors.add(Color.RED);

            PieDataSet quizDataSet = new PieDataSet(pieSlices, "Quiz Grades");
            quizDataSet.setColors(colors);

            PieData quizData = new PieData(quizDataSet);
            pieChart.setData(quizData);
            pieChart.getLegend().setEnabled(false);
            pieChart.invalidate();
        }
    }
}
