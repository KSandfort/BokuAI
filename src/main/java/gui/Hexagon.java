package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hexagon extends Polygon {

    private int coordinateIndex;
    Polygon polygon = new Polygon();
    Double[] hexagonUnitCoordinates = new Double[]{
            -1.0, 0.0,
            -0.5, 1.2247,
            0.5, 1.2247,
            1.0, 0.0,
            0.5, -1.2247,
            -0.5, -1.2247
    };

    public Hexagon(double centerX, double centerY, double radius, int coordinateIndex) {
        super();
        this.coordinateIndex = coordinateIndex;
        for (int i = 0; i < hexagonUnitCoordinates.length; i++) {
            if (i % 2 == 0) {
                this.getPoints().add(centerX + (hexagonUnitCoordinates[i] * radius));
            }
            else {
                this.getPoints().add(centerY + (hexagonUnitCoordinates[i] * (radius * 0.75)));
            }
        }
        this.setFill(Color.GRAY);
    }

}