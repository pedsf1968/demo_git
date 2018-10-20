package com.dsf.pe.topquiz.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.dsf.pe.topquiz.R;
import com.dsf.pe.topquiz.model.ScoreBoard;

public class ScoreActivity extends AppCompatActivity {
    //Déclaration des variables
    private static final int SCOREBOARD_LENGTH = 5;
    private static int SORT_BY_FIRST_NAME = 0;
    private static int SORT_BY_LAST_NAME = 1;
    private static int SORT_BY_SCORE = 2;
    public static final String PREF_KEY_SCOREBOARD = "PREF_KEY_SCOREBOARD";

    private TextView mGreetingText;
    private TextView mName1text, mName2text, mName3text, mName4text, mName5text;
    private TextView mScore1text, mScore2text, mScore3text, mScore4text, mScore5text;

    private Button mAlphabeticButton;
    private Button mScoreButton;

    private ScoreBoard SB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Branchement des variables avec leur ID
        mGreetingText = (TextView) findViewById(R.id.activity_score__text);

        mName1text = (TextView) findViewById(R.id.activity_score__name1_text);
        mName2text = (TextView) findViewById(R.id.activity_score__name2_text);
        mName3text = (TextView) findViewById(R.id.activity_score__name3_text);
        mName4text = (TextView) findViewById(R.id.activity_score__name4_text);
        mName5text = (TextView) findViewById(R.id.activity_score__name5_text);

        mScore1text = (TextView) findViewById(R.id.activity_score__score1_text);
        mScore2text = (TextView) findViewById(R.id.activity_score__score2_text);
        mScore3text = (TextView) findViewById(R.id.activity_score__score3_text);
        mScore4text = (TextView) findViewById(R.id.activity_score__score4_text);
        mScore5text = (TextView) findViewById(R.id.activity_score__score5_text);

        mAlphabeticButton = (Button) findViewById(R.id.activity_score_alphabetique_btn);
        mScoreButton = (Button) findViewById(R.id.activity_score_score_btn);

        mScoreButton.setEnabled(false);
        mAlphabeticButton.setEnabled(true);

        // on récupère les scores
        SB = new ScoreBoard();

        Intent intent = getIntent();
        if(intent!=null){
            SB = intent.getParcelableExtra(PREF_KEY_SCOREBOARD);
        }

        // On place les listeners pour les boutons
        mAlphabeticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreButton.setEnabled(true);
                mAlphabeticButton.setEnabled(false);
                ShowSortedScore(SORT_BY_LAST_NAME);
            }
        });

        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScoreButton.setEnabled(false);
                mAlphabeticButton.setEnabled(true);
                ShowSortedScore(SORT_BY_SCORE);
            }
        });

        ShowSortedScore(SORT_BY_SCORE);
    }

    public void ShowSortedScore(int index) {

        if(index==SORT_BY_FIRST_NAME)
            SB.SortScoreBoard(SORT_BY_FIRST_NAME);
        else if(index ==SORT_BY_LAST_NAME)
            SB.SortScoreBoard(SORT_BY_LAST_NAME);
        else
            SB.SortScoreBoard(SORT_BY_SCORE);

        mName1text.setText(SB.getLastName(0));
        mName2text.setText(SB.getLastName(1));
        mName3text.setText(SB.getLastName(2));
        mName4text.setText(SB.getLastName(3));
        mName5text.setText(SB.getLastName(4));

        mScore1text.setText(String.valueOf(SB.getScore(0)));
        mScore2text.setText(String.valueOf(SB.getScore(1)));
        mScore3text.setText(String.valueOf(SB.getScore(2)));
        mScore4text.setText(String.valueOf(SB.getScore(3)));
        mScore5text.setText(String.valueOf(SB.getScore(4)));
    }
}