package se.umu.cs.warn.buster.thirtygame.model;

public class Model {

    private int                    activeDice;
    private int[]                  usedDice;
    private Dice[]                 dice;
    private ThirtyDiceGameStrategy strategy;

    public Model(int nrDice) {

        dice        = new Dice[nrDice];
        usedDice    = new int[nrDice];
    }

    public void setStrategy(ThirtyDiceGameStrategy strategy) {

        this.strategy = strategy;
    }

    public int countDice(int dice[]) {
        return strategy.countScore(dice);
    }

    public Dice[] rollDice() {

        Dice rolledDice[] = new Dice[dice.length];

        for (int i = 0; i < dice.length; i++) {

            if (usedDice[i] == 1) {

                dice[i].roll();
                rolledDice[i].getScore();
            }
        }
        return rolledDice;
    }

    public void setDiceToUsed(int index) {

        if (index >= 0 && index <= usedDice.length) {

            usedDice[index] = 1;
        }
    }
}
