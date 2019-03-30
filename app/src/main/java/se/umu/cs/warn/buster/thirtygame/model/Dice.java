package se.umu.cs.warn.buster.thirtygame.model;

import java.util.Random;

public class Dice {

    private int     id;
    private int     score;
    private Random  random;

    public Dice(int id) {

        this.id = id;
        score   = 0;
        random  = new Random();
    }

    public void roll() {

        score = random.nextInt(6) + 1;
    }

    public int getId() {

        return id;
    }

    public int getScore() {

        return score;
    }
}
