package ca.ulaval.glo2004.domain.utils;

public class RoofUtils {

    public RoofUtils(){}
    public static float hypotenuseCalculator (float baseLength, float plankAngle) {
        float angleRadians = (float) Math.toRadians(plankAngle);
        return (float) (baseLength / Math.cos(angleRadians));
    }

    public static float heightCalculator(float base, float angleDegrees) {
        float angleRadians = (float) Math.toRadians(angleDegrees);
        return (float) (base * Math.tan(angleRadians));
    }

}
