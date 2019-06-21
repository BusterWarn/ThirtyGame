package se.umu.cs.warn.buster.thirtygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import se.umu.cs.warn.buster.thirtygame.model.Dice;
import se.umu.cs.warn.buster.thirtygame.model.Model;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureStartGameButton();
    }

    private void configureStartGameButton() {
        Button startBtn = (Button) findViewById(R.id.startGameBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
}
