package se.umu.cs.warn.buster.thirtygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main activity for dice game Thirty. Consists of two buttons; one to play the game and one to
 * check high score.
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creator.
     * @param savedInstanceState Bundle containing instance. Instance will always be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Main Menu");
        configureStartGameButton();
        configureHighScoresButton();
    }

    /**
     * Configures the start game button to start the actual game activity.
     * @see se.umu.cs.warn.buster.thirtygame.GameActivity
     */
    private void configureStartGameButton() {
        Button startBtn = (Button) findViewById(R.id.startGameBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }

    /**
     * Configures the high score button to start the high score activity.
     * @see se.umu.cs.warn.buster.thirtygame.HighScoreActivity
     */
    private void configureHighScoresButton() {
        Button highScoresBtn = (Button) findViewById(R.id.highScoreBtn);
        highScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighScoreActivity.class));
            }
        });
    }
}
