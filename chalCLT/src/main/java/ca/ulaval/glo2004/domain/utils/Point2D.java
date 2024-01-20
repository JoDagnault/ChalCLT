package ca.ulaval.glo2004.domain.utils;

import java.io.Serializable;

public class Point2D implements Serializable {

    private ImperialMeasure x;

    // Permet d'obtenir la coordonnée en x
    public ImperialMeasure getXCoord(){return this.x;}
    private ImperialMeasure y;

    // Permet d'obtenir la coordonnée en y
    public ImperialMeasure getYCoord(){return this.y;}

    // Constructeur par défaut. Point(0, 0)
    public Point2D(){
        this.x = new ImperialMeasure(0, 0);
        this.y = new ImperialMeasure(0, 0);
    }

    // Constructeur de Point2D
    public Point2D(ImperialMeasure x, ImperialMeasure y){
        this.x = x;
        this.y = y;
    }
}

