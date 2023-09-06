package gui;

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
    final double spreadX = 33;
    final double spreadY = spreadX * (Math.sqrt(1.5) / 2);
    List<HexagonShape> hexagonShapes = new ArrayList<>();

    public BokuBoard(double width, double height) {
        super();
        this.setMaxWidth(width);
        this.setMaxHeight(height);
        middle = width/2;
        this.setStyle("-fx-background-color: blue");
        placeHexagons();
    }

    private void placeHexagons() {
        for (int i = 0; i < 10; i++) { // For letter indices (A-J)
            for (int j = 0; j < 10; j++) { // For number indices (1-10)
                if (j < i + 6 && i < j + 6) {
                    HexagonShape hs = new HexagonShape(middle + spreadX * (i - j), middle * 1.9 + spreadY * (-i - j), 20);
                    // Change style on hover
                    hs.setOnMouseEntered(e -> hs.setFill(Color.WHITE));
                    hs.setOnMouseExited(e -> hs.setFill(Color.BLACK));
                    this.getChildren().add(hs);
                }
            }
        }
    }
}
