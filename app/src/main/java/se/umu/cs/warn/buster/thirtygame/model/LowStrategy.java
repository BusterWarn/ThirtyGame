package se.umu.cs.warn.buster.thirtygame.model;

public class LowStrategy implements ThirtyDiceGameStrategy {

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
