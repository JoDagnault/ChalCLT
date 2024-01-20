package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.cabinComposition.Accessory;
import ca.ulaval.glo2004.domain.cabinComposition.AccessoryType;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.Point2D;
import java.util.UUID;

public class AccessoryDTO {

    public DimensionCabin Dimension;
    public Point2D Position;
    public int AccessoryID;
    public UUID Uuid;
    public AccessoryType AccessoryType;
    public AccessoryDTO(Accessory accessory){
        Dimension = accessory.getDimension();
        Position = accessory.getPosition();
        AccessoryID = accessory.getAccessoryId();
        Uuid = accessory.getUUID();
        AccessoryType = accessory.getAccessoryType();
    }
}
