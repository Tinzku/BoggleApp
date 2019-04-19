package boggle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Word {

    private SimpleStringProperty wordToAdd;

    public Word(String wordToAdd) {
        this.wordToAdd = new SimpleStringProperty(wordToAdd);
    }

    public String getWord() {
        return wordToAdd.get();
    }

    public void setWord(String wordtoadd) {
        wordToAdd.set(wordtoadd);
    }

    public StringProperty wordToAddProperty() {
        return wordToAdd;
    }

    public void resetWord() {
        wordToAdd.set("");
    }
}