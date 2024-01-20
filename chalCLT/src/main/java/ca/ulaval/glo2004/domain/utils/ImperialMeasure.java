package ca.ulaval.glo2004.domain.utils;

import java.io.Serializable;
import java.util.UUID;

public class ImperialMeasure implements Serializable {

    private int feet;
    private int inches;
    private int inchDenom = 1;
    private int inchNum;
    private UUID uuid;

    // Constructeur par défaut. Valeur de 1 pied
    public ImperialMeasure(){
        feet = 1;
        inches = 0;
        validateMeasure();
    }

    // Constructeur de ImperialMeasure
    public ImperialMeasure(int p_feet, int p_inch){
        feet = p_feet;
        inches = p_inch;
        validateMeasure();
    }

    // Constructeur de ImperialMeasure
    public ImperialMeasure(int p_feet, int p_inch, int p_num, int p_denom){
        this(p_feet, p_inch);
        inchNum = p_num;
        if (p_denom == 0) throw new IllegalArgumentException("Cannot assign 0 to Denominator");
        inchDenom = p_denom;
        validateMeasure();
        this.uuid = UUID.randomUUID();
    }

    public ImperialMeasure(ImperialMeasure measure){
        feet = measure.getFeet();
        inches = measure.getInches();
        inchNum = measure.getInchNum();
        inchDenom = measure.getInchDenom();
    }

    // Formatter les dimensions en string
    public String toString() {
        return (inchNum == 0) ?
                feet + "'" + inches + "\"" :
                feet + "'" + inches + "\"" + inchNum + "/" + inchDenom;
    }

    // Permet d'ajouter des mesures
    public ImperialMeasure addMeasures(ImperialMeasure other) {
        if(other.inches < 0)
        {
            if (inches + other.inches < 0) {
                int extraInches = other.inches % 12;
                int extraFeet = other.inches / 12;
                feet = feet - (1 - extraFeet);
                inches = 12 + extraInches;
            }
            else {
                feet += other.feet;
                inches += other.inches;
            }
        }
        else{
            feet += other.feet;
            inches += other.inches;
        }
        if(other.inchNum < 0){
            if(other.inchNum + inchNum < 0){
                int commonDenom = inchDenom * other.inchDenom;
                int extraInches = (other.inchNum*commonDenom/other.inchDenom) / commonDenom;
                if(inches -(1 -extraInches) < 0) {
                    int extra = other.inches % 12;
                    int extraFeet = other.inches / 12;
                    feet = feet - (1 - extraFeet);
                    inches = 12 -(1 - extra);
                }
                else{
                    inches = inches - (1 - extraInches);
                }
                inchNum = commonDenom + (other.inchNum * (commonDenom / other.inchDenom));
                inchDenom = commonDenom;
                simplifyFraction();
            }
            else{
                int commonDenom = inchDenom * other.inchDenom;
                inchNum = (inchNum * (commonDenom / inchDenom)) + (other.inchNum * (commonDenom / other.inchDenom));
                inchDenom = commonDenom;
                simplifyFraction();
            }
        }
        else if (inchNum != 0 ^ other.inchNum != 0 ){
            inchNum += other.inchNum;
            inchDenom *= other.inchDenom;
            simplifyFraction();
        }
        else if (inchNum != 0){
            int commonDenom = inchDenom * other.inchDenom;
            inchNum = (inchNum * (commonDenom / inchDenom)) + (other.inchNum * (commonDenom / other.inchDenom));
            inchDenom = commonDenom;
            this.simplifyFraction();
        }
        validateMeasure();
        return new ImperialMeasure(this.feet, this.inches, this.inchNum, this.inchDenom);
    }

