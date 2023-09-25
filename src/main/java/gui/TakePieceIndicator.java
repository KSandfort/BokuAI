package gui;

import engine.GameController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TakePieceIndicator extends Circle {

    private int coordinateIndex;
    private GameController gameController;
    private static final int radius = 15;
    public TakePieceIndicator(double centerX, double centerY, int coordinateIndex, GameController gameController) {
        super(centerX, centerY, radius);
        this.setFill(Color.DARKRED);
        this.coordinateIndex = coordinateIndex;
        this.gameController = gameController;
        this.setOnMouseClicked(e -> {
            this.gameController.removePiece(coordinateIndex);
            this.gameController.getBokuBoard().removeAllTakePieceIndicators(gameController.getGameState());
        });
    }
}
