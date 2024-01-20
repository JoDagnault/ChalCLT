package ca.ulaval.glo2004.gui;


import ca.ulaval.glo2004.domain.DTO.AccessoryDTO;
import ca.ulaval.glo2004.domain.DTO.DimensionCabinDTO;
import ca.ulaval.glo2004.domain.cabinComposition.AccessoryType;
import ca.ulaval.glo2004.domain.cabinComposition.Cabin;
import ca.ulaval.glo2004.domain.controller.CabinController;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainWindow extends JFrame {

    DrawingPanel drawingPanel;

    public ViewType viewType = ViewType.FRONT;
    public CabinController controller;
    public enum ViewType{
        FRONT, BACK, LEFT, RIGHT, TOP
    }
    public void setViewType(ViewType newType){
        this.viewType = newType;
        controller.setCurrentWall(newType);
        actionTab.updateInformationBox();
        actionTab.showView("currentCabin");
    }
    public ViewType getViewType(){return this.viewType;}
    private final ModificationTab modificationTab;
    private final InformationTab informationTab;
    private final ActionTab actionTab;
    private Zoom zoom;
    private Point mousePoint;
    private Point delta = new Point();

    public MainWindow () {
        initView();
        DimensionCabin tempDim = new DimensionCabin(new DimensionCabin(new ImperialMeasure(10,0),new ImperialMeasure(10,0), new ImperialMeasure(8,0)));
        Cabin temporaryCabin = new Cabin(tempDim,"PrisonMikeCabin", new ImperialMeasure(0,6), new ImperialMeasure(0,1));
        controller = new CabinController(temporaryCabin, this);
        drawingPanel = new DrawingPanel(this);
        setMouseListener(drawingPanel);
        setMouseMotionListener(drawingPanel);
        modificationTab = new ModificationTab(this);
        actionTab = new ActionTab(this);
        informationTab = new InformationTab(this);
        this.zoom = new Zoom(drawingPanel);  // Initialisation correcte
        drawingPanel.setZoom(zoom);
    }

    public MainWindow(Cabin cabin){
        initView();
        controller = new CabinController(cabin, this);
        drawingPanel = new DrawingPanel(this);
        setMouseListener(drawingPanel);
        setMouseMotionListener(drawingPanel);
        modificationTab = new ModificationTab(this);
        actionTab = new ActionTab(this);
        informationTab = new InformationTab(this);
        this.zoom = new Zoom(drawingPanel);  // Initialisation correcte
        drawingPanel.setZoom(zoom);
    }

    public void initView(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("ChaCLT");
        setSize( 1600,1000);
        setLayout(new BorderLayout(10, 10));
        addToolBar();
        setVisible(true);
        setLocationRelativeTo(null);
        this.viewType = ViewType.FRONT;
    }

    private void addToolBar(){
        JMenuBar toolBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenu stlMenu = new JMenu("Exporter planches STL");

        JMenuItem fileExport = getMenuItem();
        JMenuItem fileSave = new JMenuItem("Importer projet");
        fileSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Import File");
                fileChooser.setApproveButtonText("Import");
                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    controller.importProject(selectedFile.getAbsolutePath());
                }

            }
        });

        List<JMenuItem> fileItems = new ArrayList<>();
        Collections.addAll(fileItems, fileExport, fileSave);


        JMenuItem stlBrut = new JMenuItem("Exporter brut");
        stlBrut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportPanelBrut();
            }
        });

        JMenuItem stlFini = new JMenuItem("Exporter fini");
        stlFini.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportPanelFinished();
            }
        });

        JMenuItem stlGroove = new JMenuItem("Exporter retraits");
        stlGroove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exportPanelGroove();
            }
        });
        List<JMenuItem> stlItems = new ArrayList<>();
        Collections.addAll(stlItems, stlBrut, stlFini, stlGroove);

        for (JMenuItem item : fileItems) {
            fileMenu.add(item);
        }


        for (JMenuItem item : stlItems) {
            stlMenu.add(item);
        }

        toolBar.add(fileMenu);
        toolBar.add(stlMenu);

        this.setJMenuBar(toolBar);
    }

    private JMenuItem getMenuItem() {
        JMenuItem fileExport = new JMenuItem("Enregistrer-sous");
        fileExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(String.valueOf(JFileChooser.DIRECTORIES_ONLY));
                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    controller.saveFile(selectedFile.getAbsolutePath());
                }

            }
        });
        return fileExport;
    }

    private void setMouseListener(Container container){
        container.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mousePoint = e.getPoint();
                double currentX = (e.getX() - zoom.getViewX()) / zoom.getZoom();
                double currentY = (e.getY() - zoom.getViewY()) / zoom.getZoom();
                if(viewType != ViewType.TOP){
                    if(controller.mouseOnGroove(currentX, currentY) == "Front"){
                        if(!actionTab.getPanneauSeulState())
                        {
                            setViewType(ViewType.FRONT);
                            actionTab.updateRadioButton(0);
                        }
                    }
                    else if(controller.mouseOnGroove(currentX, currentY) == "Back")
                    {
                        if(!actionTab.getPanneauSeulState())
                        {
                            setViewType(ViewType.BACK);
                            actionTab.updateRadioButton(1);
                        }
                    }
                }
                else{
                    if(controller.onWallTopView(currentX, currentY) == "Back"){
                        setViewType(ViewType.FRONT);
                        actionTab.updateRadioButton(0);
                    }
                    else if(controller.onWallTopView(currentX, currentY) == "Left"){
                        setViewType(ViewType.LEFT);
                        actionTab.updateRadioButton(4);
                    }
                    else if(controller.onWallTopView(currentX, currentY)== "Right"){
                        setViewType(ViewType.RIGHT);
                        actionTab.updateRadioButton(3);
                    }
                    else if(controller.onWallTopView(currentX, currentY) == "Front"){
                        setViewType(ViewType.FRONT);
                        actionTab.updateRadioButton(0);
                    }
                }

                drawingPanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

                mousePoint = e.getPoint();
                double transformedX = (e.getX() - zoom.getViewX()) / zoom.getZoom();
                double transformedY = (e.getY() - zoom.getViewY()) / zoom.getZoom();

               //controller.selectionStatus(mousePoint.getX(), mousePoint.getY());
                // Utiliser ces coordonnées transformées pour la sélection
              controller.selectionStatus(transformedX, transformedY);
                int AccessoryID = controller.getIDSelected();
                if(AccessoryID != -1)
                {
                    AccessoryDTO currentAccessory = controller.getAccessory(AccessoryID);
                    if(currentAccessory.AccessoryType == AccessoryType.DOOR)
                    {
                        setModificationTab(new DimensionCabinDTO(currentAccessory.Dimension), currentAccessory.Position, "porte");
                        modificationTab.showView("currentDoor");
                    }
                    else {
                        setModificationTab(new DimensionCabinDTO(currentAccessory.Dimension), currentAccessory.Position, "fenetre");
                        modificationTab.showView("currentWindow");
                    }
                }
                drawingPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        resetView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoom.resetView();
            }
        });
    }
    private void setMouseMotionListener(final Container container){
        container.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double xMove = ((e.getX() - zoom.getViewX())/zoom.getZoom()) - ((mousePoint.x - zoom.getViewX())/zoom.getZoom());
                double yMove = ((e.getY()- zoom.getViewY())/zoom.getZoom()) - ((mousePoint.y- zoom.getViewY())/zoom.getZoom());
                double xPixels = xMove/3;
                double yPixels = yMove/3;
                delta.setLocation(xPixels, yPixels);
                if(controller.getIDSelected() != -1){
                    AccessoryDTO currentAccessory = controller.getAccessory(controller.getIDSelected());
                    Point2D currentPoint = new Point2D(currentAccessory.Position.getXCoord().convertDoubleToImperial(delta.x), currentAccessory.Position.getYCoord().convertDoubleToImperial(delta.y));
                    if(currentAccessory.AccessoryType == AccessoryType.DOOR){
                        controller.editAccessory(new Point2D(currentAccessory.Position.getXCoord().addMeasures(currentPoint.getXCoord()), currentAccessory.Position.getYCoord()),
                                new DimensionCabin(currentAccessory.Dimension.getLength(), currentAccessory.Dimension.getWidth(), currentAccessory.Dimension.getHeight()),currentAccessory.AccessoryID);
                        setModificationTab(new DimensionCabinDTO(currentAccessory.Dimension), currentAccessory.Position,"porte");
                        modificationTab.showView("currentDoor");
                    }
                    else{
                        controller.editAccessory(new Point2D(currentAccessory.Position.getXCoord().addMeasures(currentPoint.getXCoord()), currentAccessory.Position.getYCoord().addMeasures(currentPoint.getYCoord())),
                                new DimensionCabin(currentAccessory.Dimension.getLength(), currentAccessory.Dimension.getWidth(), currentAccessory.Dimension.getHeight()),currentAccessory.AccessoryID);
                        setModificationTab(new DimensionCabinDTO(currentAccessory.Dimension), currentAccessory.Position, "fenetre");
                        modificationTab.showView("currentWindow");
                    }
                }
                mousePoint = new Point(e.getX() + delta.x, e.getY() + delta.y);
                drawingPanel.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                double currentPositionX = (e.getX() - zoom.getViewX()) / zoom.getZoom();
                double currentPositionY = (e.getY() - zoom.getViewY())/ zoom.getZoom();
                if(viewType != ViewType.TOP) {
                    if(controller.mouseOnWall(currentPositionX, currentPositionY)){
                        informationTab.updateInformationTab("");
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnGroove(currentPositionX, currentPositionY) == "Front"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("Front");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnGroove(currentPositionX, currentPositionY) == "Back"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("Back");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnRoof(currentPositionX, currentPositionY, viewType) == "Plank"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("Plank");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnRoof(currentPositionX, currentPositionY, viewType) == "Extension"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("Extension");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnRoof(currentPositionX, currentPositionY, viewType) == "PinionLeft"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("PinionLeft");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.mouseOnRoof(currentPositionX, currentPositionY, viewType) == "PinionRight"){
                        if(actionTab.getPanneauSeulState()){
                            informationTab.updateInformationTab("NotOnWall");
                        }
                        else{
                            informationTab.updateInformationTab("PinionRight");
                        }
                        informationTab.showView("currentPanel");
                    }
                    else {
                        informationTab.updateInformationTab("NotOnWall");
                        informationTab.showView("currentPanel");
                    }
                }
                else {
                    if(controller.onWallTopView(currentPositionX, currentPositionY) == "Front"){
                        informationTab.updateInformationTab("Front");
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.onWallTopView(currentPositionX, currentPositionY) == "Back"){
                        informationTab.updateInformationTab("Back");
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.onWallTopView(currentPositionX, currentPositionY) == "Right"){
                        informationTab.updateInformationTab("Right");
                        informationTab.showView("currentPanel");
                    }
                    else if(controller.onWallTopView(currentPositionX, currentPositionY) == "Left"){
                        informationTab.updateInformationTab("Left");
                        informationTab.showView("currentPanel");
                    }
                    else {
                        informationTab.updateInformationTab("NotOnWall");
                        informationTab.showView("currentPanel");
                    }
                }
            }
        });
    }

    public CabinController getController() {
        return controller;
    }
    public DrawingPanel getDrawingPanel(){
        return drawingPanel;
    }
    public void setModificationTab(DimensionCabinDTO dimension, Point2D position, String accessoryType){
        modificationTab.showCurrentModificationTab(accessoryType,dimension,position);
    }

    public ModificationTab getModificationTab(){
        return modificationTab;
    }
    public InformationTab getInformationTab(){return  informationTab;}
    public ActionTab getActionTab(){return  actionTab;}
    JMenuItem resetView = new JMenuItem("Réinitialiser la vue");

}


