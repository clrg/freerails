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

import java.io.Serializable;

/**
 * A Transaction is a change in a player's bank balance and/or assets.
 */
public interface Transaction extends Serializable {

    // TODO is money always needed, if so, can we already implement it

    /**
     * Positive means credit.
     */
    Money price();

    // TODO what is this category good for

    /**
     * @return
     */
    TransactionCategory getCategory();

}