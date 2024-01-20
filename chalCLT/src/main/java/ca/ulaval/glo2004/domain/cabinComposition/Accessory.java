package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Accessory implements Serializable {

    private static int currentID = -1;
    private int accessoryId;
    private DimensionCabin dimension;
    private Point2D position;
    private UUID uuid;
    private boolean selectionStatus = false;
    private static int selectedID;
    private boolean validity;
    public boolean isValid(){
        return this.validity;
    }
    public void setValidity(boolean validity){
        this.validity = validity;
    }
    private boolean withinWallLimits;
    public boolean isWithinWallLimits(){
        return this.withinWallLimits;
    }
    public void setWithinWallLimits(boolean within){
        this.withinWallLimits = within;
    }
    private List<Integer> overlappingAccessories = new ArrayList<>();
    public void addOverlappingAccessory(int accessoryId){
        this.overlappingAccessories.add(accessoryId);
    }
    public void removeOverlappingAccessory(int accessoryId){
        if(this.overlappingAccessories.contains(accessoryId)){
            this.overlappingAccessories.remove(Integer.valueOf(accessoryId));
        }
    }
    // Retourne true si l'accessoire overlap un autre
    public boolean isOverlapping(){
        if (this.overlappingAccessories.size() == 0){
            return false;
        }
        else {
            return true;
        }
    }
    // Obtenir la position
    public Point2D getPosition(){
        return this.position;
    }

    // Obtenir le height
    public ImperialMeasure getHeight (){return this.dimension.getHeight();}

    // Obtenir le length
    public ImperialMeasure getLength (){return this.dimension.getLength();}

    // Obtenir la position en x
    public ImperialMeasure getXPosition (){return this.position.getXCoord();}

    // Obtenir la position en y
    public ImperialMeasure getYPosition (){return this.position.getYCoord();}

    // Constructeur par défaut. Dimension 1 pied x 1 pied x 1 pied. Position (0, 0).
    public Accessory() {
        this.dimension = new DimensionCabin();
        this.position = new Point2D();
        this.accessoryId = ++currentID;
    }

    // Constructeur
    public Accessory(Point2D position, DimensionCabin dimension) {
        this.position = position;
        this.dimension = dimension;
        this.accessoryId = ++currentID;
        this.uuid = UUID.randomUUID();
    }

    // Test si l'accessoire Overlap un autre accessoire
    public Boolean isAccessoryOverlapping (Accessory wallAccessory, ImperialMeasure minAccessoryDistance){
        return !(
                // Si l'accessoire se trouve en dessous de wallAccessory
                this.getYPosition().isBiggerOrEqual(wallAccessory.getYPosition().addMeasuresCopy(wallAccessory.getHeight()).addMeasuresCopy(minAccessoryDistance))
                // Si l'accessoire se trouve au dessus de wallAccessory
                || this.getYPosition().addMeasuresCopy(this.getHeight()).addMeasuresCopy(minAccessoryDistance).isSmallerOrEqual(wallAccessory.getYPosition())
                // Si l'accessoire se trouve à gauche de wallAccessory
                || this.getXPosition().addMeasuresCopy(this.getLength()).addMeasuresCopy(minAccessoryDistance).isSmallerOrEqual(wallAccessory.getXPosition())
                // Si l'accessoire se trouve à droite de wallAccessory
                || this.getXPosition().isBiggerOrEqual(wallAccessory.getXPosition().addMeasuresCopy(wallAccessory.getLength()).addMeasuresCopy(minAccessoryDistance))
                );}

    @Override
    public String toString() {
        return getAccessoryType().name() + " : de dimension " + getXPosition() + " x "+  getYPosition();
    }

    // Test si l'accessoire est d'instance Window ou Door
    public AccessoryType getAccessoryType() {
        AccessoryType accessoryType = null;
        if (this instanceof Window){
            accessoryType = AccessoryType.WINDOW;
        }
        else if (this instanceof Door){
            accessoryType = AccessoryType.DOOR;
        }
        return accessoryType;
    }

    // Permet de modifier les dimensions
    public void editDimension(DimensionCabin dimension) {
        this.dimension = dimension;
    }

    // Permet de modifier la position
    public void editPosition(Point2D position) {
        this.position = position;
    }

    // Permet d'obtenir des dimensions
    public DimensionCabin getDimension(){
       return this.dimension;
    }

    // Permet d'obtenir le ID d'un accessoire
    public int getAccessoryId() {
        return this.accessoryId;
    }

    // Permet de savoir si un accessoire est sélectionné
    public boolean isSelected(double x, double y, double offSetX, double offSetY){
        return( isInsideAccessoryLength(x,offSetX) && isInsideAccessoryHeight(y, offSetY));
    }

    // Permet de savoir si la position se trouve dans le length de l'accessoire
    private boolean isInsideAccessoryLength(double x, double offSetX){
        return(x < (offSetX + position.getXCoord().convertImperialToPixels() + dimension.getLength().convertImperialToPixels())&& x > (offSetX + position.getXCoord().convertImperialToPixels()));
    }

    // Permet de savoir si la position se trouve dans le height de l'accessoire
    private boolean isInsideAccessoryHeight(double y, double offSetY){
        return (y < (offSetY + position.getYCoord().convertImperialToPixels()+ dimension.getHeight().convertImperialToPixels())&&(y>(offSetY+ position.getYCoord().convertImperialToPixels())));
    }
    public void modifySelectionStatus(){
        this.selectionStatus = !this.selectionStatus;
    }
    public boolean getSelectionStatus(){
        return selectionStatus;
    }
    public void setIDSelected(int accessoryId){
        selectedID = accessoryId;
    }
    public void resetIDSelected(){
        selectedID = -1;
    }
    public int getIDSelected(){
        return selectedID;
    }
    public UUID getUUID(){
        return uuid;
    }
}