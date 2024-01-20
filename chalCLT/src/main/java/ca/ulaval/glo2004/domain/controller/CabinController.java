package ca.ulaval.glo2004.domain.controller;

import ca.ulaval.glo2004.domain.DTO.*;
import ca.ulaval.glo2004.domain.cabinComposition.*;
import ca.ulaval.glo2004.domain.file.ExportFile;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import ca.ulaval.glo2004.domain.utils.UndoRedoUtils;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domain.panel.ExportPanel;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class CabinController {

    private Cabin cabin;
    private Wall currentWall;
    MainWindow mainWindow;
    private Stack<byte[]> undoStack;
    private Stack<byte[]> redoStack;
    private ImperialMeasure gridSpacing;
    private final ExportPanel exportPanel;

    // Constructeur par défaut de CabonController
    public CabinController(Cabin p_cabin, MainWindow p_mainWindow){
        this.gridSpacing = new ImperialMeasure(0, 6, 44, 128);
        cabin = p_cabin;
        setCurrentWall(MainWindow.ViewType.FRONT);
        this.exportPanel =  new ExportPanel(cabin);
        mainWindow = p_mainWindow;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void performAction() {
        saveState(cabin);
    }

    private void saveState(Cabin p_cabin) {
        undoStack.push(UndoRedoUtils.serializeToByte(p_cabin));
    }

    public void undo(){
        if(!undoStack.isEmpty()) {
            redoStack.push(UndoRedoUtils.serializeToByte(cabin));
            byte[] previous_state = undoStack.pop();
            cabin = UndoRedoUtils.deserializeFromByte(previous_state);
            setCurrentWall(mainWindow.getViewType());
            mainWindow.getActionTab().updateInformationBox();
            mainWindow.getActionTab().showView("currentCabin");
        }
    }

    public void redo(){
        if (!redoStack.isEmpty()){
            undoStack.push(UndoRedoUtils.serializeToByte(cabin));
            byte[] previous_state = redoStack.pop();
            cabin = UndoRedoUtils.deserializeFromByte(previous_state);
            setCurrentWall(mainWindow.getViewType());
            mainWindow.getActionTab().updateInformationBox();
            mainWindow.getActionTab().showView("currentCabin");
        }
    }

    // Permet de définir les dimensions d'une Cabin
    public void setCabinDimension(DimensionCabin dimensionCabin){

        cabin.setDimension(dimensionCabin);

    }

    // Permet d'obtenir les dimensions d'une Cabin
    public DimensionCabinDTO getCabinDimension(){
        return new DimensionCabinDTO(cabin.getDimensionCabin());
    }

    // Permet de définir la thickness d'une Cabin
    public void setCabinThickness(ImperialMeasure thickness){
        cabin.setThickness(thickness);
    }

    // Permet d'obtenir la thickness d'une Cabin
    public ImperialMeasureDTO getCabinThickness(){
        return new ImperialMeasureDTO(cabin.getThickness());
    }

    public ImperialMeasureDTO getErrorMargin(){ return new ImperialMeasureDTO(cabin.getErrorMargin());}
    public void setErrorMargin(ImperialMeasure errorMargin){cabin.setErrorMargin(errorMargin);}

    public ImperialMeasureDTO getMinAccessoryDistance(){ return new ImperialMeasureDTO(cabin.getMinAccessoryDistance());}


    public void setCurrentWall(MainWindow.ViewType viewType){
        for(Wall wall : cabin.getWallList()){
            if(wall.getWallType().name().equals(viewType.name())){
                currentWall = wall;
            }
        }
    }

    // Permet d'obtenir le mur courrant
    public WallDTO getCurrentWall(){
        return new WallDTO(currentWall);
    }

    // Permet de savoir si un accessoire a bien été ajouté
    public boolean addAccessory(Point2D position, ImperialMeasure height, ImperialMeasure length, AccessoryType accessoryType){
        // Should return true if added successfully else false.
        performAction();
        DimensionCabin dimension = new DimensionCabin(height, cabin.getThickness(), length);
        return currentWall.addAccessory(position, dimension, accessoryType);

    }

    // Permet de savoir si un accessoire a bien été modifié
    public boolean editAccessory (Point2D newPosition, DimensionCabin newDimension, int accessoryID){
        // Should return true if edited successfully else false.
        return currentWall.editAccessory(newPosition, newDimension, accessoryID);
    }

    // Permet de supprimer un accessoire
    public void deleteAccessory(int accessoryID){
        performAction();
        currentWall.deleteAccessory(accessoryID);
    }

    // Permet de connaitre le statut de la séléction
    public void selectionStatus(double x, double y){
        ImperialMeasure width = cabin.getDimensionCabin().getWidth();
        if(!(getCurrentWall() == null)) {
            currentWall.selectionStatus(x, y, width);
        }
    }

    // Permet d'obtenir le ID de l'accessoire sélectionné
    public int getIDSelected(){
        return currentWall.getAccessorySelected();
    }

    // Permet d'obtenir l'accessoire sélectionné
    public AccessoryDTO getAccessory(int IDSelected){
        return new AccessoryDTO(currentWall.getAccessoryList().get(currentWall.indexOfAccessory(IDSelected)));
    }
    public void resetDimension(){
        performAction();
        cabin.resetDimension();
        for(Wall wall: cabin.getWallList()) {
            if (wall.getWallType() == WallType.FRONT || wall.getWallType() == WallType.BACK) {
                wall.setWallDimension(new DimensionCabin(cabin.getDimensionCabin().getLength(), cabin.getThickness(), cabin.getDimensionCabin().getHeight()));
            } else {
                wall.setWallDimension(new DimensionCabin(cabin.getDimensionCabin().getWidth(), cabin.getThickness(), cabin.getDimensionCabin().getHeight()));
            }
        }
        cabin.getRoof().setDimension(cabin.getDimensionCabin());
    }
    public void resetAccessory(){
        for(Wall wall : cabin.getWallList()){
            wall.getAccessoryList().clear();
        }
    }
    public void resetSelectionStatus(){
        currentWall.resetSelectionStatus();
    }
    public boolean mouseOnWall(double x, double y){
        if(currentWall.getWallType() == WallType.FRONT || currentWall.getWallType() == WallType.BACK){
            return currentWall.mouseOnWall(x, y);
        }
        else{
            return currentWall.mouseOnSideWall(x,y,cabin);
        }
    }
    public String mouseOnGroove(double x, double y){
        if(currentWall.getWallType() == WallType.LEFT){
            if(currentWall.mouseOnGrooveRight(x,y, cabin)){
                return "Front";
            }
            else if(currentWall.mouseOnGrooveLeft(x,y, cabin)){
                return "Back";
            }
        }
        else if(currentWall.getWallType() == WallType.RIGHT){
            if(currentWall.mouseOnGrooveLeft(x,y, cabin)){
                return "Front";
            }
            else if(currentWall.mouseOnGrooveRight(x,y, cabin)){
                return"Back";
            }
        }
        return "";
    }
    public String mouseOnRoof(double x, double y, MainWindow.ViewType viewType){
        String onRoof = "";
        if(viewType == MainWindow.ViewType.FRONT && cabin.getRoof().isOnInclinedPlank(x,y,cabin)){
            onRoof = "Plank";
        }
        else if( viewType == MainWindow.ViewType.BACK && cabin.getRoof().isOnInclinedPlankBack(x,y,cabin)){
            onRoof = "Plank";
        }
        else if(viewType == MainWindow.ViewType.BACK && cabin.getRoof().isOnVerticalExtension(x,y,cabin)){
            onRoof = "Extension";
        }
        else if(viewType== MainWindow.ViewType.LEFT && cabin.getRoof().isOnInclinedPlankLeftSide(x,y,cabin)){
            onRoof = "Plank";
        }
        else if(viewType== MainWindow.ViewType.LEFT && cabin.getRoof().isOnPinionLeft(x,y,cabin)){
            onRoof ="PinionLeft";
        }
        else if(viewType== MainWindow.ViewType.RIGHT && cabin.getRoof().isOnInclinedPlankRightSide(x,y,cabin)){
            onRoof = "Plank";
        }
        else if(viewType == MainWindow.ViewType.RIGHT && cabin.getRoof().isOnPinionRight(x,y,cabin)){
            onRoof = "PinionRight";
        }
        else if(viewType== MainWindow.ViewType.LEFT && cabin.getRoof().isInsideExtensionLeft(x,y,cabin)){
            onRoof ="Extension";
        }
        else if(viewType== MainWindow.ViewType.RIGHT && cabin.getRoof().isInsideExtensionRight(x,y,cabin)){
            onRoof ="Extension";
        }
        return onRoof;
    }
    public String onWallTopView(double x, double y){
        String onWall = "";
        if(cabin.getWallList().get(0).isOnTopWallBack(x,y,cabin)){
            onWall = "Back";
        }
        if(cabin.getWallList().get(1).isOnTopWallFront(x,y,cabin)){
            onWall = "Front";
        }
        if(cabin.getWallList().get(2).isOnTopWallLeft(x,y,cabin)){
            onWall = "Left";
        }
        if(cabin.getWallList().get(3).isOnTopWallRight(x,y,cabin)){
            onWall = "Right";
        }
        return onWall;
    }

    public List<WallDTO> getListWall(){
        return cabin.getListWall();
    }

    public void exportPanelBrut(){
        exportPanel.exportPanelBrut();
    }

    public void exportPanelFinished(){
        exportPanel.exportPanelFinished();
    }
    public void exportPanelGroove(){exportPanel.exportPanelGroove();
    }

    public void saveFile(String p_filepath){
        try {
            ExportFile.serializeCabin(cabin, p_filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importProject(String p_filepath) {
        Cabin newCabin;
        try {
            newCabin = ExportFile.deserializeCabin(p_filepath);
            loadProject(newCabin);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadProject(Cabin cabin){
        new MainWindow(cabin);
        mainWindow.dispose();
    }

    public CabinDTO getCabin() {return new CabinDTO(cabin);}
    public RoofDTO getRoof(){
        return (new RoofDTO(cabin,cabin.getRoof()));
    }
    public void setAngle(float angle){
        cabin.getRoof().setPlankAngle(angle);
    }
    public void setGridSpacing(ImperialMeasure gridSpacing) {
        this.gridSpacing = gridSpacing;
    }
    public ImperialMeasureDTO getGridSpacing() {
        return new ImperialMeasureDTO(this.gridSpacing);
    }
    public void refreshDimension(){
        setCabinDimension(cabin.getDimensionCabin());
    }

    public void validateAccessories(){
        for(Accessory accessory : this.currentWall.getAccessoryList()){
            this.currentWall.validateAccessory(accessory);
            this.currentWall.isAccessoryValid(accessory);
        }
    }
}
