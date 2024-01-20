package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wall implements Serializable {
    private DimensionCabin wallDimension;
    private WallType wallType;
    private ImperialMeasure minAccessoryDistance;
    private UUID uuid;

    // Obtenir la distance minimale entre les accessoires
    public ImperialMeasure getMinAccessoryDistance() {
        return minAccessoryDistance;
    }

    // Définir la distance minimale entre les accessoires
    public void setMinAccessoryDistance(ImperialMeasure minAccessoryDistance) {
        this.minAccessoryDistance = minAccessoryDistance;
    }

    // Liste d'accessoires se trouvant sur le mur
    private List<Accessory> accessoryList = new ArrayList<>();

    // Obtenir la liste d'accessoire se trouvant sur le mur
    public List<Accessory> getAccessoryList() {return this.accessoryList;}

    // Obtenir le type du mur
    public WallType getWallType(){return this.wallType;}

    // Constructeur par défaut de Wall
    public Wall(DimensionCabin p_wallDimension, WallType p_wallType){
        this.wallType = p_wallType;
        this.wallDimension = p_wallDimension;
    }

    // Constructeur de Wall
    public Wall(DimensionCabin p_wallDimension, WallType p_wallType, ImperialMeasure minAccessoryDistance){
        this.wallType = p_wallType;
        this.wallDimension = p_wallDimension;
        this.minAccessoryDistance = minAccessoryDistance;
        this.uuid = UUID.randomUUID();
    }
    public Wall(Wall wall){
        wallType = wall.getWallType();
        wallDimension = wall.getWallDimension();
        minAccessoryDistance = wall.getMinAccessoryDistance();
    }

    // Permet de savoir si une porte est valide sur un mur
    public boolean isDoorOnFloor(Accessory targetAccessory){
        return targetAccessory.getYPosition().addMeasuresCopy(targetAccessory.getHeight()).isEqual(this.wallDimension.getHeight().subtractMeasuresCopy(this.minAccessoryDistance));
    }

    public void checkAccessoryWithinWallLimits(Accessory targetAccessory){
        if (targetAccessory.getAccessoryType().equals(AccessoryType.DOOR) && !isDoorOnFloor(targetAccessory)){
            targetAccessory.setWithinWallLimits(false);
            return;
        }

        // Retourne faux si l'accessoire n'est pas dans les bordures du mur
        if(this.getWallType().equals(WallType.FRONT) || this.getWallType().equals(WallType.BACK)){
            if (
                    // Si l'accessoire se trouve à droite de la bordure gauche du mur
                    targetAccessory.getXPosition().isBiggerOrEqual(minAccessoryDistance)
                            // Si l'accessoire se trouve à gauche de la bordure droite du mur
                            && targetAccessory.getXPosition().addMeasuresCopy(targetAccessory.getLength()).addMeasuresCopy(minAccessoryDistance).isSmallerOrEqual(this.wallDimension.getLength())
                            // Si l'accessoire se trouve en dessous de la bordure supérieur du mur
                            && targetAccessory.getYPosition().isBiggerOrEqual(minAccessoryDistance)
                            // Si l'accessoire n'est pas une porte et qu'il se trouve au dessus de la bordure inférieure du mur
                            && ((!targetAccessory.getAccessoryType().equals(AccessoryType.DOOR) && targetAccessory.getYPosition().addMeasuresCopy(targetAccessory.getHeight()).addMeasuresCopy(minAccessoryDistance).isSmallerOrEqual(this.wallDimension.getHeight())) || targetAccessory.getAccessoryType().equals(AccessoryType.DOOR))
            ){
                targetAccessory.setWithinWallLimits(true);}
            else {
                targetAccessory.setWithinWallLimits(false);}
        }
        // Retourne faux si l'accessoire n'est pas dans les bordures du mur
        else if (this.getWallType().equals(WallType.LEFT) || this.getWallType().equals(WallType.RIGHT)){
            if (
                    // Si l'accessoire se trouve à droite de la bordure gauche du mur
                    targetAccessory.getXPosition().isBiggerOrEqual(minAccessoryDistance.addMeasuresCopy(this.wallDimension.getWidth().getHalf()))
                            // Si l'accessoire se trouve à gauche de la bordure droite du mur
                            && targetAccessory.getXPosition().addMeasuresCopy(targetAccessory.getLength()).addMeasuresCopy(minAccessoryDistance).addMeasuresCopy(this.wallDimension.getWidth().getHalf()).isSmallerOrEqual(this.wallDimension.getLength())
                            // Si l'accessoire se trouve en dessous de la bordure supérieur du mur
                            && targetAccessory.getYPosition().isBiggerOrEqual(minAccessoryDistance)
                            // Si l'accessoire n'est pas un porte et qu'il se trouve au dessus de la bordure inférieure du mur
                            && ((!targetAccessory.getAccessoryType().equals(AccessoryType.DOOR) && targetAccessory.getYPosition().addMeasuresCopy(targetAccessory.getHeight()).addMeasuresCopy(minAccessoryDistance).isSmallerOrEqual(this.wallDimension.getHeight())) || targetAccessory.getAccessoryType().equals(AccessoryType.DOOR))
            ){
                targetAccessory.setWithinWallLimits(true);}
            else {
                targetAccessory.setWithinWallLimits(false);}
        }
    }

    public void checkAccessoryOverlap(Accessory targetAccessory){
        for (Accessory accessory : this.getAccessoryList()) {

            if (accessory == targetAccessory){
                continue;
            }

            if(targetAccessory.isAccessoryOverlapping(accessory, minAccessoryDistance)){
                targetAccessory.addOverlappingAccessory(accessory.getAccessoryId());
                accessory.addOverlappingAccessory(targetAccessory.getAccessoryId());
            }
            else{
                targetAccessory.removeOverlappingAccessory(accessory.getAccessoryId());
                accessory.removeOverlappingAccessory(targetAccessory.getAccessoryId());
            }
            this.isAccessoryValid(accessory);
        }
    }

    public void validateAccessory (Accessory targetAccessory){
        this.checkAccessoryOverlap(targetAccessory);
        this.checkAccessoryWithinWallLimits(targetAccessory);
    }

    public boolean isAccessoryValid(Accessory targetAccessory) {
        if(targetAccessory.isWithinWallLimits() && !targetAccessory.isOverlapping()){
            targetAccessory.setValidity(true);
            return true;
        }
        targetAccessory.setValidity(false);
        return false;
    }


    // Permet de supprimer un accessoire
    public void deleteAccessory(int accessoryID) {
        for (Accessory accessory : accessoryList){
            if(accessory.getAccessoryId() != accessoryID){
                accessory.removeOverlappingAccessory(accessoryID);
            }
            this.isAccessoryValid(accessory);
        }
        if(accessoryID != -1){
            this.accessoryList.remove((indexOfAccessory(accessoryID)));
        }
    }

    // Permet d'ajouter un accessoire
    public boolean addAccessory(Point2D position, DimensionCabin dimension, AccessoryType accessoryType) {
        // Should return true if added successfully else false.

        Accessory accessory = null;

        if (accessoryType == AccessoryType.WINDOW){
            accessory = new Window(position, dimension);
        }
        else if (accessoryType == AccessoryType.DOOR){
            accessory = new Door(position, dimension);
        }
        else {return false;}

        this.accessoryList.add(accessory);
        this.validateAccessory(accessory);
        return this.isAccessoryValid(accessory);

    }

    // Permet de modifier un accessoire
    public boolean editAccessory(Point2D newPosition, DimensionCabin newDimension, int accessoryID){
        // Should return true if edited successfully
        Accessory accessory = accessoryList.get(indexOfAccessory(accessoryID));
        accessory.editDimension(newDimension);
        accessory.editPosition(newPosition);
        this.validateAccessory(accessory);
        return this.isAccessoryValid(accessory);
    }

    // Permet d'obtenir les dimensions d'un mur
    public DimensionCabin getWallDimension(){
        return wallDimension;
    }

    // Permet de définir les dimensions d'un mur
    public void setWallDimension(DimensionCabin wallDimension) {
        this.wallDimension = wallDimension;
    }

    // Permet de connaitre le statut de la sélection
    public void selectionStatus(double x, double y, ImperialMeasure width){
        int lastID = 0;
        double offSetY = 500-(wallDimension.getHeight().convertImperialToPixels()/2);
        double offSetX = 600-(width.convertImperialToPixels()/2);
        if(getWallType() == WallType.FRONT || getWallType() == WallType.BACK){
            offSetX = 600-(wallDimension.getLength().convertImperialToPixels()/2);
        }
        for (Accessory accessory : this.accessoryList) {
            if(lastID == -1){
                lastID = 0;
            }
            if (!accessory.isSelected(x, y, offSetX, offSetY))
            {
                if(accessory.getSelectionStatus()){
                    accessory.modifySelectionStatus();
                }
                if (accessory.getIDSelected() == accessoryList.indexOf(accessory)){
                    lastID = accessory.getIDSelected();
                    accessory.resetIDSelected();
                }
            }
            //Permet de sélectionné seulement le dernier accessoire ajouté lorsque deux accessoires sont superposés
            else if(accessory.getAccessoryId() != accessory.getIDSelected() && accessory.getIDSelected() < accessory.getAccessoryId() && accessory.getIDSelected() != -1){
                if(indexOfAccessory(accessory.getIDSelected())== -1)
                {
                    accessory.modifySelectionStatus();
                    accessory.setIDSelected(accessory.getAccessoryId());
                    lastID = accessory.getIDSelected();
                    continue;
                }
                else if(accessoryList.get(indexOfAccessory(accessory.getIDSelected())).getSelectionStatus()){
                    accessoryList.get(indexOfAccessory(accessory.getIDSelected())).modifySelectionStatus();
                }
                accessory.modifySelectionStatus();
                accessory.setIDSelected(accessory.getAccessoryId());
                lastID = accessory.getIDSelected();
            }
            else if (accessory.isSelected(x,y,offSetX,offSetY)) {
                accessory.modifySelectionStatus();
                accessory.setIDSelected(accessory.getAccessoryId());
                lastID = accessory.getIDSelected();
            }
        }
    }
    public void resetSelectionStatus(){
        for(Accessory accessory : this.accessoryList){
            if(accessory.getSelectionStatus()){
                accessory.modifySelectionStatus();
            }
        }
    }

    // Permet d'obtenir l'accessoire sélectionner
    public int getAccessorySelected(){
        for(Accessory accessory : this.accessoryList){
            if (accessory.getSelectionStatus()){
                return accessory.getAccessoryId();
            }
        }
        return -1;
    }
    public int indexOfAccessory(int IDSelected){
        for(Accessory accessory : this.accessoryList){
            if(accessory.getAccessoryId() == IDSelected){
                return accessoryList.indexOf(accessory);
            }
        }
        return -1;
    }
    public boolean mouseOnWall(double x, double y){
        return (isInsideWallLength(x) && isInsideWallHeight(y));
    }
    public boolean mouseOnSideWall(double x, double y, Cabin currentCabin){
        return (isInsideSideWallLength(x, currentCabin.getErrorMargin(), currentCabin)&& isInsideWallHeight(y));
    }
    private boolean isInsideWallLength(double x){
        return (x > 600 - (wallDimension.getLength().convertImperialToPixels()/2) && x < ((600- (wallDimension.getLength().convertImperialToPixels())/2) + wallDimension.getLength().convertImperialToPixels()));
    }
    private boolean isInsideWallHeight(double y){
        return (y > 500 - (wallDimension.getHeight().convertImperialToPixels()/2) && y < ((500 - (wallDimension.getHeight().convertImperialToPixels())/2)+ wallDimension.getHeight().convertImperialToPixels()));
    }
    private boolean isInsideSideWallLength(double x, ImperialMeasure errorMargin, Cabin currentCabin){
        return (x > (600 - (wallDimension.getLength().convertImperialToPixels()/2)) + currentCabin.getThickness().convertImperialToPixels()/2 + errorMargin.convertImperialToPixels()/2)
                && x < ((600- wallDimension.getLength().convertImperialToPixels()/2) + (wallDimension.getLength().convertImperialToPixels() - currentCabin.getThickness().convertImperialToPixels()/2 - errorMargin.convertImperialToPixels()/2));
    }
    public boolean mouseOnGrooveLeft(double x, double y, Cabin currentCabin){
        return (isInsideGrooveLeftWidth(x,currentCabin.getErrorMargin(), currentCabin) && isInsideWallHeight(y));
    }
    public boolean mouseOnGrooveRight(double x, double y, Cabin currentCabin){
        return(isInsideGrooveRightWidth(x,currentCabin.getErrorMargin(), currentCabin)&& isInsideWallHeight(y));
    }
    private boolean isInsideGrooveLeftWidth(double x, ImperialMeasure errorMargin, Cabin currentCabin){
        return (x > (600 - wallDimension.getLength().convertImperialToPixels()/2) && x < ((600 - wallDimension.getLength().convertImperialToPixels()/2) + currentCabin.getThickness().convertImperialToPixels()/2 - errorMargin.convertImperialToPixels()/2));
    }
    private boolean isInsideGrooveRightWidth(double x, ImperialMeasure errorMargin, Cabin currentCabin){
        return(x > ((600 - wallDimension.getLength().convertImperialToPixels()/2) + wallDimension.getLength().convertImperialToPixels() - currentCabin.getThickness().convertImperialToPixels()/2 + errorMargin.convertImperialToPixels()/2) && x < ((600 - wallDimension.getLength().convertImperialToPixels()/2) + wallDimension.getLength().convertImperialToPixels()));
    }
    public boolean isOnTopWallBack(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (400 - currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        return (isInsideRectangle(x,y, offSetX, offSetY, wallDimension.getLength(), currentCabin.getThickness().getHalf().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf()))||
                isInsideRectangle(x,y, offSetX + currentCabin.getThickness().getHalf().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels(), offSetY, wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getErrorMargin())), currentCabin.getThickness().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf())));
    }
    public boolean isOnTopWallFront(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (400 - currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2) + currentCabin.getDimensionCabin().getWidth().subtractMeasuresCopy(currentCabin.getThickness()).convertImperialToPixels();
        return (isInsideRectangle(x,y,offSetX, offSetY + currentCabin.getThickness().getHalf().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels(), wallDimension.getLength(), currentCabin.getThickness().getHalf())||
                isInsideRectangle(x,y, offSetX + currentCabin.getThickness().getHalf().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels(), offSetY + currentCabin.getErrorMargin().getHalf().convertImperialToPixels(), wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getErrorMargin())), currentCabin.getThickness().subtractMeasuresCopy(currentCabin.getErrorMargin())));
    }
    public boolean isOnTopWallLeft(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (400 - currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2) + currentCabin.getThickness().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels();
        return (isInsideRectangle(x,y,offSetX, offSetY - currentCabin.getThickness().getHalf().convertImperialToPixels(), currentCabin.getThickness().getHalf().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf()), wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getErrorMargin())))||
                isInsideRectangle(x,y, offSetX, offSetY, currentCabin.getThickness().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf()), wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getThickness()).addMeasuresCopy(currentCabin.getErrorMargin()))));
    }
    public boolean isOnTopWallRight(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2) + currentCabin.getDimensionCabin().getLength().subtractMeasuresCopy(currentCabin.getThickness()).convertImperialToPixels();
        double offSetY = (400 - currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2) + currentCabin.getThickness().getHalf().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels();
        return(isInsideRectangle(x,y,offSetX + currentCabin.getThickness().getHalf().addMeasuresCopy(currentCabin.getErrorMargin().getHalf()).convertImperialToPixels(),offSetY,currentCabin.getThickness().getHalf().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf()),wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getErrorMargin()))) ||
                isInsideRectangle(x,y, offSetX , offSetY + currentCabin.getThickness().getHalf().convertImperialToPixels(),  currentCabin.getThickness().subtractMeasuresCopy(currentCabin.getErrorMargin().getHalf()), wallDimension.getLength().subtractMeasuresCopy(currentCabin.getThickness().addMeasuresCopy(currentCabin.getThickness()).addMeasuresCopy(currentCabin.getErrorMargin()))));
    }
    private boolean isInsideRectangle(double x, double y,double positionX, double positionY, ImperialMeasure lenghtRectangle, ImperialMeasure widthRectangle) {
        return (isInsideX(x,positionX,lenghtRectangle) && isInsideY(y, positionY,widthRectangle));
    }
    private boolean isInsideX(double x, double offSetX, ImperialMeasure lenghtRectangle){
        return(x> offSetX && x < offSetX + lenghtRectangle.convertImperialToPixels());
    }
    private boolean isInsideY(double y, double offSetY, ImperialMeasure widthRectangle){
        return(y > offSetY && y< offSetY + widthRectangle.convertImperialToPixels());
    }
    public UUID getUUID(){
        return uuid;
    }
}