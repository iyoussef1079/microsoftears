/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.controller.Controller;
import ca.ulaval.glo2004.domain.controller.DisplayMode;
import ca.ulaval.glo2004.domain.drawing.SvgDrawer;
import ca.ulaval.glo2004.domain.teardrop.*;
import ca.ulaval.glo2004.domain.controller.UserException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Utilisateur
 */
public class MainFrame extends javax.swing.JFrame {

    String firstTextField = "";
    String secondTextField = "";
    String thirdTextField = "";
    String fourthTextField = "";
    boolean isAfterDragged = false;
    public Controller controller = new Controller();
    public UndoRedoManager undoRedoManager = new UndoRedoManager();
    public Point2D.Float point = new Point2D.Float(27.0f, 27.0f);
    private Unit unit = Unit.INCHES;
    public IDrawable componentClicked;
    Hashtable<String, String> classToTreeComponant = new Hashtable<String, String>() {
        {
            put("Hatch", "Hayon");
            put("EllipseCurvedProfile", "Mur latéral");
            put("BezierCurvedProfile", "Mur latéral");
            put("Floor", "Plancher");
            put("Beam", "Poutre arrière");
            put("DividingWall", "Mur séparateur");
            put("SideOpening", "Ouverture latérale");
            put("Strut", "Ressort à gaz");
            put("Ceiling", "Toit");
        }
    };

    /**
     * Creates new form NewJFrame
     */
    public MainFrame() {
        initComponents();
    }

    public float getFloatFromTextField(String textField) {
        Pattern pattern = Pattern.compile("^([0-9]+-)?([0-9]+)?? ?([0-9]+/[0-9]+)?$");
        Matcher matcher = pattern.matcher(textField);
        if (matcher.matches()) {
            float rePieds;
            float rePouce;
            float reFrac;

            if (matcher.group(1) != null) {
                String reTiret = matcher.group(1);
                String tiretRemoved = reTiret.substring(0, reTiret.length() - 1);
                rePieds = Float.parseFloat(tiretRemoved);
            } else {
                rePieds = 0f;
            }

            if (matcher.group(2) != null) {
                rePouce = Float.parseFloat(matcher.group(2));
            } else {
                rePouce = 0f;
            }

            if (matcher.group(3) != null) {
                String[] fracSplit = matcher.group(3).split("/");
                reFrac = Float.parseFloat(fracSplit[0]) / Float.parseFloat(fracSplit[1]);
            } else {
                reFrac = 0f;
            }

            return (rePieds * 12) + rePouce + reFrac;
        } else {
            return Float.parseFloat(textField);
        }
    }

