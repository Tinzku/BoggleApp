package boggle;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private static int ALLOCATED_ID = 0;

    private GridPane board;
    private String word = "";

    // Initializes the board tiles with dices
    private GridPane initBoard() {

        // Creating tiles with dices
        ArrayList<LetteredDice> dices = LetteredDice.configure();
        ArrayList<Tile> tiles = new ArrayList<>();
        int i = 0;
        for(LetteredDice d: dices) {
            tiles.add(new Tile(d));
            System.out.print(tiles.get(i).getID());
            i++;
        }

        // Creating a GridPane-board
        board = new GridPane();
        board.setPrefSize(300, 300);
        board.setAlignment(Pos.CENTER_LEFT);

        // Vois vähän loopilla optimoida ":D"
        board.add(tiles.get(0), 0, 0);
        board.add(tiles.get(1), 0, 1);
        board.add(tiles.get(2), 0, 2);
        board.add(tiles.get(3), 0, 3);
        board.add(tiles.get(4), 1, 0);
        board.add(tiles.get(5), 1, 1);
        board.add(tiles.get(6), 1, 2);
        board.add(tiles.get(7), 1, 3);
        board.add(tiles.get(8), 2, 0);
        board.add(tiles.get(9), 2, 1);
        board.add(tiles.get(10), 2, 2);
        board.add(tiles.get(11), 2, 3);
        board.add(tiles.get(12), 3, 0);
        board.add(tiles.get(13), 3, 1);
        board.add(tiles.get(14), 3, 2);
        board.add(tiles.get(15), 3, 3);

        return board;
    }

    // Populates the window with nodes
    private Parent createRoot() {
        Pane root = new Pane();
        root.setPrefSize(600, 300);
        root.getChildren().addAll(initBoard());
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

        protected LetteredDice dice;
        private Text text;
        Rectangle border;
        int ID;

        public Tile(LetteredDice dice) {
            this.dice = dice;

            // Numbering tiles for (easy) adjacency checking
            ID = ALLOCATED_ID;
            ALLOCATED_ID++;

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
            setOnMouseClicked(event -> clickTile(ID));
        }

        /**
         * Chooses/un-chooses the letter to be used in a word
         * @param ID
         */
        //TODO: Currently only changes tile fill value
        private void clickTile(int ID) {
            if (border.getFill().equals(Color.WHITE)) {
                border.setFill(Color.LIGHTBLUE);
                // shuffle(); // käytin noppien sekottamisen testaukseen
                word = word + getDice().getValue();
                System.out.println("Word now: "+word);
            } else {
                border.setFill(Color.WHITE);
            }
        }

        public LetteredDice getDice() { return dice; }
        public int getID() { return ID; }

        // Updates the Text attribute of the Tile to match dice value
        public void updateText() { text.setText(dice.getValue()); }

        // Redundant?
        public Text getText() { return text; }
        public void setText(Text text) { this.text = text; }
    }
    public static void main(String[] args) { launch(args); }
}
