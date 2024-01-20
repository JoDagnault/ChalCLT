package ca.ulaval.glo2004.domain.DTO;

import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import java.util.UUID;

public class ImperialMeasureDTO {

    public int Feet;
    public int Inches;
    public int InchesNum;
    public int InchesDenom;
    public UUID Uuid;

    public ImperialMeasureDTO(ImperialMeasure measure){
        Feet = measure.getFeet();
        Inches = measure.getInches();
        InchesNum = measure.getInchNum();
        InchesDenom = measure.getInchDenom();
        Uuid = measure.getUUID();
    }
}
