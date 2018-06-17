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

public class DeckQuizStatsActivity extends AppCompatActivity {

    PieChart pieChart;

    public static Intent newIntent(Context packageContent) {
        Intent intent = new Intent(packageContent, DeckQuizStatsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_quiz_stats);

        pieChart = findViewById(R.id.quizPieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(10f);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(15);
        pieChart.setEntryLabelColor(Color.BLACK);

        // hardcoded prototype demo data begins here
        ArrayList<PieEntry> pieSlices = new ArrayList<>();
        ArrayList<String> pieLabels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < 5; ++i)
        {
            String letterGrade = "";

            switch (i) {
                case 0:
                    letterGrade = "<50% Correct";
                    break;
                case 1:
                    letterGrade = "50-59% Correct";
                    break;
                case 2:
                    letterGrade = "60-69% Correct";
                    break;
                case 3:
                    letterGrade = "70-79% Correct";
                    break;
                case 4:
                    letterGrade = "80-100% Correct";
                    break;
            }

            pieSlices.add(new PieEntry(20f, letterGrade, 5));
            pieLabels.add(letterGrade);
        }

        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);

        PieDataSet quizDataSet = new PieDataSet(pieSlices, "Quiz Grades");
        quizDataSet.setColors(colors);

        PieData quizData = new PieData(quizDataSet);
        pieChart.setData(quizData);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }
}
