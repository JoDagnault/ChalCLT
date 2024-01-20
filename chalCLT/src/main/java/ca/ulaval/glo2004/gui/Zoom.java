package ca.ulaval.glo2004.gui;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;


import ca.ulaval.glo2004.gui.DrawingPanel;
public class Zoom implements MouseWheelListener, MouseMotionListener{
    private double zoom = 1.0;
    private DrawingPanel drawingPanel;

    private int mouseX;  // Position X de la souris
    private int mouseY;  // Position Y de la souris
    private int viewX = 0, viewY = 0; // Position de la vue

    public Zoom(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        drawingPanel.addMouseWheelListener(this);
        drawingPanel.addMouseMotionListener(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {


        double oldZoom = zoom;
        int wheelRotation = e.getWheelRotation();
        double newZoom = calculateMouseZoom(wheelRotation, e.getX(), e.getY());
        double zoomChangeFactor = newZoom / oldZoom;
        viewX = (int)((viewX - e.getX()) * zoomChangeFactor + e.getX());
        viewY = (int)((viewY - e.getY()) * zoomChangeFactor + e.getY());
        if (newZoom == oldZoom) {
            viewX = 0;  // Réinitialiser la position X de la vue
            viewY = 0;  // Réinitialiser la position Y de la vue
        }

        setZoom(newZoom);

        drawingPanel.repaint();
    }
    @SuppressWarnings("unused")
    private double calculateMouseZoom(int wheelRotation, int positionSourisX, int positionSourisY) {
        double incrementation = 0.2;
        double zoomfactor = 1.0 + (wheelRotation * incrementation);
        double newZoom = zoom * zoomfactor;
        return Math.max(newZoom, 1.0);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }


    public void setZoom(double zoom) {
        this.zoom = zoom;
        drawingPanel.repaint();
    }

    public  double getZoom() {
        return zoom;
    }

    public int getViewX() {
        return viewX;
    }

    public int getViewY() {
        return viewY;
    }

    public void resetView() {
        zoom = 1.0;
        viewX = 0;
        viewY = 0;
        drawingPanel.repaint();
    }
}