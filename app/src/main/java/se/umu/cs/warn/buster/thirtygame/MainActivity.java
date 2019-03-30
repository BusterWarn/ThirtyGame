package se.umu.cs.warn.buster.thirtygame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.umu.cs.warn.buster.thirtygame.model.Model;

public class MainActivity extends AppCompatActivity {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model model = new Model(6);
    }

    public void initiateRollDiceButton() {

        Button rollDiceButton = findViewById(R.id.rollDice);
        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void updateDice() {


    }

    private void rollDice() {

        model.rollDice();
    }
}
