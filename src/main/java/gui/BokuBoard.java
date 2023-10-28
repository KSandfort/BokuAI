package gui;

import engine.BoardState;
import engine.GameController;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the boku board (GUI)
 */
@Getter
@Setter
public class BokuBoard extends Pane {

    double middle;
    double yOffset = -40;
    final double spreadX = 33;
    final double spreadY = spreadX * (Math.sqrt(1.5) / 2);
    List<Hexagon> hexagons = new ArrayList<>();
    private GameController gameController;

    /**
     * Constructor
     * @param width board width
     * @param height board height
     * @param gameController game controller instance
     */
    public BokuBoard(double width, double height, GameController gameController) {
        super();
        // Set dimensions
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        // Assign current game controller
        this.gameController = gameController;
        middle = width/2;
        this.setStyle("-fx-background-color:#1A4645; -fx-border-radius: 20;");
        placeHexagons();
    }

    /**
     * Distributes all hexagons on the field.
     */
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
                    hs.setOnMouseEntered(e -> hs.setFill(Color.web("0xF58800")));
                    hs.setOnMouseExited(e -> hs.setFill(hs.getBaseColour()));
                    hs.setOnMouseClicked(e -> {
                        try {
                            gameController.attemptMoveOnClick(hs.getCoordinateIndex());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    hexagons.add(hs);
                    this.getChildren().add(hs);
                }
            }
        }
    }

    /**
     * Place markers for capture moves
     * @param coordinateIndex target index
     */
    public void placeTakeMarker(int coordinateIndex) {
        int index1 = coordinateIndex / 10;
        int index2 = coordinateIndex % 10;

        TakePieceIndicator tpi = new TakePieceIndicator(
                middle - spreadX * (index1 - index2),
                middle * 1.9 + spreadY * (-index1 - index2) + yOffset,
                coordinateIndex, this.gameController);
        tpi.setFill(Color.DARKRED);

        this.getChildren().add(tpi);
    }

    /**
     * Remove all capture indicators from the board.
     * @param gameState current game state
     */
    public void removeAllTakePieceIndicators(int gameState) {
        List<Node> toRemove = new ArrayList<>();
        for (Node n : this.getChildren()) {
            if (n instanceof TakePieceIndicator) {
                toRemove.add(n);
            }
        }
        this.getChildren().removeAll(toRemove);
        if (gameState == 5) {
            gameController.setGameState(2);
        } else if (gameState == 6) {
            gameController.setGameState(1);
        }
    }

    /**
     * Refresh the board (GUI)
     * @param boardState current board state
     */
    public void updateGUI(BoardState boardState) {
        for (Hexagon hex : this.getHexagons()) {
            int colourCode = boardState.getBoard()[hex.getCoordinateIndex()];
            if (colourCode == 0) {
                hex.setBaseColour(Hexagon.unsetColour);
            }
            else if (colourCode == 1) {
                hex.setBaseColour(Color.WHITE);
            }
            else if (colourCode == -1) {
                hex.setBaseColour(Color.BLACK);
            }
            hex.setFill(hex.getBaseColour());
        }
    }

}
