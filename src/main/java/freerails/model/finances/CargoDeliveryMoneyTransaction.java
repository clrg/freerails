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

package freerails.model.finances;

import freerails.model.cargo.CargoBatch;

/**
 * A credit for delivering cargo.
 */
// TODO Is this an Item transaction?
public class CargoDeliveryMoneyTransaction extends MoneyTransaction {

    private static final long serialVersionUID = 3257009851963160372L;
    private final CargoBatch cargoBatch;
    private final int quantity;
    private final int stationId;
    private final int trainId;

    /**
     * @param money
     * @param quantity
     * @param stationId
     * @param cargoBatch
     * @param trainId
     */
    public CargoDeliveryMoneyTransaction(Money money, int quantity, int stationId, CargoBatch cargoBatch, int trainId) {
        super(money, TransactionCategory.CARGO_DELIVERY);
        this.stationId = stationId;
        this.quantity = quantity;
        this.cargoBatch = cargoBatch;
        this.trainId = trainId;
    }

    /**
     * @return
     */
    public int getTrainId() {
        return trainId;
    }

    /**
     * @return
     */
    public CargoBatch getCargoBatch() {
        return cargoBatch;
    }

    /**
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return
     */
    public int getStationId() {
        return stationId;
    }
}