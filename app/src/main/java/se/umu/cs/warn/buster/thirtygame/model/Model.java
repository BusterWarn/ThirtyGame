package se.umu.cs.warn.buster.thirtygame.model;

public class Model {

    private int                    activeDice;
    private int[]                  usedDice;
    private Dice[]                 dice;
    private ThirtyDiceGameStrategy strategy;

    public Model(int nrDice) {

        dice        = new Dice[nrDice];
        usedDice    = new int[nrDice];

        for (int i = 0; i < nrDice; i++) {

            dice[i] = new Dice(i);
        }
    }

    public void setStrategy(ThirtyDiceGameStrategy strategy) {

        this.strategy = strategy;
    }

    public int countDice(int dice[]) {
        return strategy.countScore(dice);
    }

    public int[] rollDice() {

        int[] score = new int[dice.length];

        for (int i = 0; i < dice.length; i++) {

            if (usedDice[i] == 1 || true) {

                dice[i].roll();
                score[i] = dice[i].getScore();
            }
        }
        return score;
    }

    public void setDiceToUsed(int index) {

        if (index >= 0 && index <= usedDice.length) {

            usedDice[index] = 1;
        }
    }
}
