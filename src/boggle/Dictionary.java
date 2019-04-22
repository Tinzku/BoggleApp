package boggle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {

    private Set<String> wordsSet;

    /**
     * Scans the "dict.txt" file and creates a dictionary (wordsSet) from it
     *
     * @throws IOException
     */
    public Dictionary() throws IOException {
        Path path = Paths.get("dict.txt");
        byte[] readBytes = Files.readAllBytes(path);
        String wordListContents = new String(readBytes, "UTF-8");
        String[] words = wordListContents.split("\n");
        wordsSet = new HashSet<>();
        Collections.addAll(wordsSet, words);
    }

    /**
     * Boolean to check if the word is in the dictionary
     * @param word
     * @return
     */
    public boolean contains(String word) {
        return wordsSet.contains(word);
    }
}