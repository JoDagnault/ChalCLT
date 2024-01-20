package ca.ulaval.glo2004.domain.utils;

import java.io.Serializable;
import java.util.UUID;

public class DimensionCabin implements Serializable {

    private ImperialMeasure height;
    private ImperialMeasure length;
    private ImperialMeasure width;

    private UUID uuid;

    // Constructeur par défaut. 1 pied x 1 pied x 1 pied
    public DimensionCabin(){
        height = new ImperialMeasure();
        length = new ImperialMeasure();
        width = new ImperialMeasure();
    }

    // Constructeur de DimensionCabin
    public DimensionCabin(ImperialMeasure p_length, ImperialMeasure p_width, ImperialMeasure p_height){
        height = p_height;
        length = p_length;
        width = p_width;
    }
    public DimensionCabin(DimensionCabin dimensionCabin){
        height = dimensionCabin.getHeight();
        length = dimensionCabin.getLength();
        width = dimensionCabin.getWidth();
        this.uuid = UUID.randomUUID();
    }

    // Formatter en string des dimensions
    public String toString(){
        return length + " x " + width + " x " + height;
    }

    // Permet d'ajouter des dimensions
    public void addDimension(DimensionCabin other){
        addLength(other.getLength());
        addHeight(other.getHeight());
        addWidth(other.getWidth());
    }

    // Permet d'ajouter une height
    public void addHeight(ImperialMeasure p_height){
        height.addMeasures(p_height);
    }

    // Permet d'ajouter une length
    public void addLength(ImperialMeasure p_length){
        length.addMeasures(p_length);
    }

    // Permet d'ajouter une width
    public void addWidth(ImperialMeasure p_width){
        width.addMeasures(p_width);
    }

    // Permet de définir height
    public void setHeight(ImperialMeasure height) {
        this.height = height;
    }

    // Permet de définir length
    public void setLength(ImperialMeasure length) {
        this.length = length;
    }

    // Permet de définir width
    public void setWidth(ImperialMeasure width) {
        this.width = width;
    }

    // Permet d'obtenir height
    public ImperialMeasure getHeight() {
        return height;
    }

    // Permet d'obtenir length
    public ImperialMeasure getLength() {
        return length;
    }

    // Permet d'obtenir width
    public ImperialMeasure getWidth() {
        return width;
    }

    public float getWidthFloat(){return width.convertImperialToFloat();}
    public float getLengthFloat(){return length.convertImperialToFloat();}
    public float getHeightFloat(){return height.convertImperialToFloat();}

    public UUID getUUID(){
        return uuid;
    }

}
