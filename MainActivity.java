package com.dsf.pe.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dsf.pe.topquiz.R;
import com.dsf.pe.topquiz.model.ScoreBoard;
import com.dsf.pe.topquiz.model.User;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //Déclaration des variables
    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mScoreButton;
    private User mUser;
    public ScoreBoard SB;
    public File file;

    public static String SCOREBOARD_FILENAME = "TopQuizScore.txt";

    public static final int GAME_ACTIVITY_REQUEST_CODE = 68;
    public static final int SCORE_ACTIVITY_REQUEST_CODE = 79;


    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";
    public static final String PREF_KEY_LASTNAME = "PREF_KEY_LASTNAME";
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_SCOREBOARD = "PREF_KEY_SCOREBOARD";

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User();
        SB = new ScoreBoard();

        // Branchement des variables avec leur ID
        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mScoreButton = (Button) findViewById(R.id.activity_main_score_btn);

        // On définit le fichier des scores
        File root = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        file = new File(dir, SCOREBOARD_FILENAME);

        mPreferences = getPreferences(MODE_PRIVATE);

        // On lit le fichier des scores
        SB.ReadScoreBoard(file);

        // Désactivation du bouton play
        mPlayButton.setEnabled(false);

        // Désactivation bouton score si il n'y a pas de fichier
        if(SB.getScore(0)!= 0) {
            mScoreButton.setVisibility(View.VISIBLE);
            mScoreButton.setEnabled(true);
        }
        else
            mScoreButton.setVisibility(View.INVISIBLE);

        greetUser();

        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // activation du bouton play
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setFirstName(mNameInput.getText().toString());
                mUser.setLastName(mNameInput.getText().toString());

                // Enregistre le nom de l'utilisateur
                mPreferences.edit().putString(PREF_KEY_LASTNAME, mUser.getLastname()).apply();

                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scoreActivity = new Intent(MainActivity.this, ScoreActivity.class);
                scoreActivity.putExtra(PREF_KEY_SCOREBOARD,SB);
                startActivityForResult(scoreActivity, SCORE_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            SB.SetScoreBoard(mUser.getFirstName(),mUser.getLastname(),score);
            SB.WriteScoreBoard(file);
            mScoreButton.setVisibility(View.VISIBLE);
            mScoreButton.setEnabled(true);

            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            greetUser();
        }
    }

    private void greetUser() {
        String lastname = mPreferences.getString(PREF_KEY_LASTNAME,null);

        if (lastname!=null){
            int score = mPreferences.getInt(PREF_KEY_SCORE,0);
            String greetingtxt = "Bonjour "+lastname
                    +"\nvotre ancien score est :"+ score
                    +"\nferiez-vous mieux cette fois?";

            mGreetingText.setText(greetingtxt);
            mNameInput.setText(lastname);
            mNameInput.setSelection(lastname.length()); //positionne le curseur après le prénom
            mPlayButton.setEnabled(true);
        }
    }

    //OVERRIDES
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MainActivity::onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("MainActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity::onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("MainActivity::onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity::onResume()");
    }
}
