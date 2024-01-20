package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.cabinComposition.Cabin;
import ca.ulaval.glo2004.domain.cabinComposition.Roof;
import ca.ulaval.glo2004.domain.cabinComposition.Wall;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.util.ArrayList;
import java.util.UUID;

public class CabinDTO {

    public DimensionCabin CabinDimension;
    public ArrayList<Wall> WallList = new ArrayList<>();
    public String CabinName;

    public Roof Roof;
    public UUID Uuid;
    public ImperialMeasure WallThickness;

    public ImperialMeasure Mindist;

    public CabinDTO(Cabin cabin){
        CabinDimension = new DimensionCabin(cabin.getDimensionCabin());
        CabinName = cabin.getCabinName();
        Uuid = cabin.getUuid();
        WallThickness = cabin.getThickness();
        Roof = cabin.getRoof();
        for(int i = 0; i < cabin.getWallList().size(); i++){
            WallList.add(new Wall(cabin.getWallList().get(i)));
        }
        Mindist = cabin.getMinAccessoryDistance();

    }
}
