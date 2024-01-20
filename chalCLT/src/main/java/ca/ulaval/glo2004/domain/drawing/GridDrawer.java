package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.domain.DTO.ImperialMeasureDTO;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;

import java.awt.Graphics;
import java.awt.Color;

public class GridDrawer {

    private ImperialMeasure gridSpacing;

    public GridDrawer() {
        this.gridSpacing = new ImperialMeasure(0, 6, 44, 128);
    }

    public void setGridSpacing(ImperialMeasure spacing) {
        this.gridSpacing = spacing;
    }

    public void drawGrid(Graphics g, ImperialMeasureDTO p_gridSpacing) {

        int alpha = 90;
        Color semiTransparentGray = new Color(128, 128, 128, alpha);
        g.setColor(semiTransparentGray);
        ImperialMeasure gridSpacing = new ImperialMeasure(p_gridSpacing.Feet, p_gridSpacing.Inches, p_gridSpacing.InchesNum, p_gridSpacing.InchesDenom);
        int width = 1920;
        int height = 1080;

        for (int x = 0; x <= width; x += gridSpacing.convertImperialToPixels()) {
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += gridSpacing.convertImperialToPixels()) {
            g.drawLine(0, y, width, y);
        }
    }
}
