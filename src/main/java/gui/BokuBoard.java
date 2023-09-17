package gui;

import engine.GameController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BokuBoard extends Pane {

    double middle;
    double yOffset = -40;
    final double spreadX = 33;
    final double spreadY = spreadX * (Math.sqrt(1.5) / 2);
    List<Hexagon> hexagons = new ArrayList<>();
    private GameController gameController;

    public BokuBoard(double width, double height, GameController gameController) {
        super();
        // Set dimensions
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        // Assign current game controller
        this.gameController = gameController;
        middle = width/2;
        this.setStyle("-fx-background-color: blue");
        placeHexagons();
    }

    private void placeHexagons() {
        for (int i = 0; i < 10; i++) { // For letter indices (A-J)
            for (int j = 0; j < 10; j++) { // For number indices (1-10)
                if (j < i + 6 && i < j + 6) {
                    Hexagon hs = new Hexagon(
                            middle + spreadX * (i - j),
                            middle * 1.9 + spreadY * (-i - j) + yOffset,
                            20,
                            (j*10) + i);
                    // Change style on hover
                    // hs.setOnMouseEntered(e -> hs.setFill(Color.RED));
                    // hs.setOnMouseExited(e -> hs.setFill(Color.GRAY));
                    hs.setOnMouseClicked(e -> gameController.attemptMoveOnClick(this, hs.getCoordinateIndex()));
                    // on click
                    hexagons.add(hs);
                    this.getChildren().add(hs);
                }
            }
        }
    }
}
