package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.DTO.DimensionCabinDTO;
import ca.ulaval.glo2004.domain.DTO.ImperialMeasureDTO;
import ca.ulaval.glo2004.domain.DTO.WallDTO;
import ca.ulaval.glo2004.domain.cabinComposition.Accessory;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.gui.MainWindow;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class WallDrawer {

    // Constructeur par défaut de WallDrawer
    public WallDrawer(){
    }

    // Permet de dessiner un mur
    public void drawWall(Graphics g, DimensionCabinDTO dimension, ImperialMeasureDTO thickness, ImperialMeasureDTO errorMargin, MainWindow.ViewType viewType, WallDTO currentWall){
        Graphics2D g2d = (Graphics2D) g;

        // Obtenir les dimensions du mur
        ImperialMeasure height = dimension.Height ;
        ImperialMeasure length = dimension.Length;
        ImperialMeasure width = dimension.Width;
        ImperialMeasure thicknessImperial = new ImperialMeasure(thickness.Feet, thickness.Inches, thickness.InchesNum, thickness.InchesDenom);
        ImperialMeasure errorMarginImperial = new ImperialMeasure(errorMargin.Feet, errorMargin.Inches, errorMargin.InchesNum, errorMargin.InchesDenom);

        double heightInPixels = height.convertImperialToPixels() ;
        double lengthInPixels = length.convertImperialToPixels();
        double widthInPixels = width.convertImperialToPixels();
        double thicknessInPixels = thicknessImperial.convertImperialToPixels();
        double errorMarginInPixels = errorMarginImperial.convertImperialToPixels();
        errorMarginInPixels = errorMarginInPixels/2;
        double espaceXLength = (600  - lengthInPixels/2); //Valeur de dégagement sur le côté
        double espaceXWidth = (600 - widthInPixels/2);
        double espaceY = (500 - heightInPixels/2); //Valeur de dégagement sur le dessus
        double lengthWithMargin = lengthInPixels -(2* errorMarginInPixels); //Valeur pour tenir compte de la distance supplémentaire
        double widthWithMargin = widthInPixels - (2*errorMarginInPixels); //Valeur pour tenir compte de la distance supplémentaire
        double thicknessWithMargin = thicknessInPixels -errorMarginInPixels;

        if(viewType == MainWindow.ViewType.FRONT){
            Rectangle2D.Double wallFront = new Rectangle2D.Double(espaceXLength, espaceY, lengthInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(wallFront);

            this.drawAccessories(currentWall, g2d, espaceXLength, espaceY);
        }
        else if (viewType == MainWindow.ViewType.BACK) {
            Rectangle2D.Double wallBack = new Rectangle2D.Double(espaceXLength, espaceY, lengthInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(wallBack);

            this.drawAccessories(currentWall, g2d, espaceXLength, espaceY);
        }
        else if (viewType == MainWindow.ViewType.LEFT){
            // Portion pricipale du mur
            Rectangle2D.Double wallLeft = new Rectangle2D.Double(espaceXWidth + (thicknessInPixels/2) + errorMarginInPixels, espaceY, widthWithMargin - thicknessInPixels, heightInPixels);
            g2d.setColor(Color.ORANGE);
            g2d.fill(wallLeft);

            Rectangle2D.Double grooveLeft = new Rectangle2D.Double(espaceXWidth, espaceY, (thicknessInPixels / 2) -errorMarginInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(grooveLeft);

            Rectangle2D.Double grooveRight = new Rectangle2D.Double(espaceXWidth +(widthInPixels -(thicknessInPixels/2)) + errorMarginInPixels, espaceY, (thicknessInPixels/2) -errorMarginInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(grooveRight);

            this.drawAccessories(currentWall, g2d, espaceXWidth, espaceY);
        }
        else{
            //Portion principale du mur
            Rectangle2D.Double wallRight = new Rectangle2D.Double(espaceXWidth + (thicknessInPixels/2) + errorMarginInPixels, espaceY, widthWithMargin - thicknessInPixels, heightInPixels);
            g2d.setColor(Color.ORANGE);
            g2d.fill(wallRight);

            Rectangle2D.Double grooveLeft = new Rectangle2D.Double(espaceXWidth, espaceY, (thicknessInPixels/2) - errorMarginInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(grooveLeft);

            Rectangle2D.Double grooveRight = new Rectangle2D.Double(espaceXWidth+(widthInPixels-(thicknessInPixels/2)) + errorMarginInPixels, espaceY, (thicknessInPixels/2) - errorMarginInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(grooveRight);

            this.drawAccessories(currentWall, g2d, espaceXWidth, espaceY);
        }
    }

    // Permet de dessiner un accessoire
    public void drawAccessory(Accessory accessory, Graphics2D g2d, double xOffset, double yOffset) {
        double xPosition = xOffset + accessory.getXPosition().convertImperialToPixels();
        double yPosition = yOffset + accessory.getYPosition().convertImperialToPixels();
        double width = accessory.getLength().convertImperialToPixels();
        double height = accessory.getHeight().convertImperialToPixels();

        Rectangle2D.Double accessoryShape = new Rectangle2D.Double(xPosition, yPosition, width, height);
        g2d.setColor(Color.BLACK);
        g2d.fill(accessoryShape);

        if (!accessory.isValid()) {
            g2d.setColor(Color.RED);
            g2d.drawLine((int) xPosition, (int) yPosition, (int) (xPosition + width), (int) (yPosition + height));
            g2d.drawLine((int) (xPosition + width), (int) yPosition, (int) xPosition, (int) (yPosition + height));
        }
    }

    // Permet de dessiner des accessoires
    public void drawAccessories(WallDTO currentWall, Graphics2D g2d, double xOffset, double yOffset){
        for (Accessory wallAccessory : currentWall.AccessoryList) {
            if(wallAccessory.getSelectionStatus()){
                this.drawSelectionOutline(g2d,wallAccessory, xOffset, yOffset);
            }
            this.drawAccessory(wallAccessory, g2d, xOffset, yOffset);
        }
    }

    // Permet de dessiner le outline d'un accessoire
    public void drawSelectionOutline(Graphics g, Accessory accessory, double xOffset, double yOffset){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Double selectionOutline = new Rectangle2D.Double(xOffset + (accessory.getXPosition().convertImperialToPixels()-1), yOffset + (accessory.getYPosition().convertImperialToPixels()-1), (accessory.getLength().convertImperialToPixels() +2), (accessory.getHeight().convertImperialToPixels()+2));
        g2d.setColor(Color.CYAN);
        g2d.fill(selectionOutline);
    }
    public void drawWallSeul(Graphics g, DimensionCabinDTO dimension, ImperialMeasureDTO thickness, ImperialMeasureDTO errorMargin, MainWindow.ViewType viewType, WallDTO currentWall){
        Graphics2D g2d = (Graphics2D) g;
        ImperialMeasure height = dimension.Height ;
        ImperialMeasure length = dimension.Length;
        ImperialMeasure width = dimension.Width;
        ImperialMeasure thicknessImperial = new ImperialMeasure(thickness.Feet, thickness.Inches, thickness.InchesNum, thickness.InchesDenom);
        ImperialMeasure errorMarginImperial = new ImperialMeasure(errorMargin.Feet, errorMargin.Inches, errorMargin.InchesNum, errorMargin.InchesDenom);

        double heightInPixels = height.convertImperialToPixels() ;
        double lengthInPixels = length.convertImperialToPixels();
        double widthInPixels = width.convertImperialToPixels();
        double thicknessInPixels = thicknessImperial.convertImperialToPixels();
        double errorMarginInPixels = errorMarginImperial.convertImperialToPixels();
        errorMarginInPixels = errorMarginInPixels/2;
        double espaceXLength = (600  - lengthInPixels/2); //Valeur de dégagement sur le côté
        double espaceXWidth = (600 - widthInPixels/2);
        double espaceY = (500 - heightInPixels/2); //Valeur de dégagement sur le dessus
        double lengthWithMargin = lengthInPixels -(2* errorMarginInPixels); //Valeur pour tenir compte de la distance supplémentaire
        double widthWithMargin = widthInPixels - (2*errorMarginInPixels); //Valeur pour tenir compte de la distance supplémentaire
        double thicknessWithMargin = thicknessInPixels -errorMarginInPixels;

        if(viewType == MainWindow.ViewType.FRONT){
            System.out.println(espaceY + "Wall");
            Rectangle2D.Double wallFront = new Rectangle2D.Double(espaceXLength, espaceY, lengthInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(wallFront);

            this.drawAccessories(currentWall, g2d, espaceXLength, espaceY);
        }
        else if (viewType == MainWindow.ViewType.BACK) {
            Rectangle2D.Double wallBack = new Rectangle2D.Double(espaceXLength, espaceY, lengthInPixels, heightInPixels);
            g2d.setColor(Color.GREEN);
            g2d.fill(wallBack);

            this.drawAccessories(currentWall, g2d, espaceXLength, espaceY);
        }
        else if (viewType == MainWindow.ViewType.LEFT){
            // Portion pricipale du mur
            Rectangle2D.Double wallLeft = new Rectangle2D.Double(espaceXWidth + (thicknessInPixels/2) + errorMarginInPixels, espaceY, widthWithMargin - thicknessInPixels, heightInPixels);
            g2d.setColor(Color.ORANGE);
            g2d.fill(wallLeft);

            this.drawAccessories(currentWall, g2d, espaceXWidth, espaceY);
        }
        else {
            //Portion principale du mur
            Rectangle2D.Double wallRight = new Rectangle2D.Double(espaceXWidth + (thicknessInPixels / 2) + errorMarginInPixels, espaceY, widthWithMargin - thicknessInPixels, heightInPixels);
            g2d.setColor(Color.ORANGE);
            g2d.fill(wallRight);

            this.drawAccessories(currentWall, g2d, espaceXWidth, espaceY);
        }
    }
}