    public void updateTextfield() {
        textFieldPropertise1.setBackground(Color.WHITE);
        textFieldPropertise1.setForeground(Color.BLACK);
        textFieldPropertise2.setBackground(Color.WHITE);
        textFieldPropertise2.setForeground(Color.BLACK);
        textFieldThickness.setBackground(Color.WHITE);
        textFieldThickness.setForeground(Color.BLACK);
        textFieldRadiusCurve.setBackground(Color.WHITE);
        textFieldRadiusCurve.setForeground(Color.BLACK);
        textFieldPoids.setForeground(Color.BLACK);
        textFieldPoids.setBackground(Color.WHITE);

        String component = textFieldComposantName.getText();
        if (component.equals("Plancher")) {

            labelPropertise1.setText("Marge avant");
            labelPropertise2.setText("Marge arrière");
            labelThickness1.setText("Épaisseur");
            String back = this.controller.getTearDrop().getFloor().getBackmargin().toString(unit);
            String front = this.controller.getTearDrop().getFloor().getFrontmargin().toString(unit);
            String thick = this.controller.getTearDrop().getFloor().getThickness().toString(unit);
            textFieldPropertise1.setText(front);
            textFieldPropertise2.setText(back);
            textFieldThickness.setText(thick);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);

        } else if (component.equals("Mur latéral")) {
            labelPropertise1.setText("Hauteur");
            labelPropertise2.setText("Largeur");
            String h = this.controller.getTearDrop().getRawProfile().getHeight().toString(unit);
            String l = this.controller.getTearDrop().getRawProfile().getWidth().toString(unit);
            textFieldPropertise1.setText(h);
            textFieldPropertise2.setText(l);
            labelPropertise2.setVisible(true);
            textFieldPropertise2.setVisible(true);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(false);
            textFieldThickness.setVisible(false);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);

        } else if (component.equals("Hayon")) {
            labelPropertise1.setText("Distance poutre arrière");
            labelPropertise2.setText("Distance plancher");
            labelThickness1.setText("Épaisseur");
            labelRadiusCurve.setText("Rayon de courbure");
            labelPoids.setText("Poids");
            String d_beam = this.controller.getTearDrop().getHatch().distanceBeam.toString(unit);
            String d_floor = this.controller.getTearDrop().getHatch().distanceFloor.toString(unit);
            String curve_radius = this.controller.getTearDrop().getHatch().curveRadius.toString(unit);
            String thikness = this.controller.getTearDrop().getHatch().thickness.toString(unit);
            String poids = String.valueOf(this.controller.getTearDrop().getHatch().deadWeightPounds);
            textFieldPropertise1.setText(d_beam);
            textFieldPropertise2.setText(d_floor);
            textFieldThickness.setText(thikness);
            textFieldPoids.setText(poids);
            labelThickness1.setText("Épaisseur");
            textFieldRadiusCurve.setText(curve_radius);
            labelRadiusCurve.setVisible(true);
            textFieldRadiusCurve.setVisible(true);
            textFieldThickness.setVisible(true);
            labelThickness1.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(true);
            textFieldPoids.setVisible(true);
            labelPropertise2.setVisible(true);
            textFieldPropertise2.setVisible(true);

        } else if (component.equals("Poutre arrière")) {
            labelPropertise1.setText("Hauteur");
            labelPropertise2.setText("Largeur");
            labelThickness1.setText("Position X");
            String h = this.controller.getTearDrop().getBeam().getHeight().toString(unit);
            String l = this.controller.getTearDrop().getBeam().getWidth().toString(unit);
            String x = this.controller.getTearDrop().getBeam().getTopLeftPosition().x.toString(unit);
            textFieldPropertise1.setText(h); // à update avec le beam
            textFieldPropertise2.setText(l);
            textFieldThickness.setText(x);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            labelPropertise2.setVisible(true);
            textFieldPropertise2.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);
        } else if (component.equals("Mur séparateur")) {
            labelPropertise1.setText("Distance poutre");
            labelPropertise2.setText("Distance plancher");
            labelThickness1.setText("Épaisseur");
            String h = this.controller.getTearDrop().getDividingWall().getThickness().toString(unit);
            String l = this.controller.getTearDrop().getDividingWall().getDistanceFloor().toString(unit);
            String e = this.controller.getTearDrop().getDividingWall().getDistanceBeam().toString(unit);
            textFieldPropertise1.setText(e);
            textFieldPropertise2.setText(l);
            textFieldThickness.setText(h);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);
        } else if (component.equals("Ouverture latérale")) {
            labelPropertise1.setText("Hauteur");
            labelPropertise2.setText("Largeur");
            labelThickness1.setText("Rayon du coin");
            if(componentClicked.equals(controller.getTearDrop().getDoor())){
                String h = this.controller.getTearDrop().getDoor().getHeight().toString(unit);
                String l = this.controller.getTearDrop().getDoor().getWidth().toString(unit);
                String r = this.controller.getTearDrop().getDoor().getRadius().toString(unit);
                textFieldPropertise1.setText(h);
                textFieldPropertise2.setText(l);
                textFieldThickness.setText(r);
            }
            else if(componentClicked.equals(controller.getTearDrop().getWindow())){
                String h = this.controller.getTearDrop().getWindow().getHeight().toString(unit);
                String l = this.controller.getTearDrop().getWindow().getWidth().toString(unit);
                String r = this.controller.getTearDrop().getWindow().getRadius().toString(unit);
                textFieldPropertise1.setText(h);
                textFieldPropertise2.setText(l);
                textFieldThickness.setText(r);
            }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                String h = this.controller.getTearDrop().getWindow1().getHeight().toString(unit);
                String l = this.controller.getTearDrop().getWindow1().getWidth().toString(unit);
                String r = this.controller.getTearDrop().getWindow1().getRadius().toString(unit);
                textFieldPropertise1.setText(h);
                textFieldPropertise2.setText(l);
                textFieldThickness.setText(r);
            }
            
            
            labelPropertise2.setVisible(true);
            textFieldPropertise2.setVisible(true);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);
        } else if (component.equals("Toit")) {
            labelPropertise1.setText("Épaisseur");
            String e = this.controller.getTearDrop().getCeiling().getThickness().toString(unit);
            textFieldPropertise1.setText(e);
            labelPropertise2.setVisible(false);
            textFieldPropertise2.setVisible(false);
            labelRadiusCurve.setVisible(false);
            textFieldRadiusCurve.setVisible(false);
            labelThickness1.setVisible(false);
            textFieldThickness.setVisible(false);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);

        } else if (component.equals("Ressort à gaz")) {
            labelPropertise1.setText("Numéro de pièce");
            labelPropertise2.setText("Forces d'extension requise (lbs)");
            labelRadiusCurve.setText("Longueur en extension");
            labelThickness1.setText("Force d'extension utilisée (lbs)");
            labelPoids.setText("Longueur prolongement");
            labelRadiusCurve.setVisible(true);
            textFieldRadiusCurve.setVisible(true);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            labelPoids.setVisible(true);
            textFieldPoids.setVisible(true);
            ellipseConfirmeButton.setVisible(false);
            ellipseCancelButton.setVisible(false);
            String num = this.controller.getTearDrop().getStrut().getId();
            String forceRequise = String.valueOf(this.controller.getTearDrop().getStrut().getRequiredExtensionForcePounds());
            String ForceUtilisee = String.valueOf(this.controller.getTearDrop().getStrut().getActualExtensionForcePounds());
            String longExtension = this.controller.getTearDrop().getStrut().getExtendedLength().toString(unit);
            String longProlong = this.controller.getTearDrop().getStrut().getStrokeLength().toString(unit);
            textFieldPropertise1.setText(num);
            textFieldPropertise2.setText(forceRequise);
            textFieldThickness.setText(ForceUtilisee);
            textFieldRadiusCurve.setText(longExtension);
            textFieldPoids.setText(longProlong);

        } else if (component.equals("Coin supérieur gauche") || component.equals("Coin supérieur droit") || component.equals("Coin inférieur gauche") || component.equals("Coin inférieur droit")) {
            labelPropertise1.setText("Position X");
            labelPropertise2.setText("Position Y");
            labelThickness1.setText("Rayon horizontal");
            labelRadiusCurve.setText("Rayon vertical");
            labelPropertise2.setVisible(true);
            textFieldPropertise2.setVisible(true);
            labelRadiusCurve.setVisible(true);
            textFieldRadiusCurve.setVisible(true);
            labelThickness1.setVisible(true);
            textFieldThickness.setVisible(true);
            ellipseConfirmeButton.setVisible(true);
            ellipseCancelButton.setVisible(true);
            labelPoids.setVisible(false);
            textFieldPoids.setVisible(false);
            EllipseCurvedProfile profile = (EllipseCurvedProfile) this.controller.getTearDrop().getCurvedProfile();
            Map<Quadrant, EllipseArc> ellipsemap = profile.getEllispeMap();
            if (component.equals("Coin supérieur gauche")) {
                String x = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_LEFT).getCenter().getX().toString(unit);
                String y = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_LEFT).getCenter().getY().toString(unit);
                String h = profile.getEllispeMap().get(Quadrant.TOP_LEFT).getHorizontalRadius().toString(unit);
                String w = profile.getEllispeMap().get(Quadrant.TOP_LEFT).getVerticalRadius().toString(unit);
                textFieldPropertise1.setText(x);
                textFieldPropertise2.setText(y);
                textFieldThickness.setText(h);
                textFieldRadiusCurve.setText(w);
            } else if (component.equals("Coin supérieur droit")) {
                String x = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).getCenter().getX().toString(unit);
                String y = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).getCenter().getY().toString(unit);
                String h = profile.getEllispeMap().get(Quadrant.TOP_RIGHT).getHorizontalRadius().toString(unit);
                String w = profile.getEllispeMap().get(Quadrant.TOP_RIGHT).getVerticalRadius().toString(unit);
                textFieldPropertise1.setText(x);
                textFieldPropertise2.setText(y);
                textFieldThickness.setText(h);
                textFieldRadiusCurve.setText(w);
            } else if (component.equals("Coin inférieur gauche")) {
                String x = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).getCenter().getX().toString(unit);
                String y = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).getCenter().getY().toString(unit);
                String h = profile.getEllispeMap().get(Quadrant.BOTTOM_LEFT).getHorizontalRadius().toString(unit);
                String w = profile.getEllispeMap().get(Quadrant.BOTTOM_LEFT).getVerticalRadius().toString(unit);
                textFieldPropertise1.setText(x);
                textFieldPropertise2.setText(y);
                textFieldThickness.setText(h);
                textFieldRadiusCurve.setText(w);
            } else if (component.equals("Coin inférieur droit")) {
                String x = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).getCenter().getX().toString(unit);
                String y = this.controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).getCenter().getY().toString(unit);
                String h = profile.getEllispeMap().get(Quadrant.BOTTOM_RIGHT).getHorizontalRadius().toString(unit);
                String w = profile.getEllispeMap().get(Quadrant.BOTTOM_RIGHT).getVerticalRadius().toString(unit);
                textFieldPropertise1.setText(x);
                textFieldPropertise2.setText(y);
                textFieldThickness.setText(h);
                textFieldRadiusCurve.setText(w);
            }

        }
        Point2D.Double firstPoint = this.controller.relativeToAbsolute(point.x, point.y);
        if (radioButtonMillimètre.isSelected()) {
            textFieldAssistanceGridSpacing.setText(String.valueOf(Math.round((double) 100.0 * (firstPoint.x - this.controller.decalageX) * 25.4 / 3.8) / 100.0));
        } else {
            textFieldAssistanceGridSpacing.setText(String.valueOf(Math.round((double) 100.0 * (firstPoint.x - this.controller.decalageX) / 3.8) / 100.0));
        }
    }

    public void updateValidation() {
        ArrayList<String> validations = this.controller.validateTearDrop();
        textFieldErrors.setText(null);
        textFieldErrors.setForeground(Color.red);
        for (String validation : validations) {
            if (!(validation == null)) {
                textFieldErrors.append(validation + "\n");
            }
        }
        try {
            undoRedoManager.addCommand((TearDropTrailer) controller.getTearDrop().clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        bottomPannel = new javax.swing.JPanel();
        preferencesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelUnits = new javax.swing.JLabel();
        radioButtonInches = new javax.swing.JRadioButton();
        radioButtonMillimètre = new javax.swing.JRadioButton();
        checkBoxPositionningAssistance = new javax.swing.JCheckBox();
        labelAssistanceGridSpacing = new javax.swing.JLabel();
        textFieldAssistanceGridSpacing = new javax.swing.JTextField();
        scalePanel = new javax.swing.JPanel();
        labelScales = new javax.swing.JLabel();
        labelScaleMicroTrailer = new javax.swing.JLabel();
        textFieldScaleMicroTrailer = new javax.swing.JTextField();
        errorsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textFieldErrors = new javax.swing.JTextArea();
        labelError = new javax.swing.JLabel();
        propertiesPanel = new javax.swing.JPanel();
        labelProperties = new javax.swing.JLabel();
        labelComposantName = new javax.swing.JLabel();
        textFieldComposantName = new javax.swing.JTextField();
        labelPropertise1 = new javax.swing.JLabel();
        textFieldPropertise1 = new javax.swing.JTextField();
        labelPropertise2 = new javax.swing.JLabel();
        textFieldPropertise2 = new javax.swing.JTextField();
        textFieldThickness = new javax.swing.JTextField();
        labelThickness1 = new javax.swing.JLabel();
        textFieldRadiusCurve = new javax.swing.JTextField();
        ellipseCancelButton = new javax.swing.JButton();
        ellipseConfirmeButton = new javax.swing.JButton();
        labelPoids = new javax.swing.JLabel();
        labelRadiusCurve = new javax.swing.JLabel();
        textFieldPoids = new javax.swing.JTextField();
        componentsPanel = new javax.swing.JPanel();
        labelComponants = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Composants");
        DefaultMutableTreeNode parent = new DefaultMutableTreeNode("Mur latéral");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("Coin supérieur gauche"));
        parent.add(new DefaultMutableTreeNode("Coin supérieur droit"));
        parent.add(new DefaultMutableTreeNode("Coin inférieur gauche"));
        parent.add(new DefaultMutableTreeNode("Coin inférieur droit"));
        parent = new DefaultMutableTreeNode("Hayon");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Poutre arrière");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Plancher");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Ouverture latérale");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Mur séparateur");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Ressort à gaz");
        root.add(parent);
        parent = new DefaultMutableTreeNode("Toit");
        root.add(parent);
        treeComponants = new javax.swing.JTree(root);
        drawingPanel = new ca.ulaval.glo2004.gui.DrawingPanel(this);
        buttonTopPanel = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        EllipseProfileNew = new javax.swing.JMenuItem();
        bezierProfileNew = new javax.swing.JMenuItem();
        menuFileOpen = new javax.swing.JMenuItem();
        menuFileSave = new javax.swing.JMenuItem();
        menuFileExportSVG = new javax.swing.JMenuItem();
        menuFileExportImage = new javax.swing.JMenuItem();
        menuFileQuit = new javax.swing.JMenuItem();
        addGuideRectbutton = new javax.swing.JMenu();
        menuEditRestore = new javax.swing.JMenuItem();
        menuEditCancel = new javax.swing.JMenuItem();
        addControlPointButton = new javax.swing.JMenuItem();
        addWindowButton = new javax.swing.JMenuItem();
        displayMenu = new javax.swing.JMenu();
        rawWallDisplay = new javax.swing.JCheckBoxMenuItem();
        profilDisplayButton = new javax.swing.JCheckBoxMenuItem();
        floorDisplay = new javax.swing.JCheckBoxMenuItem();
        hayonDisplayButton = new javax.swing.JCheckBoxMenuItem();
        beamDisplayButton = new javax.swing.JCheckBoxMenuItem();
        controlPointButton = new javax.swing.JCheckBoxMenuItem();
        ceilingDisplayButton = new javax.swing.JCheckBoxMenuItem();
        doorDisplay = new javax.swing.JCheckBoxMenuItem();
        strutDisplayButton = new javax.swing.JCheckBoxMenuItem();
        innerLayerDisplayButton = new javax.swing.JCheckBoxMenuItem();
        outerLayerDisplayButton = new javax.swing.JCheckBoxMenuItem();
        dividingWallDisplayButton = new javax.swing.JCheckBoxMenuItem();
        windowDisplay = new javax.swing.JCheckBoxMenuItem();
        guideRectangleDisplay = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        mainPanel.setLayout(new java.awt.BorderLayout());

        bottomPannel.setPreferredSize(new java.awt.Dimension(400, 180));
        bottomPannel.setLayout(new java.awt.BorderLayout());

        preferencesPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        preferencesPanel.setPreferredSize(new java.awt.Dimension(200, 180));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Préférences");

        labelUnits.setText("Unités:");

        radioButtonInches.setText("Pouces");
        radioButtonInches.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonInchesItemStateChanged(evt);
            }
        });
        radioButtonInches.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioButtonInchesMouseClicked(evt);
            }
        });

        radioButtonMillimètre.setText("Millimètre");
        radioButtonMillimètre.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonMillimètreItemStateChanged(evt);
            }
        });
        radioButtonMillimètre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioButtonMillimètreMouseClicked(evt);
            }
        });

        checkBoxPositionningAssistance.setText("Grille d'aide de position:");
        checkBoxPositionningAssistance.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        checkBoxPositionningAssistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxPositionningAssistanceActionPerformed(evt);
            }
        });

        labelAssistanceGridSpacing.setText("Espace de la grille d'aide:");

        textFieldAssistanceGridSpacing.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldAssistanceGridSpacingKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout preferencesPanelLayout = new javax.swing.GroupLayout(preferencesPanel);
        preferencesPanel.setLayout(preferencesPanelLayout);
        preferencesPanelLayout.setHorizontalGroup(
            preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldAssistanceGridSpacing)
                    .addGroup(preferencesPanelLayout.createSequentialGroup()
                        .addGroup(preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelUnits)
                            .addComponent(jLabel1)
                            .addGroup(preferencesPanelLayout.createSequentialGroup()
                                .addComponent(radioButtonMillimètre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radioButtonInches))
                            .addComponent(labelAssistanceGridSpacing)
                            .addComponent(checkBoxPositionningAssistance))
                        .addGap(0, 31, Short.MAX_VALUE)))
                .addContainerGap())
        );
        preferencesPanelLayout.setVerticalGroup(
            preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelUnits)
                .addGap(3, 3, 3)
                .addGroup(preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioButtonMillimètre)
                    .addComponent(radioButtonInches))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBoxPositionningAssistance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAssistanceGridSpacing)
                .addGap(6, 6, 6)
                .addComponent(textFieldAssistanceGridSpacing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        radioButtonInches.setSelected(true);
        radioButtonMillimètre.setSelected(false); //First selected
        // Group the radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMillimètre);
        buttonGroup.add(radioButtonInches);

        bottomPannel.add(preferencesPanel, java.awt.BorderLayout.EAST);

        scalePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scalePanel.setPreferredSize(new java.awt.Dimension(200, 180));

        labelScales.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelScales.setText("Échelles");

        labelScaleMicroTrailer.setText("Échelle de la micro-roulotte:");

        textFieldScaleMicroTrailer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldScaleMicroTrailerKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout scalePanelLayout = new javax.swing.GroupLayout(scalePanel);
        scalePanel.setLayout(scalePanelLayout);
        scalePanelLayout.setHorizontalGroup(
            scalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scalePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldScaleMicroTrailer)
                    .addGroup(scalePanelLayout.createSequentialGroup()
                        .addGroup(scalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelScales)
                            .addComponent(labelScaleMicroTrailer))
                        .addGap(0, 34, Short.MAX_VALUE)))
                .addContainerGap())
        );
        scalePanelLayout.setVerticalGroup(
            scalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scalePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelScales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelScaleMicroTrailer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldScaleMicroTrailer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        labelScales.setVisible(false);
        labelScaleMicroTrailer.setVisible(false);
        textFieldScaleMicroTrailer.setVisible(false);

        bottomPannel.add(scalePanel, java.awt.BorderLayout.WEST);

        errorsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textFieldErrors.setColumns(20);
        textFieldErrors.setRows(5);
        jScrollPane1.setViewportView(textFieldErrors);
        textFieldErrors.setFocusable(false);

        labelError.setText("Erreurs");

        javax.swing.GroupLayout errorsPanelLayout = new javax.swing.GroupLayout(errorsPanel);
        errorsPanel.setLayout(errorsPanelLayout);
        errorsPanelLayout.setHorizontalGroup(
            errorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
            .addGroup(errorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelError, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        errorsPanelLayout.setVerticalGroup(
            errorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, errorsPanelLayout.createSequentialGroup()
                .addComponent(labelError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bottomPannel.add(errorsPanel, java.awt.BorderLayout.CENTER);

        mainPanel.add(bottomPannel, java.awt.BorderLayout.SOUTH);

        propertiesPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        propertiesPanel.setPreferredSize(new java.awt.Dimension(200, 206));

        labelProperties.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelProperties.setText("Propriétés");

        labelComposantName.setText("Nom du composant:");

        textFieldComposantName.setText("Selectionnez un composant...");
        textFieldComposantName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldComposantNameActionPerformed(evt);
            }
        });

        labelPropertise1.setText("Hauteur");

        textFieldPropertise1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldPropertise1FocusLost(evt);
            }
        });
        textFieldPropertise1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldPropertise1KeyPressed(evt);
            }
        });

        labelPropertise2.setText("Largeur");

        textFieldPropertise2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldPropertise2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldPropertise2FocusLost(evt);
            }
        });
        textFieldPropertise2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldPropertise2KeyPressed(evt);
            }
        });

        textFieldThickness.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldThicknessFocusLost(evt);
            }
        });
        textFieldThickness.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldThicknessKeyPressed(evt);
            }
        });

        labelThickness1.setText("Épaisseur");

        textFieldRadiusCurve.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldRadiusCurveFocusLost(evt);
            }
        });
        textFieldRadiusCurve.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldRadiusCurveKeyPressed(evt);
            }
        });

        ellipseCancelButton.setText("Annuler");
        ellipseCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ellipseCancelButtonActionPerformed(evt);
            }
        });

        ellipseConfirmeButton.setText("Confirmer");
        ellipseConfirmeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ellipseConfirmeButtonActionPerformed(evt);
            }
        });

        labelPoids.setText("Poids");

        labelRadiusCurve.setText("Rayon  de courbure");

        textFieldPoids.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldPoidsFocusLost(evt);
            }
        });
        textFieldPoids.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldPoidsKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout propertiesPanelLayout = new javax.swing.GroupLayout(propertiesPanel);
        propertiesPanel.setLayout(propertiesPanelLayout);
        propertiesPanelLayout.setHorizontalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelThickness1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textFieldComposantName, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .addComponent(textFieldPropertise1)
                    .addComponent(textFieldPropertise2)
                    .addComponent(textFieldThickness, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textFieldRadiusCurve)
                    .addGroup(propertiesPanelLayout.createSequentialGroup()
                        .addComponent(ellipseCancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ellipseConfirmeButton))
                    .addGroup(propertiesPanelLayout.createSequentialGroup()
                        .addGroup(propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProperties)
                            .addComponent(labelComposantName)
                            .addComponent(labelPropertise1)
                            .addComponent(labelPropertise2)
                            .addComponent(labelRadiusCurve, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPoids, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(textFieldPoids))
                .addContainerGap())
        );
        propertiesPanelLayout.setVerticalGroup(
            propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertiesPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(labelProperties)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelComposantName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldComposantName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPropertise1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldPropertise1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPropertise2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldPropertise2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelThickness1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldThickness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(labelRadiusCurve, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldRadiusCurve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelPoids, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldPoids, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(propertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ellipseCancelButton)
                    .addComponent(ellipseConfirmeButton))
                .addGap(32, 32, 32))
        );

        textFieldComposantName.setFocusable(false);
        ellipseCancelButton.setVisible(false);
        ellipseConfirmeButton.setVisible(false);

        mainPanel.add(propertiesPanel, java.awt.BorderLayout.LINE_END);

        componentsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        componentsPanel.setPreferredSize(new java.awt.Dimension(200, 206));

        labelComponants.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelComponants.setText("Composants");

        treeComponants.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeComponantsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(treeComponants);

        javax.swing.GroupLayout componentsPanelLayout = new javax.swing.GroupLayout(componentsPanel);
        componentsPanel.setLayout(componentsPanelLayout);
        componentsPanelLayout.setHorizontalGroup(
            componentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(componentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(componentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(componentsPanelLayout.createSequentialGroup()
                        .addComponent(labelComponants)
                        .addGap(0, 96, Short.MAX_VALUE)))
                .addContainerGap())
        );
        componentsPanelLayout.setVerticalGroup(
            componentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(componentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelComponants)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(componentsPanel, java.awt.BorderLayout.LINE_START);

        drawingPanel.setBackground(new java.awt.Color(255, 255, 255));
        drawingPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                drawingPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                drawingPanelMouseMoved(evt);
            }
        });
        drawingPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                drawingPanelMouseWheelMoved(evt);
            }
        });
        drawingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawingPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                drawingPanelMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                drawingPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                drawingPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout drawingPanelLayout = new javax.swing.GroupLayout(drawingPanel);
        drawingPanel.setLayout(drawingPanelLayout);
        drawingPanelLayout.setHorizontalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );
        drawingPanelLayout.setVerticalGroup(
            drawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        mainPanel.add(drawingPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("Fichier");

        EllipseProfileNew.setText("Nouveau profil en ellipse");
        EllipseProfileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EllipseProfileNewActionPerformed(evt);
            }
        });
        fileMenu.add(EllipseProfileNew);

        bezierProfileNew.setText("Nouveau profil de bézier");
        bezierProfileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bezierProfileNewActionPerformed(evt);
            }
        });
        fileMenu.add(bezierProfileNew);

        menuFileOpen.setText("Ouvrir");
        menuFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileOpenActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileOpen);

        menuFileSave.setText("Sauvegarder");
        menuFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileSaveActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileSave);

        menuFileExportSVG.setText("Exporter en SVG");
        menuFileExportSVG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExportSVGActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileExportSVG);

        menuFileExportImage.setText("Exporter en Image");
        menuFileExportImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileExportImageActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileExportImage);

        menuFileQuit.setText("Quitter");
        menuFileQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFileQuitActionPerformed(evt);
            }
        });
        fileMenu.add(menuFileQuit);

        buttonTopPanel.add(fileMenu);

        addGuideRectbutton.setText("Éditer");
        addGuideRectbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGuideRectbuttonActionPerformed(evt);
            }
        });

        menuEditRestore.setText("Rétablir");
        menuEditRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditRestoreActionPerformed(evt);
            }
        });
        addGuideRectbutton.add(menuEditRestore);

        menuEditCancel.setText("Annuler");
        menuEditCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditCancelActionPerformed(evt);
            }
        });
        addGuideRectbutton.add(menuEditCancel);

        addControlPointButton.setText("Ajouter Point de contrôle");
        addControlPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addControlPointButtonActionPerformed(evt);
            }
        });
        addGuideRectbutton.add(addControlPointButton);

        addWindowButton.setText("Ajouter Fenêtre");
        addWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWindowButtonActionPerformed(evt);
            }
        });
        addGuideRectbutton.add(addWindowButton);

        buttonTopPanel.add(addGuideRectbutton);

        displayMenu.setText("Affichage");

        rawWallDisplay.setSelected(false);
        rawWallDisplay.setText("Profil brute");
        rawWallDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rawWallDisplayItemStateChanged(evt);
            }
        });
        displayMenu.add(rawWallDisplay);

        profilDisplayButton.setSelected(true);
        profilDisplayButton.setText("Profil");
        profilDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                profilDisplayButtonItemStateChanged(evt);
            }
        });
        profilDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilDisplayButtonActionPerformed(evt);
            }
        });
        displayMenu.add(profilDisplayButton);

        floorDisplay.setSelected(true);
        floorDisplay.setText("Plancher");
        floorDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                floorDisplayItemStateChanged(evt);
            }
        });
        displayMenu.add(floorDisplay);

        hayonDisplayButton.setSelected(true);
        hayonDisplayButton.setText("Hayon");
        hayonDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                hayonDisplayButtonItemStateChanged(evt);
            }
        });
        hayonDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hayonDisplayButtonActionPerformed(evt);
            }
        });
        displayMenu.add(hayonDisplayButton);

        beamDisplayButton.setSelected(true);
        beamDisplayButton.setText("Poutre arrière");
        beamDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                beamDisplayButtonItemStateChanged(evt);
            }
        });
        beamDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beamDisplayButtonActionPerformed(evt);
            }
        });
        displayMenu.add(beamDisplayButton);

        controlPointButton.setSelected(true);
        controlPointButton.setText("Points de contrôle");
        controlPointButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                controlPointButtonItemStateChanged(evt);
            }
        });
        controlPointButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                controlPointButtonActionPerformed(evt);
            }
        });
        displayMenu.add(controlPointButton);

        ceilingDisplayButton.setSelected(true);
        ceilingDisplayButton.setText("Toit");
        ceilingDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ceilingDisplayButtonItemStateChanged(evt);
            }
        });
        displayMenu.add(ceilingDisplayButton);

        doorDisplay.setSelected(true);
        doorDisplay.setText("Porte");
        doorDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                doorDisplayItemStateChanged(evt);
            }
        });
        displayMenu.add(doorDisplay);

        strutDisplayButton.setSelected(true);
        strutDisplayButton.setText("Ressorts à gaz");
        strutDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                strutDisplayButtonItemStateChanged(evt);
            }
        });
        strutDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                strutDisplayButtonActionPerformed(evt);
            }
        });
        displayMenu.add(strutDisplayButton);

        innerLayerDisplayButton.setSelected(true);
        innerLayerDisplayButton.setText("Mur interne");
        innerLayerDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                innerLayerDisplayButtonItemStateChanged(evt);
            }
        });
        innerLayerDisplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                innerLayerDisplayButtonActionPerformed(evt);
            }
        });
        displayMenu.add(innerLayerDisplayButton);

        outerLayerDisplayButton.setSelected(true);
        outerLayerDisplayButton.setText("Mur externe");
        outerLayerDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                outerLayerDisplayButtonItemStateChanged(evt);
            }
        });
        displayMenu.add(outerLayerDisplayButton);

        dividingWallDisplayButton.setSelected(true);
        dividingWallDisplayButton.setText("Mur séparateur");
        dividingWallDisplayButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dividingWallDisplayButtonItemStateChanged(evt);
            }
        });
        displayMenu.add(dividingWallDisplayButton);

        windowDisplay.setSelected(true);
        windowDisplay.setText("Fenêtre");
        windowDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                windowDisplayItemStateChanged(evt);
            }
        });
        displayMenu.add(windowDisplay);

        guideRectangleDisplay.setSelected(false);
        guideRectangleDisplay.setText("Rectangle guide");
        guideRectangleDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                guideRectangleDisplayItemStateChanged(evt);
            }
        });
        guideRectangleDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guideRectangleDisplayActionPerformed(evt);
            }
        });
        displayMenu.add(guideRectangleDisplay);

        buttonTopPanel.add(displayMenu);

        setJMenuBar(buttonTopPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuFileQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuFileQuitActionPerformed

    private void checkBoxPositionningAssistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxPositionningAssistanceActionPerformed
        repaint();
    }//GEN-LAST:event_checkBoxPositionningAssistanceActionPerformed

    private void treeComponantsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeComponantsMouseClicked
        // TODO add your handling code here:
        try {
            Object tp = treeComponants.getPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
            if (tp != null) {
                if (!"Composants".equals(tp.toString())) {
                    String clickedComponant = null;
                    for (Map.Entry entry : classToTreeComponant.entrySet()) {
                        if (tp.toString().equals(entry.getValue())) {
                            clickedComponant = entry.getKey().toString();
                            componantClickedRepaint(clickedComponant);
                        }
                    }
                    textFieldComposantName.setText(tp.toString());
                    updateTextfield();
                }
            } else {
                textFieldComposantName.setText("");
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_treeComponantsMouseClicked

    private void radioButtonInchesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioButtonInchesMouseClicked

    }//GEN-LAST:event_radioButtonInchesMouseClicked

    private void radioButtonMillimètreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_radioButtonMillimètreMouseClicked

    }//GEN-LAST:event_radioButtonMillimètreMouseClicked

    private void drawingPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMouseMoved
        this.controller.sourisX = (evt.getX());
        this.controller.sourisY = (evt.getY());
        Point2D.Double relativePoint = this.controller.relativeToAbsolute(this.controller.sourisX, this.controller.sourisY);
        Measurement xReel = new Measurement((double) (relativePoint.x * 25.4 / (3.8))); // TODO patch la sélection d'élément
        Measurement yReel = new Measurement((double) (relativePoint.y * 25.4 / (3.8)));
        PointMeasurement realPoint = new PointMeasurement(xReel, yReel);
        try {
            Measurement distanceEntreDebutLigneEtPoint = GeometricUtils.getDistance(this.controller.getTearDrop().getStrut().getSegment().getStartPoint(), realPoint);
            Measurement distanceEntreFinLigneEtPoint = GeometricUtils.getDistance(this.controller.getTearDrop().getStrut().getSegment().getEndPoint(), realPoint);
            Measurement distanceEntreDebutEtFinLigne = GeometricUtils.getDistance(this.controller.getTearDrop().getStrut().getSegment().getStartPoint(), this.controller.getTearDrop().getStrut().getSegment().getEndPoint());
            Measurement distanceMaximaleNegative = new Measurement(-3.0d);
            Measurement distanceMaximalePositive = new Measurement(3.0d);
            if(distanceEntreDebutLigneEtPoint.add(distanceEntreFinLigneEtPoint).sub(distanceEntreDebutEtFinLigne).compareTo(distanceMaximaleNegative) == 1 && 
               distanceEntreDebutLigneEtPoint.add(distanceEntreFinLigneEtPoint).sub(distanceEntreDebutEtFinLigne).compareTo(distanceMaximalePositive) == -1 ){
                this.drawingPanel.setToolTipText("<html>" + "Point attaché au hayon => x :" + this.controller.getTearDrop().getStrut().getHatchAttachmentPosition().x.toString(unit) + " et y :" +
                                                  this.controller.getTearDrop().getStrut().getHatchAttachmentPosition().y.toString(unit) + "<br>" +
                                                 "Point attaché au mur séparateur => x :" + this.controller.getTearDrop().getStrut().getSideWallAttachmentPosition().x.toString(unit) + " et y :" +
                                                  this.controller.getTearDrop().getStrut().getSideWallAttachmentPosition().y.toString(unit) + "</html>");
            } else {
                this.drawingPanel.setToolTipText(null);
            }
        } catch (Exception e) {
            //System.out.println("Eh ben c'est la vie... quand ça chie ça chie... voir drawingPanelMouseMoved dans MainFrame caliss");
        }
    }//GEN-LAST:event_drawingPanelMouseMoved

    private void drawingPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMousePressed
        this.controller.sourisStartPanX = (float) evt.getX();
        this.controller.sourisStartPanY = (float) evt.getY();
        this.controller.typeSouris = evt.getButton();
        if (evt.getButton() == 3) {
            if(componentClicked.getClass().equals(SideOpening.class) || componentClicked.getClass().equals(ControlPoint.class)){
               JPopupMenu popMenu = new JPopupMenu();
            JMenuItem deleButton = new JMenuItem("Supprimer");
            deleButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.deleteComponant(componentClicked);
                    repaint();
                }
            });
            popMenu.add(deleButton);
            popMenu.show(drawingPanel, evt.getX(), evt.getY()); 
            }
        }
        
    }//GEN-LAST:event_drawingPanelMousePressed

    private void drawingPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMouseDragged
        this.controller.sourisX = (evt.getX());
        this.controller.sourisY = (evt.getY());

        // Si aucun composant de la roulotte est sélectionné // Condition: clic bouton milieu souris
        if (this.controller.typeSouris == 2) {

            this.controller.decalageX -= (this.controller.sourisX - this.controller.sourisStartPanX) / this.controller.zoom;
            this.controller.decalageY -= (this.controller.sourisY - this.controller.sourisStartPanY) / this.controller.zoom;
            this.controller.sourisStartPanX = this.controller.sourisX;
            this.controller.sourisStartPanY = this.controller.sourisY;
            repaint();

        } else if (this.controller.typeSouris == 1 && this.componentClicked != null) {
            isAfterDragged = true;
            Point2D.Double doublePoint = this.controller.relativeToAbsolute((float) evt.getX(), (float) evt.getY());
            Point newPoint = new Point((int) doublePoint.x, (int) doublePoint.y);
            PointMeasurement p = this.controller.pixelToUnit(newPoint);

            if (componentClicked.getClass().equals(ControlPoint.class
            )) {
                PointMeasurement oldCenter = ((ControlPoint) componentClicked).getCenter();

                try {
                    this.controller.moove(componentClicked, p);

                    // intentionally find problems with new control point position
                    for (IDrawable drawable : this.controller.getTearDrop().getDrawables()) {
                        drawable.getPolygon();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((ControlPoint) componentClicked).setCenter(oldCenter);
                    this.controller.getTearDrop().invalidateAll();
                }
            } else {
                this.controller.moove(componentClicked, p);
            }
            updateTextfield();
            repaint();

        }

    }//GEN-LAST:event_drawingPanelMouseDragged

    private void drawingPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_drawingPanelMouseWheelMoved

        Point2D.Double mouseBeforeZoom = this.controller.relativeToAbsolute(this.controller.sourisX, this.controller.sourisY);

        int rouletteSouris = evt.getWheelRotation();

        if (rouletteSouris < 0) {
            this.controller.zoom *= 1.1;
        }
        if (rouletteSouris > 0) {
            this.controller.zoom *= 0.9;
        }

        Point2D.Double mouseAfterZoom = this.controller.relativeToAbsolute(this.controller.sourisX, this.controller.sourisY);

        float dX = this.controller.decalageX;
        float dY = this.controller.decalageY;
        this.controller.decalageX += mouseBeforeZoom.x - mouseAfterZoom.x;
        this.controller.decalageY += mouseBeforeZoom.y - mouseAfterZoom.y;
        updateTextfield();
        repaint();
    }//GEN-LAST:event_drawingPanelMouseWheelMoved

    private void profilDisplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilDisplayButtonActionPerformed

    }//GEN-LAST:event_profilDisplayButtonActionPerformed

    private void profilDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_profilDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getCurvedProfile().setDisplayMode(DisplayMode.HIDDEN);

        } else if (state == 1) {
            this.controller.getTearDrop().getCurvedProfile().setDisplayMode(DisplayMode.REGULAR);
        }
        repaint();
    }//GEN-LAST:event_profilDisplayButtonItemStateChanged

    private void floorDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_floorDisplayItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getFloor().setDisplayMode(DisplayMode.HIDDEN);
        } else if (state == 1) {
            this.controller.getTearDrop().getFloor().setDisplayMode(DisplayMode.REGULAR);
        }
        repaint();
    }//GEN-LAST:event_floorDisplayItemStateChanged

    private void radioButtonMillimètreItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonMillimètreItemStateChanged

        int state = evt.getStateChange();
        if (state == 1) {
            this.unit = Unit.MILLIMETERS;
            updateTextfield();
        }
    }//GEN-LAST:event_radioButtonMillimètreItemStateChanged

    private void radioButtonInchesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonInchesItemStateChanged
        int state = evt.getStateChange();
        if (state == 1) {
            this.unit = Unit.INCHES;
            updateTextfield();
        }
    }//GEN-LAST:event_radioButtonInchesItemStateChanged

    private void textFieldComposantNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldComposantNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldComposantNameActionPerformed

    private void textFieldPropertise1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldPropertise1FocusLost
        String component = textFieldComposantName.getText();
        if (!textFieldPropertise1.getText().equals(this.firstTextField)) {
            switch (component) {
                case "Plancher":
                    try {
                    float new_marge = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    boolean valid = controller.validInput(new_marge);
                    if (valid) {
                        textFieldPropertise1.setBackground(Color.WHITE);
                        textFieldPropertise1.setForeground(Color.BLACK);
                        Measurement back = this.controller.getTearDrop().getFloor().getBackmargin();
                        Measurement thick = this.controller.getTearDrop().getFloor().getThickness();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(thick, new Measurement(new_marge * 25.4), back);
                            repaint();
                        } else {
                            controller.editFloor(thick, new Measurement(new_marge), back);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_marge));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }
                break;
                case "Mur latéral":
                    try {
                    float new_h = getFloatFromTextField(textFieldPropertise1.getText());
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement width = this.controller.getTearDrop().getRawProfile().getWidth();
                    boolean valid = controller.validInput(new_h);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editSideWall(new Measurement(new_h * 25.4), width);
                            repaint();
                        } else {
                            controller.editSideWall(new Measurement(new_h), width);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_h));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);

                }
                break;
                case "Hayon":
                    try {
                    float new_dBeam = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement d_floor = controller.getTearDrop().getHatch().getDistanceFloor();
                    Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                    Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                    boolean valid = controller.validInput(new_dBeam);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(thickness, new Measurement(new_dBeam * 25.4), d_floor, curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(thickness, new Measurement(new_dBeam), d_floor, curveRadius);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_dBeam));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }
                break;
                case "Poutre arrière":
                    //hauteur
                    try {
                    float new_h = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement width = this.controller.getTearDrop().getBeam().getWidth();
                    Measurement x = this.controller.getTearDrop().getBeam().getTopLeftPositionX();
                    boolean valid = controller.validInput(new_h);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editBeam(new Measurement(new_h * 25.4), width, x);
                            repaint();
                        } else {
                            controller.editBeam(new Measurement(new_h), width, x);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_h));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }
                break;
                case "Mur séparateur":
                    try {
                    float dBeam = getFloatFromTextField(textFieldPropertise1.getText());
                    Measurement dFloor = this.controller.getTearDrop().getDividingWall().getDistanceFloor();
                    Measurement thickness = this.controller.getTearDrop().getDividingWall().getThickness();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(new Measurement(dBeam), dFloor, thickness);
                    } else {
                        this.controller.editDividingWall(new Measurement(dBeam * 25.4), dFloor, thickness);
                    }
                } catch (Exception e) {

                }
                break;
                case "Toit":
                    try {
                    float thickness = getFloatFromTextField(textFieldPropertise1.getText());
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editCeiling(new Measurement(thickness));
                        repaint();
                    } else {
                        this.controller.editCeiling(new Measurement(thickness * 25.4));
                        repaint();
                    }
                } catch (Exception e) {

                } 
                break;
                case "Ouverture latérale":
                    try{
                        float h = getFloatFromTextField(textFieldPropertise1.getText());
                        Measurement l = this.controller.getTearDrop().getDoor().getWidth();;
                        Measurement r = this.controller.getTearDrop().getDoor().getRadius();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            l =  this.controller.getTearDrop().getWindow().getWidth();
                            r =  this.controller.getTearDrop().getWindow().getRadius();
                        }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            l =  this.controller.getTearDrop().getWindow1().getWidth();
                            r =  this.controller.getTearDrop().getWindow1().getRadius();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(new Measurement(h), l, r, componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(new Measurement(h*25.4), l, r, componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    } break;
                default:
                    break;
            }

            updateValidation();
            this.firstTextField = textFieldPropertise1.getText();
        }
    }//GEN-LAST:event_textFieldPropertise1FocusLost

    private void textFieldPropertise2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldPropertise2FocusLost
        String component = textFieldComposantName.getText();
        if (!textFieldPropertise2.getText().equals(this.secondTextField)) {
            switch (component) {
                case "Ouverture latérale":
                    try{
                        float l = getFloatFromTextField(textFieldPropertise2.getText());
                        Measurement h = this.controller.getTearDrop().getDoor().getHeight();;
                        Measurement r = this.controller.getTearDrop().getDoor().getRadius();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            h =  this.controller.getTearDrop().getWindow().getHeight();
                            r =  this.controller.getTearDrop().getWindow().getRadius();
                        }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            h =  this.controller.getTearDrop().getWindow1().getHeight();
                            r =  this.controller.getTearDrop().getWindow1().getRadius();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(h, new Measurement(l), r, componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(h, new Measurement(l*25.4), r, componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    } break;
                case "Plancher":
                    try {
                    float new_marge = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_marge);
                    if (valid) {
                        textFieldPropertise2.setBackground(Color.WHITE);
                        textFieldPropertise2.setForeground(Color.BLACK);
                        Measurement front = this.controller.getTearDrop().getFloor().getFrontmargin();
                        Measurement thick = this.controller.getTearDrop().getFloor().getThickness();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(thick, front, new Measurement(new_marge * 25.4));
                            repaint();
                        } else {
                            controller.editFloor(thick, front, new Measurement(new_marge));
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_marge));
                    }

                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }
                break;
                case "Mur latéral":
                    try {
                    float new_l = getFloatFromTextField(textFieldPropertise2.getText());
                    boolean valid = controller.validInput(new_l);
                    if (valid) {
                        Measurement height = this.controller.getTearDrop().getRawProfile().getHeight();
                        if (this.unit == Unit.INCHES) {
                            controller.editSideWall(height, new Measurement(new_l * 25.4));
                            repaint();
                        } else {
                            controller.editSideWall(height, new Measurement(new_l));
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_l));
                    }
                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }
                break;
                case "Hayon":
                    try {
                    float new_dFloor = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_dFloor);
                    if (valid) {
                        Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                        Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                        Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(thickness, d_Beam, new Measurement(new_dFloor * 25.4), curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(thickness, d_Beam, new Measurement(new_dFloor), curveRadius);
                            repaint();
                        }
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_dFloor));
                    }
                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }
                break;
                case "Poutre arrière":
                    //largeur
                    try {
                    float new_l = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_l);
                    if (valid) {
                        textFieldPropertise2.setBackground(Color.WHITE);
                        textFieldPropertise2.setForeground(Color.BLACK);
                        Measurement heignt = this.controller.getTearDrop().getBeam().getHeight();
                        Measurement x = this.controller.getTearDrop().getBeam().getTopLeftPositionX();
                        if (this.unit == Unit.INCHES) {
                            controller.editBeam(heignt, new Measurement(new_l * 25.4), x);
                            repaint();
                        } else {
                            controller.editBeam(heignt, new Measurement(new_l), x);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_l));
                    }
                } catch (UserException e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }
                break;
                case "Mur séparateur":
                    try {
                    float dFloor = getFloatFromTextField(textFieldPropertise2.getText());
                    Measurement dBeam = this.controller.getTearDrop().getDividingWall().getDistanceBeam();
                    Measurement thickness = this.controller.getTearDrop().getDividingWall().getThickness();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(dBeam, new Measurement(dFloor), thickness);
                        repaint();
                    } else {
                        this.controller.editDividingWall(dBeam, new Measurement(dFloor * 25.4), thickness);
                        repaint();
                    }
                } catch (Exception e) {

                }
                break;
                default:
                    break;
            }

            updateValidation();
            this.secondTextField = textFieldPropertise2.getText();
        }
    }//GEN-LAST:event_textFieldPropertise2FocusLost

    private void textFieldThicknessFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldThicknessFocusLost
        String component = textFieldComposantName.getText();
        if (!textFieldThickness.getText().equals(this.thirdTextField)) {
            switch (component) {
                case "Ouverture latérale":
                    try{
                        float r = getFloatFromTextField(textFieldThickness.getText());
                        Measurement l = this.controller.getTearDrop().getDoor().getWidth();;
                        Measurement h = this.controller.getTearDrop().getDoor().getHeight();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            l =  this.controller.getTearDrop().getWindow().getWidth();
                            h =  this.controller.getTearDrop().getWindow().getHeight();
                        }
                        else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            l =  this.controller.getTearDrop().getWindow1().getWidth();
                            h =  this.controller.getTearDrop().getWindow1().getHeight();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(h, l, new Measurement(r), componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(h, l, new Measurement(r*25.4), componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    } break;
                case "Plancher":
                    try {
                    String champ = labelThickness1.getText();
                    float new_thickness = getFloatFromTextField(textFieldThickness.getText());
                    boolean valid = controller.validInput(new_thickness);
                    if (valid) {
                        textFieldThickness.setBackground(Color.WHITE);
                        textFieldThickness.setForeground(Color.BLACK);
                        Measurement front = this.controller.getTearDrop().getFloor().getFrontmargin();
                        Measurement back = this.controller.getTearDrop().getFloor().getBackmargin();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(new Measurement(new_thickness * 25.4), front, back);
                            repaint();
                        } else {
                            controller.editFloor(new Measurement(new_thickness), front, back);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldThickness.setBackground(new Color(243, 75, 109));
                        textFieldThickness.setForeground(Color.WHITE);
                        textFieldThickness.setText(Float.toString(new_thickness));
                    }

                } catch (UserException e) {
                    textFieldThickness.setBackground(new Color(243, 75, 109));
                    textFieldThickness.setForeground(Color.WHITE);
                }
                break;
                case "Hayon":
                    try {
                    float new_thickness = getFloatFromTextField(textFieldThickness.getText());
                    String champ = labelThickness1.getText();
                    boolean valid = controller.validInput(new_thickness);
                    if (valid) {
                        textFieldThickness.setBackground(Color.WHITE);
                        textFieldThickness.setForeground(Color.BLACK);
                        Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                        Measurement d_Floor = controller.getTearDrop().getHatch().getDistanceFloor();
                        Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(new Measurement(new_thickness * 25.4), d_Beam, d_Floor, curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(new Measurement(new_thickness), d_Beam, d_Floor, curveRadius);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldThickness.setBackground(new Color(243, 75, 109));
                        textFieldThickness.setForeground(Color.WHITE);
                        textFieldThickness.setText(Float.toString(new_thickness));
                    }
                } catch (UserException e) {
                    textFieldThickness.setBackground(new Color(243, 75, 109));
                    textFieldThickness.setForeground(Color.WHITE);
                }
                break;
                case "Poutre arrière":
                    textFieldThickness.setBackground(Color.WHITE);
                    textFieldThickness.setForeground(Color.BLACK);
                    try {
                        float x = getFloatFromTextField(textFieldThickness.getText());
                        Measurement h = this.controller.getTearDrop().getBeam().getHeight();
                        Measurement w = this.controller.getTearDrop().getBeam().getWidth();
                        if (unit == Unit.MILLIMETERS) {
                            controller.editBeam(h, w, new Measurement(x));
                            repaint();
                        } else {
                            controller.editBeam(h, w, new Measurement(x * 25.4));
                            repaint();
                        }
                    } catch (UserException e) {
                        textFieldThickness.setBackground(new Color(243, 75, 109));
                        textFieldThickness.setForeground(Color.WHITE);
                    }
                    break;
                case "Mur séparateur":
                    try {
                    float thickness = getFloatFromTextField(textFieldThickness.getText());
                    Measurement dBeam = this.controller.getTearDrop().getDividingWall().getDistanceBeam();
                    Measurement dFloor = this.controller.getTearDrop().getDividingWall().getDistanceFloor();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(dBeam, dFloor, new Measurement(thickness));
                        repaint();
                    } else {
                        this.controller.editDividingWall(dBeam, dFloor, new Measurement(thickness * 25.4));
                        repaint();
                    }
                } catch (Exception e) {
                }
                break;
                default:
                    break;
            }

            updateValidation();
            this.thirdTextField = textFieldThickness.getText();
        }
    }//GEN-LAST:event_textFieldThicknessFocusLost

    private void hayonDisplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hayonDisplayButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hayonDisplayButtonActionPerformed

    private void hayonDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_hayonDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getHatch().setDisplayMode(DisplayMode.HIDDEN);
        } else if (state == 1) {
            this.controller.getTearDrop().getHatch().setDisplayMode(DisplayMode.REGULAR);
        }
        repaint();
    }//GEN-LAST:event_hayonDisplayButtonItemStateChanged

    private void beamDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_beamDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getBeam().setDisplayMode(DisplayMode.HIDDEN); // indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getBeam().setDisplayMode(DisplayMode.REGULAR); // indice à modifier après l'ajout du beam
        }
        repaint();
    }//GEN-LAST:event_beamDisplayButtonItemStateChanged

    private void textFieldRadiusCurveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldRadiusCurveFocusLost
        String component = textFieldComposantName.getText();
        if (!textFieldRadiusCurve.getText().equals(this.fourthTextField) && component.equals("Hayon")) {
            try {
                float new_curveRadius = getFloatFromTextField(textFieldRadiusCurve.getText());
                String champ = labelRadiusCurve.getText();
                boolean valid = controller.validInput(new_curveRadius);
                if (valid) {
                    textFieldRadiusCurve.setBackground(Color.WHITE);
                    textFieldRadiusCurve.setForeground(Color.BLACK);
                    Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                    Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                    Measurement d_Floor = controller.getTearDrop().getHatch().getDistanceFloor();
                    if (this.unit == Unit.INCHES) {
                        controller.editHatch(thickness, d_Beam, d_Floor, new Measurement(new_curveRadius * 25.4));
                        repaint();
                    } else {
                        controller.editHatch(thickness, d_Beam, d_Floor, new Measurement(new_curveRadius));
                        repaint();
                    }
                    updateTextfield();
                } else {
                    textFieldRadiusCurve.setBackground(new Color(243, 75, 109));
                    textFieldRadiusCurve.setForeground(Color.WHITE);
                    textFieldRadiusCurve.setText(Float.toString(new_curveRadius));
                }
            } catch (Exception e) {
                textFieldRadiusCurve.setBackground(new Color(243, 75, 109));
                textFieldRadiusCurve.setForeground(Color.WHITE);
            }

        }

        updateValidation();
        this.fourthTextField = textFieldRadiusCurve.getText();
    }//GEN-LAST:event_textFieldRadiusCurveFocusLost

    private void drawingPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMouseClicked
        try {
            Point2D.Float actualPoint = new Point2D.Float();
            actualPoint.setLocation(evt.getPoint().getX(), evt.getPoint().getY());
            IDrawable componantClicked = this.controller.getComponentClicked(actualPoint);
            this.componentClicked = componantClicked;
            for (IDrawable iDrawable : this.controller.getComponentList()) {
                if (iDrawable.getDisplayMode() == DisplayMode.HIDDEN && iDrawable == componantClicked) {
                    return;
                }
            }
            this.controller.getComponentList().parallelStream().filter(iDrawable -> (iDrawable.getDisplayMode() != DisplayMode.HIDDEN)).forEachOrdered(iDrawable -> {
                if (iDrawable != componantClicked) {
                    iDrawable.setDisplayMode(DisplayMode.REGULAR);
                } else {
                    iDrawable.setDisplayMode(DisplayMode.HIGH_LIGHT);
                    String selectedCorrespondingComponant = classToTreeComponant.get(componantClicked.getClass().getSimpleName());
                    textFieldComposantName.setText(selectedCorrespondingComponant);
                    updateTextfield();
                }
            });
            repaint();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_drawingPanelMouseClicked

    private void rawWallDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rawWallDisplayItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getList().get(0).setDisplayMode(DisplayMode.HIDDEN);
        } else if (state == 1) {
            this.controller.getList().get(0).setDisplayMode(DisplayMode.REGULAR);
        }
        repaint();
    }//GEN-LAST:event_rawWallDisplayItemStateChanged

    private void menuEditRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditRestoreActionPerformed
        TearDropTrailer tearDrop = undoRedoManager.redo();
        this.controller.setTearDrop(tearDrop);
        this.controller.getTearDrop().invalidateAll();
        updateTextfield();
        repaint();
    }//GEN-LAST:event_menuEditRestoreActionPerformed

    private void menuEditCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditCancelActionPerformed
        TearDropTrailer tearDrop = undoRedoManager.undo();
        this.controller.setTearDrop(tearDrop);
        this.controller.getTearDrop().invalidateAll();
        updateTextfield();
        repaint();
    }//GEN-LAST:event_menuEditCancelActionPerformed

    private void menuFileExportImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExportImageActionPerformed
        BufferedImage bufferedImage = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        drawingPanel.printAll(g2d);
        g2d.dispose();
        JFileChooser c = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads\\"));
        c.setDialogTitle("Sélectionnez l'emplacement");
        c.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputfile = new File(c.getSelectedFile().getPath() + ".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", outputfile);
            } catch (IOException ex) {
                // Caliss
            }
        }
    }//GEN-LAST:event_menuFileExportImageActionPerformed

    private void menuFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileSaveActionPerformed
        JFileChooser c = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads"));
        c.setDialogTitle("Sélectionnez l'emplacement");
        c.setFileFilter(new FileNameExtensionFilter("*.mard", "mard"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fos = new FileOutputStream(c.getSelectedFile().getPath() + ".mard");
                try ( ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(this.controller.getTearDrop());
                    oos.close();
                }
            } catch (IOException ex) {
                // ¯\_(ツ)_/¯
            }
        }
    }//GEN-LAST:event_menuFileSaveActionPerformed

    private void menuFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileOpenActionPerformed
        JFileChooser c = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads"));
        c.setDialogTitle("Sélectionnez l'emplacement");
        c.setFileFilter(new FileNameExtensionFilter("*.mard", "mard"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fileIn = new FileInputStream(c.getSelectedFile().getPath());
                try ( ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                    TearDropTrailer tearDrop = (TearDropTrailer) objectIn.readObject();
                    this.controller.setTearDrop(tearDrop);
                    this.undoRedoManager.setParentNode((TearDropTrailer) this.controller.getTearDrop().clone());
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                    updateValidation();
                }
            } catch (Exception ex) {
                // ¯\_(ツ)_/¯
                ex.printStackTrace();
            }
        }
        repaint();
        updateTextfield();
    }//GEN-LAST:event_menuFileOpenActionPerformed

    private void textFieldScaleMicroTrailerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldScaleMicroTrailerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldScaleMicroTrailerKeyPressed

    private void textFieldAssistanceGridSpacingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldAssistanceGridSpacingKeyPressed
        // Change the value when Enter is pressed!
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double newSpacing = getFloatFromTextField(textFieldAssistanceGridSpacing.getText());
            Point2D.Double firstPoint = this.controller.relativeToAbsolute(point.x, point.y);
            double factor;
            if (radioButtonInches.isSelected()) {
                factor = (double) newSpacing / ((firstPoint.x - this.controller.decalageX) / 3.8);
            } else {
                factor = (double) newSpacing / ((firstPoint.x - this.controller.decalageX) * 25.4 / 3.8);
            }
            point = new Point2D.Float((float) Math.abs(factor) * point.x, (float) Math.abs(factor) * point.y);
            updateTextfield();
            repaint();
        }
    }//GEN-LAST:event_textFieldAssistanceGridSpacingKeyPressed

    private void ellipseConfirmeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ellipseConfirmeButtonActionPerformed
        String component = textFieldComposantName.getText();
        try {

            Measurement x = new Measurement(getFloatFromTextField(textFieldPropertise1.getText()));
            Measurement y = new Measurement(getFloatFromTextField(textFieldPropertise2.getText()));
            PointMeasurement center = new PointMeasurement(x, y);
            Measurement h = new Measurement(getFloatFromTextField(textFieldRadiusCurve.getText()));
            Measurement v = new Measurement(getFloatFromTextField(textFieldThickness.getText()));
            Quadrant q = Quadrant.BOTTOM_LEFT;

            if (component.equals("Coin supérieur gauche")) {
                q = Quadrant.TOP_LEFT;
            } else if (component.equals("Coin supérieur droit")) {
                q = Quadrant.TOP_RIGHT;
            } else if (component.equals("Coin inférieur droit")) {
                q = Quadrant.BOTTOM_RIGHT;
            } else if (component.equals("Coin inférieur gauche")) {
                q = Quadrant.BOTTOM_LEFT;
            }

            if (unit == Unit.MILLIMETERS) {
                this.controller.editEllipseProfile(q, v, h);
                this.controller.editEllipseCenter(q, center);
            } else {
                this.controller.editEllipseProfile(q, v.mult(25.4), h.mult(25.4));
                this.controller.editEllipseCenter(q, new PointMeasurement(center.x.mult(25.4), center.y.mult(25.4)));
            }
            repaint();
        } catch (Exception ignored) {

        }
    }//GEN-LAST:event_ellipseConfirmeButtonActionPerformed

    private void ellipseCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ellipseCancelButtonActionPerformed
        updateTextfield();
    }//GEN-LAST:event_ellipseCancelButtonActionPerformed

    private void textFieldPropertise1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldPropertise1KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String component = textFieldComposantName.getText();

            if (component.equals("Plancher")) {
                try {
                    float new_marge = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    boolean valid = controller.validInput(new_marge);
                    if (valid) {
                        textFieldPropertise1.setBackground(Color.WHITE);
                        textFieldPropertise1.setForeground(Color.BLACK);
                        Measurement back = this.controller.getTearDrop().getFloor().getBackmargin();
                        Measurement thick = this.controller.getTearDrop().getFloor().getThickness();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(thick, new Measurement(new_marge * 25.4), back);
                            repaint();
                        } else {
                            controller.editFloor(thick, new Measurement(new_marge), back);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_marge));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }

            } else if (component.equals("Mur latéral")) {
                try {
                    float new_h = getFloatFromTextField(textFieldPropertise1.getText());
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement width = this.controller.getTearDrop().getRawProfile().getWidth();
                    boolean valid = controller.validInput(new_h);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editSideWall(new Measurement(new_h * 25.4), width);
                            repaint();
                        } else {
                            controller.editSideWall(new Measurement(new_h), width);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_h));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);

                }
            } else if (component.equals("Hayon")) {
                try {
                    float new_dBeam = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement d_floor = controller.getTearDrop().getHatch().getDistanceFloor();
                    Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                    Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                    boolean valid = controller.validInput(new_dBeam);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(thickness, new Measurement(new_dBeam * 25.4), d_floor, curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(thickness, new Measurement(new_dBeam), d_floor, curveRadius);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_dBeam));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }

            } else if (component.equals("Poutre arrière")) {
                //hauteur
                try {
                    float new_h = getFloatFromTextField(textFieldPropertise1.getText());
                    String champ = labelPropertise1.getText();
                    textFieldPropertise1.setBackground(Color.WHITE);
                    textFieldPropertise1.setForeground(Color.BLACK);
                    Measurement width = this.controller.getTearDrop().getBeam().getWidth();
                    Measurement x = this.controller.getTearDrop().getBeam().getTopLeftPositionX();
                    boolean valid = controller.validInput(new_h);
                    if (valid) {
                        if (this.unit == Unit.INCHES) {
                            controller.editBeam(new Measurement(new_h * 25.4), width, x);
                            repaint();
                        } else {
                            controller.editBeam(new Measurement(new_h), width, x);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise1.setBackground(new Color(243, 75, 109));
                        textFieldPropertise1.setForeground(Color.WHITE);
                        textFieldPropertise1.setText(Float.toString(new_h));
                    }
                } catch (Exception e) {
                    textFieldPropertise1.setBackground(new Color(243, 75, 109));
                    textFieldPropertise1.setForeground(Color.WHITE);
                }

            } else if (component.equals("Mur séparateur")) {
                try {
                    float dBeam = getFloatFromTextField(textFieldPropertise1.getText());
                    Measurement dFloor = this.controller.getTearDrop().getDividingWall().getDistanceFloor();
                    Measurement thickness = this.controller.getTearDrop().getDividingWall().getThickness();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(new Measurement(dBeam), dFloor, thickness);
                        repaint();
                    } else {
                        this.controller.editDividingWall(new Measurement(dBeam * 25.4), dFloor, thickness);
                        repaint();
                    }
                } catch (Exception e) {

                }
            } else if (component.equals("Toit")) {
                try {
                    float thickness = getFloatFromTextField(textFieldPropertise1.getText());
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editCeiling(new Measurement(thickness));
                        repaint();
                    } else {
                        this.controller.editCeiling(new Measurement(thickness * 25.4));
                        repaint();
                    }
                } catch (Exception e) {

                }
            } else if(component.equals("Ouverture latérale")){
                try{
                        float h = getFloatFromTextField(textFieldPropertise1.getText());
                        Measurement l = this.controller.getTearDrop().getDoor().getWidth();;
                        Measurement r = this.controller.getTearDrop().getDoor().getRadius();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            l =  this.controller.getTearDrop().getWindow().getWidth();
                            r =  this.controller.getTearDrop().getWindow().getRadius();
                        }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            l =  this.controller.getTearDrop().getWindow1().getWidth();
                            r =  this.controller.getTearDrop().getWindow1().getRadius();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(new Measurement(h), l, r, componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(new Measurement(h*25.4), l, r, componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    }
            }
                    
            updateValidation();

        }
    }//GEN-LAST:event_textFieldPropertise1KeyPressed

    private void textFieldPropertise2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldPropertise2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String component = textFieldComposantName.getText();

            if (component.equals("Plancher")) {
                try {
                    float new_marge = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_marge);
                    if (valid) {
                        textFieldPropertise2.setBackground(Color.WHITE);
                        textFieldPropertise2.setForeground(Color.BLACK);
                        Measurement front = this.controller.getTearDrop().getFloor().getFrontmargin();
                        Measurement thick = this.controller.getTearDrop().getFloor().getThickness();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(thick, front, new Measurement(new_marge * 25.4));
                            repaint();
                        } else {
                            controller.editFloor(thick, front, new Measurement(new_marge));
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_marge));
                    }

                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }

            } else if (component.equals("Mur latéral")) {
                try {
                    float new_l = getFloatFromTextField(textFieldPropertise2.getText());
                    boolean valid = controller.validInput(new_l);
                    if (valid) {
                        Measurement height = this.controller.getTearDrop().getRawProfile().getHeight();
                        if (this.unit == Unit.INCHES) {
                            controller.editSideWall(height, new Measurement(new_l * 25.4));
                            repaint();
                        } else {
                            controller.editSideWall(height, new Measurement(new_l));
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_l));
                    }
                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }

            } else if (component.equals("Hayon")) {
                try {
                    float new_dFloor = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_dFloor);
                    if (valid) {
                        Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                        Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                        Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(thickness, d_Beam, new Measurement(new_dFloor * 25.4), curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(thickness, d_Beam, new Measurement(new_dFloor), curveRadius);
                            repaint();
                        }
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_dFloor));
                    }
                } catch (Exception e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }

            } else if (component.equals("Poutre arrière")) {
                //largeur
                try {
                    float new_l = getFloatFromTextField(textFieldPropertise2.getText());
                    String champ = labelPropertise2.getText();
                    boolean valid = controller.validInput(new_l);
                    if (valid) {
                        textFieldPropertise2.setBackground(Color.WHITE);
                        textFieldPropertise2.setForeground(Color.BLACK);
                        Measurement heignt = this.controller.getTearDrop().getBeam().getHeight();
                        Measurement x = this.controller.getTearDrop().getBeam().getTopLeftPositionX();
                        if (this.unit == Unit.INCHES) {
                            controller.editBeam(heignt, new Measurement(new_l * 25.4), x);
                            repaint();
                        } else {
                            controller.editBeam(heignt, new Measurement(new_l), x);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldPropertise2.setBackground(new Color(243, 75, 109));
                        textFieldPropertise2.setForeground(Color.WHITE);
                        textFieldPropertise2.setText(Float.toString(new_l));
                    }
                } catch (UserException e) {
                    textFieldPropertise2.setBackground(new Color(243, 75, 109));
                    textFieldPropertise2.setForeground(Color.WHITE);
                }
            } else if (component.equals("Mur séparateur")) {
                try {
                    float dFloor = getFloatFromTextField(textFieldPropertise2.getText());
                    Measurement dBeam = this.controller.getTearDrop().getDividingWall().getDistanceBeam();
                    Measurement thickness = this.controller.getTearDrop().getDividingWall().getThickness();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(dBeam, new Measurement(dFloor), thickness);
                        repaint();
                    } else {
                        this.controller.editDividingWall(dBeam, new Measurement(dFloor * 25.4), thickness);
                        repaint();
                    }
                } catch (Exception e) {

                }
            } else if(component.equals("Ouverture latérale")){
                try{
                        float l = getFloatFromTextField(textFieldPropertise2.getText());
                        Measurement h = this.controller.getTearDrop().getDoor().getHeight();;
                        Measurement r = this.controller.getTearDrop().getDoor().getRadius();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            h =  this.controller.getTearDrop().getWindow().getHeight();
                            r =  this.controller.getTearDrop().getWindow().getRadius();
                        }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            h =  this.controller.getTearDrop().getWindow1().getHeight();
                            r =  this.controller.getTearDrop().getWindow1().getRadius();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(h, new Measurement(l), r, componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(h, new Measurement(l*25.4), r, componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    }
            }
                    

            updateValidation();
        }
    }//GEN-LAST:event_textFieldPropertise2KeyPressed

    private void textFieldThicknessKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldThicknessKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String component = textFieldComposantName.getText();

            if (component.equals("Plancher")) {
                try {
                    String champ = labelThickness1.getText();
                    float new_thickness = getFloatFromTextField(textFieldThickness.getText());
                    boolean valid = controller.validInput(new_thickness);
                    if (valid) {
                        textFieldThickness.setBackground(Color.WHITE);
                        textFieldThickness.setForeground(Color.BLACK);
                        Measurement front = this.controller.getTearDrop().getFloor().getFrontmargin();
                        Measurement back = this.controller.getTearDrop().getFloor().getBackmargin();
                        if (this.unit == Unit.INCHES) {
                            controller.editFloor(new Measurement(new_thickness * 25.4), front, back);
                            repaint();
                        } else {
                            controller.editFloor(new Measurement(new_thickness), front, back);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldThickness.setBackground(new Color(243, 75, 109));
                        textFieldThickness.setForeground(Color.WHITE);
                        textFieldThickness.setText(Float.toString(new_thickness));
                    }

                } catch (UserException e) {
                    textFieldThickness.setBackground(new Color(243, 75, 109));
                    textFieldThickness.setForeground(Color.WHITE);
                }

            } else if (component.equals("Hayon")) {
                try {
                    float new_thickness = getFloatFromTextField(textFieldThickness.getText());
                    String champ = labelThickness1.getText();
                    boolean valid = controller.validInput(new_thickness);
                    if (valid) {
                        textFieldThickness.setBackground(Color.WHITE);
                        textFieldThickness.setForeground(Color.BLACK);
                        Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                        Measurement d_Floor = controller.getTearDrop().getHatch().getDistanceFloor();
                        Measurement curveRadius = controller.getTearDrop().getHatch().getCurveRadius();
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(new Measurement(new_thickness * 25.4), d_Beam, d_Floor, curveRadius);
                            repaint();
                        } else {
                            controller.editHatch(new Measurement(new_thickness), d_Beam, d_Floor, curveRadius);
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldThickness.setBackground(new Color(243, 75, 109));
                        textFieldThickness.setForeground(Color.WHITE);
                        textFieldThickness.setText(Float.toString(new_thickness));
                    }
                } catch (UserException e) {
                    textFieldThickness.setBackground(new Color(243, 75, 109));
                    textFieldThickness.setForeground(Color.WHITE);
                }

            } else if (component.equals("Poutre arrière")) {

                textFieldThickness.setBackground(Color.WHITE);
                textFieldThickness.setForeground(Color.BLACK);
                try {
                    float x = getFloatFromTextField(textFieldThickness.getText());
                    Measurement h = this.controller.getTearDrop().getBeam().getHeight();
                    Measurement w = this.controller.getTearDrop().getBeam().getWidth();
                    if (unit == Unit.MILLIMETERS) {
                        controller.editBeam(h, w, new Measurement(x));
                        repaint();
                    } else {
                        controller.editBeam(h, w, new Measurement(x * 25.4));
                        repaint();
                    }
                } catch (UserException e) {
                    textFieldThickness.setBackground(new Color(243, 75, 109));
                    textFieldThickness.setForeground(Color.WHITE);
                }

            } else if (component.equals("Mur séparateur")) {
                try {
                    float thickness = getFloatFromTextField(textFieldThickness.getText());
                    Measurement dBeam = this.controller.getTearDrop().getDividingWall().getDistanceBeam();
                    Measurement dFloor = this.controller.getTearDrop().getDividingWall().getDistanceFloor();
                    if (unit == Unit.MILLIMETERS) {
                        this.controller.editDividingWall(dBeam, dFloor, new Measurement(thickness));
                        repaint();
                    } else {
                        this.controller.editDividingWall(dBeam, dFloor, new Measurement(thickness * 25.4));
                        repaint();
                    }
                } catch (Exception e) {
                }
            } else if (component.equals("Ouverture latérale")){
                try{
                        float r = getFloatFromTextField(textFieldThickness.getText());
                        Measurement l = this.controller.getTearDrop().getDoor().getWidth();;
                        Measurement h = this.controller.getTearDrop().getDoor().getHeight();;
                        if(componentClicked.equals(controller.getTearDrop().getWindow())){
                            l =  this.controller.getTearDrop().getWindow().getWidth();
                            h =  this.controller.getTearDrop().getWindow().getHeight();
                        }else if(componentClicked.equals(controller.getTearDrop().getWindow1())){
                            l =  this.controller.getTearDrop().getWindow1().getWidth();
                            h =  this.controller.getTearDrop().getWindow1().getHeight();
                        }
                        if (unit == Unit.MILLIMETERS) {
                        this.controller.editSideOpening(h, l, new Measurement(r), componentClicked);
                        repaint();
                    } else {
                        this.controller.editSideOpening(h, l, new Measurement(r*25.4), componentClicked);
                        repaint();
                    }
                    }
                    catch(Exception e){
                        
                    }
            }
            updateValidation();
        }
    }//GEN-LAST:event_textFieldThicknessKeyPressed

    private void textFieldRadiusCurveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldRadiusCurveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String component = textFieldComposantName.getText();
            if (component.equals("Hayon")) {
                try {
                    float new_curveRadius = getFloatFromTextField(textFieldRadiusCurve.getText());
                    String champ = labelRadiusCurve.getText();
                    boolean valid = controller.validInput(new_curveRadius);
                    if (valid) {
                        textFieldRadiusCurve.setBackground(Color.WHITE);
                        textFieldRadiusCurve.setForeground(Color.BLACK);
                        Measurement thickness = controller.getTearDrop().getHatch().getThickness();
                        Measurement d_Beam = controller.getTearDrop().getHatch().getDistanceBeam();
                        Measurement d_Floor = controller.getTearDrop().getHatch().getDistanceFloor();
                        if (this.unit == Unit.INCHES) {
                            controller.editHatch(thickness, d_Beam, d_Floor, new Measurement(new_curveRadius * 25.4));
                            repaint();
                        } else {
                            controller.editHatch(thickness, d_Beam, d_Floor, new Measurement(new_curveRadius));
                            repaint();
                        }
                        updateTextfield();
                    } else {
                        textFieldRadiusCurve.setBackground(new Color(243, 75, 109));
                        textFieldRadiusCurve.setForeground(Color.WHITE);
                        textFieldRadiusCurve.setText(Float.toString(new_curveRadius));
                    }
                } catch (Exception e) {
                    textFieldRadiusCurve.setBackground(new Color(243, 75, 109));
                    textFieldRadiusCurve.setForeground(Color.WHITE);
                }

            }

            updateValidation();

        }
    }//GEN-LAST:event_textFieldRadiusCurveKeyPressed

    private void EllipseProfileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EllipseProfileNewActionPerformed
        this.controller.createTeardrop(true);
        try {
            this.undoRedoManager.setParentNode((TearDropTrailer) this.controller.getTearDrop().clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < 15 ; i++) {
            updateValidation();
        }
        repaint();
        invalidateDisplayMenu();
        
    }//GEN-LAST:event_EllipseProfileNewActionPerformed

    private void bezierProfileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bezierProfileNewActionPerformed
        this.controller.createTeardrop(false);
        try {
            this.undoRedoManager.setParentNode((TearDropTrailer) this.controller.getTearDrop().clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0 ; i < 15 ; i++) {
            updateValidation();
        }
        repaint();
        invalidateDisplayMenu();
    }//GEN-LAST:event_bezierProfileNewActionPerformed

    private void menuFileExportSVGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFileExportSVGActionPerformed
        JFileChooser c = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads\\"));
        c.setDialogTitle("Sélectionnez l'emplacement");
        c.setFileFilter(new FileNameExtensionFilter("*.svg", "svg"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputfile = new File(c.getSelectedFile().getPath() + ".svg");
            try {
                SvgDrawer.saveSVG(controller.getTearDrop(), outputfile);
            } catch (IOException ex) {
                // Caliss TODO
            }
        }
    }//GEN-LAST:event_menuFileExportSVGActionPerformed

    private void controlPointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_controlPointButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_controlPointButtonActionPerformed

    private void controlPointButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_controlPointButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            for (IDrawable i : this.controller.getTearDrop().getControlPoints()) {
                i.setDisplayMode(DisplayMode.HIDDEN);
            }
            try{
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).setDisplayMode(DisplayMode.HIDDEN);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).setDisplayMode(DisplayMode.HIDDEN);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_LEFT).setDisplayMode(DisplayMode.HIDDEN);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).setDisplayMode(DisplayMode.HIDDEN);

            }catch(Exception e){
                
            }
        } else if (state == 1) {
            for (IDrawable i : this.controller.getTearDrop().getControlPoints()) {
                i.setDisplayMode(DisplayMode.REGULAR);
            }
            try{
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_LEFT).setDisplayMode(DisplayMode.REGULAR);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.BOTTOM_RIGHT).setDisplayMode(DisplayMode.REGULAR);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_LEFT).setDisplayMode(DisplayMode.REGULAR);
                controller.getTearDrop().getEllipsesControlPoints().get(Quadrant.TOP_RIGHT).setDisplayMode(DisplayMode.REGULAR);

            }catch(Exception e){
                
            }
        }
        repaint();
    }//GEN-LAST:event_controlPointButtonItemStateChanged

    private void beamDisplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beamDisplayButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_beamDisplayButtonActionPerformed

    private void ceilingDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ceilingDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getCeiling().setDisplayMode(DisplayMode.HIDDEN); // indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getCeiling().setDisplayMode(DisplayMode.REGULAR); // indice à modifier après l'ajout du beam
        }
        repaint();    }//GEN-LAST:event_ceilingDisplayButtonItemStateChanged

    private void strutDisplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_strutDisplayButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_strutDisplayButtonActionPerformed

    private void strutDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_strutDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getStrut().setDisplayMode(DisplayMode.HIDDEN); // indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getStrut().setDisplayMode(DisplayMode.REGULAR); // indice à modifier après l'ajout du beam
        }
        repaint();       }//GEN-LAST:event_strutDisplayButtonItemStateChanged

    private void addControlPointButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addControlPointButtonActionPerformed
        this.controller.addControlPoint();
        repaint();
    }//GEN-LAST:event_addControlPointButtonActionPerformed

    private void doorDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_doorDisplayItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getDoor().setDisplayMode(DisplayMode.HIDDEN); // indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getDoor().setDisplayMode(DisplayMode.REGULAR); // indice à modifier après l'ajout du beam
        }
        repaint();
    }//GEN-LAST:event_doorDisplayItemStateChanged

    private void innerLayerDisplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_innerLayerDisplayButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_innerLayerDisplayButtonActionPerformed

    private void innerLayerDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_innerLayerDisplayButtonItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_innerLayerDisplayButtonItemStateChanged

    private void outerLayerDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_outerLayerDisplayButtonItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_outerLayerDisplayButtonItemStateChanged

    private void dividingWallDisplayButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dividingWallDisplayButtonItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getDividingWall().setDisplayMode(DisplayMode.HIDDEN); // indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getDividingWall().setDisplayMode(DisplayMode.REGULAR); // indice à modifier après l'ajout du beam
        }
        repaint();
    }//GEN-LAST:event_dividingWallDisplayButtonItemStateChanged

    private void drawingPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMouseEntered

    }//GEN-LAST:event_drawingPanelMouseEntered

    private void windowDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_windowDisplayItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
            this.controller.getTearDrop().getWindow().setDisplayMode(DisplayMode.HIDDEN);
            this.controller.getTearDrop().getWindow1().setDisplayMode(DisplayMode.HIDDEN);// indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().getWindow().setDisplayMode(DisplayMode.REGULAR);
            this.controller.getTearDrop().getWindow1().setDisplayMode(DisplayMode.REGULAR);// indice à modifier après l'ajout du beam
        }
        repaint();
    }//GEN-LAST:event_windowDisplayItemStateChanged


    private void drawingPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_drawingPanelMouseReleased
        if(isAfterDragged){
            isAfterDragged = false;
            updateValidation();
        }
    }//GEN-LAST:event_drawingPanelMouseReleased

    private void textFieldPoidsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldPoidsFocusLost
        String component = textFieldComposantName.getText();
        if (component.equals("Hayon")) {
            try {
                float poids = getFloatFromTextField(textFieldPoids.getText());
                this.controller.editPoidsHatch(poids);
            } catch (Exception e) {

            }
        }
    }//GEN-LAST:event_textFieldPoidsFocusLost

    private void textFieldPoidsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldPoidsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldPoidsKeyPressed

    private void addWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWindowButtonActionPerformed
        this.controller.createWindow();
        repaint();
    }//GEN-LAST:event_addWindowButtonActionPerformed


    private void addGuideRectbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGuideRectbuttonActionPerformed

    }//GEN-LAST:event_addGuideRectbuttonActionPerformed

    private void guideRectangleDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guideRectangleDisplayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_guideRectangleDisplayActionPerformed

    private void guideRectangleDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_guideRectangleDisplayItemStateChanged
        int state = evt.getStateChange();
        if (state == 2) {
           
            this.controller.getTearDrop().rectangle1.setDisplayMode(DisplayMode.HIDDEN);
            this.controller.getTearDrop().rectangle2.setDisplayMode(DisplayMode.HIDDEN);// indice à modifier après l'ajout du beam
        } else if (state == 1) {
            this.controller.getTearDrop().rectangle1.setDisplayMode(DisplayMode.REGULAR);
            this.controller.getTearDrop().rectangle2.setDisplayMode(DisplayMode.REGULAR);// indice à modifier après l'ajout du beam
        }
        repaint();
    }//GEN-LAST:event_guideRectangleDisplayItemStateChanged

    private void textFieldPropertise2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldPropertise2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldPropertise2FocusGained


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    private void componantClickedRepaint(String componant) {
        for (IDrawable iDrawable : this.controller.getComponentList()) {
            if (iDrawable.getDisplayMode() == DisplayMode.HIDDEN && iDrawable.getClass().getSimpleName().equals(componant)) {
                return;
            }
        }
        this.controller.getComponentList().parallelStream().filter(iDrawable -> (iDrawable.getDisplayMode() != DisplayMode.HIDDEN)).forEachOrdered(iDrawable -> {
            if (!iDrawable.getClass().getSimpleName().equals(componant)) {
                iDrawable.setDisplayMode(DisplayMode.REGULAR);
            } else if (iDrawable.getClass().getSimpleName().equals(componant)) {
                iDrawable.setDisplayMode(DisplayMode.HIGH_LIGHT);
                String selectedCorrespondingComponant = classToTreeComponant.get(componant);
                textFieldComposantName.setText(selectedCorrespondingComponant);
                updateTextfield();
            }
        });
        repaint();
    }

    public boolean getCheckBoxPositionningAssistance() {
        return checkBoxPositionningAssistance.isSelected();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem EllipseProfileNew;
    private javax.swing.JMenuItem addControlPointButton;
    private javax.swing.JMenu addGuideRectbutton;
    private javax.swing.JMenuItem addWindowButton;
    private javax.swing.JCheckBoxMenuItem beamDisplayButton;
    private javax.swing.JMenuItem bezierProfileNew;
    private javax.swing.JPanel bottomPannel;
    private javax.swing.JMenuBar buttonTopPanel;
    private javax.swing.JCheckBoxMenuItem ceilingDisplayButton;
    private javax.swing.JCheckBox checkBoxPositionningAssistance;
    private javax.swing.JPanel componentsPanel;
    private javax.swing.JCheckBoxMenuItem controlPointButton;
    private javax.swing.JMenu displayMenu;
    private javax.swing.JCheckBoxMenuItem dividingWallDisplayButton;
    private javax.swing.JCheckBoxMenuItem doorDisplay;
    private ca.ulaval.glo2004.gui.DrawingPanel drawingPanel;
    private javax.swing.JButton ellipseCancelButton;
    private javax.swing.JButton ellipseConfirmeButton;
    private javax.swing.JPanel errorsPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JCheckBoxMenuItem floorDisplay;
    private javax.swing.JCheckBoxMenuItem guideRectangleDisplay;
    private javax.swing.JCheckBoxMenuItem hayonDisplayButton;
    private javax.swing.JCheckBoxMenuItem innerLayerDisplayButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAssistanceGridSpacing;
    private javax.swing.JLabel labelComponants;
    private javax.swing.JLabel labelComposantName;
    private javax.swing.JLabel labelError;
    private javax.swing.JLabel labelPoids;
    private javax.swing.JLabel labelProperties;
    private javax.swing.JLabel labelPropertise1;
    private javax.swing.JLabel labelPropertise2;
    private javax.swing.JLabel labelRadiusCurve;
    private javax.swing.JLabel labelScaleMicroTrailer;
    private javax.swing.JLabel labelScales;
    private javax.swing.JLabel labelThickness1;
    private javax.swing.JLabel labelUnits;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem menuEditCancel;
    private javax.swing.JMenuItem menuEditRestore;
    private javax.swing.JMenuItem menuFileExportImage;
    private javax.swing.JMenuItem menuFileExportSVG;
    private javax.swing.JMenuItem menuFileOpen;
    private javax.swing.JMenuItem menuFileQuit;
    private javax.swing.JMenuItem menuFileSave;
    private javax.swing.JCheckBoxMenuItem outerLayerDisplayButton;
    private javax.swing.JPanel preferencesPanel;
    private javax.swing.JCheckBoxMenuItem profilDisplayButton;
    private javax.swing.JPanel propertiesPanel;
    private javax.swing.JRadioButton radioButtonInches;
    private javax.swing.JRadioButton radioButtonMillimètre;
    private javax.swing.JCheckBoxMenuItem rawWallDisplay;
    private javax.swing.JPanel scalePanel;
    private javax.swing.JCheckBoxMenuItem strutDisplayButton;
    private javax.swing.JTextField textFieldAssistanceGridSpacing;
    private javax.swing.JTextField textFieldComposantName;
    private javax.swing.JTextArea textFieldErrors;
    private javax.swing.JTextField textFieldPoids;
    private javax.swing.JTextField textFieldPropertise1;
    private javax.swing.JTextField textFieldPropertise2;
    private javax.swing.JTextField textFieldRadiusCurve;
    private javax.swing.JTextField textFieldScaleMicroTrailer;
    private javax.swing.JTextField textFieldThickness;
    private javax.swing.JTree treeComponants;
    private javax.swing.JCheckBoxMenuItem windowDisplay;
    // End of variables declaration//GEN-END:variables

    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }
    public void invalidateDisplayMenu(){
        rawWallDisplay.setSelected(false);
        profilDisplayButton.setSelected(true);
        floorDisplay.setSelected(true);
        hayonDisplayButton.setSelected(true);
        beamDisplayButton.setSelected(true);
        controlPointButton.setSelected(true);
        ceilingDisplayButton.setSelected(true);
        doorDisplay.setSelected(true);
        strutDisplayButton.setSelected(true);
        dividingWallDisplayButton.setSelected(true);
        windowDisplay.setSelected(true);
        guideRectangleDisplay.setSelected(false);
                
    }
}
