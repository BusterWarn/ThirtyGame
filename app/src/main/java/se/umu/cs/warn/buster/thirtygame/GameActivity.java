package se.umu.cs.warn.buster.thirtygame;

import android.content.Intent;
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

import se.umu.cs.warn.buster.thirtygame.model_game.ModelThirtyGame;
import se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy;

/**
 * Activity for the game Thirty. Uses a model to represent the game, and this class as a controller
 * and a view.
 *
 * @see se.umu.cs.warn.buster.thirtygame.model_game.ModelThirtyGame
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class GameActivity extends AppCompatActivity {


    private ModelThirtyGame model;
    private String packageName;
    private final static int nrDice = 6;

    /**
     * Creator.
     * @param savedInstanceState bundle containing the model if game was in progress.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.packageName = GameActivity.this.getPackageName();

        configureMainButton();
        configureQuitButton();
        configureDiceButtons();
        configureStrategyButton();

        if (savedInstanceState != null) {
            this.model = savedInstanceState.getParcelable(Consts.modelKey);
            if (model.getGameState() != Consts.STATE_START_OF_GAME) {
                updateAllDisplays();
            }
        } else {
            this.model = new ModelThirtyGame();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelable(Consts.modelKey, model);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        model = savedInstanceState.getParcelable(Consts.modelKey);
        updateAllDisplays();
    }

    /**
     * Tries to start a new round by calling the model. If failed an exception will be caught and
     * shown via toast.
     */
    private void startNewRound() {
        updateStrategyBtnText("Chose strategy");
        try {
            model.startNewRound();
            new Thread(new Runnable() {
                public void run() {
                    updateAllDisplays();
                }
            }).start();
        } catch (Exception e) {
            displayInternalExceptionMessage(e.getMessage());
        }
    }

    /**
     * Calls the model to roll the dice and then updates the dice display.
     */
    private void rollDice() {
        new Thread(new Runnable() {
            public void run() {
                model.rollDice();
                updateAllDisplays();
            }
        }).start();
    }

    /**
     * Shows a PopupMenu with all available strategies for the user. When the user selects a
     * strategy, the model is updated and a possible score for the selected dice is calculated and
     * updated to the XXX display.
     * @param v The current view sent through onClick.
     */
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
                updateStrategyBtnText(strategies.get(item.getItemId()).getName());
                model.setStrategy(strategies.get(item.getItemId()));
                updateDiceScoreDisplay();
                return false;
            }
        });
        popupMenu.show();
    }

    /**
     * Configures the strategy button to open the strategy popup menu if available.
     * @see se.umu.cs.warn.buster.thirtygame.GameActivity#showPopupMenu(View)
     */
    private void configureStrategyButton() {

        Button strategyBtn = (Button) findViewById(R.id.strategyBtn);
        strategyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getGameState() != Consts.STATE_START_OF_GAME) {
                    showPopupMenu(v);
                }
            }
        });
    }

    /**
     * Configures the quit button by calling finish().
     */
    private void configureQuitButton() {
        Button quitBtn = findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Configures the main button by calling the handleMainClick() method.
     * @see se.umu.cs.warn.buster.thirtygame.GameActivity#handleMainClick()
     */
    private void configureMainButton() {
        Button rollDiceBtn = findViewById(R.id.mainBtn);
        rollDiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMainClick();
            }
        });
    }

    /**
     * Configures all dice buttons to once selected, toggle that die in the model and update the
     * color of die to show selected.
     */
    private void configureDiceButtons() {
        for (int i  = 0; i < nrDice; i++ ) {
            ImageButton diceBtn = (ImageButton) findViewById(getResources().
                    getIdentifier("die" + i, "id", packageName));
            final int finalIndex = i;
            diceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getGameStarted()) {
                        model.toggleDie(finalIndex);
                        updateDie(finalIndex);
                        updateDiceScoreDisplay();
                    }
                }
            });
        }
    }

    /**
     * Handles click from the main button. Multiple things can happen depending on the state of the
     * game.
     *
     * STATE_START_OF_GAME: Start the game by calling the model.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ModelThirtyGame#startGame()
     *
     * STATE_ROLL_DICE Roll dice by calling rollDice()
     * @see se.umu.cs.warn.buster.thirtygame.GameActivity#rollDice()
     *
     * STATE_END_OF_ROUND Tries to start a new round
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ModelThirtyGame#startNewRound()
     *
     * STATE_END_OF_GAME Opens InsertHighScoreActivity and passes the score. Finnishes this
     * activity.
     * @see se.umu.cs.warn.buster.thirtygame.InsertHighScoreActivity
     */
    private void handleMainClick() {
        int gameStatus = model.getGameState();

        switch (gameStatus) {
            case Consts.STATE_START_OF_GAME:
                model.startGame();
                updateAllDisplays();
                break;
            case Consts.STATE_ROLL_DICE:
                rollDice();
                break;
            case Consts.STATE_END_OF_ROUND:
                try {
                    startNewRound();
                } catch (Exception e) {
                    displayInternalExceptionMessage(e.getMessage());
                }
                break;
            case Consts.STATE_END_OF_GAME:
                Intent intent = new Intent(GameActivity.this, InsertHighScoreActivity.class);
                intent.putExtra(Consts.gameScore, model.getTotalScore());
                intent.putExtra(Consts.historyStrategy, model.getStrategyHistory());
                intent.putExtra(Consts.historyScore, model.getScoreHistory());
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * Updates a TextView with text.
     * @param view The TextView.
     * @param text The text.
     */
    private void updateTextViewWithText(final TextView view, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }

    /**
     * Update all displays.
     */
    private void updateAllDisplays() {
        updateMainButtonDisplay();
        updateScoreDisplay();
        updateThrowsLeftDisplay();
        updateRoundsLeftDisplay();
        updateDiceScoreDisplay();
        updateDiceDisplay();
    }

    /**
     * Update the main button display depending on the state of the game.
     */
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

    /**
     * Updates the rounds left display with how many rounds of the game is left.
     */
    private void updateRoundsLeftDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.roundsLeftDisplayCount),
                Integer.toString(model.getRoundsLeft())
        );
    }

    /**
     * Updates the score display with the total score.
     */
    private void updateScoreDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.scoreDisplayCount),
                Integer.toString(model.getTotalScore())
        );
    }

    /**
     * Updates the throws left display with how many throws left there is in the current round.
     */
    private void updateThrowsLeftDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.throwsLeftDisplayCount),
                Integer.toString(model.getThrowsLeft())
        );
    }

    /**
     * Updates the dice score display with how much score the current selected dice have with the
     * selected strategy.
     */
    private void updateDiceScoreDisplay() {
        updateTextViewWithText(
                (TextView) findViewById(R.id.diceScoreDisplayCount),
                Integer.toString(model.calculateScore())
        );
    }

    /**
     * Updates all dice buttons with their images.
     */
    private void updateDiceDisplay() {

        for (int i = 0; i < nrDice; i++) {
            updateDie(i);
        }
    }

    /**
     * Updates the strategy button with a new text.
     * @param newText
     */
    private void updateStrategyBtnText(String newText) {
        final Button btn = (Button) findViewById(R.id.strategyBtn);
        updateTextViewWithText(btn, newText);
    }

    /**
     * Updates a die with that dies score and color.
     * @param id ID of the dice to update.
     */
    private void updateDie(final int id) {
        final int score = (model.getDieScore(id) > 0 ? model.getDieScore(id) : 1);
        final String color = model.getDieColor(id);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView image = (ImageView) findViewById(getResources().
                        getIdentifier("die" + id, "id", packageName));
                image.setImageResource(getResources()
                        .getIdentifier(color + (score), "drawable", packageName));
            }
        });
    }

    /**
     * Displays a Toast error message.
     * @param message The message.
     */
    private void displayInternalExceptionMessage(String message) {
        Toast errorToast = Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT);
        errorToast.show();
    }
}
