package ca.ulaval.glo2004.domain.drawing;

import ca.ulaval.glo2004.gui.MainWindow;
import java.awt.*;

public class CabinDrawer {

    private MainWindow mainWindow;
    private TopDrawer topDrawer;
    private WallDrawer wallDrawer;
    private RoofDrawer roofDrawer;
    private GridDrawer gridDrawer;

    // Constructeur de CabinDrawer
    public CabinDrawer(MainWindow p_mainWindow){
        mainWindow = p_mainWindow;
        this.wallDrawer = new WallDrawer();
        this.topDrawer = new TopDrawer();
        this.roofDrawer = new RoofDrawer();
        this.gridDrawer = new GridDrawer();

    }

    // Permet de dessiner les éléments
    public void draw(Graphics g){

        if(getCurrentViewType() == MainWindow.ViewType.TOP){
            topDrawer.drawTop(g, mainWindow.controller.getCabinDimension(), mainWindow.controller.getCabinThickness(), mainWindow.controller.getErrorMargin());
        }
        else {
            roofDrawer.drawRoof(g, mainWindow.controller.getCabinDimension(), mainWindow.controller.getCabinThickness(), mainWindow.controller.getErrorMargin(), mainWindow.controller.getRoof(), getCurrentViewType());
            wallDrawer.drawWall(g, mainWindow.controller.getCabinDimension(), mainWindow.controller.getCabinThickness(),mainWindow.controller.getErrorMargin(), getCurrentViewType(), mainWindow.controller.getCurrentWall()); //Ajout de viewType pour savoir quel mur on doit afficher
        }
        gridDrawer.drawGrid(g, mainWindow.controller.getGridSpacing());
    }
    public void drawPanneauSeul(Graphics g){
        if(getCurrentViewType() == MainWindow.ViewType.TOP){
            topDrawer.drawTop(g, mainWindow.controller.getCabinDimension(), mainWindow.controller.getCabinThickness(), mainWindow.controller.getErrorMargin());
        }
        else{
            wallDrawer.drawWallSeul(g, mainWindow.controller.getCabinDimension(), mainWindow.controller.getCabinThickness(),mainWindow.controller.getErrorMargin(), getCurrentViewType(), mainWindow.controller.getCurrentWall());
        }
        gridDrawer.drawGrid(g, mainWindow.controller.getGridSpacing());
    }

    // Permet d'obtenir le type de vue courrant
    public MainWindow.ViewType getCurrentViewType(){
        return mainWindow.getViewType();
    }

}
