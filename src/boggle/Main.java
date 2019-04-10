package boggle;

import javafx.application.Application;
import javafx.geometry.Pos;
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

public class Main extends Application {

    // Initializes the board tiles with dices
    private GridPane initBoard() {

        // Creating tiles with dices
        ArrayList<LetteredDice> dices = LetteredDice.configure();
        ArrayList<Tile> tiles = new ArrayList<>();
        for(LetteredDice d: dices) {
            tiles.add(new Tile(d));
        }

        // Creating a GridPane-board
        GridPane board = new GridPane();
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

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("gamewindow.fxml"));
        //primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setScene(new Scene(createRoot()));
        primaryStage.setTitle("Boggle");
        primaryStage.show();
    }

    /**
     * Tile ?
     */
    private class Tile extends StackPane {

        private LetteredDice dice;
        private Text text;

        public Tile(LetteredDice dice) {
            Rectangle border = new Rectangle(50, 50);
            border.setFill(Color.WHITE); // painalluksen feedback muuttuva väri?
            border.setStroke(Color.BLACK);
            this.dice = dice;
            text = new Text(dice.getValue());
            text.setFont(Font.font(30));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);
        }

        private LetteredDice getDice() {
            return dice;
        }

        private void setDice(LetteredDice dice) {
            this.dice = dice;
        }

        //TODO: edit methods to operate with String values instead of Text-objects
        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }
    }

    public static void main(String[] args) { launch(args); }
}
