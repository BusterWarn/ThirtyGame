package se.umu.cs.warn.buster.thirtygame.model;

import java.util.Collections;

public class CombinationStrategy implements ThirtyDiceGameStrategy {

    private int combination;

    public void setCombination(int combination) {

        this.combination = combination;
    }

    @Override
    public int countScore(int dice[]) {

        sortDice(dice);
        int diceLeft            = dice.length;
        //int usedDicePositon[]   = initiateUsedDiceArray(dice.length);
        int usedDicePositon[]   = new int[dice.length];
        int score               = 0;

        for (int combLen = 1; combLen <= dice.length; combLen++) {

            for (int i = 0; i < dice.length; i++) {

                if (diceLeft >= combLen) {

                    int sum = findDiceCombination(dice, usedDicePositon,
                            this.combination, combLen, 0, 0, 0);
                    if (sum > 0) {

                        score += sum;
                    }
                }
            }
        }

        System.out.println("Total score: " + score);
        System.out.println("Dice used:");
        for (int i = 0; i < usedDicePositon.length; i++) {

            System.out.println("Dice: " + usedDicePositon[i] + " - " + dice[i]);
        }
        return 0;
    }

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

        System.out.println("Dice sorted to order: ");
        for (int i = 0; i < dice.length; i++) {

            System.out.println("dice["+i+"]: " + dice[i]);
        }
    }

    private int[] initiateUsedDiceArray(int length) {

        int array[] = new int[length];
        for (int i = 0; i < array.length; i++) {

            array[i] = 0;
        }
        return array;
    }

    private int findDiceCombination(int dice[], int usedDicePos[], int target,
                                    int combLen, int sum, int pos,
                                    int nrDiceFound) {

        for (int i = pos; i < dice.length; i++) {

            if (nrDiceFound == combLen - 1) {

                if (usedDicePos[i] == 0 && (dice[i] + sum) == target) {

                    System.out.println("Set dice " + i + " to used. dice["+i+"] is " + dice[i] + ", sum is " + sum);
                    usedDicePos[i] = 1;
                    return dice[i] + sum;
                }
            } else if (usedDicePos[i] == 0 && dice[i] + sum <= target) {

                int result = findDiceCombination(dice, usedDicePos, target,
                        combLen, sum + dice[i], i + 1,
                        nrDiceFound + 1);

                if (result == target) {

                    System.out.println("Set dice " + i + " to used. dice["+i+"] is " + dice[i] + ", sum is " + sum);
                    usedDicePos[i] = 1;
                    return result;
                }
            }
        }
        return -1;
    }
}
