package se.umu.cs.warn.buster.thirtygame.model;

public class DiceRound {

    private int                     activeDice;
    private int                     throwsLeft;
    private int                     totalScore;
    private int[]                   toggledDice;
    private int[]                   usedDice;
    private Dice[]                  dice;
    private ThirtyDiceGameStrategy  strategy;

    public DiceRound () {
        dice        = new Dice[Consts.NR_DICE];
        usedDice    = new int[Consts.NR_DICE];
        toggledDice = new int[Consts.NR_DICE];
        throwsLeft  = Consts.NR_THROWS;
        totalScore  = 0;

        for (int i = 0; i < Consts.NR_DICE; i++) {
            dice[i] = new Dice(i);
            toggledDice[i] = 0;
            usedDice[i] = 0;
        }
    }

    public int getThrowsLeft() {
        return throwsLeft;
    }

    public ThirtyDiceGameStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ThirtyDiceGameStrategy strategy) {
        this.strategy = strategy;
    }

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

    public int getDieScore(int i) {
        if (i >= 0 && i < dice.length)
            return dice[i].getScore();
        return -1;
    }

    public String getDieColor(int i) {
        if (i >= 0 && i < dice.length)
            return dice[i].getColor();
        return "grey";
    }

    public int countDice() {
        int diceScore[] = new int[dice.length];
        for (int i = 0; i < diceScore.length; i++) {
            diceScore[i] = dice[i].getScore();
        }
        return strategy.countScore(diceScore);
    }

    public void rollDice() {
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

    private void setDiceToUsed() {
        for (int i = 0; i < dice.length; i++) {
            if (toggledDice[i] == 1) {
                usedDice[i] = 1;
                dice[i].setColor(Dice.COLOR_RED);
            }
        }
    }

    public void toggleDie(int index) {
        if (    index >= 0
                && index <= toggledDice.length
                && usedDice[index] != 1) {
            if (toggledDice[index] == 1) {
                toggledDice[index] = 0;
                dice[index].setColor(Dice.COLOR_GREY);
            } else {
                toggledDice[index] = 1;
                dice[index].setColor(Dice.COLOR_WHITE);
            }
        }
    }
}
