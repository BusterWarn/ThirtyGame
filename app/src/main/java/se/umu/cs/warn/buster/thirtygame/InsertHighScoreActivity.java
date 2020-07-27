package se.umu.cs.warn.buster.thirtygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity for inserting high score. This activity is used after the end of a game to save users
 * score.
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class InsertHighScoreActivity extends AppCompatActivity {

    /**
     * Creator.
     * @param savedInstanceState bundle containing the score from played game.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_high_score);
        getSupportActionBar().setTitle("Insert New High Score");

        configureSaveScoreBtn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        String name = ((EditText) findViewById(R.id.username)).getText().toString();
        if (name.length() == 0) {
            name = null;
        }
        savedInstanceState.putString(Consts.gameName, name);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.getString(Consts.gameName) != null) {
            EditText username = (EditText) findViewById(R.id.username);
            username.setText(savedInstanceState.getString(Consts.gameName),
                    TextView.BufferType.EDITABLE);
        }
    }

    /**
     * Configures the Save score button to open High Score activity, passing name + score and then
     * calling finnish().
     * @see se.umu.cs.warn.buster.thirtygame.HighScoreActivity
     */
    private void configureSaveScoreBtn() {
        Button startBtn = (Button) findViewById(R.id.saveScoreBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {

            EditText username = (EditText)findViewById(R.id.username);
            @Override
            public void onClick(View v) {
                if (username.getText().length() > 0) {
                    Intent intent = new Intent(InsertHighScoreActivity.this,
                            HighScoreActivity.class);
                    intent.putExtra(Consts.gameScore, getIntent()
                            .getIntExtra(Consts.gameScore, -1));
                    intent.putExtra(Consts.gameName, username.getText().toString());
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

}
