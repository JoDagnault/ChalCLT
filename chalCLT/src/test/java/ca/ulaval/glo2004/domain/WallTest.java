package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.cabinComposition.*;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class WallTest {

    Wall wallFront = new Wall(new DimensionCabin(new ImperialMeasure(10, 0), new ImperialMeasure(0, 6), new ImperialMeasure(8, 0)), WallType.FRONT, new ImperialMeasure(0, 3));
    Wall wallRight = new Wall(new DimensionCabin(new ImperialMeasure(10, 0), new ImperialMeasure(0, 6), new ImperialMeasure(8, 0)), WallType.RIGHT, new ImperialMeasure(0, 3));
    DimensionCabin standardDimension = new DimensionCabin(new ImperialMeasure(2, 0), new ImperialMeasure(0, 0), new ImperialMeasure(2, 0));

    Accessory windowTopBoundOverlapFront = new Window(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(0, 2)), standardDimension);
    @Test
    public void isWindowValidTopBoundOverlapFront()
    {
        wallFront.validateAccessory(windowTopBoundOverlapFront);
        assertFalse(wallFront.isAccessoryValid(windowTopBoundOverlapFront));
    }

    Accessory windowTopBoundEdgeOverlapFront = new Window(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(0, 3)), standardDimension);
    @Test
    public void isWindowValidTopBoundEdgeOverlapFront()
    {
        wallFront.validateAccessory(windowTopBoundEdgeOverlapFront);
        assertTrue(wallFront.isAccessoryValid(windowTopBoundEdgeOverlapFront));
    }

    Accessory windowBottomBoundOverlapFront = new Window(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(7, 0)), standardDimension);
    @Test
    public void isWindowValidBottomBoundOverlapFront()
    {
        wallFront.validateAccessory(windowBottomBoundOverlapFront);
        assertFalse(wallFront.isAccessoryValid(windowBottomBoundOverlapFront));
    }

    Accessory windowBottomBoundEdgeOverlapFront = new Window(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(5, 9)), standardDimension);
    @Test
    public void isWindowValidBottomBoundEdgeOverlapFront()
    {
        wallFront.validateAccessory(windowBottomBoundEdgeOverlapFront);
        assertTrue(wallFront.isAccessoryValid(windowBottomBoundEdgeOverlapFront));
    }

    Accessory windowRightBoundOverlapFront = new Window(new Point2D(new ImperialMeasure(8, 0), new ImperialMeasure(3, 0)), standardDimension);
    @Test
    public void isWindowValidRightBoundOverlapFront()
    {
        wallFront.validateAccessory(windowRightBoundOverlapFront);
        assertFalse(wallFront.isAccessoryValid(windowRightBoundOverlapFront));
    }

    Accessory windowRightBoundEdgeOverlapFront = new Window(new Point2D(new ImperialMeasure(7, 9), new ImperialMeasure(3, 0)), standardDimension);
    @Test
    public void isWindowValidRightBoundEdgeOverlapFront()
    {
        wallFront.validateAccessory(windowRightBoundEdgeOverlapFront);
        assertTrue(wallFront.isAccessoryValid(windowRightBoundEdgeOverlapFront));
    }

    Accessory windowLeftBoundOverlapFront = new Window(new Point2D(new ImperialMeasure(0, 2), new ImperialMeasure(3, 0)), standardDimension);
    @Test
    public void isWindowValidLeftBoundOverlapFront()
    {
        wallFront.validateAccessory(windowLeftBoundOverlapFront);
        assertFalse(wallFront.isAccessoryValid(windowLeftBoundOverlapFront));
    }

    Accessory windowLeftBoundEdgeOverlapFront = new Window(new Point2D(new ImperialMeasure(0, 3), new ImperialMeasure(3, 0)), standardDimension);
    @Test
    public void isWindowValidleftBoundEdgeOverlapFront()
    {
        wallFront.validateAccessory(windowLeftBoundEdgeOverlapFront);
        assertTrue(wallFront.isAccessoryValid(windowLeftBoundEdgeOverlapFront));
    }

    Accessory doorNotOnBottom = new Door(new Point2D(new ImperialMeasure(1, 0), new ImperialMeasure(5, 0)), standardDimension);
    @Test
    public void isDoorValidNotOnBottomFront()
    {
        assertFalse(wallFront.isAccessoryValid(doorNotOnBottom));
    }
    Accessory doorOnBottom = new Door(new Point2D(new ImperialMeasure(1, 0), new ImperialMeasure(6, 0)), standardDimension);
    @Test
    public void isDoorValidOnBottomFront()
    {
        wallFront.validateAccessory(doorOnBottom);
        assertTrue(wallFront.isAccessoryValid(doorOnBottom));
    }

    Accessory doorOnBottomRightOverlap = new Door(new Point2D(new ImperialMeasure(7, 7), new ImperialMeasure(6, 0)), standardDimension);
    @Test
    public void isDoorValidOnBottomRightWallRightOverlap()
    {
        wallRight.validateAccessory(doorOnBottomRightOverlap);
        assertFalse(wallRight.isAccessoryValid(doorOnBottomRightOverlap));
    }

    Accessory doorOnBottomRightEdgeOverlap = new Door(new Point2D(new ImperialMeasure(7, 6), new ImperialMeasure(6, 0)), standardDimension);
    @Test
    public void isDoorValidOnBottomRightWallRightEdgeOverlap()
    {
        wallRight.validateAccessory(doorOnBottomRightEdgeOverlap);
        assertTrue(wallRight.isAccessoryValid(doorOnBottomRightEdgeOverlap));
    }
    Accessory doorOnBottomLeftOverlap = new Door(new Point2D(new ImperialMeasure(0, 5), new ImperialMeasure(6, 0)), standardDimension);
    @Test
    public void isDoorValidOnBottomRightWallLeftOverlap()
    {
        wallRight.validateAccessory(doorOnBottomLeftOverlap);
        assertFalse(wallRight.isAccessoryValid(doorOnBottomLeftOverlap));
    }

    Accessory doorOnBottomLeftEdgeOverlap = new Door(new Point2D(new ImperialMeasure(0, 6), new ImperialMeasure(6, 0)), standardDimension);
    @Test
    public void isDoorValidOnBottomRightWallLeftEdgeOverlap()
    {
        wallRight.validateAccessory(doorOnBottomLeftEdgeOverlap);
        assertTrue(wallRight.isAccessoryValid(doorOnBottomLeftEdgeOverlap));
    }

    Accessory windowOverlap = new Window(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(0, 2)), standardDimension);
    @Test
    public void isWindowOverlapping()
    {
        wallRight.addAccessory(new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(0, 2)), standardDimension, AccessoryType.WINDOW);
        wallRight.validateAccessory(windowOverlap);
        assertFalse(wallRight.isAccessoryValid(windowOverlap));
    }
    @Test
    public void selectionStatusTrue(){
        double x = 492;
        double y = 228;
        wallFront.addAccessory(new Point2D(new ImperialMeasure(1,0),new ImperialMeasure(1,0)),standardDimension, AccessoryType.WINDOW);
        wallFront.selectionStatus(x,y,wallFront.getWallDimension().getWidth());
        int result = wallFront.indexOfAccessory(wallFront.getAccessorySelected());
        assertEquals(0,result);
    }
    @Test
    public void selectionStatusFalse(){
        double x = 420;
        double y = 156;
        wallFront.addAccessory(new Point2D(new ImperialMeasure(1,0),new ImperialMeasure(1,0)),standardDimension, AccessoryType.WINDOW);
        wallFront.selectionStatus(x,y,wallFront.getWallDimension().getWidth());
        int result = wallFront.indexOfAccessory(wallFront.getAccessorySelected());
        assertEquals(-1,result);
    }
    @Test
    public void selectionStatusTrueTwoAccessory(){
        double x = 528;
        double y = 264;
        wallFront.addAccessory(new Point2D(new ImperialMeasure(5,0), new ImperialMeasure(2,0)), standardDimension, AccessoryType.WINDOW);
        wallFront.addAccessory(new Point2D(new ImperialMeasure(2,0), new ImperialMeasure(2,0)), standardDimension, AccessoryType.WINDOW);
        wallFront.selectionStatus(x,y, wallFront.getWallDimension().getWidth());
        int result = wallFront.indexOfAccessory(wallFront.getAccessorySelected());
        assertEquals(1, result);
        assertFalse(wallFront.getAccessoryList().get(0).getSelectionStatus());
        assertTrue(wallFront.getAccessoryList().get(1).getSelectionStatus());
    }

    @Test
    public void resetSelectionStatus(){
        double x = 528;
        double y = 264;
        wallFront.addAccessory(new Point2D(new ImperialMeasure(5,0), new ImperialMeasure(2,0)), standardDimension, AccessoryType.WINDOW);
        wallFront.addAccessory(new Point2D(new ImperialMeasure(2,0), new ImperialMeasure(2,0)), standardDimension, AccessoryType.WINDOW);
        wallFront.selectionStatus(x,y, wallFront.getWallDimension().getWidth());
        wallFront.resetSelectionStatus();
        int result = wallFront.indexOfAccessory(wallFront.getAccessorySelected());
        assertEquals(-1, result);
    }

}

