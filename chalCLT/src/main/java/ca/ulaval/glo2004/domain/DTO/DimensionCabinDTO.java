package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;

import java.util.UUID;

public class DimensionCabinDTO {
    public ImperialMeasure Height;
    public ImperialMeasure Length;
    public ImperialMeasure Width;

    public UUID Uuid;

    public DimensionCabinDTO(DimensionCabin dimensionCabin){
        Height = new ImperialMeasure(dimensionCabin.getHeight());
        Length = new ImperialMeasure(dimensionCabin.getLength());
        Width = new ImperialMeasure(dimensionCabin.getWidth());
        Uuid = dimensionCabin.getUUID();
    }

    @Override
    public String toString() {
        return Length + " x " + Width + " x " + Height;
    }
}
