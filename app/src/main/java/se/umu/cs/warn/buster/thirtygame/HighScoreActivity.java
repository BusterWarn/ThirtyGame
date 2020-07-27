package se.umu.cs.warn.buster.thirtygame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import se.umu.cs.warn.buster.thirtygame.model_high_score.ScoreDatabase;
import se.umu.cs.warn.buster.thirtygame.model_high_score.ScoreEntity;

/**
 * Activity for viewing high score. Uses a Room database to save scores.
 *
 * @see <a href="https://developer.android.com/training/data-storage/room">Save data in a local
 * database using Room</a>
 * @see se.umu.cs.warn.buster.thirtygame.model_high_score
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class HighScoreActivity extends AppCompatActivity {

    ScoreDatabase db;

    /**
     * Creator.
     * @param savedInstanceState bundle containing name and score of finished game if it is to be
     *                           inserted into database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getSupportActionBar().setTitle("High Scores");

        db = ScoreDatabase.getInstance(this);


        String name = getIntent().getStringExtra(Consts.gameName);
        int score = getIntent().getIntExtra(Consts.gameScore, -1);

        // If there is a name to save.
        if (name != null && !name.equals("") && score >= 0) {
            addNewScore(name, score);
            getIntent().putExtra(Consts.gameScore, -1);
            getIntent().putExtra(Consts.gameName, "");
        }
        updateHighScoreTable();

        configureMainMenuBtn();
    }

    /**
     * Configures button to exit activity by calling finnish().
     */
    private void configureMainMenuBtn() {
        Button mainMenuBtn = (Button) findViewById(R.id.main_menu_button);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Adds a new score to the table.
     * @param name Name of person who scored.
     * @param score Their score.
     */
    private void addNewScore(String name, int score) {

        db.scoreDao().insert(new ScoreEntity(name, score));
    }

    /**
     * Updated the table in the ScrollView by inserting all entries.
     */
    private void updateHighScoreTable() {

        ArrayList<ScoreEntity> scores = (ArrayList<ScoreEntity>) db.scoreDao().getScores();
        for (ScoreEntity score : scores) {
            addRowToTable(score.name, score.score);
        }
    }

    /**
     * Adds a single row to the table in the ScrollView.
     * @param name Name of person who scored.
     * @param score Their score.
     */
    private void addRowToTable(String name, int score) {
        TableLayout tableLayout = findViewById(R.id.high_score_table);
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView rowName = (TextView)getLayoutInflater()
                .inflate(R.layout.style_text_view_high_scores_name, null);
        TextView rowScore = (TextView)getLayoutInflater()
                .inflate(R.layout.style_text_view_high_scores_score, null);
        rowName.setText(name);
        rowScore.setText(String.valueOf(score));

        row.addView(rowName);
        row.addView(rowScore);
        tableLayout.addView(row);
    }
}
