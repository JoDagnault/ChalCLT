package ca.ulaval.glo2004.domain.cabinComposition;

import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.RoofUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

import static ca.ulaval.glo2004.domain.utils.RoofUtils.heightCalculator;
import static ca.ulaval.glo2004.domain.utils.RoofUtils.hypotenuseCalculator;

public class Roof implements Serializable {
    private InclinedPlank inclinedPlank;
    private Pinion pinionLeft;
    private Pinion pinionRight;
    private VerticalExtension verticalExtension;
    private OrientationType orientation;
    private float plankAngle;
    protected Cabin cabin;
    private UUID uuid;

    public Roof(Cabin p_cabin) {
        cabin = p_cabin;
        orientation = OrientationType.FRONT;
        plankAngle = 15.0f;
        inclinedPlank = new InclinedPlank(cabin, this);
        pinionLeft = new Pinion(PinionType.LEFT, cabin, this);
        pinionRight = new Pinion(PinionType.RIGHT, cabin, this);
        verticalExtension = new VerticalExtension(cabin, this);
        this.uuid = UUID.randomUUID();
    }

    public void setOrientation (OrientationType orientation) {
        this.orientation = orientation;
        if (orientation == OrientationType.LEFT || orientation == OrientationType.RIGHT) {
            pinionLeft.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
            pinionRight.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
            verticalExtension.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
            inclinedPlank.setDimension(new DimensionCabin(ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle)), cabin.getThickness(), cabin.getDimensionCabin().getWidth()));
        }

        else {
            pinionLeft.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
            pinionRight.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
            verticalExtension.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
            inclinedPlank.setDimension(new DimensionCabin(ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle)), cabin.getThickness(), cabin.getDimensionCabin().getWidth()));
        }
    }

    public OrientationType getOrientation() {return orientation;}

    public InclinedPlank getInclinedPlank() {return inclinedPlank;}

    public void setInclinedPlank(InclinedPlank inclinedPlank) {
        this.inclinedPlank = inclinedPlank;
    }

    public VerticalExtension getVerticalExtension() {return verticalExtension;}

    public void setVerticalExtension(VerticalExtension verticalExtension) {
        this.verticalExtension = verticalExtension;
    }

    public Pinion getPinionLeft() {return pinionLeft;}

    public void setPinionLeft(Pinion pinionLeft) {
        this.pinionLeft = pinionLeft;
    }

    public Pinion getPinionRight() {return pinionRight;}

    public void setPinionRight(Pinion pinionRight) {
        this.pinionRight = pinionRight;
    }

    public float getPlankAngle() {
        return plankAngle;
    }

    // Définie l'angle de la planche inclinée
    public void setPlankAngle(float p_plankAngle) {
        if (isAngleValid(plankAngle)) {
            plankAngle = p_plankAngle;
            if (getOrientation() == OrientationType.LEFT || getOrientation() == OrientationType.RIGHT) {
                inclinedPlank.setDimension(new DimensionCabin(ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle)), cabin.getThickness(), cabin.getDimensionCabin().getHeight()));
                verticalExtension.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
                pinionLeft.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
                pinionRight.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getLengthFloat(), plankAngle))));
            }
            else {
                inclinedPlank.setDimension(new DimensionCabin(ImperialMeasure.convertFloatToImperial(hypotenuseCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle)), cabin.getThickness(), cabin.getDimensionCabin().getHeight()));
                verticalExtension.setDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
                pinionLeft.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
                pinionRight.setDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), ImperialMeasure.convertFloatToImperial(heightCalculator(cabin.getDimensionCabin().getWidthFloat(), plankAngle))));
            }
        }
    }

    // Vérifie si l'angle est valid
    public boolean isAngleValid(float plankAngle) {
        return (plankAngle >= 0 && plankAngle < 90);
    }

    public void setDimension(DimensionCabin dimension) {
        inclinedPlank.setDimension(dimension);
        verticalExtension.setDimension(dimension);
        pinionLeft.setDimension(dimension);
        pinionRight.setDimension(dimension);
    }
    public boolean isOnInclinedPlank(double x, double y, Cabin currentCabin){
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        ImperialMeasure thicknessPlank = getVerticalExtension().getDimension().getHeight().addMeasuresCopy(ImperialMeasure.convertFloatToImperial(RoofUtils.hypotenuseCalculator(thicknessImperial.convertImperialToFloat(), (90 - (180 - (90 + getPlankAngle()))))));
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2 - getVerticalExtension().getDimension().getHeight().convertImperialToPixels());
        return (isInsideRectangle(x,y,offSetX,offSetY- thicknessImperial.convertImperialToPixels(),currentCabin.getDimensionCabin().getLength(),thicknessPlank)||isInsideRectangle(x,y,offSetX, offSetY-thicknessImperial.convertImperialToPixels(), verticalExtension.getDimension().getLength(), thicknessPlank));
    }
    public boolean isOnInclinedPlankBack(double x, double y, Cabin currentCabin){
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2 - getVerticalExtension().getDimension().getHeight().convertImperialToPixels());
        return (isInsideRectangle(x,y,offSetX, offSetY - thicknessImperial.convertImperialToPixels(), inclinedPlank.getDimension().getLength(), thicknessImperial));
    }
    public boolean isOnVerticalExtension(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getLength().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2- getVerticalExtension().getDimension().getHeight().convertImperialToPixels());
        return(isInsideRectangle(x,y,offSetX,offSetY,inclinedPlank.getDimension().getLength(),verticalExtension.getDimension().getHeight()));
    }
    public boolean isOnPinionLeft(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2);
        double errorMargin = currentCabin.getErrorMargin().convertImperialToPixels()/2;
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        int[] coordX = {(int)(offSetX + errorMargin + thicknessImperial.convertImperialToPixels()/2), (int) (offSetX + errorMargin + thicknessImperial.convertImperialToPixels()/2), (int) (offSetX + pinionLeft.getDimension().getLength().convertImperialToPixels() - ((errorMargin + thicknessImperial.convertImperialToPixels())/2))};
        int[] coordY = {(int)(offSetY), (int)(offSetY- pinionLeft.getDimension().getHeight().convertImperialToPixels()), (int)(offSetY)};
        return(isInsideTriangle(x,y,coordX, coordY));
    }
    public boolean isOnPinionRight(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2);
        double errorMargin = currentCabin.getErrorMargin().convertImperialToPixels()/2;
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        int[] coordX = {(int)(offSetX + errorMargin + thicknessImperial.convertImperialToPixels()/2), (int) (offSetX + pinionLeft.getDimension().getLength().convertImperialToPixels() - ((errorMargin + thicknessImperial.convertImperialToPixels())/2)), (int) (offSetX + pinionLeft.getDimension().getLength().convertImperialToPixels() - ((errorMargin + thicknessImperial.convertImperialToPixels())/2))};
        int[] coordY = {(int)(offSetY), (int)(offSetY),(int)(offSetY- pinionLeft.getDimension().getHeight().convertImperialToPixels())};
        return (isInsideTriangle(x,y,coordX,coordY));
    }
    public boolean isOnInclinedPlankRightSide(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2);
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        int[] coordX = {(int)(offSetX), (int)(offSetX), (int)(offSetX + thicknessImperial.convertImperialToPixels()/2), (int) (offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()), (int) (offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels())};
        int[] coordY = {(int) (offSetY - thicknessImperial.convertImperialToPixels()), (int)(offSetY), (int)(offSetY), (int)(offSetY - getVerticalExtension().getDimension().getHeight().convertImperialToPixels()), (int) ((offSetY - getVerticalExtension().getDimension().getHeight().convertImperialToPixels())-thicknessImperial.convertImperialToPixels())};
        return(isInsidePolygon(x,y,coordX,coordY));
    }
    public boolean isOnInclinedPlankLeftSide(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2);
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        int[] coordX = {(int) (offSetX), (int)(offSetX), (int)(offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels() -(thicknessImperial.convertImperialToPixels())/2), (int)(offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()), (int)(offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels())};
        int[] coordY = {(int) (offSetY- getVerticalExtension().getDimension().getHeight().convertImperialToPixels() - thicknessImperial.convertImperialToPixels()), (int)(offSetY - getVerticalExtension().getDimension().getHeight().convertImperialToPixels()), (int)(offSetY), (int) (offSetY), (int) (offSetY - thicknessImperial.convertImperialToPixels())};
        return(isInsidePolygon(x,y,coordX,coordY));
    }
    public boolean isInsideExtensionLeft(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2 - getVerticalExtension().getDimension().getHeight().convertImperialToPixels());
        ImperialMeasure errorMargin = currentCabin.getErrorMargin();
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        return (isInsideRectangle(x,y,offSetX, offSetY, thicknessImperial.getHalf().subtractMeasuresCopy(errorMargin.getHalf()), verticalExtension.getDimension().getHeight()));
    }
    public boolean isInsideExtensionRight(double x, double y, Cabin currentCabin){
        double offSetX = (600- currentCabin.getDimensionCabin().getWidth().convertImperialToPixels()/2);
        double offSetY = (500 - currentCabin.getDimensionCabin().getHeight().convertImperialToPixels()/2 - getVerticalExtension().getDimension().getHeight().convertImperialToPixels());
        ImperialMeasure errorMargin = currentCabin.getErrorMargin().getHalf();
        ImperialMeasure thicknessImperial = currentCabin.getThickness();
        return (isInsideRectangle(x,y,offSetX + currentCabin.getDimensionCabin().getWidth().convertImperialToPixels() - (thicknessImperial.convertImperialToPixels()/2) + errorMargin.convertImperialToPixels(), offSetY, thicknessImperial.getHalf().subtractMeasuresCopy(errorMargin), verticalExtension.getDimension().getHeight()));
    }
    private boolean isInsideTriangle(double x, double y,int[] coordX, int[] coordY){
        Polygon pinion = new Polygon(coordX, coordY, 3);
        return (pinion.contains(x,y));
    }
    private boolean isInsideRectangle(double x, double y,double positionX, double positionY, ImperialMeasure lenghtRectangle, ImperialMeasure widthRectangle) {
        return (isInsideX(x,positionX,lenghtRectangle) && isInsideY(y, positionY,widthRectangle));
    }
    private boolean isInsidePolygon(double x, double y, int[] coordX, int[] coordY){
        Polygon polygon = new Polygon(coordX, coordY, 5);
        return(polygon.contains(x,y));
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