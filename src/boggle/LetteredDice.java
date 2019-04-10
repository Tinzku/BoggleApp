package boggle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class simulating a lettered dice used in Boggle game
 */
public class LetteredDice {

    private final String[] valueSet;
    private String value;                   // Because "QU" includes two characters, values are Strings

    private final static Random r = new Random();

    // Creates a Dice-object with given letters as sides
    public LetteredDice(String[] valueSet) {
        this.valueSet = valueSet;
        this.roll();
    }

    // Assigns a random letter from the set to the value
    protected void roll() {
        int seed = r.nextInt(valueSet.length);
        value = valueSet[seed];
    }

    // Returns the uppermost face (value)
    protected String getValue() {
        return value;
    }

    // Sets the dice configuration
    public static ArrayList<LetteredDice> configure() {
        ArrayList<LetteredDice> dices = new ArrayList<>();
        dices.add(new LetteredDice(new String[]{"R", "I", "F", "O", "B", "X"}));
        dices.add(new LetteredDice(new String[]{"I", "F", "E", "H", "E", "Y"}));
        dices.add(new LetteredDice(new String[]{"D", "E", "N", "O", "W", "S"}));
        dices.add(new LetteredDice(new String[]{"U", "T", "O", "K", "N", "D"}));
        dices.add(new LetteredDice(new String[]{"H", "M", "S", "R", "A", "O"}));
        dices.add(new LetteredDice(new String[]{"L", "U", "P", "E", "T", "S"}));
        dices.add(new LetteredDice(new String[]{"A", "C", "I", "T", "O", "A"}));
        dices.add(new LetteredDice(new String[]{"Y", "L", "G", "K", "U", "E"}));
        dices.add(new LetteredDice(new String[]{"QU", "B", "M", "J", "O", "A"}));
        dices.add(new LetteredDice(new String[]{"E", "H", "I", "S", "P", "N"}));
        dices.add(new LetteredDice(new String[]{"V", "E", "T", "I", "G", "N"}));
        dices.add(new LetteredDice(new String[]{"B", "A", "L", "I", "Y", "T"}));
        dices.add(new LetteredDice(new String[]{"E", "Z", "A", "V", "N", "D"}));
        dices.add(new LetteredDice(new String[]{"R", "A", "L", "E", "S", "C"}));
        dices.add(new LetteredDice(new String[]{"U", "W", "I", "L", "R", "G"}));
        dices.add(new LetteredDice(new String[]{"P", "A", "C", "E", "M", "D"}));
        return dices;
    }
}