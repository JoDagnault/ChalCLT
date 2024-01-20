package ca.ulaval.glo2004.gui;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class ActionTab extends JPanel {

    MainWindow mainWindow;
    private CardLayout cardLayout;
    private JPanel informationCard;
    private JRadioButton[] radioButtons;
    private boolean panneauSeulState = false;

    ActionTab(MainWindow p_mainwindow){
        mainWindow = p_mainwindow;
        setPreferredSize(new Dimension(Short.MAX_VALUE, 100));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        addInformationCard();
        addEditCabinBox();
        addUtilsBar();
        mainWindow.add(this, BorderLayout.NORTH);
    }
    private void addView(String name, JPanel view){
        informationCard.add(name, view);
    }
    private void InitView(){
        addView("infoChalet", addInformationBox());
    }
    public void showView(String name){
        cardLayout.show(informationCard, name);
    }
    private void addInformationCard(){
        informationCard = new JPanel();
        cardLayout = new CardLayout();
        informationCard.setLayout(cardLayout);
        informationCard.setPreferredSize(new Dimension(300,95));
        InitView();
        informationCard.add(addInformationBox(), BorderLayout.WEST);
        add(informationCard, BorderLayout.WEST);
    }

    private JPanel addInformationBox() {
        JPanel informationBox = new JPanel();
        informationBox.setBorder(new TitledBorder("Informations"));
        JPanel info = new JPanel(new GridLayout(4,1));
        info.add(new JLabel("Dimension du chalet : " + this.mainWindow.controller.getCabinDimension()));
        info.add(new JLabel("Vue selectionnée : " + this.mainWindow.getViewType()));
        info.add(new JLabel("Angle du toit : " + this.mainWindow.controller.getRoof().plankAngle));
        informationBox.add(info);

        add(informationBox, BorderLayout.WEST);
        return informationBox;
    }
    public void updateInformationBox(){
        JPanel currentInfoCabin = addInformationBox();
        addView("currentCabin", currentInfoCabin);
    }

    private void addEditCabinBox(){
        JPanel editCabinBox = new JPanel(new GridLayout(2,3,5,5));
        editCabinBox.setBorder(new TitledBorder("Actions"));

        JPanel vuePanel = new JPanel();

        vuePanel.add(new JLabel("Vue: "));

        MainWindow.ViewType[] viewTypeValues = {MainWindow.ViewType.FRONT, MainWindow.ViewType.BACK, MainWindow.ViewType.TOP, MainWindow.ViewType.RIGHT, MainWindow.ViewType.LEFT};

        ButtonGroup vueButtonGroup = new ButtonGroup();
        createRadioButton();

        for(int i = 0; i < radioButtons.length ; i++){
            final MainWindow.ViewType viewType = viewTypeValues[i];
            radioButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainWindow.setViewType(viewType);
                    mainWindow.drawingPanel.repaint();
                }
            });
            vueButtonGroup.add(radioButtons[i]);
            vuePanel.add(radioButtons[i]);
        }
        editCabinBox.add(vuePanel);
        JButton editCabinButton = new JButton("Modifier les dimensions du chalet");
        JButton editWindowButton = new JButton("Ajouter / Modifier une fenêtre");
        JButton editDoorButton = new JButton("Ajouter / Modifier une porte");
        JButton editRoofButton = new JButton("Modifier le toit");
        JButton resetCabin = new JButton("Nouveaux / Recommencer le chalet"); // Ajouter une  confirmation

        editCabinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getModificationTab().showView("wallModification");
                mainWindow.controller.resetSelectionStatus();
                mainWindow.drawingPanel.repaint();
            }
        });
        editWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getModificationTab().showView("windowModification");
                mainWindow.controller.resetSelectionStatus();
                mainWindow.drawingPanel.repaint();
            }
        });
        editDoorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getModificationTab().showView("doorModification");
                mainWindow.controller.resetSelectionStatus();
                mainWindow.drawingPanel.repaint();
            }
        });

        editRoofButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.getModificationTab().showView("roofModification");
                mainWindow.controller.resetSelectionStatus();
                mainWindow.drawingPanel.repaint();
            }
        });

        resetCabin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.controller.setAngle(15);
                mainWindow.controller.resetDimension();
                mainWindow.controller.resetAccessory();
                mainWindow.getModificationTab().showView("defaultPanel");
                mainWindow.getModificationTab().initView();
                mainWindow.setViewType(MainWindow.ViewType.FRONT);
                updateRadioButton(0);
                mainWindow.drawingPanel.repaint();
            }
        });

        editCabinBox.add(editCabinButton);
        editCabinBox.add(editWindowButton);
        editCabinBox.add(editDoorButton);
        editCabinBox.add(editRoofButton);
        editCabinBox.add(resetCabin);

        add(editCabinBox);
    }

    private void addUtilsBar(){
        JPanel utilsBox = new JPanel();
        utilsBox.setBorder(new TitledBorder("Utilitaire :"));

        // Bouton Undo
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    mainWindow.getController().undo();
                    mainWindow.drawingPanel.repaint();
            }
        });

        // Bouton Redo
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    mainWindow.getController().redo();
                    mainWindow.drawingPanel.repaint();

            }
        });
        JCheckBox panneauSeul = new JCheckBox("Panneau seul");
        panneauSeul.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(panneauSeulState){
                    panneauSeulState = false;
                    mainWindow.drawingPanel.repaint();
                }
                else{
                    panneauSeulState = true;
                    mainWindow.drawingPanel.repaint();
                }
            }
        });

        // Ajouter les boutons au panneau utilitaire
        utilsBox.add(undoButton);
        utilsBox.add(redoButton);
        utilsBox.add(panneauSeul);

        // Ajouter le panneau utilitaire au panneau principal
        add(utilsBox, BorderLayout.EAST);
    }
    private JPanel getInformationCard(){return informationCard;}
    private void createRadioButton(){
        final JRadioButton radioFront = new JRadioButton("Front", true);
        JRadioButton radioBack = new JRadioButton("Back", false);
        JRadioButton radioTop = new JRadioButton("Top", false);
        JRadioButton radioRight= new JRadioButton("Right", false);
        JRadioButton radioLeft = new JRadioButton("Left", false);
        radioButtons = new JRadioButton[]{radioFront, radioBack, radioTop, radioRight, radioLeft};
    }
    public void updateRadioButton(int view){
        for(int i =0; i < radioButtons.length; i++){
            if(i == view){
                radioButtons[i].setSelected(true);
            }
            else{
                radioButtons[i].setSelected(false);
            }
        }
    }
    public boolean getPanneauSeulState(){
        return panneauSeulState;
    }

}
