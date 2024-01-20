package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.io.Serializable;
import static ca.ulaval.glo2004.domain.utils.RoofUtils.hypotenuseCalculator;

public class InclinedPlank implements Serializable {
    private DimensionCabin dimension;
    private Cabin cabin;
    private Roof roof;

    public InclinedPlank(Cabin p_cabin, Roof p_roof) {
        this.cabin = p_cabin;
        this.roof = p_roof;
        this.dimension = new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(cabin.getDimensionCabin().getWidthFloat(), roof.getPlankAngle())));
    }

    // Obtenir les dimensions de la planche inclinée
    public DimensionCabin getDimension() {
        return dimension;
    }

    // Définie les dimensions de la planche inclinée
    public void setDimension(DimensionCabin dimensions) {
        dimension.setLength(dimensions.getLength());
        dimension.setHeight(ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(dimensions.getWidthFloat(), roof.getPlankAngle())));
    }

    public Roof getRoof() {
        return roof;
    }
}
