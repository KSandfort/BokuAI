package gui;

import engine.GameController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

/**
 * Part of the GUI that the user has to click on in order to capture a piece.
 */
@Getter
@Setter
public class TakePieceIndicator extends Circle {

    private int coordinateIndex;
    private GameController gameController;
    private static final int radius = 15;

    /**
     * Constructor
     * @param centerX center x coordinate
     * @param centerY center y coordinate
     * @param coordinateIndex index of the piece where the indicator is placed
     * @param gameController game controller instance
     */
    public TakePieceIndicator(double centerX, double centerY, int coordinateIndex, GameController gameController) {
        super(centerX, centerY, radius);
        this.setFill(Color.DARKRED);
        this.coordinateIndex = coordinateIndex;
        this.gameController = gameController;
        this.setOnMouseClicked(e -> {
            this.gameController.removePiece(this.coordinateIndex);
        });
    }
}
