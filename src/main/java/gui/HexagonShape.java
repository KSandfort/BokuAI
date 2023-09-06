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

    public HexagonShape() {
        super();
        this.getPoints().addAll(hexagonUnitCoordinates);
    }

}