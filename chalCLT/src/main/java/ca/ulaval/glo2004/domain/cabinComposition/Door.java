package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.Point2D;
import java.io.Serializable;

public class Door extends Accessory implements Serializable {

    // Constructeur de Door
    public Door(Point2D position, DimensionCabin dimension){
        super(position, dimension);
    }

}