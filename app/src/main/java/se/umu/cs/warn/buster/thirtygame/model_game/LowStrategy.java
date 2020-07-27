package se.umu.cs.warn.buster.thirtygame.model_game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:     Implementation of ThirtyDiceGameStrategy following the strategy design pattern.
 * Computes the sum of all dice with a score <= 3.
 *
 * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class LowStrategy implements ThirtyDiceGameStrategy, Parcelable {

    /**
     * Constructor
     */
    protected LowStrategy() {}

    /**
     * Parcelable constructor. Only used in parcelable creator.
     * @param in Received parcel with object information.
     */
    protected LowStrategy(Parcel in) {}

    /**
     * {@inheritDoc}
     */
    public static final Creator<LowStrategy> CREATOR = new Creator<LowStrategy>() {
        @Override
        public LowStrategy createFromParcel(Parcel in) {
            return new LowStrategy(in);
        }

        @Override
        public LowStrategy[] newArray(int size) {
            return new LowStrategy[size];
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Low strategy";
    }

    /**
     * From an array of dice, counts the score of those dice. The score consists of all dice which
     * have an individual score of 3 or lower.
     *
     * @param dice The array of dice.
     * @return The score
     */
    @Override
    public int countScore(int dice[]) {
        int score = 0;

        for (int i = 0; i < dice.length; i++) {
            if (dice[i] <= 3) {
                score += dice[i];
            }
        }
        return score;
    }
}
