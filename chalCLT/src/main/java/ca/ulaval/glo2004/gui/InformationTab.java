package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.DTO.CabinDTO;
import ca.ulaval.glo2004.domain.DTO.RoofDTO;
import ca.ulaval.glo2004.domain.DTO.WallDTO;
import ca.ulaval.glo2004.domain.cabinComposition.Cabin;
import ca.ulaval.glo2004.domain.controller.CabinController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InformationTab extends JPanel {
    MainWindow mainWindow;
    private JPanel informationTab;
    private CardLayout cardLayout;
    private final CabinController controller;

    InformationTab(MainWindow mainWindow){
        this.mainWindow = mainWindow;
        controller = mainWindow.getController();
        informationTab = new JPanel();
        setPreferredSize(new Dimension(200, 90));
        cardLayout = new CardLayout();
        informationTab.setLayout(cardLayout);
        InitView();
        mainWindow.add(getInformationTab(), BorderLayout.EAST);
    }
    private void addView(String name, JPanel view){
        informationTab.add(name, view);
    }
    private void InitView(){
        addView("defaultPanel", addInformationTab());
    }
    public void showView(String name){
        cardLayout.show(informationTab, name);
    }

    private JPanel addInformationTab(){
        JPanel defaultInformationTab = new JPanel();
        defaultInformationTab.setBorder(new TitledBorder("Information Panneau"));
        JPanel infoDimension = new JPanel(new GridLayout(4,1));
        infoDimension.setPreferredSize(new Dimension(200, 80));
        addLabel(infoDimension, controller.getCurrentWall(), controller.getCabin());
        defaultInformationTab.add(infoDimension);
        add(defaultInformationTab, BorderLayout.NORTH);
        return defaultInformationTab;
    }
    public void updateInformationTab(String wallName){
        JPanel currentInformationTab = new JPanel();
        currentInformationTab.setBorder(new TitledBorder("Information Panneau"));
        JPanel infoUpdated = new JPanel(new GridLayout(4,1));
        infoUpdated.setPreferredSize(new Dimension(200,80));
        if(wallName == "Back")
        {
            addLabel(infoUpdated, controller.getListWall().get(0), controller.getCabin());
        }
        else if (wallName == "Front"){
            addLabel(infoUpdated, controller.getListWall().get(1), controller.getCabin());
        }
        else if(wallName == "Left"){
            addLabel(infoUpdated, controller.getListWall().get(2), controller.getCabin());
        }
        else if(wallName == "Right"){
            addLabel(infoUpdated, controller.getListWall().get(3), controller.getCabin());
        }
        else if(wallName == "Plank"){
            addPlankLabel(infoUpdated, controller.getRoof());
        }
        else if(wallName == "Extension"){
            addExtensionLabel(infoUpdated, controller.getRoof());
        }
        else if(wallName == "PinionLeft"){
            addPinionLeftLabel(infoUpdated, controller.getRoof());
        }
        else if(wallName == "PinionRight"){
            addPinionRightLabel(infoUpdated, controller.getRoof());
        }
        else if(wallName == "NotOnWall"){
            addCabinLabel(infoUpdated, controller.getCabin());
        }
        else{
            addLabel(infoUpdated, controller.getCurrentWall(), controller.getCabin());
        }

        currentInformationTab.add(infoUpdated);
        addView("currentPanel", currentInformationTab);

    }
    private void addLabel(Container container, WallDTO currentWall, CabinDTO currentCabin){
        container.add(new JLabel("Hauteur: " + currentWall.WallDimension.getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentWall.WallDimension.getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentCabin.WallThickness.toString()));
        container.add(new JLabel("Panneau: " + currentWall.wallType.toString()));
    }
    private void addCabinLabel(Container container, CabinDTO currentCabin){
        container.add(new JLabel("Hauteur: " + currentCabin.CabinDimension.getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentCabin.CabinDimension.getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentCabin.CabinDimension.getWidth().toString()));
        container.add(new JLabel("Panneau: " + "N/A"));

    }
    private void addPlankLabel(Container container, RoofDTO currentRoof){
        container.add(new JLabel("Hauteur: " + currentRoof.inclinedPlank.getDimension().getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentRoof.inclinedPlank.getDimension().getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentRoof.inclinedPlank.getDimension().getWidth().toString()));
        container.add(new JLabel("Panneau: " + "Planche Inclinée"));
    }
    private void addExtensionLabel(Container container, RoofDTO currentRoof){
        container.add(new JLabel("Hauteur: " + currentRoof.verticalExtension.getDimension().getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentRoof.verticalExtension.getDimension().getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentRoof.verticalExtension.getDimension().getWidth().toString()));
        container.add(new JLabel("Panneau: " + "Extension Verticale"));
    }
    private void addPinionLeftLabel(Container container, RoofDTO currentRoof){
        container.add(new JLabel("Hauteur: " + currentRoof.pinionLeft.getDimension().getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentRoof.pinionLeft.getDimension().getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentRoof.pinionLeft.getDimension().getWidth().toString()));
        container.add(new JLabel("Panneau: " + "Pinion Gauche"));
    }
    private void addPinionRightLabel(Container container, RoofDTO currentRoof){
        container.add(new JLabel("Hauteur: " + currentRoof.pinionRight.getDimension().getHeight().toString()));
        container.add(new JLabel("Longueur: "+ currentRoof.pinionRight.getDimension().getLength().toString()));
        container.add(new JLabel("Épaisseur: "+ currentRoof.pinionRight.getDimension().getWidth().toString()));
        container.add(new JLabel("Panneau: " + "Pinion Droite"));
    }
    private JPanel getInformationTab(){return informationTab;}
}
