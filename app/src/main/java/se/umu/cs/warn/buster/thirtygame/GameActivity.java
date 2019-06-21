package se.umu.cs.warn.buster.thirtygame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.umu.cs.warn.buster.thirtygame.model.Consts;
import se.umu.cs.warn.buster.thirtygame.model.Model;
import se.umu.cs.warn.buster.thirtygame.model.ThirtyDiceGameStrategy;

public class GameActivity extends AppCompatActivity {

    private int         nrDice;
    private Model       model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.nrDice     = 6;
        this.model      = new Model();

        configureMainButton();
        configureQuitButton();
        configureDiceButtons();
        configureStrategyButton();
    }

    private void configureStrategyButton() {
        Button strategyBtn = (Button) findViewById(R.id.menuBtn);
        strategyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void configureQuitButton() {
        Button quitBtn = findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureMainButton() {
        Button rollDiceBtn = findViewById(R.id.mainBtn);
        rollDiceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleMainClick();
                }
            });
    }

    private void configureDiceButtons() {
        final String packageName = GameActivity.this.getPackageName();
        for (int i  = 0; i < nrDice; i++ ) {
            ImageButton diceBtn = (ImageButton) findViewById(getResources().
                    getIdentifier("die" + i, "id", packageName));
            final int finalIndex = i;
            diceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.toggleDie(finalIndex);
                    updateDie(finalIndex, packageName);
                    updateDiceScoreDisplay();
                }
            });
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_strategy, popupMenu.getMenu());
        final ArrayList<ThirtyDiceGameStrategy> strategies = model.getStrategies();
        int nrStrategies = strategies.size();
        for (int i = 0; i < nrStrategies; i++) {
            popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, strategies.get(i).getName());
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final Button btn = (Button) findViewById(R.id.menuBtn);
                final String text = strategies.get(item.getItemId()).getName();
                updateTextViewWithText(btn, text);
                model.setStrategy(strategies.get(item.getItemId()));
                return false;
            }
        });
        popupMenu.show();
    }

    private void handleMainClick() {
        int gameStatus = model.getGameState();
        if (gameStatus == Consts.STATE_START_OF_GAME) {
            model.startGame();
            updateAllDisplays();
        } else if (gameStatus == Consts.STATE_END_OF_ROUND) {
            try {
                startNewRound();
            } catch (Exception e) {
                displayInternalExceptionMessage(e.getMessage());
            }
        } else if (gameStatus == Consts.STATE_ROLL_DICE) {
            rollDice();
        }
    }

    private void startNewRound() {
        try {
            model.startNewRound();
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("Starting new round");
                    updateAllDisplays();
                }
            }).start();
        } catch (Exception e) {
            displayInternalExceptionMessage(e.getMessage());
        }
    }

    private void rollDice() {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Rolling dice");
                model.rollDice();
                updateAllDisplays();
            }
        }).start();
    }

    private void updateTextViewWithText(final TextView view, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }

    private void updateAllDisplays() {
        updateMainButtonDisplay();
        updateScoreDisplay();
        updateThrowsLeftDisplay();
        updateDiceScoreDisplay();
        updateDiceDisplay();
    }

    private void updateMainButtonDisplay() {
        final String mainBtnText;
        int state = model.getGameState();

        if (state == Consts.STATE_START_OF_GAME) {
            mainBtnText = getString(R.string.start_rolling);
        } else if (state == Consts.STATE_END_OF_ROUND) {
            mainBtnText = getString(R.string.new_round);
        } else if (state == Consts.STATE_ROLL_DICE) {
            mainBtnText = getString(R.string.roll_dice);
        } else {
            mainBtnText = getString(R.string.end_of_game);
        }
        updateTextViewWithText((Button) findViewById(R.id.mainBtn), mainBtnText);
    }

    private void updateScoreDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.scoreDisplayCount),
                Integer.toString(model.getTotalScore())
        );
    }

    private void updateThrowsLeftDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.throwsLeftDisplayCount),
                Integer.toString(model.getThrowsLeft())
        );
    }

    private void updateDiceScoreDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.diceScoreDisplayCount),
                Integer.toString(model.calculateScore())
        );
    }

    private void updateDiceDisplay() {
        String packageName = GameActivity.this.getPackageName();
        for (int i = 0; i < nrDice; i++) {
            updateDie(i, packageName);
        }
    }

    private void updateDie(final int i, final String packageName) {
        final int score = (model.getDieScore(i) > 0 ? model.getDieScore(i) : 1);
        final String color = model.getDieColor(i);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView image = (ImageView) findViewById(getResources().
                        getIdentifier("die" + i, "id", packageName));
                image.setImageResource(getResources()
                        .getIdentifier(color + (score), "drawable", packageName));
            }
        });
    }

    private void displayInternalExceptionMessage(String message) {
        Toast errorToast = Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT);
        errorToast.show();
    }
}
