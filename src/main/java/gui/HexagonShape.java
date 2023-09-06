package gui;

import javafx.scene.shape.Polygon;

public class HexagonShape extends Polygon {
    Polygon polygon = new Polygon();
    Double[] hexagonUnitCoordinates = new Double[]{
            -1.0, 0.0,
            -0.5, 1.2247,
            0.5, 1.2247,
            1.0, 0.0,
            0.5, -1.2247,
            -0.5, -1.2247
    };

    public HexagonShape(double centerX, double centerY, double radius) {
        super();
        double stretchX = radius;
        double stretchY = stretchX * (Math.sqrt(1.5));
        for (int i = 0; i < hexagonUnitCoordinates.length; i++) {
            if (i % 2 == 0) {
                this.getPoints().add(centerX + (hexagonUnitCoordinates[i] * stretchX));
            }
            else {
                this.getPoints().add(centerY + (hexagonUnitCoordinates[i] * stretchX * 0.75));
            }
        }
    }

}