package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.cabinComposition.*;

import java.util.UUID;

public class RoofDTO {

    public InclinedPlank inclinedPlank;
    public Pinion pinionLeft;
    public Pinion pinionRight;
    public float plankAngle;
    public VerticalExtension verticalExtension;
    public OrientationType orientation;
    public UUID Uuid;

    public RoofDTO(Cabin cabin, Roof roof){
        inclinedPlank = new InclinedPlank(cabin, roof);
        pinionLeft = new Pinion(PinionType.LEFT, cabin, roof);
        pinionRight = new Pinion(PinionType.RIGHT, cabin, roof);
        verticalExtension = new VerticalExtension(cabin, roof);
        plankAngle = roof.getPlankAngle();
        orientation = roof.getOrientation();
        roof.getUUID();
    }
}