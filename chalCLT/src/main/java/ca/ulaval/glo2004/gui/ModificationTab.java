package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.DTO.DimensionCabinDTO;
import ca.ulaval.glo2004.domain.DTO.ImperialMeasureDTO;
import ca.ulaval.glo2004.domain.DTO.WallDTO;
import ca.ulaval.glo2004.domain.cabinComposition.Accessory;
import ca.ulaval.glo2004.domain.cabinComposition.AccessoryType;
import ca.ulaval.glo2004.domain.cabinComposition.OrientationType;
import ca.ulaval.glo2004.domain.cabinComposition.WallType;
import ca.ulaval.glo2004.domain.controller.CabinController;
import ca.ulaval.glo2004.domain.utils.DimensionCabin;
import ca.ulaval.glo2004.domain.utils.ImperialMeasure;
import ca.ulaval.glo2004.domain.utils.Point2D;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ModificationTab {
    MainWindow mainWindow;
    private final CabinController controller;
    private final DrawingPanel drawingPanel;
    private final JPanel modificationTab;
    private final CardLayout cardLayout;

    ModificationTab(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        controller = mainWindow.getController();
        drawingPanel = mainWindow.getDrawingPanel();
        modificationTab = new JPanel();
        modificationTab.setPreferredSize(new Dimension(250, 20));
        modificationTab.setBorder(LineBorder.createGrayLineBorder());
        cardLayout = new CardLayout();
        modificationTab.setLayout(cardLayout);
        initView();
        mainWindow.add(getModificationTab(), BorderLayout.WEST);
    }

    public void showView(String name) {
        cardLayout.show(modificationTab, name);
    }

    public JPanel getModificationTab() {
        return modificationTab;
    }

    public void initView() {
        addView("defaultPanel", createDefaultTabCard());
        addView("wallModification", createWallModificationCard());
        addView("doorModification", createDefaultAccessoryModificationCard("porte"));
        addView("windowModification", createDefaultAccessoryModificationCard("fenêtre"));
        addView("roofModification", createDefaultRoofAccessoryModificationCard());
    }

    private void addView(String name, JPanel view) {
        modificationTab.add(view, name);
    }

    private JPanel createWallModificationCard() {

        final JPanel wallModificationCard = new JPanel();

        wallModificationCard.add(new JLabel("Modification des dimensions des murs :"));

        wallModificationCard.add(imperialInputFields("Hauteur :", new ImperialMeasureDTO(controller.getCabinDimension().Height)));
        wallModificationCard.add(imperialInputFields("Longueur :", new ImperialMeasureDTO(controller.getCabinDimension().Length)));
        wallModificationCard.add(imperialInputFields("Largeur :", new ImperialMeasureDTO(controller.getCabinDimension().Width)));
        wallModificationCard.add(imperialInputFields("Épaisseur :", controller.getCabinThickness()));
        wallModificationCard.add(imperialInputFields("Distance supplémentaire :", controller.getErrorMargin()));
        wallModificationCard.add(imperialInputFields("Espacement Grille:", controller.getGridSpacing()));

        JButton submitWallEdit = new JButton("Valider");
        submitWallEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DimensionCabinDTO initialDimension = controller.getCabinDimension();
                updateCabinDimension(getValues(wallModificationCard, new ArrayList<Integer>()));
                updateAccessoryPosition(initialDimension);
                mainWindow.getInformationTab().updateInformationTab("");
                mainWindow.getInformationTab().showView("currentPanel");
                mainWindow.getActionTab().updateInformationBox();
                mainWindow.getActionTab().showView("currentCabin");

            }
        });
        wallModificationCard.add(submitWallEdit);

        return wallModificationCard;
    }

    private JPanel createDefaultAccessoryModificationCard(final String selectedAccessory) {
        final JPanel editAccessoryPanel = new JPanel();
        editAccessoryPanel.add(new JLabel("Création / Modification d'une " + selectedAccessory + " : "));
        if (selectedAccessory == "porte") {
            editAccessoryPanel.add(imperialInputFields("Hauteur", new ImperialMeasureDTO(new ImperialMeasure(7, 4))));
            editAccessoryPanel.add(imperialInputFields("Largeur", new ImperialMeasureDTO(new ImperialMeasure(3, 2))));
            editAccessoryPanel.add(imperialInputFields("Position Horizontale", new ImperialMeasureDTO(new ImperialMeasure(5, 0))));
            editAccessoryPanel.add(imperialInputFields("Position Verticale", this.controller.getMinAccessoryDistance()));
        } else {
            editAccessoryPanel.add(imperialInputFields("Hauteur", new ImperialMeasureDTO(new ImperialMeasure(2, 0))));
            editAccessoryPanel.add(imperialInputFields("Largeur", new ImperialMeasureDTO(new ImperialMeasure(2, 4))));
            editAccessoryPanel.add(imperialInputFields("Position Horizontale", new ImperialMeasureDTO(new ImperialMeasure(5, 0))));
            editAccessoryPanel.add(imperialInputFields("Position Verticale", new ImperialMeasureDTO(new ImperialMeasure(4, 0))));
        }
        AddSubmit(editAccessoryPanel, "Valider", selectedAccessory, editAccessoryPanel);
        AddDelete(editAccessoryPanel, "Supprimer");

        return editAccessoryPanel;
    }

    private JPanel createDefaultRoofAccessoryModificationCard(){
        final JPanel editRoofPanel = new JPanel();
        editRoofPanel.add(new JLabel("Entrez l'angle du toît désiré"));
        final JTextField angleInput = new JTextField(10);
        angleInput.setText(String.format("%.2f", controller.getRoof().plankAngle));
        editRoofPanel.add(angleInput);
        JButton submitAngle = new JButton("Confirmer angle");
        submitAngle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.performAction();
                controller.setAngle(Float.parseFloat(angleInput.getText()));
                controller.refreshDimension();
                drawingPanel.repaint();
                mainWindow.getActionTab().updateInformationBox();
                mainWindow.getActionTab().showView("currentCabin");

            }
        });
        editRoofPanel.add(submitAngle);

        // Ajouter un menu déroulant pour l'orientation du toit
        OrientationType[] orientations = {OrientationType.FRONT, OrientationType.BACK, OrientationType.LEFT, OrientationType.RIGHT};
        final JComboBox<OrientationType> orientationComboBox = new JComboBox<>(orientations);
        editRoofPanel.add(orientationComboBox);

        // Bouton pour confirmer l'angle et l'orientation
        JButton submitButton = new JButton("Confirmer");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrientationType selectedOrientation = (OrientationType) orientationComboBox.getSelectedItem();
                /*controller.getRoof().setOrientation(selectedOrientation);*/
            }
        });

        return editRoofPanel;
    }

    public void showCurrentModificationTab(final String selectedAccessory, DimensionCabinDTO dimension, Point2D position) {
        final JPanel currentTab = new JPanel();
        currentTab.add(new JLabel("Création / Modification d'une " + selectedAccessory + " : "));
        if (selectedAccessory == "porte") {
            currentTab.add(imperialInputFields("Hauteur", new ImperialMeasureDTO(dimension.Height)));
            currentTab.add(imperialInputFields("Largeur", new ImperialMeasureDTO(dimension.Length)));
            currentTab.add(imperialInputFields("Position Horizontale", new ImperialMeasureDTO(position.getXCoord())));
            currentTab.add(imperialInputFields("Position Verticale", new ImperialMeasureDTO(controller.getCabinDimension().Height.subtractMeasures(dimension.Height).subtractMeasures(position.getYCoord()))));
        } else {
            currentTab.add(imperialInputFields("Hauteur", new ImperialMeasureDTO(dimension.Height)));
            currentTab.add(imperialInputFields("Largeur", new ImperialMeasureDTO(dimension.Length)));
            currentTab.add(imperialInputFields("Position Horizontale", new ImperialMeasureDTO(position.getXCoord())));
            currentTab.add(imperialInputFields("Position Verticale", new ImperialMeasureDTO(controller.getCabinDimension().Height.subtractMeasures(dimension.Height).subtractMeasures(position.getYCoord()))));
        }
        AddSubmit(currentTab, "Valider", selectedAccessory, currentTab);
        AddDelete(currentTab, "Supprimer");

        if (selectedAccessory == "porte") {
            addView("currentDoor", currentTab);
        } else {
            addView("currentWindow", currentTab);
        }
    }

    // This is Spaggethi workaround but working
    private List<ImperialMeasure> getValues(JPanel panel, List<Integer> values) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                String value = textField.getText();
                values.add(Integer.parseInt(value));
            }
            if (component instanceof JPanel) {
                getValues((JPanel) component, values);
            }
        }
        List<ImperialMeasure> imperialMeasures = new ArrayList<>();
        for (int i = 0; i < values.size(); i += 4) {
            int feet = values.get(i);
            int inches = values.get(i + 1);
            int inchesNominator = values.get(i + 2);
            int inchesDenominator = values.get(i + 3);
            imperialMeasures.add(new ImperialMeasure(feet, inches, inchesNominator, inchesDenominator));
        }
        return imperialMeasures;
    }

    private void updateCabinDimension(List<ImperialMeasure> imperialMeasures) {
        System.out.println("UpdatingDimension");
        controller.performAction();
        controller.setCabinDimension(new DimensionCabin(imperialMeasures.get(1), imperialMeasures.get(2), imperialMeasures.get(0)));
        controller.setCabinThickness(imperialMeasures.get(3));
        controller.setErrorMargin(imperialMeasures.get(4));
        controller.setGridSpacing(imperialMeasures.get(5));
        drawingPanel.repaint();
    }

    private void updateDoor(List<ImperialMeasure> imperialMeasures) {
        ImperialMeasureDTO defaultWidth = controller.getCabinThickness();
        if (controller.getIDSelected() != -1) {
            controller.performAction();
            controller.editAccessory(new Point2D(imperialMeasures.get(2), controller.getCabinDimension().Height.subtractMeasures(imperialMeasures.get(0)).subtractMeasures(imperialMeasures.get(3))), new DimensionCabin(imperialMeasures.get(1), new ImperialMeasure(defaultWidth.Feet, defaultWidth.Inches), imperialMeasures.get(0)), controller.getIDSelected());
        } else {
            controller.addAccessory(new Point2D(imperialMeasures.get(2), controller.getCabinDimension().Height.subtractMeasures(imperialMeasures.get(0)).subtractMeasures(imperialMeasures.get(3))), imperialMeasures.get(1), imperialMeasures.get(0), AccessoryType.DOOR);
        }
        drawingPanel.repaint();
    }

    private void updateWindow(List<ImperialMeasure> imperialMeasures) {
        ImperialMeasureDTO defaultWidth = controller.getCabinThickness();
        if (controller.getIDSelected() != -1) {
            controller.performAction();
            controller.editAccessory(new Point2D(imperialMeasures.get(2), controller.getCabinDimension().Height.subtractMeasures(imperialMeasures.get(0)).subtractMeasures(imperialMeasures.get(3))), new DimensionCabin(imperialMeasures.get(1), new ImperialMeasure(defaultWidth.Feet, defaultWidth.Inches), imperialMeasures.get(0)), controller.getIDSelected());
        } else {
            controller.addAccessory(new Point2D(imperialMeasures.get(2), controller.getCabinDimension().Height.subtractMeasures(imperialMeasures.get(0)).subtractMeasures(imperialMeasures.get(3))), imperialMeasures.get(1), imperialMeasures.get(0), AccessoryType.WINDOW);
        }
        drawingPanel.repaint();
    }
    private void updateAccessoryPosition(DimensionCabinDTO dimension) {
        float ratioHeight =  controller.getCabinDimension().Height.convertImperialToFloat() / dimension.Height.convertImperialToFloat();
        float ratioLenght = controller.getCabinDimension().Length.convertImperialToFloat()/ dimension.Length.convertImperialToFloat();
        float ratioWidth = controller.getCabinDimension().Width.convertImperialToFloat() / dimension.Width.convertImperialToFloat() ;
        for (WallDTO wall : controller.getListWall()) {
            if(wall.wallType == WallType.FRONT || wall.wallType == WallType.BACK){
                for (Accessory accessory : wall.AccessoryList) {
                    if (accessory.getAccessoryType() == AccessoryType.DOOR) {
                        ImperialMeasure offSet = accessory.getHeight().addMeasuresCopy(mainWindow.controller.getCabin().Mindist);
                        float newPositionX = accessory.getXPosition().convertImperialToFloat()*ratioLenght;
                        accessory.editPosition(new Point2D(accessory.getXPosition().convertDoubleToImperial(newPositionX), controller.getCabinDimension().Height.subtractMeasures(offSet)));
                    } else {
                        float newPositionX = accessory.getXPosition().convertImperialToFloat()*ratioLenght;
                        float newPositionY = accessory.getYPosition().convertImperialToFloat()*ratioHeight;
                        accessory.editPosition(new Point2D(accessory.getXPosition().convertDoubleToImperial(newPositionX),accessory.getYPosition().convertDoubleToImperial(newPositionY)));
                    }
                }
            }
            else{
                for(Accessory accessory: wall.AccessoryList){
                    if(accessory.getAccessoryType() == AccessoryType.DOOR){
                        ImperialMeasure offSet = accessory.getHeight().addMeasuresCopy(mainWindow.controller.getCabin().Mindist);
                        float newPositionX = accessory.getXPosition().convertImperialToFloat() * ratioWidth;
                        accessory.editPosition(new Point2D(accessory.getXPosition().convertDoubleToImperial(newPositionX), controller.getCabinDimension().Height.subtractMeasures(offSet)));

                    }
                    else{
                        float newPositionX = accessory.getXPosition().convertImperialToFloat()*ratioWidth;
                        float newPositionY = accessory.getYPosition().convertImperialToFloat()*ratioHeight;
                        accessory.editPosition(new Point2D(accessory.getXPosition().convertDoubleToImperial(newPositionX), accessory.getYPosition().convertDoubleToImperial(newPositionY)));
                    }
                }
            }
        }
        controller.validateAccessories();
    }

    private JPanel imperialInputFields(String distanceName, ImperialMeasureDTO p_measure){
        JPanel imperialPanel = new JPanel();
        imperialPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        imperialPanel.setBorder(BorderFactory.createLineBorder(new Color(108,76,233), 1));

        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getInstance());
        numberFormatter.setValueClass(Integer.class);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        imperialPanel.add(new JLabel(distanceName), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        imperialPanel.add(new JLabel("Feet:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JFormattedTextField feetInput = new JFormattedTextField(numberFormatter);
        feetInput.setValue(p_measure.Feet);
        feetInput.setColumns(5);
        imperialPanel.add(feetInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        imperialPanel.add(new JLabel("Inches:"), gbc);

        gbc.gridx = 1;
        JFormattedTextField inchInput = new JFormattedTextField(numberFormatter);
        inchInput.setValue(p_measure.Inches);
        inchInput.setColumns(5);
        imperialPanel.add(inchInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span two columns
        gbc.anchor = GridBagConstraints.CENTER;
        imperialPanel.add(new JLabel("Optional :"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        imperialPanel.add(new JLabel("Inches Nominator:"), gbc);

        gbc.gridx = 1;
        JFormattedTextField inchNumInput = new JFormattedTextField(numberFormatter);
        inchNumInput.setValue(p_measure.InchesNum);
        inchNumInput.setColumns(5);
        imperialPanel.add(inchNumInput, gbc);;

        gbc.gridx = 0;
        gbc.gridy = 5;
        imperialPanel.add(new JLabel("Inches Denominator:"), gbc);

        gbc.gridx = 1;
        JFormattedTextField inchDenom = new JFormattedTextField(numberFormatter);
        inchDenom.setValue(p_measure.InchesDenom);
        inchDenom.setColumns(5);
        imperialPanel.add(inchDenom, gbc);
        return imperialPanel;
    }

    private JPanel createDefaultTabCard(){
        JPanel defaultTab = new JPanel();
        defaultTab.add(new JLabel("Veuillez selectionner un élément a modifier"));
        return defaultTab;
    }

    public JButton AddDelete(Container container, String text){
        JButton buttonDelete = new JButton(text);
        container.add(buttonDelete);
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.deleteAccessory((controller.getIDSelected()));
                drawingPanel.repaint();
            }
        });
        return buttonDelete;
    }
    public JButton AddSubmit(final Container container, String text, final String selectedAccessory, final JPanel currentTab) {
        JButton submitButton = new JButton(text);
        container.add(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedAccessory.equals("porte")) {
                    updateDoor(getValues(currentTab, new ArrayList<Integer>()));
                } else {
                    updateWindow(getValues(currentTab, new ArrayList<Integer>()));
                }
            }
        });
        return submitButton;
    }

    
}