package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.DTO.WallDTO;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Cabin implements Serializable {
    private ImperialMeasure minAccessoryDistance; // minimum 3 pouces
    private DimensionCabin dimensionCabin;
    final private ImperialMeasure defaultLength = new ImperialMeasure(10,0,0,1);
    final private ImperialMeasure defaultHeight =   new ImperialMeasure(8,0,0,1);
    final private  ImperialMeasure defaultWidth = new ImperialMeasure(10,0,0,1);
    final private ImperialMeasure defaultThickness = new ImperialMeasure(0,6,0,1);
    private Roof roof;
    private String cabinName;
    private ImperialMeasure thickness;
    private ImperialMeasure errorMargin;

    private UUID uuid;

    private ArrayList<Wall> wallList = new ArrayList<Wall>();


    public Cabin(DimensionCabin dimensionCabin, String cabinName, ImperialMeasure thickness, ImperialMeasure errorMargin, Roof roof) {
        this.dimensionCabin = dimensionCabin;
        this.cabinName = cabinName;
        this.thickness = thickness;
        this.createWalls();
        this.setMinAccessoryDistance(new ImperialMeasure(0, 3));
        this.errorMargin = errorMargin;
        this.roof = roof;
        uuid = UUID.randomUUID();
    }
    public Cabin(DimensionCabin dimensionCabin, String cabinName, ImperialMeasure thickness, ImperialMeasure errorMargin) {
        this.dimensionCabin = dimensionCabin;
        this.cabinName = cabinName;
        this.thickness = thickness;
        this.createWalls();
        this.setMinAccessoryDistance(new ImperialMeasure(0, 3));
        this.errorMargin = errorMargin;
        this.roof = new Roof(this);
        uuid = UUID.randomUUID();
    }

    public void addWall(DimensionCabin p_wallDimension, WallType p_wallType) {
        Wall newWall = new Wall(p_wallDimension, p_wallType);
        this.wallList.add(newWall);
    }

    public void createWalls(){
        DimensionCabin wallDimension = new DimensionCabin(this.dimensionCabin.getLength(), this.getThickness(), this.dimensionCabin.getHeight());
        addWall(wallDimension, WallType.BACK);
        addWall(wallDimension, WallType.FRONT);
        addWall(wallDimension, WallType.LEFT);
        addWall(wallDimension, WallType.RIGHT);
    };


    public ArrayList<Wall> getWallList(){
        return this.wallList;
    }
    public ArrayList<WallDTO> getListWall(){
        ArrayList <WallDTO> listWallDTO = new ArrayList<>();
        for(Wall wall: this.wallList){
            listWallDTO.add(new WallDTO(wall));
        }
        return listWallDTO;
    }

    public Roof getRoof() {
        return roof;
    }

    public ImperialMeasure getMinAccessoryDistance() {
        return minAccessoryDistance;
    }

    public void setMinAccessoryDistance(ImperialMeasure minAccessoryDistance){
        // Refuser si < 3
        this.minAccessoryDistance = minAccessoryDistance;
        for (Wall wall : this.wallList){
            wall.setMinAccessoryDistance(minAccessoryDistance);
        }
    }

    // Permet de définir les dimensions de Cabin
    public void setDimension(DimensionCabin dimensionCabin){
        this.dimensionCabin = dimensionCabin;
        for(Wall wall: this.wallList)
        {
            if(wall.getWallType() == WallType.FRONT || wall.getWallType() == WallType.BACK){
                wall.setWallDimension(new DimensionCabin(dimensionCabin.getLength(), this.getThickness(), dimensionCabin.getHeight()));
            }
            else{
                wall.setWallDimension(new DimensionCabin(dimensionCabin.getWidth(), this.getThickness(), dimensionCabin.getHeight()));
            }
        }
        roof.setDimension(dimensionCabin);
    }

    public DimensionCabin getDimensionCabin(){
        return dimensionCabin;
    }

    public void setThickness(ImperialMeasure thickness){
        this.thickness = thickness;
    }

    public ImperialMeasure getThickness(){
        return thickness;
    }
    public void setErrorMargin(ImperialMeasure errorMargin){ this.errorMargin = errorMargin;}
    public ImperialMeasure getErrorMargin(){return errorMargin;}

    public UUID getUuid(){
        return uuid;
    }

    public String getCabinName() {
        return cabinName;
    }
    public void resetDimension(){
        setDimension(new DimensionCabin(defaultLength, defaultWidth, defaultHeight));
        setThickness(defaultThickness);
    }
}