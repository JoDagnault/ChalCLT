package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.Point2D;

import java.io.Serializable;

public class Window extends Accessory implements Serializable {

    // Constructeur de Window
    public Window(Point2D position, DimensionCabin dimension){
        super(position, dimension);
    }
}
