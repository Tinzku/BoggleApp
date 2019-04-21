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

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

//TODO: Implement game logic and structure
//TODO: Implement Dictionary
//TODO: Implement Timer

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

    // Initializes the board tiles with dices
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

    private TableView createTable() {
        // Create a table to show the words
        table = new TableView<>();
        table.setEditable(true);
        TableColumn<Word, String> sanaCol = new TableColumn("Words");
        sanaCol.setCellValueFactory(new PropertyValueFactory<Word, String>("wordToAdd"));
        table.setColumnResizePolicy(table.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(sanaCol);
        table.setTranslateX(320);
        return table;
    }

    // Label to show the current word that the user is trying to get
    private VBox createScoreLabel() {
        Label pisteet = new Label("Score:");
        pisteet.setFont(new Font("Arial", 30));
        scoreLabel = new Label();
        scoreLabel.setText("0");
        scoreLabel.setFont(new Font("Arial", 32));
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(pisteet, scoreLabel);
        vbox.setTranslateX(200);
        vbox.setTranslateY(50);
        return vbox;
    }

    private VBox createWarningLabel() { ;
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


    // Populates the window with nodes
    private Parent createRoot() {
        Pane root = new Pane();
        root.setPrefSize(570, 450);
        root.getChildren().addAll(initBoard());
        root.getChildren().addAll(createTable());
        root.getChildren().addAll(createScoreLabel());
        root.getChildren().add(createWarningLabel());
        Button reset = new Button("Reset");
        reset.setTranslateX(210);
        reset.setTranslateY(180);
        Button confirm = new Button("Confirm");
        confirm.setTranslateX(210);
        confirm.setTranslateY(210);
        Button newGame = new Button("New Game");
        newGame.setTranslateX(210);
        newGame.setTranslateY(150);
        confirm.setOnMouseClicked(event -> confirm());
        reset.setOnMouseClicked(event -> reset());
        newGame.setOnMouseClicked(event -> newGame());
        root.getChildren().addAll(confirm, reset, newGame);
        return root;
    }

    private void confirm() {
        String w = word.toString().toLowerCase();
        if (!addedWords.contains(w)) {
            if (dictionary.contains(w)) {
                Word addedWord = new Word(w);
                table.getItems().add(addedWord);
                addedWords.add(w);
                score.countScore(word);
                System.out.println(score.getScore());
                scoreLabel.setText("" + score.getScore());
                reset();
                warningLabel.setText("");
            } else {
                reset();
                warningLabel.setText("Sanaa ei löytynyt sanakirjasta.");
            }
        } else {
            reset();
            warningLabel.setText("Sana on jo käytetty!");
        }

    }

    // Rolls dice in the start of a new round
    private void shuffle() {
        ObservableList<Node> nodes = board.getChildren();
        for (Node n: nodes) {
            // Casting Node to Tile, rolling the dice and updating the text accordingly
            Tile t = (Tile) n;
            t.getDice().roll();
            t.updateText();
        }
    }

    // Reset
    private void reset() {
        ObservableList<Node> nodes = board.getChildren();
        for (Node n: nodes) {
            Tile t = (Tile) n;
            t.resetBorder();
            pressedTiles.clear();
            word = new StringBuilder();
        }
    }

    // New Game
    private void newGame() {
        shuffle();
        reset();
        table.getItems().clear();
        addedWords.clear();
        score.reset();
        scoreLabel.setText("0");
        warningLabel.setText("");
    }

    // Checks if the two letters are adjacent on the board
    private boolean checkAdjacency() {
        boolean value = false;
        int size = pressedTiles.size();
        if (size <= 1) {
            System.out.println("Ei tarvi vielä tsekkailla");
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

    private void timer() {
        /*
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> doSomething()));
        timeline.play();
        */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("gamewindow.fxml"));
        //primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setScene(new Scene(createRoot()));
        primaryStage.setTitle("Boggle");
        primaryStage.show();
    }

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
                warningLabel.setText("Saman ruudun painaminen aloittaa sanan alusta.");
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