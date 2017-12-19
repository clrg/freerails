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
 * Created on 07-Jul-2003
 *
 */
package freerails.move;

import freerails.world.finances.Bill;
import freerails.world.finances.Money;
import freerails.world.finances.Receipt;
import freerails.world.finances.Transaction;
import freerails.world.player.Player;
import freerails.world.top.MapFixtureFactory;

/**
 * JUnit test.
 */
public class AddTransactionMoveTest extends AbstractMoveTestCase {

    /**
     *
     */
    @Override
    public void testMove() {
        Money currentBalance = getWorld().getCurrentBalance(
                MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(0), currentBalance);

        Transaction t = new Receipt(new Money(100),
                Transaction.Category.MISC_INCOME);
        Move m = new AddTransactionMove(MapFixtureFactory.TEST_PRINCIPAL, t);
        assertTryMoveIsOk(m);
        assertTryUndoMoveFails(m);
        assertDoMoveIsOk(m);
        currentBalance = getWorld().getCurrentBalance(
                MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(100), currentBalance);

        final Player PLAYER_WITHOUT_ACCOUNT = new Player(
                "PLAYER_WITHOUT_ACCOUNT", 4);

        assertSurvivesSerialisation(m);

        Move m2 = new AddTransactionMove(PLAYER_WITHOUT_ACCOUNT.getPrincipal(),
                t);
        assertTryMoveFails(m2);

        assertOkAndRepeatable(m);
    }

    /**
     *
     */
    public void testConstrainedMove() {
        Money currentBalance = getWorld().getCurrentBalance(
                MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(0), currentBalance);

        Transaction t = new Bill(new Money(100),
                Transaction.Category.MISC_INCOME);
        Move m = new AddTransactionMove(MapFixtureFactory.TEST_PRINCIPAL, t,
                true);

        // This move should fail since there is no money in the account and
        // it is constrained is set to true.
        assertTryMoveFails(m);
    }
}