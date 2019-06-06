package se.umu.cs.warn.buster.thirtygame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import se.umu.cs.warn.buster.thirtygame.model.Dice;
import se.umu.cs.warn.buster.thirtygame.model.Model;

public class MainActivity extends AppCompatActivity {

    private Model   model;
    private int     nrDice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nrDice = 6;
        this.model = new Model(this.nrDice);
        initiateRollDiceButton();
    }

    public void initiateRollDiceButton() {

        Button rollDiceButton = findViewById(R.id.rollDice);
        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateDice();
            }
        });
    }

    private void updateDice() {

        new Thread(new Runnable() {
            public void run() {

                String packageName = getPackageName();
                int[] score = model.rollDice();
                for (int i = 0; i < score.length; i++) {

                    ImageView image = (ImageView) findViewById(getResources().
                            getIdentifier("die" + (i + 1), "id", packageName));

                    image.setImageResource(getResources()
                            .getIdentifier("white" + score[i], "drawable", packageName));
                }
            }
        }).start();
    }

    public void changeDice(View v) {
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 6; i++) {

                    String id = "die" + (i + 1);
                    ImageView image = (ImageView) findViewById(R.id.die1);
                    image.setImageResource(R.drawable.white2);
                }
            }
        }).start();
    }

    private void rollDice() {

        model.rollDice();
    }
}
