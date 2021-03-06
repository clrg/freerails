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
 *
 */
package freerails.model;

import freerails.model.finances.*;
import freerails.model.player.FreerailsPrincipal;
import freerails.model.player.Player;
import freerails.model.world.FullWorld;
import freerails.model.world.World;
import junit.framework.TestCase;

/**
 *
 */
public class ItemsTransactionAggregatorTest extends TestCase {

    /**
     *
     */
    public void test1() {
        World world = new FullWorld();
        Player player = new Player("name", 0);
        world.addPlayer(player);
        FreerailsPrincipal principal = world.getPlayer(0).getPrincipal();
        ItemsTransactionAggregator aggregator = new ItemsTransactionAggregator(world, principal);
        aggregator.setCategory(TransactionCategory.TRACK);
        int quant = aggregator.calculateQuantity();
        assertEquals(0, quant);
        Transaction transaction = new ItemTransaction(TransactionCategory.TRACK, 10, 5, new Money(100));
        world.addTransaction(principal, transaction);

        quant = aggregator.calculateQuantity();
        assertEquals(5, quant);
        transaction = new ItemTransaction(TransactionCategory.TRACK, 10, 11, new Money(200));
        world.addTransaction(principal, transaction);
    }

}
