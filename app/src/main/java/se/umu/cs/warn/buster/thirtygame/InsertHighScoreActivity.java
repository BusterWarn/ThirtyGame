package se.umu.cs.warn.buster.thirtygame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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

        addRowToTable("Total score","", getIntent()
                .getIntExtra(Consts.gameScore, -1));

        String[] strategies = getIntent().getStringArrayExtra(Consts.historyStrategy);
        int[] scores = getIntent().getIntArrayExtra(Consts.historyScore);
        if (strategies != null && scores != null) {
            for (int i = 0; i < strategies.length; i++) {
                if (strategies[i].equals("") || scores[i] == -1)
                    continue;
                addRowToTable("R" + (i + 1), strategies[i], scores[i]);
            }
        }

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

    /**
     * Adds a single row to the table in the ScrollView with two strings and an integer representing
     * a score.
     * @param leftString String to the left, this string will be bold.
     * @param middleString String in the middle.
     * @param score The score, which will be centered right in row.
     */
    private void addRowToTable(String leftString, String middleString, int score) {
        TableLayout tableLayout = findViewById(R.id.score_table);
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView rowLeftString = new TextView(this);
        TextView rowMiddleString = new TextView(this);
        TextView rowScore = new TextView(this);

        rowLeftString.setTextAppearance(this, R.style.ScoreTextViewBoldLeft);
        rowMiddleString.setTextAppearance(this, R.style.ScoreTextViewLeft);
        rowScore.setTextAppearance(this, R.style.ScoreTextViewRight);

        rowLeftString.setText(leftString);
        rowMiddleString.setText(middleString);
        rowScore.setText(String.valueOf(score));

        row.addView(rowLeftString);
        row.addView(rowMiddleString);
        row.addView(rowScore);
        tableLayout.addView(row);
    }

}
