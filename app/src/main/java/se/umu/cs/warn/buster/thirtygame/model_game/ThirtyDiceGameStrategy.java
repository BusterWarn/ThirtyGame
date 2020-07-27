package se.umu.cs.warn.buster.thirtygame.model_game;

import android.os.Parcelable;

/**
 * Description:     Interface for strategies in the Thirty game. This follows the strategy design
 *                  pattern.
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public interface ThirtyDiceGameStrategy extends Parcelable {

    /**
     * Gets the name of the strategy as a String.
     * @return The name of the strategy.
     */
    public String getName();

    /**
     * From an array of dice, counts the score of those dice depending on this strategy.
     * @param dice The array of dice.
     * @return The score.
     */
    public int countScore(int dice[]);
}
