package se.umu.cs.warn.buster.thirtygame.model;

import java.util.ArrayList;

public class Model {

    private boolean gameStarted;
    private int totalScore;
    private int roundsLeft;
    private DiceRound diceRound;
    private ArrayList<ThirtyDiceGameStrategy> strategies;

    public Model() {
        gameStarted = false;
        totalScore = 0;
        roundsLeft = Consts.NR_ROUNDS;
        strategies = new ArrayList<>();
        strategies.add(new LowStrategy());
        for (int i = Consts.LOWEST_COMBINATION_STRATEGY; i <= Consts.HIGHEST_COMBINATION_STRATEGY; i++) {
            CombinationStrategy strategy = new CombinationStrategy();
            strategy.setCombination(i);
            strategies.add(strategy);
        }
    }

    public ArrayList<ThirtyDiceGameStrategy> getStrategies() {
        return strategies;
    }

    public int getGameState() {
        if (!gameStarted) {
            System.out.println("Returning this shit you FUCEMLAKFNKLÖ");
            return  Consts.STATE_START_OF_GAME;
        } else if (roundsLeft <= 0) {
            return Consts.STATE_END_OF_GAME;
        } else if (diceRound.getThrowsLeft() <= 0) {
            return Consts.STATE_END_OF_ROUND;
        }
        return Consts.STATE_ROLL_DICE;
    }

    public void startGame() {
        gameStarted = true;
        diceRound = new DiceRound();
    }

    public void startNewRound() throws Exception {
        if (roundsLeft <= 0) {
            throw new Exception("No more rounds you fool");
        } else if (diceRound.getStrategy() == null){
            throw new Exception("You need to select a strategy");
        } else {
            if (diceRound != null) {
                totalScore += diceRound.calculateScore();
            }
            diceRound = new DiceRound();
            roundsLeft--;
            diceRound.setStrategy(null);
        }
    }

    public int getRoundsLeft() {
        return roundsLeft;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getThrowsLeft() {
        return diceRound.getThrowsLeft();
    }

    public int calculateScore() {
        return diceRound.calculateScore();
    }

    public void setStrategy(ThirtyDiceGameStrategy strategy) {
        System.out.println("Setting strategty to: " + strategy.getName());
        diceRound.setStrategy(strategy);
    }

    public int getDieScore(int index) {
        return diceRound.getDieScore(index);
    }

    public String getDieColor(int index) {
        return diceRound.getDieColor(index);
    }

    public void rollDice() {
        diceRound.rollDice();
    }

    public void toggleDie(int index) {
        diceRound.toggleDie(index);
    }
}
