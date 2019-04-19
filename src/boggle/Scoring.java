package boggle;

public class Scoring {

    public int score;

    public Scoring() {
        score = 0;
    }

    public void countScore(StringBuilder word) {
        int characterCount = word.length();
        if (characterCount >= 8) {
            score += 11;
        } else if (characterCount == 7) {
            score += 5;
        } else if (characterCount == 6) {
            score += 3;
        } else if (characterCount == 5) {
            score += 2;
        } else if (characterCount < 2) {
            System.out.println("Nothing added to the score");
        } else {
            score += 1;
        }
    }

    public void reset() {
        score = 0;
    }

    public int getScore() {
        return score;
    }
}
