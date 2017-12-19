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
 * TrainDetailsJPanel.java
 *
 * Created on 16 June 2030, 20:03
 */

package freerails.client.view;

import freerails.client.renderer.RenderersRoot;
import freerails.controller.ModelRoot;
import freerails.world.cargo.CargoType;
import freerails.world.cargo.ImmutableCargoBundle;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.KEY;
import freerails.world.top.NonNullElements;
import freerails.world.top.ReadOnlyWorld;
import freerails.world.top.SKEY;
import freerails.world.train.TrainModel;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * This JPanel displays a side-on view of a train and a summary of the cargo
 * that it is carrying.
 */
public class TrainDescriptionJPanel extends javax.swing.JPanel implements View {

    private static final long serialVersionUID = 3977018444325664049L;

    private ReadOnlyWorld w;

    private FreerailsPrincipal principal;

    private int trainNumber = -1;

    private Serializable lastTrain, lastCargoBundle;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private freerails.client.view.TrainListCellRenderer trainViewJPanel1;

    public TrainDescriptionJPanel() {
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics arg0) {
        // Check whether the train or its cargo have changed since the last call
        // to this method.
        updateIfNecessary();

        super.paintComponent(arg0);
    }

    private void updateIfNecessary() {
        TrainModel train = (TrainModel) w.get(principal, KEY.TRAINS,
                trainNumber);

        for (int i = 0; i < train.getNumberOfWagons(); i++) {
            // this.sideOnTrainViewJPanel1.addWagon(train.getWagon(i));
        }
        int cargoBundleID = train.getCargoBundleID();
        Serializable cb = w.get(principal, KEY.CARGO_BUNDLES,
                cargoBundleID);

        if (train != lastTrain || cb != lastCargoBundle)
            displayTrain(trainNumber);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        trainViewJPanel1 = new freerails.client.view.TrainListCellRenderer();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.TitledBorder("Current Details"));
        setPreferredSize(new java.awt.Dimension(250, 97));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1
                .setText("<html><head></head><body>Trains X: 20 passengers, 15 tons of mfg goods, 12 sacks of mail, and 7 tons of livestock.</body></html>");
        jLabel1.setMinimumSize(new java.awt.Dimension(250, 17));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(trainViewJPanel1, gridBagConstraints);

    }// GEN-END:initComponents

    public void setup(ModelRoot mr, RenderersRoot vl, Action closeAction) {

        this.trainViewJPanel1.setup(mr, vl, closeAction);
        trainViewJPanel1.setHeight(30);
        trainViewJPanel1.setCenterTrain(true);
        this.w = mr.getWorld();
        principal = mr.getPrincipal();
    }

    public void displayTrain(int newTrainNumber) {

        NonNullElements it = new NonNullElements(KEY.TRAINS, w, principal);
        it.gotoIndex(newTrainNumber);

        this.trainNumber = newTrainNumber;

        trainViewJPanel1.display(newTrainNumber);
        TrainModel train = (TrainModel) w.get(principal, KEY.TRAINS,
                newTrainNumber);

        for (int i = 0; i < train.getNumberOfWagons(); i++) {
            // this.sideOnTrainViewJPanel1.addWagon(train.getWagon(i));
        }
        int cargoBundleID = train.getCargoBundleID();
        ImmutableCargoBundle cb = (ImmutableCargoBundle) w.get(principal,
                KEY.CARGO_BUNDLES, cargoBundleID);
        String s = "Train #" + it.getNaturalNumber() + ": ";
        int numberOfTypesInBundle = 0;
        for (int i = 0; i < w.size(SKEY.CARGO_TYPES); i++) {
            int amount = cb.getAmount(i);
            if (0 != amount) {
                CargoType ct = (CargoType) w.get(SKEY.CARGO_TYPES, i);
                String cargoTypeName = ct.getDisplayName();
                if (0 != numberOfTypesInBundle) {
                    s += "; ";
                }
                numberOfTypesInBundle++;

                s += cargoTypeName + " (" + amount + ")";
            }
        }
        if (0 == numberOfTypesInBundle) {
            s += "no cargo";
        }
        s += ".";
        this.jLabel1.setText(s);
        this.lastCargoBundle = cb;
        this.lastTrain = train;
    }
    // End of variables declaration//GEN-END:variables

}
