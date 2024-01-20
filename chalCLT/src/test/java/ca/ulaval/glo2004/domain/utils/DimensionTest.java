package ca.ulaval.glo2004.domain.utils;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class DimensionTest {


    ImperialMeasure height = new ImperialMeasure(2,1);
    ImperialMeasure length = new ImperialMeasure(1,5);
    ImperialMeasure width = new ImperialMeasure(0,1, 1,2);

    ImperialMeasure height1 = new ImperialMeasure(3,1);
    ImperialMeasure length1 = new ImperialMeasure(1,6);
    ImperialMeasure width1 = new ImperialMeasure(0,2, 2,3);


    private final DimensionCabin testDimension = new DimensionCabin(length, width, height);
    private final DimensionCabin testDimension1 = new DimensionCabin(length1, width1, height1);

    @Test
    public void displayDimensionTest(){
        assertThat(testDimension.toString()).isEqualTo("1'5\" x 0'1\"1/2 x 2'1\"");
        assertThat(testDimension1.toString()).isEqualTo("1'6\" x 0'2\"2/3 x 3'1\"");
    }
    @Test
    public void addLengthTest(){
        testDimension.addLength(length1);
        assertThat(testDimension.getLength().toString()).isEqualTo("2'11\"");
    }
    @Test
    public void addWidthTest(){
        testDimension.addWidth(width1);
        assertThat(testDimension.getWidth().toString()).isEqualTo("0'4\"1/6");
    }
    @Test
    public void addHeightTest(){
        testDimension.addHeight(height1);
        assertThat(testDimension.getHeight().toString()).isEqualTo("5'2\"");
    }
    @Test
    public void addDimensionTest(){
        testDimension.addDimension(testDimension1);
        assertThat(testDimension.toString()).isEqualTo("2'11\" x 0'4\"1/6 x 5'2\"");
    }

}
