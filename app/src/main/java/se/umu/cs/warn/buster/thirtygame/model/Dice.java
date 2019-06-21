package se.umu.cs.warn.buster.thirtygame.model;

import java.util.Random;

public class Dice {

    public static final int COLOR_GREY  = 0;
    public static final int COLOR_WHITE = 1;
    public static final int COLOR_RED   = 2;

    private int     id;
    private int     score;
    private int     color;
    private Random  random;

    public Dice(int id) {

        this.id = id;
        score   = 0;
        color   = COLOR_RED;
        random  = new Random();
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        if (color == COLOR_GREY) {
            return "grey";
        } else if (color == COLOR_RED) {
            return "red";
        } else {
            return "white";
        }
    }

    public void roll() {
        score = random.nextInt(6) + 1;
    }

    public int getScore() {
        return score;
    }

    public void setColor(int color) {
        if (color >= 0 && color < 3) {
            this.color = color;
        }
    }
}
