package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.DTO.DimensionCabinDTO;
import ca.ulaval.glo2004.domain.DTO.ImperialMeasureDTO;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TopDrawer {

    // Constructeur par défaut de TopDrawer
    public TopDrawer(){
    }

    // Permet de dessiner le top
    public void drawTop(Graphics g, DimensionCabinDTO dimension, ImperialMeasureDTO thickness, ImperialMeasureDTO errorMargin){
        Graphics2D g2d = (Graphics2D) g;

        ImperialMeasure length = dimension.Length;
        ImperialMeasure width = dimension.Width;
        ImperialMeasure thicknessImperial = new ImperialMeasure(thickness.Feet, thickness.Inches, thickness.InchesNum, thickness.InchesDenom);
        ImperialMeasure errorMarginImperial = new ImperialMeasure(errorMargin.Feet, errorMargin.Inches, errorMargin.InchesNum, errorMargin.InchesDenom);


        double lengthInPixels = length.convertImperialToPixels();
        double widthInPixels = width.convertImperialToPixels();
        double thicknessInPixels = thicknessImperial.convertImperialToPixels();
        double errorMarginInPixels = errorMarginImperial.convertImperialToPixels();
        errorMarginInPixels = errorMarginInPixels/2;
        double espaceX = (600- lengthInPixels/2);
        double espaceY = (400 - widthInPixels/2);
        double lengthWithMargin = lengthInPixels -(2*errorMarginInPixels);
        double widthWithMargin = widthInPixels -(2*errorMarginInPixels);
        double thicknessWithMargin = thicknessInPixels - errorMarginInPixels;

        //Affichage mur de devant
        Rectangle2D.Double wallFront = new Rectangle2D.Double(espaceX+(thicknessInPixels/2)+ errorMarginInPixels, espaceY+(widthInPixels-thicknessInPixels) + errorMarginInPixels, (lengthWithMargin - (thicknessInPixels)), thicknessWithMargin);
        Rectangle2D.Double grooveFrontRight = new Rectangle2D.Double(espaceX+(lengthInPixels - thicknessInPixels/2) - errorMarginInPixels,espaceY+(widthInPixels-thicknessInPixels/2) + errorMarginInPixels,thicknessInPixels/2 +errorMarginInPixels, thicknessInPixels/2 - errorMarginInPixels);
        Rectangle2D.Double grooveFrontLeft = new Rectangle2D.Double(espaceX,espaceY+(widthInPixels-thicknessInPixels/2) + errorMarginInPixels,thicknessInPixels/2 + errorMarginInPixels, thicknessInPixels/2 - errorMarginInPixels);
        g2d.setColor(Color.GREEN);
        g2d.fill(wallFront);
        g2d.fill(grooveFrontRight);
        g2d.fill(grooveFrontLeft);

        //Affichage mur de derrière
        Rectangle2D.Double wallBack = new Rectangle2D.Double(espaceX+(thicknessInPixels/2) + errorMarginInPixels, espaceY, lengthWithMargin - thicknessInPixels, thicknessWithMargin);
        Rectangle2D.Double grooveBackLeft = new Rectangle2D.Double(espaceX,espaceY,thicknessInPixels/2 + errorMarginInPixels, thicknessInPixels/2 -errorMarginInPixels);
        Rectangle2D.Double grooveBackRight = new Rectangle2D.Double(espaceX+(lengthInPixels-thicknessInPixels/2) - errorMarginInPixels, espaceY, thicknessInPixels/2 + errorMarginInPixels, thicknessInPixels/2 - errorMarginInPixels);
        g2d.setColor(Color.GREEN);
        g2d.fill(wallBack);
        g2d.fill(grooveBackRight);
        g2d.fill(grooveBackLeft);

        //Affichage mur de gauche
        Rectangle2D.Double wallLeft = new Rectangle2D.Double(espaceX, espaceY+(thicknessInPixels) + errorMarginInPixels, thicknessWithMargin, (widthWithMargin-(2*thicknessInPixels)));
        Rectangle2D.Double grooveLeftTop = new Rectangle2D.Double(espaceX,espaceY+(thicknessInPixels/2) + errorMarginInPixels, thicknessInPixels/2 - errorMarginInPixels, thicknessInPixels/2 + errorMarginInPixels);
        Rectangle2D.Double grooveLeftBottom = new Rectangle2D.Double(espaceX, espaceY+(widthInPixels-thicknessInPixels) - errorMarginInPixels,thicknessInPixels/2 - errorMarginInPixels, (thicknessInPixels/2));
        g2d.setColor(Color.ORANGE);
        g2d.fill(wallLeft);
        g2d.fill(grooveLeftTop);
        g2d.fill(grooveLeftBottom);

        //Affichage mur de droite
        Rectangle2D.Double wallRight = new Rectangle2D.Double(espaceX+(lengthInPixels-thicknessInPixels) + errorMarginInPixels, espaceY+thicknessInPixels +errorMarginInPixels,thicknessWithMargin,widthWithMargin-(2*thicknessInPixels));
        Rectangle2D.Double grooveRightTop = new Rectangle2D.Double(espaceX+(lengthInPixels-(thicknessInPixels/2)) + errorMarginInPixels, espaceY+(thicknessInPixels/2) + errorMarginInPixels, thicknessInPixels/2 -errorMarginInPixels, (thicknessInPixels/2) + errorMarginInPixels);
        Rectangle2D.Double grooveRightBottom = new Rectangle2D.Double(espaceX+(lengthInPixels-(thicknessInPixels/2) + errorMarginInPixels),espaceY+(widthInPixels-thicknessInPixels) - errorMarginInPixels,thicknessInPixels/2 - errorMarginInPixels, (thicknessInPixels/2));
        g2d.setColor(Color.ORANGE);
        g2d.fill(wallRight);
        g2d.fill(grooveRightTop);
        g2d.fill(grooveRightBottom);
    }
}
