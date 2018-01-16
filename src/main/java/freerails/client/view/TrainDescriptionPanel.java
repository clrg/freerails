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
 */

package freerails.client.view;

import freerails.client.renderer.RendererRoot;
import freerails.controller.ModelRoot;
import freerails.world.KEY;
import freerails.world.NonNullElementWorldIterator;
import freerails.world.ReadOnlyWorld;
import freerails.world.SKEY;
import freerails.world.cargo.CargoType;
import freerails.world.cargo.ImmutableCargoBatchBundle;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.train.TrainModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.Serializable;

/**
 * Displays a side-on view of a train and a summary of the cargo
 * that it is carrying.
 */
public class TrainDescriptionPanel extends javax.swing.JPanel implements View {

    private static final long serialVersionUID = 3977018444325664049L;
    private ReadOnlyWorld world;
    private FreerailsPrincipal principal;
    private int trainNumber = -1;
    private Serializable lastTrain, lastCargoBundle;
    private JLabel label1;
    private TrainListCellRenderer trainViewJPanel1;

    public TrainDescriptionPanel() {
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Check whether the train or its cargo have changed since the last call
        // to this method.
        updateIfNecessary();

        super.paintComponent(g);
    }

    private void updateIfNecessary() {
        TrainModel train = (TrainModel) world.get(principal, KEY.TRAINS, trainNumber);

        int cargoBundleID = train.getCargoBundleID();
        Serializable cb = world.get(principal, KEY.CARGO_BUNDLES, cargoBundleID);

        if (train != lastTrain || cb != lastCargoBundle) displayTrain(trainNumber);
    }


    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        label1 = new JLabel();
        trainViewJPanel1 = new freerails.client.view.TrainListCellRenderer();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new TitledBorder("Current Details"));
        setPreferredSize(new java.awt.Dimension(250, 97));
        label1.setFont(new java.awt.Font("Dialog", 0, 12));
        label1.setText("<html><head></head><body>Trains X: 20 passengers, 15 tons of mfg goods, 12 sacks of mail, and 7 tons of livestock.</body></html>");
        label1.setMinimumSize(new java.awt.Dimension(250, 17));
        label1.setHorizontalTextPosition(SwingConstants.LEADING);
        label1.setVerticalTextPosition(SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(label1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(trainViewJPanel1, gridBagConstraints);

    }

    public void setup(ModelRoot modelRoot, RendererRoot vl, Action closeAction) {

        trainViewJPanel1.setup(modelRoot, vl, closeAction);
        trainViewJPanel1.setHeight(30);
        trainViewJPanel1.setCenterTrain(true);
        world = modelRoot.getWorld();
        principal = modelRoot.getPrincipal();
    }

    public void displayTrain(int newTrainNumber) {

        NonNullElementWorldIterator it = new NonNullElementWorldIterator(KEY.TRAINS, world, principal);
        it.gotoIndex(newTrainNumber);

        trainNumber = newTrainNumber;

        trainViewJPanel1.display(newTrainNumber);
        TrainModel train = (TrainModel) world.get(principal, KEY.TRAINS, newTrainNumber);

        int cargoBundleID = train.getCargoBundleID();
        ImmutableCargoBatchBundle cb = (ImmutableCargoBatchBundle) world.get(principal, KEY.CARGO_BUNDLES, cargoBundleID);
        StringBuilder s = new StringBuilder("Train #" + it.getNaturalNumber() + ": ");
        int numberOfTypesInBundle = 0;
        for (int i = 0; i < world.size(SKEY.CARGO_TYPES); i++) {
            int amount = cb.getAmountOfType(i);
            if (0 != amount) {
                CargoType ct = (CargoType) world.get(SKEY.CARGO_TYPES, i);
                String cargoTypeName = ct.getDisplayName();
                if (0 != numberOfTypesInBundle) {
                    s.append("; ");
                }
                numberOfTypesInBundle++;

                s.append(cargoTypeName).append(" (").append(amount).append(')');
            }
        }
        if (0 == numberOfTypesInBundle) {
            s.append("no cargo");
        }
        s.append('.');
        label1.setText(s.toString());
        lastCargoBundle = cb;
        lastTrain = train;
    }

}