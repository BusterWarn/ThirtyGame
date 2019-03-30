package se.umu.cs.warn.buster.thirtygame.model;

public class tempmain {

    public static void main(String[] args) {

        Dice die = new Dice(1);

        for (int i = 0; i < 1000; i++) {

            die.roll();
        }
        CombinationStrategy c = new CombinationStrategy();
        c.setCombination(5);
        int dice[] = {1, 1, 1, 2, 4, 4, 3};
        c.countScore(dice);

        Model m = new Model(5);
    }
}
