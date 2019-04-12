package boggle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

//TODO: Implement Dictionary
//TODO: Implement Timer
//TODO: Implement Word formation out of chosen tiles (letters)

public class Main extends Application {

    private int apu = 0;
    private GridPane board;
    private String[][] boardMatrix;
    private String word = "";
    private ArrayList<Tile> pressedTiles = new ArrayList<>();

    // Initializes the board tiles with dices
    private GridPane initBoard() {

        // Creating tiles with dices
        ArrayList<LetteredDice> dices = LetteredDice.configure();
        ArrayList<Tile> tiles = new ArrayList<>();
        for(LetteredDice d: dices) {
            tiles.add(new Tile(d));
        }

        // Creating a matrix to assist with word formation
        boardMatrix = new String[4][4];
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

    // Populates the window with nodes
    private Parent createRoot() {
        Pane root = new Pane();
        root.setPrefSize(600, 300);
        root.getChildren().addAll(initBoard());
        Button b = new Button("Reset");
        b.setOnMouseClicked(event -> reset());
        root.getChildren().add(b);
        return root;
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
        }
    }

    // Checks if the two letters are adjacent on the board
    private boolean checkAdjacency() {
        boolean value = false;
        int size = pressedTiles.size();
        if(size <= 1) {
            System.out.println("Ei tarvi vielä tsekkailla");
            value = true;
        } else {
            // getgetget
           int x1 = pressedTiles.get(size-2).getPos().getX();
           int y1 = pressedTiles.get(size-2).getPos().getY();
           int x2 = pressedTiles.get(size-1).getPos().getX();
           int y2 = pressedTiles.get(size-1).getPos().getY();
           System.out.println(x1+" "+x2+" "+y1+" "+y2);

           if(x1 == x2 && (y1+1 == y2 || y1-1 == y2)) { //same x-coordinate
               System.out.println("On on!");
               value = true;
           } else if(y1 == y2 && (x1+1 == x2 || x1-1 == x2)) { //same y-coordinate
               System.out.println("On on!");
               value = true;
           } else {
               System.out.println("Ei ole!");
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
     * Class representing a single tile on the board
     */
    private class Tile extends StackPane {

        private LetteredDice dice;
        private Text text;
        private Rectangle border;

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

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            // Event handler for pressing a tile
            setOnMouseClicked(event -> clickTile(this));
        }

        private void clickTile(Tile t) {
            if (border.getFill().equals(Color.WHITE)) {
                border.setFill(Color.LIGHTBLUE);
                pressedTiles.add(t);
                if(checkAdjacency()) {
                    // lisätään letteri sanaan jos tsekki ok
                }
            } else {
                border.setFill(Color.WHITE);
            }
        }

        private LetteredDice getDice() { return dice; }

        // Resets the color (the state) of the tile
        public void resetBorder() { border.setFill(Color.WHITE); }

        // Updates the Text attribute of the Tile to match dice value
        private void updateText() { text.setText(dice.getValue()); }

        private void setPos(int x, int y) { pos.setX(x); pos.setY(y); }
        private Position getPos() { return pos; }
    }
    public static void main(String[] args) { launch(args); }
}