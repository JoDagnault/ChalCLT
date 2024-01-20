package ca.ulaval.glo2004.domain.utils;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class ImperialMesureTest {

    private final ImperialMeasure impMesureWithoutFrac = new ImperialMeasure(20, 10);
    private final ImperialMeasure impMesureWithFrac = new ImperialMeasure(20, 10, 2,3);
    private final ImperialMeasure impMesureBig = new ImperialMeasure(1, 40, 10, 2);

    @Test
    public void displayMesure(){
        assertThat(impMesureWithoutFrac.toString()).isEqualTo("20'10\"");
        assertThat(impMesureWithFrac.toString()).isEqualTo("20'10\"2/3");
        assertThat(impMesureBig.toString()).isEqualTo("4'9\"");
    }

    @Test
    public void addMesuresOneWithOneWithoutFraction(){
        impMesureWithFrac.addMeasures(impMesureWithoutFrac);
        assertThat(impMesureWithFrac.getFeet()).isEqualTo(41);
        assertThat(impMesureWithFrac.getInches()).isEqualTo(8);
        assertThat(impMesureWithFrac.getInchNum()).isEqualTo(2);
        assertThat(impMesureWithFrac.getInchDenom()).isEqualTo(3);
    }

    @Test
    public void addMesuresWithTwoFractions(){
        impMesureWithFrac.addMeasures(impMesureWithFrac);
        assertThat(impMesureWithFrac.getFeet()).isEqualTo(41);
        assertThat(impMesureWithFrac.getInches()).isEqualTo(9);
        assertThat(impMesureWithFrac.getInchNum()).isEqualTo(1);
        assertThat(impMesureWithFrac.getInchDenom()).isEqualTo(3);
    }


    private final ImperialMeasure subImpMesureWithoutFrac = new ImperialMeasure(20, 10);
    private final ImperialMeasure subImpMesureWithFrac1 = new ImperialMeasure(10, 10, 2,3);
    private final ImperialMeasure subImpMesureWithFrac2 = new ImperialMeasure(5, 5, 1,6);

    @Test
    public void subtractMesuresOneWithOneWithoutFraction(){
        subImpMesureWithoutFrac.subtractMeasures(subImpMesureWithFrac1);
        assertThat(subImpMesureWithoutFrac.getFeet()).isEqualTo(9);
        assertThat(subImpMesureWithoutFrac.getInches()).isEqualTo(11);
        assertThat(subImpMesureWithoutFrac.getInchNum()).isEqualTo(1);
        assertThat(subImpMesureWithoutFrac.getInchDenom()).isEqualTo(3);
    }

    @Test
    public void subtractMesuresWithTwoFractions(){
        subImpMesureWithFrac1.subtractMeasures(subImpMesureWithFrac2);
        assertThat(subImpMesureWithFrac1.getFeet()).isEqualTo(5);
        assertThat(subImpMesureWithFrac1.getInches()).isEqualTo(5);
        assertThat(subImpMesureWithFrac1.getInchNum()).isEqualTo(1);
        assertThat(subImpMesureWithFrac1.getInchDenom()).isEqualTo(2);
    }
    @Test
    public void addMesuresCopyOneWithOneWithoutFraction(){
        ImperialMeasure result = impMesureWithFrac.addMeasuresCopy(impMesureWithoutFrac);
        assertThat(impMesureWithFrac.getFeet()).isEqualTo(20);
        assertThat(impMesureWithFrac.getInches()).isEqualTo(10);
        assertThat(impMesureWithFrac.getInchNum()).isEqualTo(2);
        assertThat(impMesureWithFrac.getInchDenom()).isEqualTo(3);
        assertThat(impMesureWithoutFrac.getFeet()).isEqualTo(20);
        assertThat(impMesureWithoutFrac.getInches()).isEqualTo(10);
        assertThat(result.getFeet()).isEqualTo(41);
        assertThat(result.getInches()).isEqualTo(8);
        assertThat(result.getInchNum()).isEqualTo(2);
        assertThat(result.getInchDenom()).isEqualTo(3);

    }

    @Test
    public void addMesuresCopyWithTwoFractions(){
        ImperialMeasure result = impMesureWithFrac.addMeasuresCopy(impMesureWithFrac);
        assertThat(impMesureWithFrac.getFeet()).isEqualTo(20);
        assertThat(impMesureWithFrac.getInches()).isEqualTo(10);
        assertThat(impMesureWithFrac.getInchNum()).isEqualTo(2);
        assertThat(impMesureWithFrac.getInchDenom()).isEqualTo(3);
        assertThat(result.getFeet()).isEqualTo(41);
        assertThat(result.getInches()).isEqualTo(9);
        assertThat(result.getInchNum()).isEqualTo(1);
        assertThat(result.getInchDenom()).isEqualTo(3);
    }

    @Test
    public void subtractMesuresCopyOneWithOneWithoutFraction(){
        ImperialMeasure result = subImpMesureWithoutFrac.subtractMeasuresCopy(subImpMesureWithFrac1);
        assertThat(subImpMesureWithFrac1.getFeet()).isEqualTo(10);
        assertThat(subImpMesureWithFrac1.getInches()).isEqualTo(10);
        assertThat(subImpMesureWithFrac1.getInchNum()).isEqualTo(2);
        assertThat(subImpMesureWithFrac1.getInchDenom()).isEqualTo(3);
        assertThat(subImpMesureWithoutFrac.getFeet()).isEqualTo(20);
        assertThat(subImpMesureWithoutFrac.getInches()).isEqualTo(10);
        assertThat(result.getFeet()).isEqualTo(9);
        assertThat(result.getInches()).isEqualTo(11);
        assertThat(result.getInchNum()).isEqualTo(1);
        assertThat(result.getInchDenom()).isEqualTo(3);
    }

    @Test
    public void subtractMesuresCopyWithTwoFractions(){
        ImperialMeasure result = subImpMesureWithFrac1.subtractMeasuresCopy(subImpMesureWithFrac2);
        assertThat(subImpMesureWithFrac1.getFeet()).isEqualTo(10);
        assertThat(subImpMesureWithFrac1.getInches()).isEqualTo(10);
        assertThat(subImpMesureWithFrac1.getInchNum()).isEqualTo(2);
        assertThat(subImpMesureWithFrac1.getInchDenom()).isEqualTo(3);
        assertThat(subImpMesureWithFrac2.getFeet()).isEqualTo(5);
        assertThat(subImpMesureWithFrac2.getInches()).isEqualTo(5);
        assertThat(subImpMesureWithFrac2.getInchNum()).isEqualTo(1);
        assertThat(subImpMesureWithFrac2.getInchDenom()).isEqualTo(6);
        assertThat(result.getFeet()).isEqualTo(5);
        assertThat(result.getInches()).isEqualTo(5);
        assertThat(result.getInchNum()).isEqualTo(1);
        assertThat(result.getInchDenom()).isEqualTo(2);
    }

    private final ImperialMeasure targetMeasure = new ImperialMeasure(10, 10);
    private final ImperialMeasure smallerFeet = new ImperialMeasure(9, 10);
    private final ImperialMeasure biggerFeet = new ImperialMeasure(11, 10);
    private final ImperialMeasure smallerInch = new ImperialMeasure(10, 9);
    private final ImperialMeasure biggerInch = new ImperialMeasure(10, 11);
    private final ImperialMeasure targetMeasureFraction = new ImperialMeasure(10, 10, 2,  5);
    private final ImperialMeasure biggerFraction = new ImperialMeasure(10, 10, 2, 3);
    private final ImperialMeasure smallerFraction = new ImperialMeasure(10, 10, 2, 8);

    @Test
    public void isSmallerOrEqual() {
        assertFalse(targetMeasure.isSmallerOrEqual(smallerFeet));
        assertFalse(targetMeasure.isSmallerOrEqual(smallerInch));
        assertTrue(targetMeasure.isSmallerOrEqual(targetMeasure));
        assertTrue(targetMeasure.isSmallerOrEqual(biggerFeet));
        assertTrue(targetMeasure.isSmallerOrEqual(biggerInch));
        assertTrue(targetMeasureFraction.isSmallerOrEqual(biggerFraction));
        assertTrue(targetMeasureFraction.isSmallerOrEqual(targetMeasureFraction));
        assertFalse(targetMeasureFraction.isSmallerOrEqual(smallerFraction));
    }
    @Test
    public void isBiggerOrEqual() {
        assertTrue(targetMeasure.isBiggerOrEqual(smallerFeet));
        assertTrue(targetMeasure.isBiggerOrEqual(smallerInch));
        assertTrue(targetMeasure.isBiggerOrEqual(targetMeasure));
        assertFalse(targetMeasure.isBiggerOrEqual(biggerFeet));
        assertFalse(targetMeasure.isBiggerOrEqual(biggerInch));
        assertFalse(targetMeasureFraction.isBiggerOrEqual(biggerFraction));
        assertTrue(targetMeasureFraction.isBiggerOrEqual(targetMeasureFraction));
        assertTrue(targetMeasureFraction.isBiggerOrEqual(smallerFraction));
    }

    ImperialMeasure oddMeasureWithoutFraction = new ImperialMeasure(5, 1);

    @Test
    public void getHalfOddMeasureWithoutFraction() {
        ImperialMeasure result = oddMeasureWithoutFraction.getHalf();
        assertThat(result.getFeet()).isEqualTo(2);
        assertThat(result.getInches()).isEqualTo(6);
        assertThat(result.getInchNum()).isEqualTo(1);
        assertThat(result.getInchDenom()).isEqualTo(2);
    }

    ImperialMeasure oddMeasureWithFraction = new ImperialMeasure(5, 5, 1, 3);
    @Test
    public void getHalfOddMeasureWithFraction() {
        ImperialMeasure result = oddMeasureWithFraction.getHalf();
        assertThat(result.getFeet()).isEqualTo(2);
        assertThat(result.getInches()).isEqualTo(8);
        assertThat(result.getInchNum()).isEqualTo(2);
        assertThat(result.getInchDenom()).isEqualTo(3);
    }

    ImperialMeasure evenMeasureWithoutFraction = new ImperialMeasure(6, 4);

    @Test
    public void getHalfevenMeasureWithoutFractionn() {
        ImperialMeasure result = evenMeasureWithoutFraction.getHalf();
        assertThat(result.getFeet()).isEqualTo(3);
        assertThat(result.getInches()).isEqualTo(2);
    }

    ImperialMeasure evenMeasureWithFraction = new ImperialMeasure(6, 4, 1, 4);
    @Test
    public void getHalfevenMeasureWithFraction() {
        ImperialMeasure result = evenMeasureWithFraction.getHalf();
        assertThat(result.getFeet()).isEqualTo(3);
        assertThat(result.getInches()).isEqualTo(2);
        assertThat(result.getInchNum()).isEqualTo(1);
        assertThat(result.getInchDenom()).isEqualTo(8);
    }

    @Test
    public void convertImperialToPixelsWithoutFraction(){
        double result = evenMeasureWithoutFraction.convertImperialToPixels();
        double result2 = oddMeasureWithoutFraction.convertImperialToPixels();
        assertThat(result).isEqualTo(228);
        assertThat(result2).isEqualTo(183);
    }
    @Test
    public void convertImperialToPixelsWithFraction(){
        double result = evenMeasureWithFraction.convertImperialToPixels();
        double result2 = oddMeasureWithFraction.convertImperialToPixels();
        assertThat(result).isEqualTo(228.75);
        assertThat(result2).isEqualTo(196);
    }
    @Test
    public void convertImperialToFloatWithoutFraction(){
        float resultEven = evenMeasureWithoutFraction.convertImperialToFloat();
        float resultOdd = oddMeasureWithoutFraction.convertImperialToFloat();
        assertThat(resultEven).isEqualTo(76);
        assertThat(resultOdd).isEqualTo(61);
    }
    @Test
    public void convertImperialToFloatWithFraction(){
        float resultEven = evenMeasureWithFraction.convertImperialToFloat();
        float resultOdd = oddMeasureWithFraction.convertImperialToFloat();
        assertThat(resultEven).isEqualTo((float) 76.25);
        assertThat(resultOdd).isEqualTo((float) 65.333336);
    }
    ImperialMeasure doubleToImperialWithoutFrac = new ImperialMeasure(20,10,0,1);
    ImperialMeasure doubleToImperialWithFrac =  new ImperialMeasure(6,5,1,4);

    @Test
    public void convertFloatToImperial(){
        double doubleWithoutFrac = 250;
        double doubleWithFrac = 77.25;
        ImperialMeasure result = doubleToImperialWithoutFrac.convertDoubleToImperial(doubleWithoutFrac);
        assertThat(result.getFeet()).isEqualTo(20);
        assertThat(result.getInches()).isEqualTo(10);
        assertThat(result.getInchNum()).isEqualTo(0);
        assertThat(result.getInchDenom()).isEqualTo(1);

        ImperialMeasure result2 = doubleToImperialWithFrac.convertDoubleToImperial(doubleWithFrac);
        assertThat(result2.getFeet()).isEqualTo(6);
        assertThat(result2.getInches()).isEqualTo(5);
        assertThat(result2.getInchNum()).isEqualTo(1);
        assertThat(result2.getInchDenom()).isEqualTo(4);
    }
}
