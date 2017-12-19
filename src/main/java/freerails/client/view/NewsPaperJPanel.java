/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * NewsPaperJPanel.java
 *
 * Created on 21 December 2002, 18:38
 */
package freerails.client.view;

import freerails.client.renderer.RenderersRoot;
import freerails.controller.ModelRoot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JPanel that displays a newspaper headline.
 */
public class NewsPaperJPanel extends javax.swing.JPanel implements View {
    private static final long serialVersionUID = 3258410638366946868L;
    private final Image pieceOfNewspaper;
    private ActionListener callBack;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel headline;

    /**
     *
     */
    public NewsPaperJPanel() {
        initComponents();

        Image tempImage = (new javax.swing.ImageIcon(getClass().getResource(
                "/freerails/data/newspaper.png"))).getImage();

        GraphicsConfiguration defaultConfiguration = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();
        pieceOfNewspaper = defaultConfiguration.createCompatibleImage(tempImage
                        .getWidth(null), tempImage.getHeight(null),
                Transparency.BITMASK);

        Graphics g = pieceOfNewspaper.getGraphics();

        g.drawImage(tempImage, 0, 0, null);
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                callBack.actionPerformed(new ActionEvent(this, 0, null));
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() { // GEN-BEGIN:initComponents
        headline = new javax.swing.JLabel();
        JPanel jPanel1 = new JPanel();
        JLabel anyKeyToContinueJLabel = new JLabel();
        setLayout(null);
        setPreferredSize(new java.awt.Dimension(640, 400));
        setMinimumSize(new java.awt.Dimension(640, 400));
        setMaximumSize(new java.awt.Dimension(640, 400));
        setOpaque(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        headline.setPreferredSize(new java.awt.Dimension(620, 110));
        headline.setMinimumSize(new java.awt.Dimension(620, 110));
        headline.setText("NEWSPAPER HEADLINE");
        headline.setForeground(java.awt.Color.black);
        headline.setBackground(java.awt.Color.white);
        headline.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headline.setFont(new java.awt.Font("Lucida Bright", 1, 36));
        headline.setMaximumSize(new java.awt.Dimension(620, 110));

        add(headline);
        headline.setBounds(10, 70, 620, 110);

        jPanel1.setBorder(new javax.swing.border.BevelBorder(0));

        anyKeyToContinueJLabel.setText("Click to continue");
        anyKeyToContinueJLabel.setForeground(java.awt.Color.black);
        anyKeyToContinueJLabel.setBackground(java.awt.Color.darkGray);
        jPanel1.add(anyKeyToContinueJLabel);

        add(jPanel1);
        jPanel1.setBounds(230, 260, 190, 30);
    }

    // GEN-END:initComponents
    private void formKeyPressed(java.awt.event.KeyEvent evt) { // GEN-FIRST:event_formKeyPressed
        // Add your handling code here:
        this.setVerifyInputWhenFocusTarget(false);
    }

    // GEN-LAST:event_formKeyPressed
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.pieceOfNewspaper, 0, 0, null);
        this.paintChildren(g);
    }

    /**
     * @param s
     */
    public void setHeadline(String s) {
        this.headline.setText(s);
    }

    /**
     * @param mr
     * @param vl
     * @param closeAction
     */
    public void setup(ModelRoot mr, RenderersRoot vl, Action closeAction) {
        this.callBack = closeAction;
    }

    // End of variables declaration//GEN-END:variables
}