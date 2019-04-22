package boggle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.Contract;

/**
 * The "Word" -class is used to create word -objects that get added to the table.
 */
public class Word {

    private SimpleStringProperty wordToAdd;

    /**
     * Constructor
     *
     * @param wordToAdd
     */
    public Word(String wordToAdd) {
        this.wordToAdd = new SimpleStringProperty(wordToAdd);
    }

    /**
     * Getter
     * @return
     */
    public String getWord() {
        return wordToAdd.get();
    }

    /**
     * Setter
     * @param wordtoadd
     */
    public void setWord(String wordtoadd) {
        wordToAdd.set(wordtoadd);
    }

    /**
     * AddProperty used by createTable()
     * @return
     */
    public StringProperty wordToAddProperty() {
        return wordToAdd;
    }
}