package se.umu.cs.warn.buster.thirtygame.model_game;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Description:     Representation of a Dice in the game Thirty.
 *
 * Author:          Buster Hultgren Wärn - busterw@cs.umu.se.
 * Course:          Application in mobile development.
 * Written:         2020-07-23.
 */
public class Dice implements Parcelable {

    public static final int COLOR_GREY  = 0;
    public static final int COLOR_WHITE = 1;
    public static final int COLOR_RED   = 2;

    private int     id;
    private int     score;
    private int     color;
    private Random  random;

    /**
     * Constructor
     * @param id Identification for the dice, simply an integer.
     */
    public Dice(int id) {

        this.id = id;
        score   = 0;
        color   = COLOR_RED;
        random  = new Random();
    }

    /**
     * Parcelable constructor. Only used in parcelable creator.
     * @param in Received parcel with object information.
     */
    protected Dice(Parcel in) {
        id = in.readInt();
        score = in.readInt();
        color = in.readInt();
        random  = new Random();
    }

    /**
     * {@inheritDoc}
     */
    public static final Creator<Dice> CREATOR = new Creator<Dice>() {
        @Override
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

    /**
     * Gets the ID of the dice.
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the color of the dice as a string.
     * @return The color of the dice as a string.
     */
    public String getColor() {
        if (color == COLOR_GREY) {
            return "grey";
        } else if (color == COLOR_RED) {
            return "red";
        } else {
            return "white";
        }
    }

    /**
     * Rolls dice and gives it random in range 1-6.
     */
    public void roll() {
        score = random.nextInt(6) + 1;
    }

    /**
     * Gets the score of the dice.
     * @return The score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Set color for dice
     * @param color Color for dice. 0 is grey, 1 is white and 2 is red. Other integers are invalid.
     */
    public void setColor(int color) {
        if (color >= 0 && color < 3) {
            this.color = color;
        }
    }

    /**
     * Set score for dice
     * @param score New score
     */
    public void setScore(int score) {
        this.score = score;
    }

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
        dest.writeInt(id);
        dest.writeInt(score);
        dest.writeInt(color);
    }
}
