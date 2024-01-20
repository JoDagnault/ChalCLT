package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.cabinComposition.*;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WallDTO {

    public DimensionCabin WallDimension;
    public ImperialMeasure minAccessoryDistance;
    public List<Accessory> AccessoryList = new ArrayList<>();
    public WallType wallType;
    public UUID Uuid;

    public WallDTO(Wall wall){
        WallDimension = new DimensionCabin(wall.getWallDimension());
        minAccessoryDistance = new ImperialMeasure(wall.getMinAccessoryDistance());
        wallType = wall.getWallType();
        AccessoryList.addAll(wall.getAccessoryList());
        Uuid = wall.getUUID();
    }
}
