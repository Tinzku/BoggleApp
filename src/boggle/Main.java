package boggle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Main extends Application {

    // GUI variables
    private GridPane board;
    private TableView<Word> table;
    private Label scoreLabel;
    private Label warningLabel;

    // Game variables
    public ArrayList<String> addedWords;
    private ArrayList<Tile> pressedTiles;
    private StringBuilder word;
    private Scoring score;
    private Dictionary dictionary;

    /**
     * Initializes the board tiles with dice
     */
    private GridPane initBoard() {

        // Creating tiles with dices
        ArrayList<LetteredDice> dices = LetteredDice.configure();
        Collections.shuffle(dices); //shuffling the dice positions
        ArrayList<Tile> tiles = new ArrayList<>();
        for(LetteredDice d: dices) {
            tiles.add(new Tile(d));
        }

        // Creating a GridPane-board
        board = new GridPane();
        board.setPrefSize(300, 300);
        board.setAlignment(Pos.CENTER_LEFT);

        // Adding String values (letters) to matrix and Tiles to GridPane
        int k = 0;
        for (int i=0; i < 4; i++) {
            for (int j=0; j < 4; j++) {
                tiles.get(k).setPos(i, j);
                board.add(tiles.get(k), i, j);
                k++;
            }
        }
        return board;
    }


    /**
     * Creates a new TableView object "table" that shows the words that the user has tried
     */
    private TableView createTable() {
        table = new TableView<>();
        table.setEditable(true);
        TableColumn<Word, String> sanaCol = new TableColumn("Words");
        sanaCol.setCellValueFactory(new PropertyValueFactory<Word, String>("wordToAdd")); // Cells have correct value
        table.setColumnResizePolicy(table.CONSTRAINED_RESIZE_POLICY); // The "Words" -column takes up the whole space
        table.getColumns().add(sanaCol);
        table.setTranslateX(320);
        return table;
    }

    /**
     * Label to show the score
     */
    private VBox createScoreLabel() {
        Label pisteet = new Label("Score:");
        pisteet.setFont(new Font("Arial", 30));
        scoreLabel = new Label();
        scoreLabel.setText("0");
        scoreLabel.setFont(new Font("Arial", 32));
        final VBox vbox = new VBox(); // Place the labels in a VBox
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));  // Set padding
        vbox.getChildren().addAll(pisteet, scoreLabel);
        vbox.setTranslateX(200);
        vbox.setTranslateY(50);
        return vbox; // Return the VBox
    }

    /**
     * Label to show some warnings, for example "word is already added" and "word is not found in the dictionary"
     */
    private VBox createWarningLabel() {
        warningLabel = new Label();
        warningLabel.setText("");
        warningLabel.setTextFill(Color.RED);
        warningLabel.setFont(new Font("Arial", 13));
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().add(warningLabel);
        vbox.setTranslateX(0);
        vbox.setTranslateY(250);
        return vbox;
    }

    /**
     * Populates the window with nodes
     */
    private Parent createRoot() {
        Pane root = new Pane();
        root.setPrefSize(570, 450);
        root.getChildren().addAll(initBoard());         // Adds all the of the methods above
        root.getChildren().addAll(createTable());
        root.getChildren().addAll(createScoreLabel());
        root.getChildren().add(createWarningLabel());
        Button reset = new Button("Reset");         // Add button "Reset"
        reset.setTranslateX(210);
        reset.setTranslateY(180);
        Button confirm = new Button("Confirm");     // Add button "Confirm"
        confirm.setTranslateX(210);
        confirm.setTranslateY(210);
        Button newGame = new Button("New Game");    // Add button "New Game"
        newGame.setTranslateX(210);
        newGame.setTranslateY(150);
        confirm.setOnMouseClicked(event -> confirm()); // Set the mouse clicked events for the buttons
        reset.setOnMouseClicked(event -> reset());
        newGame.setOnMouseClicked(event -> newGame());
        root.getChildren().addAll(confirm, reset, newGame);
        return root;
    }

    /**
     * The method that pressing the "Confirm" -button actives
     */
    private void confirm() {
        String w = word.toString().toLowerCase();   // Transforms the StringBuilder "Word" to string "w"
        if (!addedWords.contains(w)) {              // If ArrayList "addedWords" doesn't contain the word that is trying to be added, add w
            if (dictionary.contains(w)) {           // If w is in the dictionary
                Word addedWord = new Word(w);       // Create a new Word -object
                table.getItems().add(addedWord);    // Add the object to the table
                addedWords.add(w);                  // Add the word to the "addedWords" ArrayList
                score.countScore(word);             // Count the score
                System.out.println(score.getScore());  // Print the score
                scoreLabel.setText("" + score.getScore());  // Update label score
                reset();                            // Reset -method and reset warning label
                warningLabel.setText("");
            } else {                                // If the word is not in the dictionary, reset the board and update warning label
                reset();
                warningLabel.setText("Word was not found in the dictionary.");
            }
        } else {                                    // If word is already in "addedWords" list, reset the board and show warning
            reset();
            warningLabel.setText("Word has already been used!");
        }

    }

    /**
     * Rolls dice in the start of a new game
     */
    private void shuffle() {
        ObservableList<Node> nodes = board.getChildren();
        for (Node n: nodes) {
            // Casting Node to Tile, rolling the dice and updating the text accordingly
            Tile t = (Tile) n;
            t.getDice().roll();
            t.updateText();
        }
    }

    /**
     * Reset -method clears all pressed tiles and allows a new word to be tried
     */
    private void reset() {
        ObservableList<Node> nodes = board.getChildren();
        for (Node n: nodes) {
            Tile t = (Tile) n;
            t.resetBorder();
            pressedTiles.clear();
            word = new StringBuilder();
        }
    }

    /**
     * New Game
     */
    private void newGame() {
        shuffle();
        reset();
        table.getItems().clear();       // Clear table
        addedWords.clear();             // Clear addedWords
        score.reset();                  // Reset score
        scoreLabel.setText("0");        // Reset labels
        warningLabel.setText("");
    }

    /**
     * Checks if the two letters are adjacent on the board
     */
    private boolean checkAdjacency() {
        boolean value = false;
        int size = pressedTiles.size();
        if (size <= 1) {
            System.out.println("No need to check yet");
            value = true;
        } else {
           int x1 = pressedTiles.get(size-2).getPos().getX();
           int y1 = pressedTiles.get(size-2).getPos().getY();
           int x2 = pressedTiles.get(size-1).getPos().getX();
           int y2 = pressedTiles.get(size-1).getPos().getY();
           System.out.println(x1+" "+x2+" "+y1+" "+y2);

           if (x1 == x2 && (y1+1 == y2 || y1-1 == y2)) { //same x-coordinate
               value = true;
           } else if (y1 == y2 && (x1+1 == x2 || x1-1 == x2)) { //same y-coordinate
               value = true;
           } else if (Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 1){ // diagonal check
               value = true;
           } else {
               reset();
               System.out.println("Väärä valinta, lauta resetoidaan");
               value = false;
           }
        }
        return value;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("gamewindow.fxml"));
        //primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setScene(new Scene(createRoot()));
        primaryStage.setTitle("Boggle");
        primaryStage.show();
    }

    /**
     * Init() initializes some objects used for the program
     */
    @Override
    public void init() {
        pressedTiles = new ArrayList<>();
        addedWords = new ArrayList<>();
        word = new StringBuilder();
        score = new Scoring();
        try {
            dictionary = new Dictionary();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Class representing a single tile on the board
     */
    public class Tile extends StackPane {

        private LetteredDice dice;
        private Text text;
        private Rectangle border;
        private String letter;

        // Saving position data to help adjacency checking
        private Position pos;

        public Tile(LetteredDice dice) {
            this.dice = dice;
            pos = new Position(0, 0);

            // Setting border properties
            border = new Rectangle(50, 50);
            border.setFill(Color.WHITE);
            border.setStroke(Color.BLACK);

            // Setting the text properties
            text = new Text(dice.getValue());
            text.setFont(Font.font(30));

            // Letter
            letter = dice.getValue();

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            // Event handler for pressing a tile
            setOnMouseClicked(event -> clickTile(this));
        }

        private void clickTile(Tile t) {
            if (border.getFill().equals(Color.WHITE)) {
                border.setFill(Color.LIGHTBLUE);
                pressedTiles.add(t);
                if (checkAdjacency()) {
                    word.append(t.getDice().getValue());
                    System.out.println(word);

                }
            } else {
                warningLabel.setText("Pressing the same tile twice resets the word.");
                reset();
            }
        }

        public String getLetter() {
            return letter;
        }

        private LetteredDice getDice() {
            return dice;
        }

        // Resets the color (the state) of the tile
        public void resetBorder() {
            border.setFill(Color.WHITE);
        }

        // Updates the Text attribute of the Tile to match dice value
        private void updateText() {
            text.setText(dice.getValue());
        }

        private void setPos(int x, int y) {
            pos.setX(x);
            pos.setY(y);
        }

        private Position getPos() {
            return pos;
        }
    }

    public static void main(String[] args) { launch(args); }
}