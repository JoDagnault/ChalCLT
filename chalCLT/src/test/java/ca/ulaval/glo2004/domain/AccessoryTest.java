package ca.ulaval.glo2004.domain;

import ca.ulaval.glo2004.domain.cabinComposition.Accessory;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import org.junit.Assert;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

public class AccessoryTest {

    private class AccessoryTestClass extends Accessory{

        public AccessoryTestClass(){
            super();
        }

        public AccessoryTestClass(Point2D position, DimensionCabin dimension){
            super(position, dimension);
        }
    }

    // Les tests seront basés sur une distance minimale entre les accessoires de 3 pouces
    ImperialMeasure minAccessoryDistance = new ImperialMeasure(0, 3);

    AccessoryTestClass targetAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(3, 6), new ImperialMeasure(2, 7)),
            new DimensionCabin(new ImperialMeasure(4, 8), new ImperialMeasure(0, 0), new ImperialMeasure(2,3))
    );


    AccessoryTestClass overlappingTopAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(4,0), new ImperialMeasure(2,0)),
            new DimensionCabin(new ImperialMeasure(3, 0), new ImperialMeasure(0, 0), new ImperialMeasure(1, 0))
    );

    AccessoryTestClass overlappingTopEdgeAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(4,0), new ImperialMeasure(2,0)),
            new DimensionCabin(new ImperialMeasure(3, 0), new ImperialMeasure(0, 0), new ImperialMeasure(0, 4))
    );

    @Test
    public void isAccessoryOverlappingTopBound()
    {
        assertTrue(targetAccessory.isAccessoryOverlapping(overlappingTopAccessory, minAccessoryDistance));
        assertFalse(targetAccessory.isAccessoryOverlapping(overlappingTopEdgeAccessory, minAccessoryDistance));
    }


    AccessoryTestClass overlappingBottomAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(4, 0)),
            new DimensionCabin(new ImperialMeasure(3, 0), new ImperialMeasure(0, 0), new ImperialMeasure(1, 3))
    );

    AccessoryTestClass overlappingBottomEdgeAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(4, 0), new ImperialMeasure(5, 1)),
            new DimensionCabin(new ImperialMeasure(3, 0), new ImperialMeasure(0, 0), new ImperialMeasure(1, 3))
    );

    @Test
    public void isAccessoryOverlappingBottomBound()
    {
        assertTrue(targetAccessory.isAccessoryOverlapping(overlappingBottomAccessory, minAccessoryDistance));
        assertFalse(targetAccessory.isAccessoryOverlapping(overlappingBottomEdgeAccessory, minAccessoryDistance));
    }


    AccessoryTestClass overlappingRightAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(8, 0), new ImperialMeasure(3, 0)),
            new DimensionCabin(new ImperialMeasure(1, 3), new ImperialMeasure(0,0), new ImperialMeasure(1, 0))
    );

    AccessoryTestClass overlappingRightEdgeAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(8, 5), new ImperialMeasure(3, 0)),
            new DimensionCabin(new ImperialMeasure(1, 3), new ImperialMeasure(0,0), new ImperialMeasure(1, 0))
    );

    @Test
    public void isAccessoryOverlappingRightBound()
    {
        assertTrue(targetAccessory.isAccessoryOverlapping(overlappingRightAccessory, minAccessoryDistance));
        assertFalse(targetAccessory.isAccessoryOverlapping(overlappingRightEdgeAccessory, minAccessoryDistance));
    }
    AccessoryTestClass overlappingLeftAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(3, 0), new ImperialMeasure(3, 0)),
            new DimensionCabin(new ImperialMeasure(1, 5), new ImperialMeasure(0,0), new ImperialMeasure(1, 0))
    );

    AccessoryTestClass overlappingLeftEdgeAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(3, 0), new ImperialMeasure(3, 0)),
            new DimensionCabin(new ImperialMeasure(0, 3), new ImperialMeasure(0,0), new ImperialMeasure(1, 0))
    );

    @Test
    public void isAccessoryOverlappingLeftBound()
    {
        assertTrue(targetAccessory.isAccessoryOverlapping(overlappingLeftAccessory, minAccessoryDistance));
        assertFalse(targetAccessory.isAccessoryOverlapping(overlappingLeftEdgeAccessory, minAccessoryDistance));
    }

    AccessoryTestClass nonOverlappingAccessory = new AccessoryTestClass(
            new Point2D(new ImperialMeasure(1, 0), new ImperialMeasure(5, 0)),
            new DimensionCabin(new ImperialMeasure(0, 3), new ImperialMeasure(0,0), new ImperialMeasure(1, 0))
    );

    @Test
    public void isAccessoryOverlappingNonOverlapping()
    {
        assertFalse(targetAccessory.isAccessoryOverlapping(nonOverlappingAccessory, minAccessoryDistance));
    }

    @Test
    public void initialSelectionStatus(){
        assertFalse(targetAccessory.getSelectionStatus());
    }
    @Test
    public void modifySelectionStatus(){
        targetAccessory.modifySelectionStatus();
        assertTrue(targetAccessory.getSelectionStatus());
        targetAccessory.modifySelectionStatus();
        assertFalse(targetAccessory.getSelectionStatus());
    }
    @Test
    public void setIDSelected(){
        targetAccessory.setIDSelected(targetAccessory.getAccessoryId());
        Assert.assertEquals(targetAccessory.getAccessoryId(), targetAccessory.getIDSelected());
    }

    @Test
    public void resetIDSelected(){
        targetAccessory.setIDSelected(targetAccessory.getAccessoryId());
        targetAccessory.resetIDSelected();
        assertEquals(-1, targetAccessory.getIDSelected());
    }

    @Test
    public void isSelectedTrue(){
        double offSetX = 420;
        double offSetY = 156;
        double x = 556;
        double y = 259;
        assertTrue(targetAccessory.isSelected(x,y,offSetX,offSetY));
    }

    @Test
    public void isSelectedFalse(){
        double offSetX = 420;
        double offSetY = 156;
        double x = 450;
        double y = 175;
        assertFalse(targetAccessory.isSelected(x,y,offSetX,offSetY));
    }

    AccessoryTestClass editAccessory = new AccessoryTestClass(new Point2D(new ImperialMeasure(1,0), new ImperialMeasure(1,0)),
            new DimensionCabin(new ImperialMeasure(2,0), new ImperialMeasure(0,3), new ImperialMeasure(2,0)));
    @Test
    public void editPositionAccessoryWithoutFraction(){
        editAccessory.editPosition(new Point2D(new ImperialMeasure(6,0), new ImperialMeasure(4,5)));
        assertThat(editAccessory.getXPosition().getFeet()).isEqualTo(6);
        assertThat(editAccessory.getXPosition().getInches()).isEqualTo(0);
        assertThat(editAccessory.getYPosition().getFeet()).isEqualTo(4);
        assertThat(editAccessory.getYPosition().getInches()).isEqualTo(5);
    }

    @Test
    public void editPositionAccessoryWithFraction(){
        editAccessory.editPosition(new Point2D(new ImperialMeasure(5,0,1,4), new ImperialMeasure(3,2,2,3)));
        assertThat(editAccessory.getXPosition().getFeet()).isEqualTo(5);
        assertThat(editAccessory.getXPosition().getInches()).isEqualTo(0);
        assertThat(editAccessory.getXPosition().getInchNum()).isEqualTo(1);
        assertThat(editAccessory.getXPosition().getInchDenom()).isEqualTo(4);

        assertThat(editAccessory.getYPosition().getFeet()).isEqualTo(3);
        assertThat(editAccessory.getYPosition().getInches()).isEqualTo(2);
        assertThat(editAccessory.getYPosition().getInchNum()).isEqualTo(2);
        assertThat(editAccessory.getYPosition().getInchDenom()).isEqualTo(3);
    }

    @Test
    public void editDimensionAccessory(){
        editAccessory.editDimension(new DimensionCabin(new ImperialMeasure(2,4,1,5), new ImperialMeasure(0,4,0,1), new ImperialMeasure(1,4)));
        assertThat(editAccessory.getDimension().getLength().getFeet()).isEqualTo(2);
        assertThat(editAccessory.getDimension().getLength().getInches()).isEqualTo(4);
        assertThat(editAccessory.getDimension().getLength().getInchNum()).isEqualTo(1);
        assertThat(editAccessory.getDimension().getLength().getInchDenom()).isEqualTo(5);

        assertThat(editAccessory.getDimension().getWidth().getFeet()).isEqualTo(0);
        assertThat(editAccessory.getDimension().getWidth().getInches()).isEqualTo(4);
        assertThat(editAccessory.getDimension().getWidth().getInchNum()).isEqualTo(0);
        assertThat(editAccessory.getDimension().getWidth().getInchDenom()).isEqualTo(1);

        assertThat(editAccessory.getDimension().getHeight().getFeet()).isEqualTo(1);
        assertThat(editAccessory.getDimension().getHeight().getInches()).isEqualTo(4);
    }
}
