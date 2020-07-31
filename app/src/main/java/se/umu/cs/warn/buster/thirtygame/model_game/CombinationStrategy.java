package se.umu.cs.warn.buster.thirtygame.model_game;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:     Implementation of ThirtyDiceGameStrategy following the strategy design pattern.
 * Computes the score of a sum of how many dice fits a specific number / combination.
 *
 * @see se.umu.cs.warn.buster.thirtygame.model_game.ThirtyDiceGameStrategy
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class CombinationStrategy implements ThirtyDiceGameStrategy, Parcelable {

    private int combination;

    /**
     * Constructor
     * @param combination the specific combination the dice will need to match.
     */
    protected CombinationStrategy(int combination) {
        this.combination = combination;
    }

    /**
     * Parcelable constructor. Only used in parcelable creator.
     * @param in Received parcel with object information.
     */
    protected CombinationStrategy(Parcel in) {
        combination = in.readInt();
    }

    /**
     * {@inheritDoc}
     */
    public static final Creator<CombinationStrategy> CREATOR = new Creator<CombinationStrategy>() {
        @Override
        public CombinationStrategy createFromParcel(Parcel in) {
            return new CombinationStrategy(in);
        }

        @Override
        public CombinationStrategy[] newArray(int size) {
            return new CombinationStrategy[size];
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
        dest.writeInt(combination);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Combination: " + combination;
    }

    /**
     * From an array of dice, counts the score of those dice for this combination of strategy.
     *
     * @param dice The array of dice.
     * @return The score
     */
    @Override
    public int countScore(int dice[]) {
        sortDice(dice);
        int diceLeft            = dice.length;
        int usedDicePositon[]   = new int[dice.length];
        int score               = 0;

        // Iterate through number of dice to be used in strategy. Starting from 1 and going through
        // every possible number of combination
        for (int combLen = 1; combLen <= dice.length; combLen++) {

            // Test all
            for (int i = 0; i < dice.length; i++) {
                // Is there dice lift for this combination size?
                if (diceLeft >= combLen) {
                    int sum = findDiceCombination(dice, usedDicePositon, combLen, 0, 0,
                            0);
                    if (sum > 0) {
                        score += sum;
                        diceLeft -= combLen;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Recursively finds, for a number of dice in a dice array, if that number of dice will produce
     * a sum equal to this strategies combination.
     *
     * @param dice The dice array
     * @param usedDicePos An array of same length as the dice array, show which dice in that array
     *                    are already used, and cannot be used again.
     * @param combLen The number of dice that must be used in the combination.
     * @param sum The sum of the dice score. Used in recursion and should be set to 0.
     * @param pos The position of the current iterated dice in the dice array. Used in recursion and
     *            should be set to 0.
     * @param nrDiceFound The number of found dice that fits the combination. Used in recursion and
     *                    should be set to 0.
     * @return The sum of the dice that equals the combination. If combination is not found, -1 is
     * returned.
     */
    private int findDiceCombination(int dice[], int usedDicePos[], int combLen,
                                    int sum, int pos, int nrDiceFound) {
        for (int i = pos; i < dice.length; i++) {

            // Is there only one die left?
            if (nrDiceFound == combLen - 1) {
                // Is last die available and does it match the sum?
                if (usedDicePos[i] == 0 && (dice[i] + sum) == this.combination) {
                    usedDicePos[i] = 1;
                    return dice[i] + sum;
                }
            } else if (usedDicePos[i] == 0 && dice[i] + sum <= this.combination) {
                // Possible die for combination found, test it with other available dice through
                // recursion
                int result = findDiceCombination(dice, usedDicePos, combLen, sum + dice[i],
                        i + 1, nrDiceFound + 1);

                // Is the correct combination found?
                if (result == this.combination) {
                    usedDicePos[i] = 1;
                    return result;
                }
            }
        }
        return -1;
    }

    /**
     * Sorts dice array in descending order.
     * @param dice Array of integers representing dice.
     */
    private void sortDice(int dice[]) {
        for (int i = 0; i < dice.length; i++) {
            for (int j = i; j > 0; j--) {
                if (dice[j] > dice[j - 1]) {
                    int temp    = dice[j - 1];
                    dice[j - 1] = dice[j];
                    dice[j]     = temp;
                } else {
                    break;
                }
            }
        }
    }
}
