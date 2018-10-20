package com.dsf.pe.topquiz.controller;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dsf.pe.topquiz.R;
import com.dsf.pe.topquiz.model.Question;
import com.dsf.pe.topquiz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mQuestionTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "Bundle Extra";
    public static final String BUNDLE_STATE_SCORE = "Bundle Score courant";
    public static final String BUNDLE_STATE_QUESTION = "Bundle Question courante";
    public static final int NUMBER_OF_QUESTION = 4;

    // Pour la gestion des évènements
    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("GameActivity::onCreate()");

        mQuestionBank = this.generateQuestion();

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = NUMBER_OF_QUESTION;
        }

        mEnableTouchEvents = true;

        // Branchement des variables avec leur ID
        mQuestionTextView = (TextView) findViewById(R.id.activity_game_question_text);
        mAnswerButton1 = (Button) findViewById(R.id.activity_game_answer1_btn);
        mAnswerButton2 = (Button) findViewById(R.id.activity_game_answer2_btn);
        mAnswerButton3 = (Button) findViewById(R.id.activity_game_answer3_btn);
        mAnswerButton4 = (Button) findViewById(R.id.activity_game_answer4_btn);

        // Numérotation des boutons pour les différentier
        mAnswerButton1.setTag(0);
        mAnswerButton2.setTag(1);
        mAnswerButton3.setTag(2);
        mAnswerButton4.setTag(3);

        // The tag value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Enregistre le score et la question courante en cas de réinitialisation lors d'une rotation de l'écran
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        super.onSaveInstanceState(outState);
    }

    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        mQuestionTextView.setText( question.getQuestion());
        mAnswerButton1.setText(question.getChoiceList().get(0));
        mAnswerButton2.setText(question.getChoiceList().get(1));
        mAnswerButton3.setText(question.getChoiceList().get(2));
        mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestion(){
        Question question1 = new Question("Quelle est la capitale de la France",
                Arrays.asList("Berlin","Rome","Lisbonne","Paris"),3);

        Question question2  = new Question("Quelle est la capitale de l'Italie",
                Arrays.asList("Berlin","Rome","Lisbonne","Paris"),1);

        Question question3 = new Question("Quelle est la capitale de l'Espagne",
                Arrays.asList("Madrid","Rome","Lisbonne","Paris"),0);

        Question question4 = new Question("Quelle est la capitale du Portugal",
                Arrays.asList("Madrid","Rome","Lisbonne","Paris"),2);

        Question question5 = new Question("Quelle est la capitale de la Suisse",
                Arrays.asList("Madrid","Bern","Lisbonne","Paris"),1);

        Question question6 = new Question("Quelle est la capitale de la Belgique",
                Arrays.asList("Madrid","Bern","Lisbonne","Bruxelles"),3);

        Question question7 = new Question("Quelle est la capitale de la Corée du Sud",
                Arrays.asList("Paris","Séoul","Lisbonne","Pyeongyang"),1);

        Question question8 = new Question("Quelle est la capitale de l'Allemagne",
                Arrays.asList("Paris","Rome","Berlin","Bern"),2);

        Question question9 = new Question("Quelle est la capitale de la Hollande",
                Arrays.asList("Madrid","Amsterdam","Rome","Oslo"),1);

        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9));
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if(responseIndex == mCurrentQuestion.getAnswerIndex()){
            // Bonne réponse
            Toast.makeText(this, "Bonne réponse!", Toast.LENGTH_SHORT).show();
            mScore+=100;
        } else {
            // Mauvaise réponse
            Toast.makeText(this, "Mauvaise réponse!", Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;

                if (--mNumberOfQuestions == 0) {
                    // No question left, end the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bravo!")
                .setMessage("Votre score est:" + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // End the activity
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("GameActivity::onDestroy()");
    }
}
