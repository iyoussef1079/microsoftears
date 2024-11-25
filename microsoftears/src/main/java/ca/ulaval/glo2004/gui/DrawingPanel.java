/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.domain.drawing.MainDrawer;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {

    public Dimension initialDimension;
    private MainFrame mainFrame;
    

    public DrawingPanel() {
    }

    public DrawingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.85);
        int height = (int) (width * 0.5);
        initialDimension = new Dimension(width, height);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.85);
        int height = (int) (width * 0.5);
        if (this.mainFrame != null) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if(this.mainFrame.getCheckBoxPositionningAssistance()){
                float alpha = 0.25f;
                Color color = new Color(0.5f, 0.5f, 0.5f, alpha);
                g2d.setColor(color);
                for (float x = 0; x <= width; x += this.mainFrame.point.x){
                    for (float y = 0; y <= height; y += this.mainFrame.point.y){
                        Rectangle2D.Float rect = new Rectangle2D.Float(x, y, this.mainFrame.point.x, this.mainFrame.point.y);
                        g2d.draw(rect);
                    }
                }
            }
            g2d.setColor(Color.BLACK);
            MainDrawer componentDrawer = new MainDrawer(mainFrame.controller, initialDimension);
            componentDrawer.draw(g);
        }
    }
}