    // Permet d'ajouter des mesures
    public ImperialMeasure addMeasuresCopy(ImperialMeasure other) {
        int newFeet = feet;
        int newInches = inches;
        int newInchNum = inchNum;
        int newInchDenom = inchDenom;

        newFeet += other.feet;
        newInches += other.inches;

        if (newInchNum != 0 ^ other.inchNum != 0 ){
            newInchNum += other.inchNum;
            newInchDenom *= other.inchDenom;
            int [] simplifiedValues = simplifyFraction(newInchNum, newInchDenom);
            newInchNum = simplifiedValues[0];
            newInchDenom = simplifiedValues[1];
        }
        else if (newInchNum != 0){
            int commonDenom = newInchDenom * other.inchDenom;
            newInchNum = (newInchNum * (commonDenom / newInchDenom)) + (other.inchNum * (commonDenom / other.inchDenom));
            newInchDenom = commonDenom;
            int [] simplifiedValues = simplifyFraction(newInchNum, newInchDenom);
            newInchNum = simplifiedValues[0];
            newInchDenom = simplifiedValues[1];
        }
        int [] validatedValues = validateMeasure(newFeet, newInches, newInchNum, newInchDenom);
        return new ImperialMeasure(validatedValues[0], validatedValues[1], validatedValues[2], validatedValues[3]);
    }

    // Permet de soustraire une mesure
    public ImperialMeasure subtractMeasures(ImperialMeasure other) {
        feet -= other.feet;
        inches -= other.inches;

        if (inches < 0){
            feet--;
            inches += 12;
        }

        if (inchNum != 0 || other.inchNum != 0) {
            int commonDenom = inchDenom * other.inchDenom;
            inchNum = (inchNum * (commonDenom / inchDenom)) - (other.inchNum * (commonDenom / other.inchDenom));
            inchDenom = commonDenom;

            if (inchNum < 0){
                feet--;
                inches += 11;
                inchNum = inchDenom + inchNum;
            }
            simplifyFraction();
        }
        validateMeasure();
        return new ImperialMeasure(this.feet, this.inches, this.inchNum, this.inchDenom);
    }

    // Permet de soustraire une mesure
    public ImperialMeasure subtractMeasuresCopy(ImperialMeasure other) {
        int newFeet = feet;
        int newInches = inches;
        int newInchNum = inchNum;
        int newInchDenom = inchDenom;


        newFeet -= other.feet;
        newInches -= other.inches;

        if (newInches < 0){
            newFeet--;
            newInches += 12;
        }

        if (newInchNum != 0 || other.inchNum != 0) {
            int commonDenom = newInchDenom * other.inchDenom;
            newInchNum = (newInchNum * (commonDenom / newInchDenom)) - (other.inchNum * (commonDenom / other.inchDenom));
            newInchDenom = commonDenom;

            if (newInchNum < 0){
                newFeet--;
                newInches += 11;
                newInchNum = newInchDenom + newInchNum;
            }
            int [] simplifiedValues = simplifyFraction(newInchNum, newInchDenom);
            newInchNum = simplifiedValues[0];
            newInchDenom = simplifiedValues[1];
        }
        int [] validatedValues = validateMeasure(newFeet, newInches, newInchNum, newInchDenom);
        return new ImperialMeasure(validatedValues[0], validatedValues[1], validatedValues[2], validatedValues[3]);
    }

    // Permet de comparer des mesures <=
    public boolean isSmallerOrEqual(ImperialMeasure other){
        return (this.feet < other.feet
                || (this.feet == other.feet && this.inches < other.inches)
                || (this.feet == other.feet && this.inches == other.inches && this.inchNum * other.inchDenom <= other.inchNum * this.inchDenom)
        );
    }

    // Permet de comparer des mesures >=
    public boolean isBiggerOrEqual(ImperialMeasure other){
        return (this.feet > other.feet
                || (this.feet == other.feet && this.inches > other.inches)
                || (this.feet == other.feet && this.inches == other.inches && this.inchNum * other.inchDenom >= other.inchNum * this.inchDenom)
        );
    }

    // Permet de comparer des mesures ==
    public boolean isEqual(ImperialMeasure other){
        return (this.feet == other.feet && this.inches == other.inches);
    }

    // Permet d'obtenir la moitié d'une mesure
    public ImperialMeasure getHalf(){
        int newInches = inches;
        int newFeet = feet;
        int newInchNum = inchNum;
        int newInchDenom = inchDenom;

        int totalInches = newFeet * 12 + newInches;
        newInchNum += totalInches * newInchDenom;
        newInchDenom *= 2;

        int [] simplifiedValues = simplifyFraction(newInchNum, newInchDenom);
        newInchNum = simplifiedValues[0];
        newInchDenom = simplifiedValues[1];

        int [] validatedValues = validateMeasure(0, 0, newInchNum, newInchDenom);
        return new ImperialMeasure(validatedValues[0], validatedValues[1], validatedValues[2], validatedValues[3]);
    }

