package se.umu.cs.warn.buster.thirtygame.model_game;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

import se.umu.cs.warn.buster.thirtygame.Consts;

/**
 * Description:     The model, containing business logic, for a dice game of Thirty. The game
 * persists of 10 rounds, each round represented with a DiceRound. Each DiceRound has a connected
 * ThirtyGameStrategy strategy that can only be used once for each round.
 *
 * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
 * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class ModelThirtyGame implements Parcelable {

    private boolean gameStarted;
    private int totalScore;
    private int roundsLeft;
    private DiceRound diceRound;
    private String[] strategyHistory;
    private int[] scoreHistory;
    private ArrayList<ThirtyDiceGameStrategy> strategies;

    /**
     * Constructor.
     */
    public ModelThirtyGame() {
        gameStarted = false;
        totalScore = 0;
        roundsLeft = Consts.NR_ROUNDS;
        strategyHistory = new String[Consts.NR_ROUNDS];
        scoreHistory = new int[Consts.NR_ROUNDS];
        Arrays.fill(scoreHistory, -1);
        strategies = new ArrayList<>();
        strategies.add(new LowStrategy());
        for (int i = Consts.LOWEST_COMBINATION_STRATEGY; i <= Consts.HIGHEST_COMBINATION_STRATEGY; i++) {
            CombinationStrategy strategy = new CombinationStrategy(i);
            strategies.add(strategy);
        }
    }

    /**
     * Parcelable constructor. Only used in parcelable creator.
     * @param in Received parcel with object information.
     */
    protected ModelThirtyGame(Parcel in) {
        gameStarted = in.readByte() != 0;
        totalScore = in.readInt();
        roundsLeft = in.readInt();
        diceRound = in.readParcelable(DiceRound.class.getClassLoader());
        in.readStringArray(strategyHistory);
        in.readIntArray(scoreHistory);
        Parcelable[] parcelableArray = in.readParcelableArray(ThirtyDiceGameStrategy.class.getClassLoader());
        if (parcelableArray != null) {
            ThirtyDiceGameStrategy[] s = Arrays.copyOf(parcelableArray, parcelableArray.length,
                    ThirtyDiceGameStrategy[].class);
            strategies = new ArrayList<>(Arrays.asList(s));
        }
    }

    /**
     * {@inheritDoc}
     */
    public static final Creator<ModelThirtyGame> CREATOR = new Creator<ModelThirtyGame>() {
        @Override
        public ModelThirtyGame createFromParcel(Parcel in) {
            return new ModelThirtyGame(in);
        }

        @Override
        public ModelThirtyGame[] newArray(int size) {
            return new ModelThirtyGame[size];
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (gameStarted ? 1 : 0));
        dest.writeInt(totalScore);
        dest.writeInt(roundsLeft);
        dest.writeParcelable(diceRound, flags);
        dest.writeStringArray(strategyHistory);
        dest.writeIntArray(scoreHistory);
        dest.writeParcelableArray(strategies.toArray(new ThirtyDiceGameStrategy[0]), flags);
    }

    /**
     * Starts the game.
     */
    public void startGame() {
        gameStarted = true;
        diceRound = new DiceRound();
    }

    /**
     * Tries to start a new round. A new round can be started iff there rounds left and a strategy
     * is selected for the previous round.
     * @throws Exception Simple warning to user, should be displayed as Toast.
     */
    public void startNewRound() throws Exception {
        if (roundsLeft <= 0)
            throw new Exception("You have no more rounds!");

        if (diceRound.getStrategy() == null)
            throw new Exception("You need to select a strategy");

        // If this is not the first round
        if (diceRound != null) {
            int roundScore = diceRound.calculateScore();
            totalScore += roundScore;
            ThirtyDiceGameStrategy strategy = diceRound.getStrategy();
            for (int i = 0; i < strategies.size(); i++) {
                if (strategies.get(i).getName().equals(strategy.getName())) {
                    strategies.remove(i);
                    break;

                }
            }
            putRoundToHistory(roundScore, strategy.getName());
        }
        diceRound = new DiceRound();
        roundsLeft--;
        diceRound.setStrategy(null);
    }

    /**
     * Calculates the score of the dice from the current DiceRound with the selected
     * ThirtyGameStrategy.
     * @return The score.
     */
    public int calculateScore() {
        if (diceRound == null)
            return 0;
        return diceRound.calculateScore();
    }

    /**
     * Calls the DiceRound to roll each dice.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound#rollDice()
     */
    public void rollDice() {
        if (diceRound != null)
            diceRound.rollDice();
    }

    /**
     * Calls the DiceRound to toggle a dice between used and unused.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound#toggleDie(int)
     * @param id Id of the dice.
     */
    public void toggleDie(int id) {
        if (diceRound != null)
            diceRound.toggleDie(id);
    }

    /**
     * Gets the current used strategy.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
     * @return The strategy.
     */
    public ArrayList<ThirtyDiceGameStrategy> getStrategies() {
        return strategies;
    }

    /**
     * Gets the current state of the game as an integer, defined in Consts.
     * @see se.umu.cs.warn.buster.thirtygame.Consts
     * @return The state of the game; start of the game, end of the game, end of the round
     * or dice rolling time.
     */
    public int getGameState() {
        if (!gameStarted) {
            return  Consts.STATE_START_OF_GAME;
        } else if (roundsLeft <= 0) {
            return Consts.STATE_END_OF_GAME;
        } else if (diceRound.getThrowsLeft() <= 0) {
            return Consts.STATE_END_OF_ROUND;
        }
        return Consts.STATE_ROLL_DICE;
    }

    /**
     * Gets number of rounds left. Each round is represented as a DiceRound.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound
     * @return The number of ronds left.
     */
    public int getRoundsLeft() {
        return roundsLeft;
    }

    /**
     * Gets the total score gathered yet so far.
     * @return The score.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gets the number of throws left for this round.
     * @return The number of throws left for this round.
     */
    public int getThrowsLeft() {
        if (diceRound == null)
            return 0;
        return diceRound.getThrowsLeft();
    }

    /**
     * Gets if the game has started.
     * @return If the game has started.
     */
    public boolean getGameStarted() { return gameStarted; }

    /**
     * Gets the score of a specific dice by calling DiceRound.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound#getDieScore(int)
     * @param id ID of the specific die.
     * @return The score of that die.
     */
    public int getDieScore(int id) {
        if (diceRound == null)
            return -1;
        return diceRound.getDieScore(id);
    }

    /**
     * Gets an array with history of the games score per round.
     * @return The array of each played out round and what the score was that round. If score is -1
     * round has not been played out.
     */
    public int[] getScoreHistory() {
        return scoreHistory;
    }

    /**
     * Gets an array with history of the games chosen strategies per round.
     * @return The array of each played out round and the selected strategy of that round. If string
     * equals "" (empty) round has not been played out.
     */
    public String[] getStrategyHistory() {
        return strategyHistory;
    }

    /**
     * Gets the color of a specific dice by calling DiceRound.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound#getDieColor(int)
     * @param id ID of the specific die.
     * @return The color of that die.
     */
    public String getDieColor(int id) {
        if (diceRound == null)
            return "grey";
        return diceRound.getDieColor(id);
    }

    /**
     * Sets the strategy of the current DiceRound.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
     * @see se.umu.cs.warn.buster.thirtygame.model_game.DiceRound#getDieColor(int)
     * @param strategy The strategy to use.
     */
    public void setStrategy(ThirtyDiceGameStrategy strategy) {
        if (diceRound != null)
            diceRound.setStrategy(strategy);
    }

    /**
     * Fills the next empty item in the history arrays with a score and a strategy respectively.
     * Should be used at the end of a round to indicate what strategy and score was used.
     * @param score
     * @param strategy
     */
    private void putRoundToHistory(int score, String strategy) {
        for (int i = 0; i < scoreHistory.length; i++) {
            if (scoreHistory[i] == -1) {
                scoreHistory[i] = score;
                strategyHistory[i] = strategy;
                break;
            }
        }
    }
}
