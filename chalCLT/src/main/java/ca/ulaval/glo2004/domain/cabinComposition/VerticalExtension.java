package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.io.Serializable;
import static ca.ulaval.glo2004.domain.utils.RoofUtils.heightCalculator;

public class VerticalExtension implements Serializable {
    private DimensionCabin dimension;
    private Cabin cabin;
    private Roof roof;

    // Constructeur de VeticalExtension
    public VerticalExtension(Cabin p_cabin, Roof p_roof) {
        this.cabin = p_cabin;
        this.roof = p_roof;
        this.dimension = new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), roof.getPlankAngle())));
    }

    // Obtenir les dimensions
    public DimensionCabin getDimension() {
        return dimension;
    }

    // Définir les dimensions
    public void setDimension(DimensionCabin dimensions) {
        if (roof.getOrientation() == OrientationType.FRONT || roof.getOrientation() == OrientationType.BACK) {
            this.dimension = new DimensionCabin(dimensions.getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(dimensions.getWidthFloat(), roof.getPlankAngle())));
        }
        else {
            this.dimension = new DimensionCabin(dimensions.getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(dimensions.getLengthFloat(), roof.getPlankAngle())));
        }
    }

    public Roof getRoof() {
        return roof;
    }
}
