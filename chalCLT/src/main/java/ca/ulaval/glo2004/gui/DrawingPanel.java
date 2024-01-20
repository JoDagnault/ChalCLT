package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.drawing.CabinDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DrawingPanel extends JPanel {
    private MainWindow mainWindow;
    private Zoom zoom;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setBackground(Color.black);
        mainWindow.add(this, BorderLayout.CENTER);
    }
    public void setZoom(Zoom zoom) {
        this.zoom = zoom;
   }



    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //Sauvegarder la transformation originale

        AffineTransform originalTransform = g2d.getTransform();

        //Appliquer la transformation pour le zoom

        g2d.translate(zoom.getViewX(), zoom.getViewY());
        g2d.scale(zoom.getZoom(), zoom.getZoom());

        //Restaurez la transformation originale
        CabinDrawer cabinDrawer = new CabinDrawer(mainWindow);

        if(mainWindow.getActionTab().getPanneauSeulState()){
            cabinDrawer.drawPanneauSeul(g2d);
        }
        else{
            cabinDrawer.draw(g2d);
        }
        g2d.setTransform(originalTransform);
    }
}