    // Permet de vérifier si une mesure est valide
    private void validateMeasure(){
        if(inchNum != 0) {
            while(inchNum >= inchDenom) {
                inchNum -= inchDenom;
                inches += 1;
            }
        }
        if(inches>= 12){
            feet += inches / 12;
            inches %= 12;
        }
    }

    // Permet de vérifier si une mesure est valide
    private int[] validateMeasure(int p_feet, int p_inches, int p_inchNum, int p_inchDenom){
        if(p_inchNum != 0) {
            while(p_inchNum >= p_inchDenom) {
                p_inchNum -= p_inchDenom;
                p_inches += 1;
            }
        }
        if(p_inches>= 12){
            p_feet += p_inches / 12;
            p_inches %= 12;
        }
        return new int[] {p_feet, p_inches, p_inchNum, p_inchDenom};
    }

    // Permet de simplifier une fraction
    private void simplifyFraction(){
        int gcd = findGcd(inchNum, inchDenom);
        inchNum /= gcd;
        inchDenom /= gcd;
    }

    // Permet de simplifier une fraction
    private int[] simplifyFraction(int p_inchNum, int p_inchDenom){
        int gcd = findGcd(p_inchNum, p_inchDenom);
        p_inchNum /= gcd;
        p_inchDenom /= gcd;
        return new int[] {p_inchNum, p_inchDenom};
    }

    private static int findGcd(int a, int b){
        return b == 0 ? a : findGcd(b, a%b);
    }

    // Permet d'obtenir le nombre de pieds
    public int getFeet(){
        return feet;
    }

    // Permet d'obtenir le nombre de pouces
    public int getInches(){
        return inches;
    }

    // Permet d'obtenir le dénominateur en pouces
    public int getInchDenom() {
        return inchDenom;
    }

    // Permet d'obtenir le numérateur en pouces
    public int getInchNum() {
        return inchNum;
    }

    // Permet de définir le nombre de pied
    public void setFeet(int feet) {
        this.feet = feet;
    }

    // Permet de définir le nombre de pouce
    public void setInches(int inches) {
        this.inches = inches;
        validateMeasure();
    }

    // Permet de définir la fraction
    public void setFraction (int num, int denom){
        if (denom == 0) throw new IllegalArgumentException("Cannot assign 0 to Denominator");
        inchNum = num;
        inchDenom = denom;
        validateMeasure();
    }

    // Dans le domaine toutes les mesures devrait être en inches
    // Permet de convertir les mesures impériales en pixels
    public double convertImperialToPixels(){
        double feetMeasure = this.getFeet();
        double inchMeasure = this.getInches();
        double numInchMeasure = this.getInchNum();
        double denomInchMeasure = this.getInchDenom();
        int pixelsFeet = 36;
        int pixelsInch = 3;
        double pixelMeasure;
        pixelMeasure = (feetMeasure*pixelsFeet) + (inchMeasure*pixelsInch) + (pixelsInch*numInchMeasure/denomInchMeasure);
        return pixelMeasure;
    }

    // Permet de convertir les mesures impériales en int
    public float convertImperialToFloat(){
        return this.getFeet() * 12 + this.getInches() + (float)this.getInchNum() / (float)this.getInchDenom();
    }

    // Permet de convertir les doubles en Impérial
    public ImperialMeasure convertDoubleToImperial(double value){
        double extra = 0;
        int newFeet = (int) (value/12);
        extra = value - (newFeet*12);
        int newInches = (int)(extra);
        extra = extra - newInches;
        int newInchNum = (int) (extra*128);
        int newInchDenom = 128;
        int [] simplifyValue = simplifyFraction(newInchNum, newInchDenom);
        newInchNum = simplifyValue[0];
        newInchDenom = simplifyValue[1];
        return new ImperialMeasure(newFeet, newInches, newInchNum, newInchDenom);

    }

    // Permet de convertir les float en Impérial
    public static ImperialMeasure convertFloatToImperial(float inputValue) {
        int totalInches = (int)inputValue;
        int feet = totalInches / 12;
        int remainingInches = totalInches % 12;
        int inchNum = 0;
        int inchDenom = 1;
        return new ImperialMeasure(feet,remainingInches,inchNum,inchDenom);
    }

    public UUID getUUID(){
        return uuid;
    }
}
