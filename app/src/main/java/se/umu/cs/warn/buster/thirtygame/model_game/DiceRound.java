package se.umu.cs.warn.buster.thirtygame.model_game;

import android.os.Parcel;
import android.os.Parcelable;

import se.umu.cs.warn.buster.thirtygame.Consts;

/**
 * Description:     Represents a round in the dice game Thirty.
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class DiceRound implements Parcelable {

    private int                     throwsLeft;
    private int                     totalScore;
    private int[]                   toggledDice;
    private int[]                   usedDice;
    private Dice[]                  dice;
    private ThirtyDiceGameStrategy  strategy;

    /**
     * Constructor.
     */
    public DiceRound() {
        dice        = new Dice[Consts.NR_DICE];
        usedDice    = new int[Consts.NR_DICE];
        toggledDice = new int[Consts.NR_DICE];
        throwsLeft  = Consts.NR_THROWS;
        totalScore  = 0;

        for (int i = 0; i < Consts.NR_DICE; i++) {
            dice[i] = new Dice(i);
            toggledDice[i] = 1;
            usedDice[i] = 1;
        }
    }

    /**
     * Parcelable constructor. Only used in parcelable creator.
     * @param in Received parcel with object information.
     */
    protected DiceRound(Parcel in) {
        throwsLeft = in.readInt();
        totalScore = in.readInt();
        toggledDice = in.createIntArray();
        usedDice = in.createIntArray();
        dice = in.createTypedArray(Dice.CREATOR);
        strategy = in.readParcelable(ThirtyDiceGameStrategy.class.getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    public static final Creator<DiceRound> CREATOR = new Creator<DiceRound>() {
        @Override
        public DiceRound createFromParcel(Parcel in) {
            return new DiceRound(in);
        }

        @Override
        public DiceRound[] newArray(int size) {
            return new DiceRound[size];
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
        dest.writeInt(throwsLeft);
        dest.writeInt(totalScore);
        dest.writeIntArray(toggledDice);
        dest.writeIntArray(usedDice);
        dest.writeTypedArray(dice, flags);
        dest.writeParcelable(strategy, flags);
    }

    /**
     * If there are throws left, rolls all unused dice - gives each dice a random number between
     * 1-6.
     */
    public void rollDice() {
        if (throwsLeft == 3) {
            toggleDiceToUnused();
        }
        if (throwsLeft > 0) {
            for (int i = 0; i < dice.length; i++) {
                if (toggledDice[i] == 1)
                    usedDice[i] = 1;
                dice[i].setColor(Dice.COLOR_RED);
                if (usedDice[i] == 0) {
                    dice[i].setColor(Dice.COLOR_GREY);
                    dice[i].roll();
                }
            }
            throwsLeft--;
        }
    }

    /**
     * Calculate the score for all dice via the selected strategy.
     * @return The score.
     */
    public int calculateScore() {
        if (strategy != null) {
            int score[] = new int[Consts.NR_DICE];
            for (int i = 0; i < Consts.NR_DICE; i++) {
                if (toggledDice[i] == 1) {
                    score[i] = dice[i].getScore();
                } else {
                    score[i] = 0;
                }
            }
            return strategy.countScore(score);
        }
        return 0;
    }

    /**
     * Toggles a die between selected and not selected. Selected dice will be used to count score
     * and locked.
     * @param id Id of the die to toggle.
     */
    public void toggleDie(int id) {
        if (    id >= 0
                && id <= toggledDice.length
                && usedDice[id] != 1) {
            if (toggledDice[id] == 1) {
                toggledDice[id] = 0;
                dice[id].setColor(Dice.COLOR_GREY);
            } else {
                toggledDice[id] = 1;
                dice[id].setColor(Dice.COLOR_WHITE);
            }
        }
    }

    /**
     * Gets number of throws left for this round.
     * @return The number of throws left for this round.
     */
    public int getThrowsLeft() {
        return throwsLeft;
    }

    /**
     * Gets the strategy used in this round.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
     * @return The strategy used in this round.
     */
    public ThirtyDiceGameStrategy getStrategy() {
        return strategy;
    }

    /**
     * Gets the score of a die.
     * @param id Id of the die.
     * @return The score of the die.
     */
    public int getDieScore(int id) {
        if (id >= 0 && id < dice.length)
            return dice[id].getScore();
        return -1;
    }

    /**
     * Gets the color of a die.
     * @param id Id of the die.
     * @return The score of the die.
     */
    public String getDieColor(int id) {
        if (id >= 0 && id < dice.length)
            return dice[id].getColor();
        return "grey";
    }

    /**
     * Sets strategy of the round. Does not lock the round to this particular strategy.
     * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
     * @param strategy The strategy.
     */
    public void setStrategy(ThirtyDiceGameStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Toggles all dice to unused. Used in beginning of a new round.
     */
    private void toggleDiceToUnused() {
        for (int i = 0; i < dice.length; i++) {
            usedDice[i]     = 0;
            toggledDice[i]  = 0;
            dice[i].setColor(Dice.COLOR_GREY);
        }
    }
}
