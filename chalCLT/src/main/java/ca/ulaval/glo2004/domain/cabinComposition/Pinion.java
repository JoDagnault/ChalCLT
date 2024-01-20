package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.cabinComposition.Roof;

import java.io.Serializable;

import static ca.ulaval.glo2004.domain.utils.RoofUtils.heightCalculator;

public class Pinion implements Serializable {
    private DimensionCabin dimension;
    private PinionType pinionType;
    private Cabin cabin;
    private Roof roof;

    // Constructeur de Pinion
    public Pinion(PinionType pinionType, Cabin p_cabin, Roof p_roof) {
        this.cabin = p_cabin;
        this.roof = p_roof;
        this.pinionType = pinionType;
        this.dimension = new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), roof.getPlankAngle())));
    }

    // Obtenir les dimensions du pinion
    public DimensionCabin getDimension() {
        return dimension;
    }

    // Obtenir le type du pinion
    public PinionType getPinionType() {
        return pinionType;
    }

    // Obtenir les dimensions du pinion
    public void setDimension(DimensionCabin dimensions) {
        dimension.setLength(dimensions.getWidth());
        dimension.setHeight(ImperialMeasure.convertFloatToImperial(heightCalculator(dimensions.getWidthFloat(), roof.getPlankAngle())));
    }

    // Définir le type du pinion
    public void setPinionType(PinionType pinionType) {
        this.pinionType = pinionType;
    }

    public Roof getRoof() {
        return roof;
    }
}
