package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.DTO.DimensionCabinDTO;
import ca.ulaval.glo2004.domain.DTO.ImperialMeasureDTO;
import ca.ulaval.glo2004.domain.DTO.RoofDTO;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domain.cabinComposition.Roof;
import ca.ulaval.glo2004.domain.utils.RoofUtils;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RoofDrawer {

    public RoofDrawer() {
    }

    public void drawRoof(Graphics g, DimensionCabinDTO dimension, ImperialMeasureDTO thickness, ImperialMeasureDTO errorMargin, RoofDTO roof, MainWindow.ViewType viewType) {
            ImperialMeasure height = dimension.Height ;
            ImperialMeasure length = dimension.Length;
            ImperialMeasure width = dimension.Width;
            ImperialMeasure thicknessImperial = new ImperialMeasure(thickness.Feet, thickness.Inches, thickness.InchesNum, thickness.InchesDenom);
            ImperialMeasure errorMarginImperial = new ImperialMeasure(errorMargin.Feet, errorMargin.Inches, errorMargin.InchesNum, errorMargin.InchesDenom);
            double errorMarginInPixels = errorMarginImperial.convertImperialToPixels()/2;

            double angle = ImperialMeasure.convertFloatToImperial(RoofUtils.hypotenuseCalculator(thicknessImperial.convertImperialToFloat(), (90 - (180 - (90 + roof.plankAngle))))).convertImperialToPixels();
            double lengthCabinPixel = length.convertImperialToPixels();
            double widthCabin = width.convertImperialToPixels();
            double heightCabinInPixel = height.convertImperialToPixels();
            double plankLengthInPixel =  roof.inclinedPlank.getDimension().getLength().convertImperialToPixels();

            double plankHeightInPixel = roof.verticalExtension.getDimension().getHeight().convertImperialToPixels();
            double plankLengthAndInclined = plankHeightInPixel + angle;
            double plankThickness = thicknessImperial.convertImperialToPixels();
            double realyOffset = (500 - heightCabinInPixel/2);
            double xOffset = (600 - lengthCabinPixel/2);
            double xOffsetSideView = (600 - widthCabin/2);
            double yOffset = (500 - heightCabinInPixel/2 - roof.verticalExtension.getDimension().getHeight().convertImperialToPixels());


            double PinonHeight = roof.pinionLeft.getDimension().getHeight().convertImperialToPixels();
            double PinonLength = roof.pinionLeft.getDimension().getLength().convertImperialToPixels();
            double PinonLeftxCoord = xOffsetSideView + PinonLength - (errorMarginInPixels + plankThickness / 2);
            int[] xCoord = {(int) (xOffsetSideView + errorMarginInPixels + plankThickness/2 ) , (int)(xOffsetSideView + errorMarginInPixels + plankThickness/2), (int) PinonLeftxCoord};
            int[] yCoord = {(int) realyOffset, (int) (realyOffset - PinonHeight), (int) realyOffset};

            int[] xCoordIncPlankLeftView = {(int) xOffsetSideView, (int) xOffsetSideView,(int) (xOffsetSideView+ widthCabin - plankThickness/2), (int) (xOffsetSideView + widthCabin),(int) (xOffsetSideView + widthCabin)};
            int[] yCoordIncPlankLeftView = {(int) (yOffset - plankThickness), (int) yOffset, (int) realyOffset, (int) realyOffset , (int) (realyOffset - plankThickness)  };

            int[] xCoordIncPlankRightView = {(int) xOffsetSideView, (int) xOffsetSideView,(int) (xOffsetSideView + plankThickness/2), (int) (xOffsetSideView + widthCabin),(int) (xOffsetSideView + widthCabin)};
            int[] yCoordIncPlankRightView = {(int) (realyOffset - plankThickness), (int) realyOffset, (int) realyOffset, (int) yOffset , (int) (yOffset - plankThickness)  };

            int[] xCoordPinionRight = {(int) (xOffsetSideView + errorMarginInPixels + plankThickness/2), (int)PinonLeftxCoord, (int) PinonLeftxCoord};
            int[] yCoordPinionRight = {(int) realyOffset, (int) realyOffset, (int) (realyOffset - PinonHeight)};

            if(viewType == MainWindow.ViewType.FRONT){
                drawInclinedPlank(g, xOffset, yOffset - plankThickness, plankLengthInPixel, plankLengthAndInclined);
            } else if (viewType == MainWindow.ViewType.BACK) {
                drawVerticalExtension(g, xOffset, yOffset, plankLengthInPixel, plankHeightInPixel);
                drawInclinedPlank(g,xOffset, (yOffset - plankThickness), plankLengthInPixel, plankThickness );
            } else if (viewType == MainWindow.ViewType.LEFT) {
                drawPinon(g, xCoord, yCoord);
                drawVerticalExtension(g, xOffsetSideView, yOffset, plankThickness/2 - errorMarginInPixels, plankHeightInPixel);
                drawInclinedPlankLeft(g,xCoordIncPlankLeftView, yCoordIncPlankLeftView);
            } else if (viewType == MainWindow.ViewType.RIGHT){
                drawPinon(g, xCoordPinionRight, yCoordPinionRight);
                drawVerticalExtension(g, xOffsetSideView + widthCabin - plankThickness/2 + errorMarginInPixels, yOffset,plankThickness/2 - errorMarginInPixels, plankHeightInPixel);
                drawInclinedPlankLeft(g, xCoordIncPlankRightView,yCoordIncPlankRightView);
            }
        }
    public void drawInclinedPlank(Graphics g, double x, double y, double width, double height){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Double frontRoof = new Rectangle2D.Double(x, y, width, height);
        g2d.setColor(Color.BLUE);
        g2d.fill(frontRoof);
    }
    public void drawPinon(Graphics g, int[] x, int[] y){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fillPolygon(x, y, 3);
    }

    public void drawVerticalExtension(Graphics g, double x, double y, double width, double height){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Double verticalRoof = new Rectangle2D.Double(x, y, width, height);
        g2d.setColor(Color.MAGENTA);
        g2d.fill(verticalRoof);
    }

    public void drawInclinedPlankLeft(Graphics g, int[]x, int[]y){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(x, y, 5);
    }

}
