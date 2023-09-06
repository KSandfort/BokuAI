package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class BokuBoard extends Pane {

    double middle;
    final double spreadX = 40;
    final double spreadY = spreadX * (Math.sqrt(1.5) / 2);

    public BokuBoard(double width, double height) {
        super();
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        middle = width/2;
        this.setStyle("-fx-background-color: blue");
        HexagonShape hs = new HexagonShape();
        this.getChildren().add(hs);
        placeHexagons();
    }

    private void placeHexagons() {
        for (int i = 0; i < 10; i++) { // For letter indices (A-J)
            for (int j = 0; j < 10; j++) { // For number indices (1-10)
                if (j < i + 6 && i < j + 6) {
                    this.getChildren().add(new Circle(middle + spreadX * (i - j), middle * 1.9 + spreadY * (-i - j), 20));
                }
            }
        }
    }
}
